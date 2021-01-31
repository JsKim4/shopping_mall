package me.kjs.mall.partial;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NewProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_product_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private LocalDateTime beginDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_product_calendar_id")
    private NewProductCalendar newProductCalendar;

    public static NewProduct creatNewProduct(NewProductCalendar newProductCalendar, Product product, LocalDate beginDate, LocalDate endDate) {
        return NewProduct.builder()
                .newProductCalendar(newProductCalendar)
                .product(product)
                .beginDateTime(beginDate.atTime(0, 0))
                .endDateTime(endDate.plusDays(1).atTime(0, 0))
                .build();

    }

    public void delete() {
        newProductCalendar = null;
    }

    public void loading() {
        product.loading();
    }

    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    public LocalDate getBeginDate() {
        return beginDateTime.toLocalDate();
    }

    public Product getProduct() {
        return product;
    }
}
