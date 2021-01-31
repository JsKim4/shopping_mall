package me.kjs.mall.order.specific;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderSpecificRepository extends JpaRepository<OrderSpecific, Long> {
    @Query("select os from OrderSpecific os where os.order.nonMember.name = :name and os.orderCode = :orderCode")
    Optional<OrderSpecific> findNonMemberOrderSpecificByNameAndCode(@Param("name") String name, @Param("orderCode") String orderCode);
}
