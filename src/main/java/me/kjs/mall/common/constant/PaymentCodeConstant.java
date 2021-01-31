package me.kjs.mall.common.constant;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public final class PaymentCodeConstant {

    public static final List<String> PAYMENT_CANCEL_SUCCESS_CODES = Arrays.asList("2001", "2211");
    public static final List<String> PAYMENT_CERTIFICATION_SUCCESS_CODES = Arrays.asList("0000");
    public static final List<String> PAYMENT_APPROVE_SUCCESS_CODES = Arrays.asList("3001", "4000", "4100", "A000", "0000");
    public static final List<String> PAYMENT_ACCEPT_NOTIFY_ALLOW_IP = Arrays.asList("121.133.126.10", "121.133.126.11", "211.33.136.39");

    private PaymentCodeConstant() {
    }
}
