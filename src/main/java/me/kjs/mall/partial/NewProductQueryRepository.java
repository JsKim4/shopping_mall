package me.kjs.mall.partial;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.partial.dto.NewProductSearchCondition;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.QProduct;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewProductQueryRepository {
    private final EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    public void setUp() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<Product> findByNowDateAndSearchCondition(LocalDate date, NewProductSearchCondition newProductSearchCondition) {
        QueryResults<Product> products = jpaQueryFactory.select(QNewProduct.newProduct.product)
                .from(QNewProduct.newProduct)
                .join(QNewProduct.newProduct.product, QProduct.product)
                .where(QNewProduct.newProduct.beginDateTime.loe(date.atTime(0, 1)).and(
                        QNewProduct.newProduct.endDateTime.goe(date.atTime(0, 1))
                ))
                .orderBy(QNewProduct.newProduct.newProductCalendar.id.asc())
                .offset(newProductSearchCondition.getOffset())
                .limit(newProductSearchCondition.getLimit())
                .fetchResults();
        for (Product product : products.getResults()) {
            product.loading();
        }
        return new CommonPage(products, newProductSearchCondition.getPage());
    }
}
