package me.kjs.mall.payment.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {
    private final String status;

    public PaymentException(String status, String message) {
        super(message);
        this.status = status;
    }

    public String getFormattingMessage() {
        return super.getMessage().replace(" ", "%20");
    }
}
