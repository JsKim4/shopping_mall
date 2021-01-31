package me.kjs.mall.payment.exception;

public class NotAvailableOrderPurchaseLimitPaymentException extends PaymentException {
    public NotAvailableOrderPurchaseLimitPaymentException(int maximumPurchaseLimit, int nowMonthOrderPrice, int orderPriceExcludeDeliveryFee) {
        super("GreenStore_9999", createMessage(maximumPurchaseLimit, nowMonthOrderPrice, orderPriceExcludeDeliveryFee));
    }

    private static String createMessage(int maximumPurchaseLimit, int nowMonthOrderPrice, int orderPriceExcludeDeliveryFee) {
        return "주문 도중 구매 한도금액을 초과하였습니다.\n당월 구매 한도 : ${1} \n당월 구매 금액 : ${2} \n현재 구매 금액 : ${3}"
                .replace("${1}", String.valueOf(maximumPurchaseLimit))
                .replace("${2}", String.valueOf(nowMonthOrderPrice))
                .replace("${3}", String.valueOf(orderPriceExcludeDeliveryFee));
    }
}
