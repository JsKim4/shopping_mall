package me.kjs.mall.cart;

import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findTopByMemberAndProduct(Member member, Product product);

    List<Cart> findAllByMember(Member member);
}
