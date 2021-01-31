package me.kjs.mall.payment.exception;

public class PaymentCertificationNotAvailableException extends PaymentException {
    public PaymentCertificationNotAvailableException() {
        super("400", "인증데이터가 올바르지 않습니다.");
    }
}
