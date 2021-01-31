package me.kjs.mall.api.coupon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.coupon.*;
import me.kjs.mall.coupon.dto.*;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.type.DiscountType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponApiController {

    private final CouponService couponService;

    private final CouponQueryRepository couponQueryRepository;

    private final IssueCouponService issueCouponService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto createCoupon(@RequestBody @Validated CouponSaveDto couponSaveDto,
                                    Errors errors) {
        hasErrorsThrow(errors);
        validation(couponSaveDto, errors);
        hasErrorsThrow(errors);
        Coupon coupon = couponService.createCoupon(couponSaveDto);
        CouponDto couponDto = CouponDto.couponToDto(coupon);
        return ResponseDto.created(couponDto);
    }

    @PutMapping("/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto updateCoupon(@RequestBody @Validated CouponSaveDto couponSaveDto,
                                    @PathVariable("couponId") Long couponId,
                                    Errors errors) {
        hasErrorsThrow(errors);
        validation(couponSaveDto, errors);
        hasErrorsThrow(errors);
        couponService.updateCoupon(couponSaveDto, couponId);
        return ResponseDto.noContent();
    }


    @PostMapping("/condition/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto updateCouponIssueCondition(@RequestBody @Validated CouponConditionUpdateDto couponConditionUpdateDto,
                                                  @PathVariable("couponId") Long couponId,
                                                  Errors errors) {
        hasErrorsThrow(errors);
        validation(couponConditionUpdateDto, errors);
        hasErrorsThrow(errors);
        couponService.addCouponCondition(couponConditionUpdateDto, couponId);
        return ResponseDto.noContent();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto queryCoupons(@Validated CouponSearchCondition couponSearchCondition,
                                    Errors errors) {
        hasErrorsThrow(errors);
        if (couponSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "delete notice don't query");
        }
        CommonPage<Coupon> coupons = couponQueryRepository.findCouponBySearchCondition(couponSearchCondition);
        CommonPage result = coupons.updateContent(coupons.getContents().stream().map(CouponDto::couponToDto).collect(Collectors.toList()));
        return ResponseDto.ok(result);
    }

    @GetMapping("/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto queryCoupon(@PathVariable("couponId") Long couponId) {
        Coupon coupon = couponService.findCouponById(couponId);
        CouponDto couponDto = CouponDto.couponToDto(coupon);
        return ResponseDto.ok(couponDto);
    }


    @PutMapping("/used/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto useCoupon(@PathVariable("couponId") Long couponId) {
        couponService.useCoupon(couponId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto unUseCoupon(@PathVariable("couponId") Long couponId) {
        couponService.unUseCoupon(couponId);
        return ResponseDto.noContent();
    }

    @DeleteMapping("/{couponId}")
    @PreAuthorize("hasRole('ROLE_COUPON')")
    public ResponseDto deleteCoupon(@PathVariable("couponId") Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseDto.noContent();
    }


    private void validation(CouponSaveDto couponSaveDto, Errors errors) {
        if (couponSaveDto.getDiscountType() == DiscountType.NONE) {
            errors.rejectValue("discountType", "wrong value", "coupon discountType can't NONE");
        } else if (couponSaveDto.getDiscountType() == DiscountType.PERCENT) {
            if (couponSaveDto.getDiscountAmount() > 50) {
                errors.rejectValue("discountAmount", "wrong value", "PERCENT type discountAmount can't more then 50");
            }
        } else if (couponSaveDto.getDiscountType() == DiscountType.FLAT_RATE) {
            if (couponSaveDto.getMinPrice() < couponSaveDto.getDiscountAmount() + 100) {
                errors.rejectValue("discountAmount", "wrong value", "discountAmount can't more minPrice");
            }
        }
    }

    private void validation(CouponConditionUpdateDto couponConditionUpdateDto, Errors errors) {
        if (couponConditionUpdateDto.isUseDateTime()) {
            if (couponConditionUpdateDto.getBeginDateTime().isAfter(couponConditionUpdateDto.getEndDateTime())) {
                errors.rejectValue("beginDateTime", "wrong value", "coupon beginDateTime can't after endDateTime");
            }
        }
    }


    @GetMapping("/issue/current")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto queryCoupons(IssueCouponSearchCondition issueCouponSearchCondition,
                                    @CurrentMember Member member) {
        List<IssueCoupon> coupons = issueCouponService.queryIssueCouponByMember(member);
        final CouponStatus couponStatus = issueCouponSearchCondition.getCouponStatus();
        List<IssueCouponDto> issueCoupons = coupons.stream()
                .filter(c -> c.getCouponStatus() == couponStatus)
                .map(IssueCouponDto::issueCouponToDto)
                .collect(Collectors.toList());
        return ResponseDto.ok(issueCoupons);
    }


}
