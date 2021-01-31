package me.kjs.mall.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("select o from Order o" +
            " join fetch o.orderPayment op" +
            " where op.paymentCode = :paymentCode")
    Optional<Order> findByPaymentCode(@Param("paymentCode") String paymentCode);
}
