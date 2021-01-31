package me.kjs.mall.payment.exception;

public class PaymentApproveFailException extends PaymentException {
    public PaymentApproveFailException(String status, String message) {
        super(status, "ApproveException_" + message);
    }
}
