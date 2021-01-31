package me.kjs.mall.partial;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.QCategory;
import me.kjs.mall.category.QCategoryProduct;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.QProduct;
import me.kjs.mall.product.base.QBaseProduct;
import me.kjs.mall.product.dto.ProductSimpleDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductRecommendRepository {
    private final EntityManager entityManager;

    private final CategoryRepository categoryRepository;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    void setUp() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    public CommonPage<ProductSimpleDto> findRecommendProductBySearchCondition(CommonSearchCondition commonSearchCondition, Member member) {
        String categoryName = member.getTargetName();
        List<Category> categories = categoryRepository.findAllByName(categoryName);
        List<Long> baseProductIds = queryFactory.selectDistinct(QBaseProduct.baseProduct.id)
                .from(QCategoryProduct.categoryProduct)
                .rightJoin(QCategoryProduct.categoryProduct.baseProduct, QBaseProduct.baseProduct)
                .leftJoin(QCategoryProduct.categoryProduct.category, QCategory.category)
                .where(QCategory.category.in(categories))
                .fetch();

        QueryResults<Product> result = queryFactory.select(QProduct.product)
                .from(QProduct.product)
                .join(QProduct.product.baseProduct, QBaseProduct.baseProduct)
                .where(QBaseProduct.baseProduct.id.in(baseProductIds))
                .limit(commonSearchCondition.getLimit())
                .offset(commonSearchCondition.getOffset())
                .fetchResults();

        CommonPage<Product> commonPage = new CommonPage(result, commonSearchCondition.getPage());
        return commonPage.updateContent(commonPage.getContents().stream().map(ProductSimpleDto::productToSimpleDto).collect(Collectors.toList()));
    }
}
