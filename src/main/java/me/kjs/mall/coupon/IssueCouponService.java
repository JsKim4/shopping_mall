package me.kjs.mall.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class IssueCouponService {

    private final IssueCouponRepository issueCouponRepository;

    public List<IssueCoupon> queryIssueCouponByMember(Member member) {
        List<IssueCoupon> issueCoupons = issueCouponRepository.findAllByMember(member);
        for (IssueCoupon issueCoupon : issueCoupons) {
            issueCoupon.loading();
        }
        return AvailableUtil.isUsedFilter(issueCoupons);
    }
}
