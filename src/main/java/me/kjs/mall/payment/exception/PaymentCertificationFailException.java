package me.kjs.mall.payment.exception;

public class PaymentCertificationFailException extends PaymentException {
    public PaymentCertificationFailException(String status, String message) {
        super(status, "CertificationException_" + message);
    }
}
