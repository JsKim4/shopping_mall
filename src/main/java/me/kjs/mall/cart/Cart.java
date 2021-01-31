package me.kjs.mall.cart;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cart extends BaseEntity implements OwnerCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int quantity;

    public static Cart createCart(Product product, Member member) {
        return Cart.builder()
                .product(product)
                .member(member)
                .quantity(0)
                .build();
    }

    @Override
    public boolean isOwner(Member member) {
        return this.member == member;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart loading() {
        product.loading();
        return this;
    }

    public List<String> getThumbnail() {
        return product.getBaseProductThumbnailImage();
    }

    public String getName() {
        return product.getBaseProductName();
    }

    public int getDiscountPercent() {
        return product.getDiscountPercent();
    }

    public int getDiscountPrice() {
        return product.getDiscountPrice();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public int getOriginPrice() {
        return product.getOriginPrice();
    }

    public int getStock() {
        return product.getStock();
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getSumDiscountPrice() {
        return product.getDiscountPrice() * quantity;
    }

    public int getSumPrice() {
        return product.getPrice() * quantity;
    }

    public int getSumOriginPrice() {
        return product.getOriginPrice() * quantity;
    }
}
