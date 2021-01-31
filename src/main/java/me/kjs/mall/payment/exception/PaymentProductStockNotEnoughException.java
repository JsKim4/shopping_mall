package me.kjs.mall.payment.exception;

public class PaymentProductStockNotEnoughException extends PaymentException {
    public PaymentProductStockNotEnoughException() {
        this("GreenStore_9999", "주문 도중 재고량이 감소하여 주문이 실패 하였습니다.");
    }

    private PaymentProductStockNotEnoughException(String status, String message) {
        super(status, message);
    }
}
