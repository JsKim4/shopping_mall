package me.kjs.mall.partial;

import me.kjs.mall.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BestReviewProductRepository extends JpaRepository<BestReviewProduct, Long> {
    boolean existsByProduct(Product product);

    @Query("select max(brp.orders) from BestReviewProduct brp")
    Optional<Integer> findMaxOrders();

    Optional<BestReviewProduct> findByProduct(Product product);
}
