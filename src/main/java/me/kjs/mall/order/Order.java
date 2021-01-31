package me.kjs.mall.order;


import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.cancel.PaymentCancelRequest;
import me.kjs.mall.order.cancel.dto.CancelOrderInterface;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderNonMemberInfoDto;
import me.kjs.mall.order.dto.create.OrderCreateEntityDto;
import me.kjs.mall.order.dto.create.ProductAndQuantityDto;
import me.kjs.mall.order.payment.*;
import me.kjs.mall.order.payment.dto.PaymentVirtualBankNotifyDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;
import me.kjs.mall.payment.nicepay.PaymentCertificationResponseDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity implements OwnerCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String orderName;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_payment_id")
    private OrderPayment orderPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member orderMember;
    @Enumerated(EnumType.STRING)
    private YnType nonMemberOrder;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "non_member_id")
    private NonMember nonMember;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderSpecific> orderSpecifics = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderCancel> orderCancels = new ArrayList<>();

    public static Order createOrder(OrderCreateEntityDto orderCreateEntityDto, Member member) {
        List<ProductAndQuantityDto> productDtos = orderCreateEntityDto.getProductAndQuantityDtoList();
        int destinationSize = orderCreateEntityDto.getOrderDestinationSaveDtos().size();
        String orderName = productDtos.get(0).getProduct().getBaseProductName()
                + (productDtos.size() == 1 ? "" : (" 외 " + (productDtos.size() * destinationSize - 1) + "건"));
        Order order = Order.builder()
                .orderMember(member)
                .orderName(orderName)
                .nonMemberOrder(YnType.N)
                .build();

        int point = orderCreateEntityDto.getUsePoint();
        List<Integer> points = new ArrayList<>();
        for (int i = 0; i < destinationSize; i++) {
            points.add(point / destinationSize + (point % destinationSize != 0 ? i < point / destinationSize ? 1 : 0 : 0));
        }

        for (int i = 0; i < destinationSize; i++) {
            OrderDestinationSaveDto orderDestinationSaveDto = orderCreateEntityDto.getOrderDestinationSaveDtos().get(i);
            Integer usePoint = points.get(i);
            order.orderSpecifics.add(OrderSpecific.createOrderSpecific(order, usePoint, orderCreateEntityDto.getProductAndQuantityDtoList(), orderDestinationSaveDto));
        }
        order.orderPayment = OrderPayment.createOrderPayment(order.orderSpecifics, orderCreateEntityDto.getPaymentMethod(), orderCreateEntityDto.getUsePoint());

        return order;
    }

    public static Order createOrder(OrderCreateEntityDto orderCreateEntityDto, OrderNonMemberInfoDto member) {
        List<ProductAndQuantityDto> productDtos = orderCreateEntityDto.getProductAndQuantityDtoList();
        int destinationSize = orderCreateEntityDto.getOrderDestinationSaveDtos().size();
        String orderName = productDtos.get(0).getProduct().getBaseProductName()
                + (productDtos.size() == 1 ? "" : (" 외 " + (productDtos.size() * destinationSize - 1) + "건"));
        Order order = Order.builder()
                .orderName(orderName)
                .nonMemberOrder(YnType.Y)
                .nonMember(NonMember.createNonMember(member))
                .build();

        for (int i = 0; i < destinationSize; i++) {
            OrderDestinationSaveDto orderDestinationSaveDto = orderCreateEntityDto.getOrderDestinationSaveDtos().get(i);
            order.orderSpecifics.add(OrderSpecific.createOrderSpecific(order, 0, orderCreateEntityDto.getProductAndQuantityDtoList(), orderDestinationSaveDto));
        }
        order.orderPayment = OrderPayment.createOrderPayment(order.orderSpecifics, orderCreateEntityDto.getPaymentMethod(), orderCreateEntityDto.getUsePoint());

        return order;
    }

    @Override
    public boolean isOwner(Member member) {
        return this.orderMember == member;
    }

    public PaymentCertificationResponse createPaymentCertificationResponse(PaymentCertificationResponseDto paymentCertificationResponseDto) {
        return orderPayment.createPaymentCertificationResponse(paymentCertificationResponseDto);
    }

    public PaymentApproveRequest createPaymentApproveRequest() {
        for (OrderSpecific orderSpecific : orderSpecifics) {
            orderSpecific.paymentApproveRequest();
        }
        return orderPayment.createPaymentApproveRequest();
    }

    public PaymentApproveResponse createPaymentApproveResponse(PaymentApproveResponseDto paymentApproveResponseDto) {
        PaymentApproveResponse paymentApproveResponse = orderPayment.createPaymentApproveResponse(paymentApproveResponseDto);
        return paymentApproveResponse;
    }

    public boolean isCertificationResponseSuccess() {
        return orderPayment.isCertificationResponseSuccess();
    }

    public boolean isApproveResponseSuccess() {
        return orderPayment.isApproveResponseSuccess();
    }

    public void paymentApproveSuccess() {
        for (OrderSpecific orderSpecific : orderSpecifics) {
            orderSpecific.paymentApproveSuccess();
        }
    }

    public void paymentAccept() {
        for (OrderSpecific orderSpecific : orderSpecifics) {
            orderSpecific.paymentAccept();
        }
        orderPayment.paymentAccept();
    }

    public void paymentApproveFail() {
        for (OrderSpecific orderSpecific : orderSpecifics) {
            orderSpecific.paymentApproveFail();
        }
    }

    public boolean isOnlyOneOrder() {
        return orderSpecifics.size() == 1;
    }

    public PaymentCancelRequest cancelOrderAndCreatePaymentCancelRequest(OrderSpecific orderSpecific, CancelOrderInterface cancelCauseDto) {
        OrderCancel orderCancel = OrderCancel.createOrderCancel(this, orderSpecific, cancelCauseDto);
        orderCancels.add(orderCancel);
        return orderCancel.getPaymentCancelRequest();
    }

    public int getOrderPriceExcludeDeliveryFee() {
        return orderPayment.getTotalPaymentPriceExcludeDeliveryFee();
    }

    public int getUsePoint() {
        return orderPayment.getUsePoint();
    }

    public Long getMemberId() {
        return nonMemberOrder == YnType.Y ? null : orderMember.getId();
    }

    public String getMemberName() {
        return nonMemberOrder == YnType.Y ? nonMember.getName() : orderMember.getName();
    }

    public String getMemberEmail() {
        return nonMemberOrder == YnType.Y ? nonMember.getEmail() : orderMember.getEmail();
    }

    public String getOrderName() {
        return orderName;
    }

    public boolean isAvailablePayment() {
        return orderPayment.getTotalPaymentPrice() > 99;
    }

    public List<Long> getOrderSpecificIds() {
        return orderSpecifics.stream().map(OrderSpecific::getId).collect(Collectors.toList());
    }

    public List<String> getOrderCodes() {
        return orderSpecifics.stream().map(OrderSpecific::getOrderCode).collect(Collectors.toList());
    }

    public PaymentMethod getPaymentMethod() {
        return orderPayment.getPaymentMethod();
    }

    public boolean isVirtualBankPaymentAccept(PaymentVirtualBankNotifyDto paymentVirtualBankNotifyDto) {
        return orderPayment.isVirtualBankPaymentAccept(paymentVirtualBankNotifyDto) && orderSpecifics.get(0).getOrderState() == OrderState.PAYMENT_WAIT;
    }

    public String getOrderMemberPhoneNumber() {
        return nonMemberOrder == YnType.Y ? nonMember.getPhoneNumber() : orderMember.getPhoneNumber();
    }

    public int getPaymentPrice() {
        return orderPayment.getTotalPaymentPrice();
    }

    public PaymentVirtualBank getPaymentVirtualBank() {
        return orderPayment.getPaymentVirtualBank();
    }
}
