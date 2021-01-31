package me.kjs.mall.coupon;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.type.DiscountType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueCoupon extends BaseEntity implements OwnerCheck, AvailableCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_coupon_id")
    private Long id;

    private LocalDate expiredDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public static IssueCoupon issueCoupon(Coupon coupon, Member member, LocalDate expiredDate) {
        return IssueCoupon.builder()
                .expiredDate(expiredDate)
                .couponStatus(CouponStatus.ISSUE)
                .status(CommonStatus.USED)
                .member(member)
                .coupon(coupon)
                .build();
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public boolean isUseAvailable(int price) {
        return status == CommonStatus.USED &&
                getCouponStatus() == CouponStatus.ISSUE &&
                coupon.isUseAvailable(price);
    }

    public void use() {
        couponStatus = CouponStatus.USED;
    }

    public CouponStatus getCouponStatus() {
        if (couponStatus == CouponStatus.ISSUE && expiredDate.isBefore(LocalDate.now())) {
            return CouponStatus.EXPIRED;
        }
        return couponStatus;
    }

    @Override
    public boolean isOwner(Member member) {
        return member == this.member;
    }

    public DiscountType getDiscountType() {
        return coupon.getDiscountType();
    }

    public int getDiscountAmount() {
        return coupon.getDiscountAmount();
    }

    public int calculateDiscountAmount(int price) {
        return coupon.calculateDiscountAmount(price);
    }

    public int getMaxDiscountPrice() {
        return coupon.getMaxDiscountPrice();
    }

    public int getMinPrice() {
        return coupon.getMinPrice();
    }

    public IssueCoupon loading() {
        coupon.loading();
        return this;
    }

    public String getTitle() {
        return coupon.getTitle();
    }

    public String getContent() {
        return coupon.getContent();
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public int getRemainUsePeriod() {
        Period period = Period.between(LocalDate.now(), expiredDate);
        return period.getDays() + 1;
    }
}