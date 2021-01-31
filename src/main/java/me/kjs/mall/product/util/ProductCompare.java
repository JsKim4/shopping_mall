package me.kjs.mall.product.util;

import me.kjs.mall.product.dto.ProductDetailDto;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ProductCompare {

    private static PriceDescSort priceDescSort;

    private static PriceAscSort priceAscSort;

    private static NameSort nameSort;

    private static CreateDateSort createDateSort;

    private static PopularitySort popularitySort;

    public static Comparator<ProductDetailDto> getProductSort(ProductSortType productSortType) {
        if (productSortType == null) {
            return getNameSort();
        }
        switch (productSortType) {
            case DESC_PRICE:
                return getPriceDescSort();
            case ASC_PRICE:
                return getPriceAscSort();
            case NAME:
                return getNameSort();
            case CREATE_DATE:
                return getCreateDateSort();
            case POPULARITY:
                return getPopularitySort();
        }
        return getNameSort();
    }

    private static PopularitySort getPopularitySort() {
        if (popularitySort == null) {
            popularitySort = new PopularitySort();
        }
        return popularitySort;
    }

    private static CreateDateSort getCreateDateSort() {
        if (createDateSort == null) {
            createDateSort = new CreateDateSort();
        }
        return createDateSort;
    }

    private static NameSort getNameSort() {
        if (nameSort == null) {
            nameSort = new NameSort();
        }
        return nameSort;
    }

    private static PriceAscSort getPriceAscSort() {
        if (priceAscSort == null) {
            priceAscSort = new PriceAscSort();
        }
        return priceAscSort;
    }

    private static PriceDescSort getPriceDescSort() {
        if (priceDescSort == null) {
            priceDescSort = new PriceDescSort();
        }
        return priceDescSort;
    }


    private final static class PriceDescSort implements Comparator<ProductDetailDto> {
        @Override
        public int compare(ProductDetailDto o1, ProductDetailDto o2) {
            int price = o1.getPrice() - o2.getPrice();
            return nameCompare(price, o1, o2);
        }
    }

    private final static class PriceAscSort implements Comparator<ProductDetailDto> {
        @Override
        public int compare(ProductDetailDto o1, ProductDetailDto o2) {
            int price = o2.getPrice() - o1.getPrice();
            return nameCompare(price, o1, o2);
        }
    }

    private final static class NameSort implements Comparator<ProductDetailDto> {
        @Override
        public int compare(ProductDetailDto o1, ProductDetailDto o2) {
            return nameCompare(0, o1, o2);
        }
    }

    private final static class CreateDateSort implements Comparator<ProductDetailDto> {
        @Override
        public int compare(ProductDetailDto o1, ProductDetailDto o2) {
            LocalDateTime firstDateTime = o1.getSalesBeginDate();
            LocalDateTime secondDateTime = o2.getSalesBeginDate();
            int num = firstDateTime.compareTo(secondDateTime);
            return nameCompare(num, o1, o2);
        }
    }

    private final static class PopularitySort implements Comparator<ProductDetailDto> {
        @Override
        public int compare(ProductDetailDto o1, ProductDetailDto o2) {
            int num = o1.getOrderCount() - o2.getOrderCount();
            return nameCompare(num, o1, o2);
        }
    }

    private static int nameCompare(int num, ProductDetailDto o1, ProductDetailDto o2) {
        if (num == 0) {
            return o1.getBaseProduct().getName().compareTo(o2.getBaseProduct().getName());
        }
        return num;
    }


}
