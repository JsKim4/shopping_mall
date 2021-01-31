package me.kjs.mall.api.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.constant.PaymentCodeConstant;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.configs.properties.PaymentProperties;
import me.kjs.mall.connect.ConnectService;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.OrderService;
import me.kjs.mall.order.cancel.OrderCancel;
import me.kjs.mall.order.cancel.PaymentCancelRequest;
import me.kjs.mall.order.cancel.dto.CancelCauseDto;
import me.kjs.mall.order.cancel.dto.CancelCauseNonMemberDto;
import me.kjs.mall.order.cancel.dto.PaymentCancelResponseDto;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.exception.PaymentCancelFailException;
import me.kjs.mall.order.payment.OrderPayment;
import me.kjs.mall.order.payment.PaymentApproveResponse;
import me.kjs.mall.order.payment.PaymentCertificationResponse;
import me.kjs.mall.order.payment.dto.OrderPaymentResultDto;
import me.kjs.mall.order.payment.dto.PaymentVirtualBankNotifyDto;
import me.kjs.mall.payment.PaymentService;
import me.kjs.mall.payment.exception.PaymentApproveFailException;
import me.kjs.mall.payment.exception.PaymentCertificationFailException;
import me.kjs.mall.payment.nicepay.PaymentApproveRequestDto;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;
import me.kjs.mall.payment.nicepay.PaymentCancelRequestDto;
import me.kjs.mall.payment.nicepay.PaymentCertificationResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentApiController {

    private final PaymentService paymentService;
    private final ConnectService connectService;
    private final OrderService orderService;

    @RequestMapping("/vbank/notify")
    @ResponseBody
    public String virtualBankPaymentNotify(PaymentVirtualBankNotifyDto paymentVirtualBankNotifyDto) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();
        log.info(ip);
        if (!PaymentCodeConstant.PAYMENT_ACCEPT_NOTIFY_ALLOW_IP.contains(ip)) {
            return "NO";
        }

        log.info("paymentVirtualBankNotifyDto : {}", paymentVirtualBankNotifyDto);
        Order order = orderService.virtualBankPaymentAccept(paymentVirtualBankNotifyDto);

        connectService.sendPaymentAcceptSms(order.getOrderMemberPhoneNumber(), order.getOrderName(), order.getOrderCodes().toString(), order.getMemberName(), order.getOrderPayment().getTotalPaymentPrice());

        return "OK";
    }

    @PutMapping("/cancel/non/{orderSpecificId}")
    @ResponseBody
    public ResponseDto paymentNonMemberCancel(@PathVariable("orderSpecificId") Long orderSpecificId,
                                              @RequestBody CancelCauseNonMemberDto cancelCauseNonMemberDto) throws JsonProcessingException {
        PaymentCancelRequest paymentCancelRequest = paymentService.createOrderPaymentCancelRequest(orderSpecificId, cancelCauseNonMemberDto);

        OrderCancel orderCancel = paymentCancel(paymentCancelRequest);

        orderService.successOrderCancel(orderCancel);

        return ResponseDto.noContent();
    }

    @PutMapping("/cancel/{orderSpecificId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public ResponseDto paymentCancel(@PathVariable("orderSpecificId") Long orderSpecificId,
                                     @RequestBody CancelCauseDto cancelCauseDto,
                                     @CurrentMember Member member) throws JsonProcessingException {
        PaymentCancelRequest paymentCancelRequest = paymentService.createOrderPaymentCancelRequest(orderSpecificId, member.getId(), cancelCauseDto);

        OrderCancel orderCancel = paymentCancel(paymentCancelRequest);

        orderService.successOrderCancel(orderCancel);

        return ResponseDto.noContent();
    }

    private OrderCancel paymentCancel(PaymentCancelRequest paymentCancelRequest) throws JsonProcessingException {
        PaymentCancelResponseDto paymentCancelResponseDto = connectService.paymentCancelRequest(PaymentCancelRequestDto.paymentCancelRequestToDto(paymentCancelRequest));

        OrderCancel orderCancel = paymentService.orderUpdateByPaymentCancelResponse(paymentCancelRequest, paymentCancelResponseDto);

        if (!orderCancel.isOrderCancelSuccess()) {
            throw new PaymentCancelFailException(orderCancel.getFailMessage());
        }
        return orderCancel;
    }


    @PutMapping("/cancel/wait/{orderSpecificId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public ResponseDto paymentWaitCancel(@PathVariable("orderSpecificId") Long orderSpecificId,
                                         @RequestBody CancelCauseDto cancelCauseDto,
                                         @CurrentMember Member member) throws JsonProcessingException {

        orderService.paymentWaitCancel(orderSpecificId, member.getId());

        return ResponseDto.noContent();
    }


    @CrossOrigin("*")
    @PostMapping("/certification")
    public ModelAndView paymentCertificationResult(PaymentCertificationResponseDto paymentCertificationResponseDto, ModelAndView modelAndView) throws IOException {

        log.info("===================================");
        log.info("paymentCertificationResponseDto : {}", paymentCertificationResponseDto);
        log.info("===================================");

        Order order = paymentService.orderUpdateByPaymentCertificationResponse(paymentCertificationResponseDto);
        if (!order.isCertificationResponseSuccess()) {
            log.info("Order Certification Fail");
            paymentCertificationFail(order);
        }
        Order resultOrder = paymentApproveRequest(order);
        if (!resultOrder.isApproveResponseSuccess()) {
            paymentApproveFail(resultOrder);
        }
        log.info("Order Success");
        OrderPaymentResultDto paymentResultDto = paymentApproveSuccess(resultOrder);

        if (resultOrder.getPaymentMethod() != PaymentMethod.VBANK) {
            connectService.sendPaymentAcceptSms(resultOrder.getOrderMemberPhoneNumber(), resultOrder.getOrderName(), resultOrder.getOrderCodes().toString(), resultOrder.getMemberName(), resultOrder.getPaymentPrice());
        } else {
            connectService.sendPaymentBankInfoSms(resultOrder.getOrderMemberPhoneNumber(), resultOrder.getMemberName(), paymentResultDto.getVirtualBankResult(), resultOrder.getPaymentPrice(), resultOrder.getOrderName(), resultOrder.getOrderCodes().toString());
        }
        modelAndView.addObject("status", ExceptionStatus.SUCCESS.getStatus());
        modelAndView.addObject("message", ExceptionStatus.SUCCESS.getMessage());
        modelAndView.addObject("data", paymentResultDto);
        modelAndView.setViewName(PaymentProperties.getRedirectResultPage());
        return modelAndView;
    }


    private void paymentCertificationFail(Order order) {
        OrderPayment orderPayment = order.getOrderPayment();
        PaymentCertificationResponse paymentCertificationResponse = orderPayment.getPaymentCertificationResponse();
        throw new PaymentCertificationFailException(
                paymentCertificationResponse.getResultCode(),
                paymentCertificationResponse.getResultMessage()
        );
    }


    private OrderPaymentResultDto paymentApproveSuccess(Order resultOrder) {
        return paymentService.paymentApproveSuccess(resultOrder);
    }

    private void paymentApproveFail(Order resultOrder) {
        paymentService.paymentApproveFail(resultOrder);
        OrderPayment orderPayment = resultOrder.getOrderPayment();
        PaymentApproveResponse paymentApproveResponse = orderPayment.getPaymentApproveResponse();
        throw new PaymentApproveFailException(
                paymentApproveResponse.getResultCode(),
                paymentApproveResponse.getResultMessage()
        );
    }

    private Order paymentApproveRequest(Order order) throws JsonProcessingException {
        order = paymentService.createOrderPaymentApproveRequest(order);

        PaymentApproveRequestDto paymentApproveRequestDto = PaymentApproveRequestDto.orderPaymentApproveRequestToDto(order.getOrderPayment());
        log.info("Order Approve Request : {}", paymentApproveRequestDto);
        PaymentApproveResponseDto paymentApproveResponseDto = connectService.paymentApproveRequest(paymentApproveRequestDto);
        log.info("Order Approve Response : {}", paymentApproveResponseDto);
        log.info("Order Approve update");
        return paymentService.orderUpdateByPaymentApproveResponse(order, paymentApproveResponseDto);
    }

}
