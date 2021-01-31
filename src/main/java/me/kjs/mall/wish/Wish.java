package me.kjs.mall.wish;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Wish extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Wish createWish(Member member, Product product) {
        return Wish.builder()
                .member(member)
                .product(product)
                .build();
    }

    public Wish loading() {
        product.loading();
        return this;
    }

    public Long getProductId() {
        return product.getId();
    }

    public String getProductName() {
        return product.getBaseProductName();
    }

    public int getDiscountPercent() {
        return product.getDiscountPercent();
    }

    public int getOriginPrice() {
        return product.getOriginPrice();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public List<String> getThumbnail() {
        return product.getBaseProductThumbnailImage();
    }
}