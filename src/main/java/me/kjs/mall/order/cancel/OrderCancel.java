package me.kjs.mall.order.cancel;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.Bank;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.cancel.dto.CancelOrderInterface;
import me.kjs.mall.order.cancel.dto.PaymentCancelResponseDto;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.specific.OrderSpecific;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderCancel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_cancel_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_specific_id")
    private OrderSpecific orderSpecific;

    private int cancelPrice;

    private String cancelCause;

    private String accountName;
    private Bank bank;
    private String accountNo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_cancel_request_id")
    private PaymentCancelRequest paymentCancelRequest;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_cancel_response_id")
    private PaymentCancelResponse paymentCancelResponse;

    public static OrderCancel createOrderCancel(Order order, OrderSpecific orderSpecific, CancelOrderInterface cancelCauseDto) {
        OrderCancel orderCancel = OrderCancel.builder()
                .orderSpecific(orderSpecific)
                .order(order)
                .cancelPrice(orderSpecific.getCancelAvailablePrice())
                .cancelCause(cancelCauseDto.getCause())
                .build();
        if (orderSpecific.getPaymentMethod() == PaymentMethod.VBANK) {
            orderCancel.accountName = cancelCauseDto.getName();
            orderCancel.accountNo = cancelCauseDto.getAccountNo();
            orderCancel.bank = cancelCauseDto.getBank();
        }
        orderCancel.paymentCancelRequest = PaymentCancelRequest.createPaymentCancelRequest(order, orderCancel);
        return orderCancel;
    }

    public PaymentCancelResponse updateByPaymentCancelResponse(PaymentCancelResponseDto paymentCancelResponseDto) {
        paymentCancelResponse = PaymentCancelResponse.createPaymentCancelResponse(paymentCancelResponseDto);
        if (paymentCancelResponse.isSuccess()) {
            orderSpecific.paymentCancelSuccess(this);
        }
        return paymentCancelResponse;
    }

    public boolean isOrderCancelSuccess() {
        return paymentCancelResponse != null && paymentCancelResponse.isSuccess();
    }

    public String getFailMessage() {
        return paymentCancelResponse == null ? "4027 취소에 실패하였습니다." : paymentCancelResponse.getFailMessage();
    }

    public String getBankCode() {
        if (bank == null) {
            return null;
        }
        return bank.getCode();
    }


    public Long getOrderSpecificId() {
        return orderSpecific.getId();
    }
}
