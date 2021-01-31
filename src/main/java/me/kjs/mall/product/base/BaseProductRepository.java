package me.kjs.mall.product.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BaseProductRepository extends JpaRepository<BaseProduct, Long> {
    boolean existsByCode(String code);

    @Query("select bp from BaseProduct bp join bp.productTags " +
            " where bp.status <> 'DELETED' ")
    List<BaseProduct> findAllFetch();

    @Query("select bp from BaseProduct bp join bp.productTags where bp.id = :id")
    Optional<BaseProduct> findByIdFetchEager(@Param("id") Long baseProductId);
}
