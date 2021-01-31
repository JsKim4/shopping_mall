package me.kjs.mall.order.specific;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.CodeGeneratorUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderProductAndQuantityDto;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.payment.OrderPayment;
import me.kjs.mall.order.specific.destination.OrderDestination;
import me.kjs.mall.order.specific.destination.dto.OrderDeliveryDoingDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.exchange.OrderExchange;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.order.specific.product.dto.OrderExchangeRequestDto;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderSpecific extends BaseEntity implements OwnerCheck, AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_specific_id")
    private Long id;
    private String orderCode;
    private String orderName;
    private LocalDateTime orderDateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_destination_id")
    private OrderDestination orderDestination;

    @Embedded
    private OrderSpecificPayment orderSpecificPayment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderSpecific")
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_exchange_id")
    private OrderExchange orderExchange;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Enumerated(EnumType.STRING)
    private OrderExchangeState orderExchangeState;


    private LocalDateTime acceptStateModifyDateTime;

    public static OrderSpecific createOrderSpecific(Order order, Integer usePoint, List<ProductAndQuantityDto> productDtos, OrderDestinationSaveDto destinationDto) {

        String name = productDtos.get(0).getName() + (productDtos.size() == 1 ? "" : " 외 " + (productDtos.size() - 1) + "건");

        OrderSpecific orderSpecific = OrderSpecific.builder()
                .order(order)
                .orderDateTime(LocalDateTime.now())
                .orderDestination(OrderDestination.createOrderDestination(destinationDto))
                .orderState(OrderState.ORDER_CREATE)
                .orderCode(CodeGeneratorUtil.orderSpecificCodeGenerate())
                .orderExchange(null)
                .orderExchangeState(OrderExchangeState.NONE)
                .orderName(name)
                .acceptStateModifyDateTime(null)
                .build();


        for (ProductAndQuantityDto productDto : productDtos) {
            int accumulateExpectedPoint = productDto.getSumPrice() / 10;
            orderSpecific.orderProducts.add(OrderProduct.createOrderProduct(orderSpecific, productDto, accumulateExpectedPoint));
        }
        orderSpecific.orderSpecificPayment = OrderSpecificPayment.createOrderSpecificPayment(orderSpecific.orderProducts, usePoint);

        return orderSpecific;
    }

    @Override
    public boolean isOwner(Member member) {
        return order.isOwner(member);
    }


    public OrderSpecific requestExchange(List<OrderProductAndQuantityDto> exchangeProductList, OrderExchangeRequestDto orderExchangeRequestDto) {
        orderExchangeState = OrderExchangeState.EXCHANGE_REQUEST;
        orderExchange = OrderExchange.requestExchange(orderExchangeRequestDto);

        for (OrderProduct orderProduct : orderProducts) {
            Optional<OrderProductAndQuantityDto> orderProductAndQuantityDto = exchangeProductList.stream().filter(opad -> opad.getOrderProduct().equals(orderProduct)).findFirst();
            if (orderProductAndQuantityDto.isPresent()) {
                orderProduct.requestExchange(orderProductAndQuantityDto.get().getQuantity(), orderExchange);
            } else {
                orderProduct.cancelExchange();
            }
        }

        for (OrderProductAndQuantityDto orderProductAndQuantityDto : exchangeProductList) {
            OrderProduct orderProduct = orderProductAndQuantityDto.getOrderProduct();
            orderProduct.requestExchange(orderProductAndQuantityDto.getQuantity(), orderExchange);
        }
        return this;
    }

    public void cancelExchange() {
        orderExchangeState = OrderExchangeState.NONE;
        orderExchange = null;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.cancelExchange();
        }
    }


    public int getSumPrice() {
        return orderSpecificPayment.getSumPrice();
    }

    public int getSumOriginalPrice() {
        return orderSpecificPayment.getSumOriginPrice();
    }

    public int getSumDiscountPrice() {
        return orderSpecificPayment.getSumDiscountPrice();
    }

    public int getDeliveryFee() {
        return orderSpecificPayment.getDeliveryFee();
    }

    public int getProductCount() {
        return orderProducts.size();
    }

    public String getRepresentProductName() {
        return orderProducts.get(0).getProductName();
    }

    public int getSumPaymentPrice() {
        return orderSpecificPayment.getTotalPaymentPrice();
    }

    public int getCancelAvailablePrice() {
        return orderSpecificPayment.getCancelAvailablePrice();
    }

    public void paymentApproveSuccess() {
        orderState = OrderState.PAYMENT_WAIT;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.paymentApproveSuccess();
        }
    }

    public void paymentAccept() {
        orderState = OrderState.PAYMENT_ACCEPT;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.paymentAccept();
        }
    }

    public void paymentApproveFail() {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.paymentApproveFail();
        }
    }

    public void paymentApproveRequest() {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.paymentApproveRequest();
        }
    }

    public OrderSpecific listLoading() {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.getProduct().getBaseProductThumbnailImage();
        }
        orderDestination.loading();
        return this;
    }

    public OrderSpecific loading() {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.loading();
        }
        orderDestination.loading();
        OrderPayment orderPayment = order.getOrderPayment();
        orderPayment.getPaymentMethod();
        orderPayment.getPaymentDateTime();
        if (orderExchange != null) {
            List<String> causeImage = orderExchange.getCauseImage();
            for (String s : causeImage) {

            }
        }
        return this;
    }

    public List<String> getThumbnail() {
        OrderProduct orderProduct = orderProducts.get(0);
        Product product = orderProduct.getProduct();
        return product.getBaseProductThumbnailImage();
    }

    public void modifyOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    @Override
    public boolean isAvailable() {
        return orderState != OrderState.ORDER_CREATE;
    }

    @Override
    public boolean isUsed() {
        return orderState != OrderState.ORDER_CREATE;
    }

    public PaymentMethod getPaymentMethod() {
        OrderPayment orderPayment = order.getOrderPayment();
        return orderPayment.getPaymentMethod();
    }

    public LocalDateTime getPaymentDateTime() {
        OrderPayment orderPayment = order.getOrderPayment();
        return orderPayment.getPaymentDateTime();
    }

    public void paymentCancelSuccess(OrderCancel orderCancel) {
        orderState = OrderState.PAYMENT_CANCEL;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.orderCancel();
        }
        orderSpecificPayment.paymentCancelSuccess(orderCancel);
    }

    public boolean isCancelAvailable() {
        return orderState == OrderState.PAYMENT_ACCEPT;
    }

    public void updateDestination(OrderDestinationSaveDto orderDestinationSaveDto) {
        orderDestination.update(orderDestinationSaveDto);
    }

    public boolean isUpdateDestinationAvailable() {
        return orderState == OrderState.PAYMENT_ACCEPT;
    }

    public boolean isAcceptOrderAvailable() {
        return orderState == OrderState.DELIVERY_ACCEPT;
    }

    public void acceptOrder() {
        orderState = OrderState.ORDER_ACCEPT;
        acceptStateModifyDateTime = LocalDateTime.now();
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.isAvailableOrderAcceptAvailable()) {
                orderProduct.orderAccept();
            }
        }
    }

    public void deliveryDoingOrder(OrderDeliveryDoingDto orderDeliveryDoingDto) {
        orderState = OrderState.DELIVERY_DOING;
        orderDestination.deliveryDoingOrder(orderDeliveryDoingDto);
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.deliveryDoingOrder();
        }
    }

    public void deliveryAcceptOrder() {
        orderState = OrderState.DELIVERY_ACCEPT;
        orderDestination.deliveryAcceptOrder();
        acceptStateModifyDateTime = LocalDateTime.now().plusDays(14);
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.deliveryAcceptOrder();
        }
    }

    public String getDeliveryApiUrl() {
        if (orderState != OrderState.DELIVERY_DOING && orderState != OrderState.DELIVERY_ACCEPT) {
            return null;
        }
        return orderDestination.getDeliveryApiUrl();
    }

    public boolean isDeliveryDoingAvailable() {
        return orderState == OrderState.PAYMENT_ACCEPT || orderState == OrderState.DELIVERY_WAIT;
    }

    public boolean isDeliveryAcceptAvailable() {
        return orderState == OrderState.DELIVERY_DOING;
    }

    public void modifyOrderExchangeState(OrderExchangeState orderState) {
        orderExchangeState = orderState;
    }

    public boolean isExchangeRequestAvailable() {
        return (orderState == OrderState.DELIVERY_ACCEPT) &&
                (orderExchangeState == OrderExchangeState.NONE ||
                        orderExchangeState == OrderExchangeState.EXCHANGE_REQUEST ||
                        orderExchangeState == OrderExchangeState.EXCHANGE_REJECT);
    }

    public boolean isExchangeCancelAvailable() {
        return orderExchangeState == OrderExchangeState.EXCHANGE_RECEPTION ||
                orderExchangeState == OrderExchangeState.EXCHANGE_REQUEST ||
                orderExchangeState == OrderExchangeState.EXCHANGE_REJECT;
    }

    public String getExchangeCause() {
        if (orderExchange == null || orderExchangeState == OrderExchangeState.NONE) {
            return "상품 교환 상태가 아닙니다.";
        }
        return orderExchange.getExchangeCause();
    }

    public List<String> getCauseImage() {
        if (orderExchange == null || orderExchangeState == OrderExchangeState.NONE) {
            return Collections.EMPTY_LIST;
        }
        return orderExchange.getCauseImage();
    }

    public Long getMemberId() {
        return order.getMemberId();
    }

    public Long getOrderId() {
        return order.getId();
    }

    public String getMemberName() {
        return order.getMemberName();
    }

    public String getMemberEmail() {
        return order.getMemberEmail();
    }

    public String getMemberRecipient() {
        return orderDestination.getRecipient();
    }

    public void placeAccept() {
        orderState = OrderState.DELIVERY_WAIT;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.placeAccept();
        }
    }

    public int getUsePoint() {
        return orderSpecificPayment.getUsePoint();
    }

    public Member getMember() {
        return order.getOrderMember();
    }

    public boolean isWaitCancelAvailable() {
        return orderState == OrderState.PAYMENT_WAIT;
    }

    public void waitCancel() {
        orderState = OrderState.ORDER_CREATE;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.waitCancel();
        }
    }

    public boolean isNonMemberOrderCancelAvailable(String phoneNumber) {
        return order.getNonMemberOrder() == YnType.Y && order.getOrderMemberPhoneNumber().equals(phoneNumber);
    }
}