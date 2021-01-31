package me.kjs.mall.partial;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSeller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "best_seller_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private int salesRate;
    private LocalDate date;

    public static BestSeller createBestSeller(Product product, int salesRate, LocalDate date) {
        return BestSeller.builder()
                .product(product)
                .salesRate(salesRate)
                .date(date)
                .build();
    }

    public Product getProduct() {
        return product;
    }

    public void loading() {
        product.loading();
    }
}
