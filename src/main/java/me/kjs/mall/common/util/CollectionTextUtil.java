package me.kjs.mall.common.util;

import java.util.Collection;

public class CollectionTextUtil {

    public static boolean isBlank(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(Collection collection) {
        return !isBlank(collection);
    }

}
