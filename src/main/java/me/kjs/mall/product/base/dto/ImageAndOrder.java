package me.kjs.mall.product.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ImageAndOrder implements Comparable<ImageAndOrder> {
    private String image;
    private int sortOrder;

    public static ImageAndOrder createImageAndOrder(String s, int i) {
        return ImageAndOrder.builder()
                .image(s)
                .sortOrder(i)
                .build();
    }

    @Override
    public int compareTo(ImageAndOrder o) {
        return sortOrder - o.getSortOrder();
    }
}
