package me.kjs.mall.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DATE_FORMAT_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_FORMAT_YYYY_MM_DD_HH_mm = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter DATE_FORMAT_YYYY_MM_DD_HH_mm_kr = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
    private static final DateTimeFormatter DATE_FORMAT_YY = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public static int monthDistance(LocalDate end, LocalDate begin) {
        int distance = (end.getYear() - begin.getYear()) * 12 + (end.getMonthValue() - begin.getMonthValue());
        return distance > 0 ? distance : 0;
    }

    public static String formatToYYMMDDHHmmss(LocalDateTime now) {
        return DATE_FORMAT.format(now);
    }

    public static String formatToYYMMDDHHmm(LocalDateTime now) {
        return DATE_FORMAT_YYYY_MM_DD_HH_mm.format(now);
    }


    public static LocalDateTime formatYYMMDDHHMMSSToLocalDateTime(String authDate) {
        return LocalDateTime.parse(authDate, DATE_FORMAT_YY);
    }

    public static LocalDateTime formatYYYYMMDDHHMMSSToLocalDateTime(String authDate) {
        return LocalDateTime.parse(authDate, DATE_FORMAT);
    }

    public static LocalDate formatYYYYMMDDToLocalDate(String date) {
        return LocalDate.parse(date, DATE_FORMAT_YYYYMMDD);
    }

    public static String formatYYMMDDHHMMSSKR(LocalDateTime date) {
        return DATE_FORMAT_YYYY_MM_DD_HH_mm_kr.format(date);
    }
}
