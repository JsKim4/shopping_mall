package me.kjs.mall.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.coupon.dto.CouponConditionUpdateDto;
import me.kjs.mall.coupon.dto.CouponSaveDto;
import me.kjs.mall.coupon.exception.NotAvailableCouponUpdateException;
import me.kjs.mall.coupon.exception.NotAvailableModifyCouponUseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final IssueCouponRepository issueCouponRepository;

    private final CouponRepository couponRepository;

    private final CouponQueryRepository couponQueryRepository;

    @Transactional
    public Coupon createCoupon(CouponSaveDto couponSaveDto) {
        Coupon coupon = Coupon.createCoupon(couponSaveDto);
        return couponRepository.save(coupon);
    }

    public Coupon findCouponById(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notAvailableThrow(coupon);
        return coupon;
    }

    @Transactional
    public void updateCoupon(CouponSaveDto couponSaveDto, Long couponId) {
        Coupon coupon = findCouponById(couponId);
        if (!coupon.isModifiable()) {
            throw new NotAvailableCouponUpdateException();
        }
        coupon.updateCoupon(couponSaveDto);
    }

    @Transactional
    public void useCoupon(Long couponId) {
        Coupon coupon = findCouponById(couponId);
        if (!coupon.isStatusUsedAvailable()) {
            throw new NotAvailableModifyCouponUseStatusException();
        }
        coupon.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseCoupon(Long couponId) {
        Coupon coupon = findCouponById(couponId);
        coupon.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = findCouponById(couponId);
        coupon.delete();
    }

    @Transactional
    public void addCouponCondition(CouponConditionUpdateDto couponConditionUpdateDto, Long couponId) {
        Coupon coupon = findCouponById(couponId);
        if (!coupon.isModifiable()) {
            throw new NotAvailableCouponUpdateException();
        }
        coupon.addCouponCondition(couponConditionUpdateDto);
    }
}
