package me.kjs.mall.coupon;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.coupon.dto.CouponConditionUpdateDto;
import me.kjs.mall.coupon.dto.CouponSaveDto;
import me.kjs.mall.product.type.DiscountType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity implements AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private int discountAmount;
    private int maxDiscountPrice;
    private int minPrice;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    private int maxPeriod;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "coupon")
    @Builder.Default
    private List<CouponIssueCondition> couponIssueCondition = new ArrayList<>();

    public static Coupon createCoupon(CouponSaveDto couponSaveDto) {
        Coupon coupon = Coupon.builder()
                .title(couponSaveDto.getTitle())
                .content(couponSaveDto.getContent())
                .discountType(couponSaveDto.getDiscountType())
                .discountAmount(couponSaveDto.getDiscountAmount())
                .maxDiscountPrice(couponSaveDto.getMaxDiscountPrice())
                .minPrice(couponSaveDto.getMinPrice())
                .status(CommonStatus.CREATED)
                .maxPeriod(couponSaveDto.getMaxPeriod())
                .build();
        if (couponSaveDto.getDiscountType() == DiscountType.FLAT_RATE) {
            coupon.maxDiscountPrice = couponSaveDto.getDiscountAmount();
        }
        return coupon;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int calculateDiscountAmount(int price) {
        if (discountType == DiscountType.PERCENT) {
            int discountPrice = price * discountAmount / 100;
            if (discountPrice > maxDiscountPrice && maxDiscountPrice != 0) {
                discountPrice = maxDiscountPrice;
            }
            return discountPrice;
        }
        return discountAmount;
    }

    public boolean isUseAvailable(int price) {
        return minPrice <= price;
    }

    public int getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public Coupon loading() {
        maxPeriod = maxPeriod;
        return this;
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public boolean isModifiable() {
        return status == CommonStatus.CREATED;
    }

    public void updateCoupon(CouponSaveDto couponSaveDto) {
        title = couponSaveDto.getTitle();
        content = couponSaveDto.getContent();
        discountType = couponSaveDto.getDiscountType();
        discountAmount = couponSaveDto.getDiscountAmount();
        maxDiscountPrice = couponSaveDto.getMaxDiscountPrice();
        minPrice = couponSaveDto.getMinPrice();
        maxPeriod = couponSaveDto.getMaxPeriod();
    }

    public boolean isStatusUsedAvailable() {
        return !CollectionTextUtil.isBlank(couponIssueCondition);
    }

    public void updateStatus(CommonStatus status) {
        this.status = status;
    }

    public void delete() {
        status = CommonStatus.DELETED;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public int getMaxPeriod() {
        return maxPeriod;
    }

    public void addCouponCondition(CouponConditionUpdateDto couponConditionUpdateDto) {
        CouponIssueCondition initialize = CouponIssueCondition.initialize(this);
        initialize.updateCouponCondition(couponConditionUpdateDto);
        couponIssueCondition.add(initialize);
    }
}
