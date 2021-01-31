package me.kjs.mall.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CodeGeneratorUtil {

    static int spareKey = 0;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String orderItemCodeGenerate() {
        int n = ++spareKey;
        initSpare();
        LocalDateTime now = LocalDateTime.now();
        return "I" + getYYMMDDHHmmssSSS(now, n);
    }

    public static String orderSpecificCodeGenerate() {
        int n = ++spareKey;
        initSpare();
        LocalDateTime now = LocalDateTime.now();
        return "O" + getYYMMDDHHmmssSSS(now, n);
    }

    public static String paymentCodeGenerate() {
        int n = ++spareKey;
        initSpare();
        LocalDateTime now = LocalDateTime.now();
        return "P" + getYYMMDDHHmmssSSS(now, n);
    }

    private static String getYYMMDDHHmmssSSS(LocalDateTime now, int n) {
        StringBuilder s = new StringBuilder();
        for (int i = 10; n < i; i /= 10) {
            s.append("0");
        }
        return DATE_FORMAT.format(now) + s.toString() + n;
    }

    private static void initSpare() {
        if (spareKey > 98)
            spareKey = 0;
    }


    public static String orderCancelCodeGenerate() {
        int n = ++spareKey;
        initSpare();
        LocalDateTime now = LocalDateTime.now();
        return "C" + getYYMMDDHHmmssSSS(now, n);
    }
}
