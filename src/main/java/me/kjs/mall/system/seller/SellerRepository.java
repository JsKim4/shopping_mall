package me.kjs.mall.system.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, String> {
    @Query("select s from Seller s")
    Optional<Seller> findTop();
}
