package me.kjs.mall.order;

import me.kjs.mall.order.specific.product.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
