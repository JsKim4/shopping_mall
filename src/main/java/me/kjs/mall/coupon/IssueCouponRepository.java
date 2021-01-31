package me.kjs.mall.coupon;

import me.kjs.mall.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueCouponRepository extends JpaRepository<IssueCoupon, Long> {
    List<IssueCoupon> findAllByMember(Member member);

}
