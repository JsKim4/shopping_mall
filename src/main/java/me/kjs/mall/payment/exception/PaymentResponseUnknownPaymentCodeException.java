package me.kjs.mall.payment.exception;

public class PaymentResponseUnknownPaymentCodeException extends PaymentException {
    public PaymentResponseUnknownPaymentCodeException() {
        this("존재하지 않는 코드입니다.");
    }

    public PaymentResponseUnknownPaymentCodeException(String message) {
        super(message, "404");
    }
}
