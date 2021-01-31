package me.kjs.mall.partial;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.Product;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NewProductCalendar {
    @Id
    @Column(name = "new_product_calendar_id")
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "newProductCalendar")
    @Builder.Default
    private List<NewProduct> newProducts = new ArrayList<>();


    public static NewProductCalendar initialize(Integer newProductCalendarId) {
        return NewProductCalendar.builder()
                .id(newProductCalendarId)
                .build();
    }

    public void addNewProduct(Product product, LocalDate beginDate, LocalDate endDate) {
        newProducts.add(NewProduct.creatNewProduct(this, product, beginDate, endDate));
    }

    public void init() {
        for (NewProduct newProduct : newProducts) {
            newProduct.delete();
        }
        newProducts = new ArrayList<>();
    }

    public NewProductCalendar loading() {
        for (NewProduct newProduct : newProducts) {
            newProduct.loading();
        }
        return this;
    }

    public Integer getId() {
        return id;
    }

    public List<NewProduct> getNewProducts() {
        return newProducts;
    }
}
