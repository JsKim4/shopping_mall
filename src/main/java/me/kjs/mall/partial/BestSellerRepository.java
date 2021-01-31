package me.kjs.mall.partial;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {
    List<BestSeller> findAllByDateEqualsOrderBySalesRateDesc(LocalDate date, Pageable pageable);

    @Query("select max(bs.date) from BestSeller bs")
    Optional<LocalDate> findMaxDate();
}
