package me.kjs.mall.common.util;

public class CompareUtil {
    public static int descCompare(long a, long b) {
        return compare(a, b) * (-1);
    }

    public static int ascCompare(long a, long b) {
        return compare(a, b);
    }

    private static int compare(long a, long b) {
        long l = a - b;
        if (l > 0) {
            return 1;
        } else if (l < 0) {
            return -1;
        }
        return 0;
    }

    public static boolean equals(Long idA, Long idB) {
        if (idA == null || idB == null) {
            return false;
        }
        return (idA - idB) == 0;
    }
}
