package me.kjs.mall.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CompareUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.dto.OrderCreateByNonMemberDto;
import me.kjs.mall.order.dto.OrderQueryNonMemberDto;
import me.kjs.mall.order.dto.OrderSearchConditionDto;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.OrderCreateEntityDto;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.exception.*;
import me.kjs.mall.order.interfaces.OrderCreatorFirst;
import me.kjs.mall.order.payment.dto.PaymentVirtualBankNotifyDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.order.specific.destination.dto.OrderDeliveryDoingDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.payment.exception.NotAvailableCancelOrderStateException;
import me.kjs.mall.point.Point;
import me.kjs.mall.point.PointService;
import me.kjs.mall.point.dto.PointCreateDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.product.exception.ProductStockNotEnoughException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.isUsedFilter;
import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;
import static me.kjs.mall.common.util.ThrowUtil.notOwnerThrow;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderSpecificRepository orderSpecificRepository;
    private final PointService pointService;

    @Transactional
    public Order createOrder(OrderCreateDto orderCreateDto, Long memberId) {
        OrderCreateEntityDto orderCreateEntityDto = createOrderEntity(orderCreateDto);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        if (orderCreateDto.getPoint() > 0) {
            int point = pointService.availableUsePoint(member);
            if (point < orderCreateDto.getPoint()) {
                throw new NotEnoughPointException();
            }
        }
        if (!member.isAvailableOrder()) {
            throw new NotAvailableOrderAccountStatusException();
        }
        Order order = Order.createOrder(orderCreateEntityDto, member);
        if (!order.isAvailablePayment()) {
            throw new NotAvailablePaymentException();
        }
        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(OrderCreateByNonMemberDto orderCreateByNonMemberDto) {
        OrderCreateEntityDto orderCreateEntityDto = createOrderEntity(orderCreateByNonMemberDto);
        Order order = Order.createOrder(orderCreateEntityDto, orderCreateByNonMemberDto.getMemberInfo());
        if (!order.isAvailablePayment()) {
            throw new NotAvailablePaymentException();
        }
        return orderRepository.save(order);
    }

    public List<OrderSpecific> queryOrders(OrderSearchConditionDto orderSearchConditionDto, Member member) {
        List<OrderSpecific> orders = orderQueryRepository.findOrderByMemberAndCondition(orderSearchConditionDto, member);
        for (OrderSpecific orderSpecific : orders) {
            orderSpecific.listLoading();
        }
        return orders;
    }

    private OrderCreateEntityDto createOrderEntity(OrderCreatorFirst orderCreateDto) {
        List<Long> productIds = orderCreateDto.getProductIds();
        List<Product> allProducts = productRepository.findAllById(productIds);
        List<Product> products = isUsedFilter(allProducts);
        products = products.stream().map(Product::loading).collect(Collectors.toList());
        if (allProducts.size() != products.size()) {
            throw new NotAvailableProductContainsException();
        }

        List<ProductAndQuantityDto> productAndQuantityDtoList = productsToProductAndQuantityList(products, orderCreateDto.getProducts());
        return OrderCreateEntityDto.createOrderCreateEntity(productAndQuantityDtoList, orderCreateDto);
    }

    private List<ProductAndQuantityDto> productsToProductAndQuantityList(List<Product> products, List<ProductIdAndQuantityDto> productIdAndQuantityDtos) {
        final List<ProductAndQuantityDto> productAndQuantities = new ArrayList<>();
        for (Product product : products) {
            ProductIdAndQuantityDto productIdAndQuantityDto = productIdAndQuantityDtos.stream()
                    .filter(productId -> CompareUtil.equals(productId.getProductId(), product.getId()))
                    .findFirst().orElseThrow(NoExistIdException::new);

            if (product.isStockMoreThen(productIdAndQuantityDto.getQuantity()) == false) {
                throw new ProductStockNotEnoughException();
            }
            productAndQuantities.add(new ProductAndQuantityDto(product, productIdAndQuantityDto.getQuantity()));
        }
        return productAndQuantities;
    }

    public OrderSpecific findOrderById(Long orderSpecificId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(orderSpecific);
        notOwnerThrow(orderSpecific, member);
        return orderSpecific.loading();
    }

    @Transactional
    public void updateDestination(Long orderSpecificId, Long memberId, OrderDestinationSaveDto orderDestinationSaveDto) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(orderSpecific);
        notOwnerThrow(orderSpecific, member);
        if (orderSpecific.isUpdateDestinationAvailable() == false) {
            throw new NotAvailableUpdateOrderSpecificDestinationException();
        }

        orderSpecific.updateDestination(orderDestinationSaveDto);
    }

    @Transactional
    public void acceptOrder(Long orderSpecificId, Long memberId) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        notAvailableThrow(orderSpecific);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        notOwnerThrow(orderSpecific, member);
        if (orderSpecific.isAcceptOrderAvailable() == false) {
            throw new NotAvailableAcceptOrderStateException();
        }
        orderSpecific.acceptOrder();
        for (OrderProduct orderProduct : orderSpecific.getOrderProducts()) {
            Point point = pointService.accumulatePoint(PointCreateDto.accumulatePointByOrderAccept(orderProduct, member));
            orderProduct.accumulatePoint(point);
        }
    }

    @Transactional
    public void deliveryDoingOrder(Long orderSpecificId, OrderDeliveryDoingDto orderDeliveryDoingDto) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        if (orderSpecific.isDeliveryDoingAvailable() == false) {
            throw new NotAvailableDeliveryDoingException();
        }
        orderSpecific.deliveryDoingOrder(orderDeliveryDoingDto);
    }

    @Transactional
    public void deliveryAcceptOrder(Long orderSpecificId) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        if (orderSpecific.isDeliveryAcceptAvailable() == false) {
            throw new NotAvailableDeliveryAcceptException();
        }
        orderSpecific.deliveryAcceptOrder();
    }

    @Transactional
    public void successOrderCancel(OrderCancel orderCancel) {
        OrderSpecific orderSpecific = orderCancel.getOrderSpecific();
        pointService.orderCancel(orderSpecific);

    }

    @Transactional
    public void paymentWaitCancel(Long orderSpecificId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        notOwnerThrow(orderSpecific, member);
        if (!orderSpecific.isWaitCancelAvailable()) {
            throw new NotAvailableCancelOrderStateException();
        }
        orderSpecific.waitCancel();
        pointService.orderCancel(orderSpecific);
    }

    @Transactional
    public Order virtualBankPaymentAccept(PaymentVirtualBankNotifyDto paymentVirtualBankNotifyDto) {
        String moid = paymentVirtualBankNotifyDto.getMOID();
        Order order = orderRepository.findByPaymentCode(moid).orElseThrow(NoExistIdException::new);
        if (!order.isVirtualBankPaymentAccept(paymentVirtualBankNotifyDto)) {
            throw new NotAvailableVirtualBankPaymentAcceptException();
        }
        for (OrderSpecific orderSpecific : order.getOrderSpecifics()) {
            orderSpecific.loading();
        }
        return order;
    }

    public OrderSpecific queryOrderByNonMember(OrderQueryNonMemberDto orderQueryNonMemberDto) {
        OrderSpecific orderSpecific = orderSpecificRepository.findNonMemberOrderSpecificByNameAndCode(orderQueryNonMemberDto.getName(), orderQueryNonMemberDto.getOrderCode()).orElseThrow(NoExistIdException::new);
        return orderSpecific.loading();
    }
}
