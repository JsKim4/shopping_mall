package me.kjs.mall.product.base;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.QCategoryProduct;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.base.dto.BaseProductSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

import static me.kjs.mall.common.util.ThrowUtil.notAvailableThrow;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BaseProductQueryRepository {
    private final EntityManager em;
    private JPAQueryFactory queryFactory;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public CommonPage<BaseProduct> findBaseProductBySearchCondition(BaseProductSearchCondition baseProductSearchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (baseProductSearchCondition.getMinPrice() != 0) {
            booleanBuilder.and(QBaseProduct.baseProduct.originPrice.goe(baseProductSearchCondition.getMinPrice()));
        }
        if (baseProductSearchCondition.getMaxPrice() != 0) {
            booleanBuilder.and(QBaseProduct.baseProduct.originPrice.loe(baseProductSearchCondition.getMaxPrice()));
        }
        if (baseProductSearchCondition.getStatus() != null) {
            booleanBuilder.and(QBaseProduct.baseProduct.status.eq(baseProductSearchCondition.getStatus()));
        } else {
            booleanBuilder.and(QBaseProduct.baseProduct.status.notIn(CommonStatus.DELETED));
        }
        if (baseProductSearchCondition.getKeyword() != null) {
            booleanBuilder.and(QBaseProduct.baseProduct.name.contains(baseProductSearchCondition.getKeyword()));
        }
        if (baseProductSearchCondition.getCode() != null) {
            booleanBuilder.and(QBaseProduct.baseProduct.code.contains(baseProductSearchCondition.getCode()));
        }

        if (baseProductSearchCondition.getCategoryId() != null) {
            Category category = categoryRepository.findById(baseProductSearchCondition.getCategoryId()).orElseThrow(NoExistIdException::new);
            notAvailableThrow(category);
            booleanBuilder.and(QCategoryProduct.categoryProduct.category.eq(category));
        }

        List<Long> resultIds = queryFactory.selectDistinct(QBaseProduct.baseProduct.id)
                .from(QCategoryProduct.categoryProduct)
                .rightJoin(QCategoryProduct.categoryProduct.baseProduct, QBaseProduct.baseProduct)
                .where(booleanBuilder)
                .offset(baseProductSearchCondition.getOffset())
                .limit(baseProductSearchCondition.getLimit())
                .fetch();

        QueryResults<BaseProduct> baseProductQueryResults = queryFactory.select(QBaseProduct.baseProduct)
                .from(QBaseProduct.baseProduct)
                .where(QBaseProduct.baseProduct.id.in(resultIds))
                .orderBy(QBaseProduct.baseProduct.id.desc())
                .fetchResults();

        CommonPage<BaseProduct> commonPage = new CommonPage<>(baseProductQueryResults, baseProductSearchCondition.getPage());
        for (BaseProduct content : commonPage.getContents()) {
            content.loading();
        }
        return commonPage;
    }
}
