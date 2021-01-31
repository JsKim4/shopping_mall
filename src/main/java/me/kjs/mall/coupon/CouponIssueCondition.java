package me.kjs.mall.coupon;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.coupon.dto.CouponConditionUpdateDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueCondition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_condition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_condition_id")
    private Coupon coupon;
    private boolean useDateTime;
    private LocalDateTime issueAvailableBeginDateTime;
    private LocalDateTime issueAvailableEndDateTime;


    public static CouponIssueCondition initialize(Coupon coupon) {
        return CouponIssueCondition.builder()
                .useDateTime(false)
                .issueAvailableBeginDateTime(null)
                .issueAvailableEndDateTime(null)
                .coupon(coupon)
                .build();
    }

    public boolean isStatusUsedAvailable() {
        return useDateTime;
    }

    public void updateCouponCondition(CouponConditionUpdateDto couponConditionUpdateDto) {
        updateUseDateTime(couponConditionUpdateDto);
    }

    private void updateUseDateTime(CouponConditionUpdateDto couponConditionUpdateDto) {
        if (couponConditionUpdateDto.isUseDateTime()) {
            useDateTime = true;
            issueAvailableBeginDateTime = couponConditionUpdateDto.getBeginDateTime();
            issueAvailableEndDateTime = couponConditionUpdateDto.getEndDateTime();
        } else {
            useDateTime = false;
            issueAvailableEndDateTime = null;
            issueAvailableBeginDateTime = null;
        }
    }
}
