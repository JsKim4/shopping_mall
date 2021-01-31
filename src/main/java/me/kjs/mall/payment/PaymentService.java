package me.kjs.mall.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.BadRequestException;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.OrderQueryRepository;
import me.kjs.mall.order.OrderRepository;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.cancel.PaymentCancelRequest;
import me.kjs.mall.order.cancel.dto.CancelCauseDto;
import me.kjs.mall.order.cancel.dto.CancelCauseNonMemberDto;
import me.kjs.mall.order.cancel.dto.CancelOrderInterface;
import me.kjs.mall.order.cancel.dto.PaymentCancelResponseDto;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.payment.OrderPayment;
import me.kjs.mall.order.payment.PaymentApproveRequest;
import me.kjs.mall.order.payment.PaymentApproveResponse;
import me.kjs.mall.order.payment.dto.OrderPaymentResultDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.payment.exception.NotAvailableCancelOrderStateException;
import me.kjs.mall.payment.exception.NotNonMemberOrderException;
import me.kjs.mall.payment.exception.PaymentCertificationNotAvailableException;
import me.kjs.mall.payment.exception.PaymentResponseUnknownPaymentCodeException;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;
import me.kjs.mall.payment.nicepay.PaymentCertificationResponseDto;
import me.kjs.mall.point.Point;
import me.kjs.mall.point.PointService;
import me.kjs.mall.point.dto.PointCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderSpecificRepository orderSpecificRepository;
    private final MemberRepository memberRepository;
    private final PointService pointService;

    @Transactional(noRollbackFor = {PaymentResponseUnknownPaymentCodeException.class, PaymentCertificationNotAvailableException.class})
    public Order orderUpdateByPaymentCertificationResponse(PaymentCertificationResponseDto paymentCertificationResponseDto) {
        String moid = paymentCertificationResponseDto.getMoid();
        Order order = orderRepository.findByPaymentCode(moid).orElseThrow(PaymentResponseUnknownPaymentCodeException::new);
        order.createPaymentCertificationResponse(paymentCertificationResponseDto);
        paymentCertificationResponseAvailableCheck(order.getOrderPayment(), paymentCertificationResponseDto);
        return order;
    }

    private void paymentCertificationResponseAvailableCheck(OrderPayment orderPayment, PaymentCertificationResponseDto paymentCertificationResponseDto) {
        if (!orderPayment.getPaymentMethodOnNicePay().equals(paymentCertificationResponseDto.getPayMethod())) {
            throw new PaymentCertificationNotAvailableException();
        }
        if (!orderPayment.createSignatureByAuthToken(paymentCertificationResponseDto.getAuthToken()).equals(paymentCertificationResponseDto.getSignature())) {
            throw new PaymentCertificationNotAvailableException();
        }
    }

    @Transactional
    public Order createOrderPaymentApproveRequest(Order order) {
        Order mergeOrder = orderRepository.save(order);
        PaymentApproveRequest paymentApproveRequest = mergeOrder.createPaymentApproveRequest();
        return mergeOrder;
    }

    @Transactional
    public Order orderUpdateByPaymentApproveResponse(Order order, PaymentApproveResponseDto paymentApproveResponseDto) {
        Order mergeOrder = orderRepository.save(order);
        PaymentApproveResponse paymentApproveResponse = mergeOrder.createPaymentApproveResponse(paymentApproveResponseDto);
        String orderMemberPhoneNumber = mergeOrder.getOrderMemberPhoneNumber();
        for (OrderSpecific orderSpecific : mergeOrder.getOrderSpecifics()) {
            orderSpecific.loading();
        }
        return mergeOrder;
    }

    @Transactional
    public void paymentApproveFail(Order resultOrder) {
        Order mergeOrder = orderRepository.save(resultOrder);
        mergeOrder.paymentApproveFail();
    }

    @Transactional
    public OrderPaymentResultDto paymentApproveSuccess(Order resultOrder) {
        Order mergeOrder = orderRepository.findById(resultOrder.getId()).orElseThrow(NoExistIdException::new);
        mergeOrder.paymentApproveSuccess();
        if (mergeOrder.getPaymentMethod() != PaymentMethod.VBANK) {
            mergeOrder.paymentAccept();
        }
        if (mergeOrder.getUsePoint() > 0) {
            Point point = pointService.usePointAndRemainPoint(PointCreateDto.orderUsePointByBuy(mergeOrder, mergeOrder.getOrderMember()));
        }
        return OrderPaymentResultDto.orderToPaymentResultDto(mergeOrder);
    }

    @Transactional
    public PaymentCancelRequest createOrderPaymentCancelRequest(Long orderSpecificId, Long memberId, CancelCauseDto cancelCauseDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notOwnerThrow(orderSpecific, member);
        return getPaymentCancelRequest(cancelCauseDto, orderSpecific);
    }

    @Transactional
    public PaymentCancelRequest createOrderPaymentCancelRequest(Long orderSpecificId, CancelCauseNonMemberDto cancelCauseNonMemberDto) {
        OrderSpecific orderSpecific = orderSpecificRepository.findById(orderSpecificId).orElseThrow(NoExistIdException::new);
        if (!orderSpecific.isNonMemberOrderCancelAvailable(cancelCauseNonMemberDto.getPhoneNumber())) {
            throw new NotNonMemberOrderException();
        }
        return getPaymentCancelRequest(cancelCauseNonMemberDto, orderSpecific);
    }

    private PaymentCancelRequest getPaymentCancelRequest(CancelOrderInterface cancelOrderInterface, OrderSpecific orderSpecific) {
        if (!orderSpecific.isCancelAvailable()) {
            throw new NotAvailableCancelOrderStateException();
        }
        if (orderSpecific.getPaymentMethod() == PaymentMethod.VBANK) {
            if (CollectionTextUtil.isBlank(cancelOrderInterface.getName()) || CollectionTextUtil.isBlank(cancelOrderInterface.getAccountNo()) || cancelOrderInterface.getBank() == null) {
                throw new BadRequestException(ExceptionStatus.BAD_REQUEST, "가상계좌 결제 취소의 필수값이 입력되지 않았습니다.");
            }
        }
        Order order = orderSpecific.getOrder();
        return order.cancelOrderAndCreatePaymentCancelRequest(orderSpecific, cancelOrderInterface);
    }

    @Transactional
    public OrderCancel orderUpdateByPaymentCancelResponse(PaymentCancelRequest paymentCancelRequest, PaymentCancelResponseDto paymentCancelResponseDto) {

        OrderCancel orderCancel = orderQueryRepository.findOrderCancelById(paymentCancelRequest.getOrderCancel().getId());

        orderCancel.updateByPaymentCancelResponse(paymentCancelResponseDto);

        return orderCancel;
    }

}
