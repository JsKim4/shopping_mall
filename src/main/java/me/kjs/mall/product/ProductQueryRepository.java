package me.kjs.mall.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.QCategoryProduct;
import me.kjs.mall.common.OrderSpecifierAndEnumType;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.QBaseProduct;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.product.util.ProductSortType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<Product> findProductsBySearchCondition(ProductSearchCondition productSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (productSearchCondition.getCategoryId() != null) {
            booleanBuilder.and(QCategoryProduct.categoryProduct.category.id.eq(productSearchCondition.getCategoryId()));
        }
        if (CollectionTextUtil.isNotBlank(productSearchCondition.getKeyword())) {
            booleanBuilder.and(QBaseProduct.baseProduct.name.contains(productSearchCondition.getKeyword()));
        }
        if (CollectionTextUtil.isNotBlank(productSearchCondition.getCode())) {
            booleanBuilder.and(QBaseProduct.baseProduct.code.contains(productSearchCondition.getCode()));
        }
        List<Long> baseProducts = queryFactory.selectDistinct(QBaseProduct.baseProduct.id)
                .from(QCategoryProduct.categoryProduct)
                .rightJoin(QCategoryProduct.categoryProduct.baseProduct, QBaseProduct.baseProduct)
                .where(booleanBuilder)
                .fetch();

        BooleanBuilder secondWhereCondition = new BooleanBuilder();
        if (productSearchCondition.getStatus() != null) {
            secondWhereCondition.and(QProduct.product.status.eq(productSearchCondition.getStatus()));
        } else {
            secondWhereCondition.and(QProduct.product.status.notIn(CommonStatus.DELETED));
        }

        secondWhereCondition.and(QProduct.product.baseProduct.id.in(baseProducts));
        if (productSearchCondition.getStock() != -1) {
            secondWhereCondition.and(QProduct.product.stock.loe(productSearchCondition.getStock()));
        }


        JPAQuery<Product> format = queryFactory.select(QProduct.product)
                .from(QProduct.product)
                .join(QProduct.product.baseProduct, QBaseProduct.baseProduct)
                .where(secondWhereCondition);

        for (OrderSpecifier orderSpecifier : getProductOrderBy(productSearchCondition.getProductSortType())) {
            format = format.orderBy(orderSpecifier);
        }
        QueryResults<Product> result = format
                .limit(productSearchCondition.getLimit())
                .offset(productSearchCondition.getOffset())
                .fetchResults();
        for (Product product : result.getResults()) {
            product.loading();
        }

        return new CommonPage(result, productSearchCondition.getPage());
    }

    private final List<OrderSpecifierAndEnumType> PRODUCT_ORDER_SPECIFIERS = Arrays.asList(
            new OrderSpecifierAndEnumType(QProduct.product.baseProduct.originPrice.desc(), ProductSortType.DESC_PRICE),
            new OrderSpecifierAndEnumType(QProduct.product.baseProduct.originPrice.asc(), ProductSortType.ASC_PRICE),
            new OrderSpecifierAndEnumType(QProduct.product.baseProduct.createdDate.desc(), ProductSortType.CREATE_DATE),
            new OrderSpecifierAndEnumType(QProduct.product.baseProduct.name.asc(), ProductSortType.NAME)
    );

    private List<OrderSpecifier> getProductOrderBy(ProductSortType productSortType) {
        List<OrderSpecifierAndEnumType> orderSpecifiers = new ArrayList<>();
        OrderSpecifierAndEnumType orderSpecifierAndEnumType = PRODUCT_ORDER_SPECIFIERS.stream().filter(osaps -> osaps.getEnumType() == productSortType).findFirst().orElse(
                PRODUCT_ORDER_SPECIFIERS.get(0)
        );
        orderSpecifiers.addAll(PRODUCT_ORDER_SPECIFIERS);
        orderSpecifiers.remove(orderSpecifierAndEnumType);
        orderSpecifiers.add(0, orderSpecifierAndEnumType);
        OrderSpecifierAndEnumType deleted = null;
        for (OrderSpecifierAndEnumType orderSpecifier : orderSpecifiers) {
            if (orderSpecifier.getEnumType() == ProductSortType.ASC_PRICE || orderSpecifier.getEnumType() == ProductSortType.DESC_PRICE) {
                deleted = orderSpecifier;
            }
        }
        orderSpecifiers.remove(deleted);

        return orderSpecifiers.stream().map(OrderSpecifierAndEnumType::getOrderSpecifier).collect(Collectors.toList());
    }

    public boolean existsByBaseProduct(BaseProduct baseProduct) {
        Integer fetchFirst = queryFactory.selectOne()
                .from(QProduct.product)
                .where(QProduct.product.baseProduct.eq(baseProduct).and(
                        QProduct.product.status.notIn(CommonStatus.DELETED)
                ))
                .fetchFirst();
        return fetchFirst != null;
    }
}
