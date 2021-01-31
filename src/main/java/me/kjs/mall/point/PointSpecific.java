package me.kjs.mall.point;

import lombok.*;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointSpecific {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_detail_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;
    @Enumerated(EnumType.STRING)
    private PointState pointState;
    @Enumerated(EnumType.STRING)
    private PointKind pointKind;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_cancel_specific_id")
    private PointSpecific cancelSpecific;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_accum_specific_id")
    private PointSpecific accumSpecific;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accumSpecific")
    @Builder.Default
    private List<PointSpecific> usedList = new ArrayList<>();
    private LocalDate remainPointExpireDate;
    private int amount;


    public static PointSpecific createAccumPoint(Point point) {
        PointSpecific pointSpecific = PointSpecific.builder()
                .member(point.getMember())
                .point(point)
                .pointState(point.getState())
                .pointKind(point.getKind())
                .remainPointExpireDate(point.getPointExpiredDate())
                .amount(point.getAmount())
                .build();
        pointSpecific.accumSpecific = pointSpecific;
        pointSpecific.cancelSpecific = pointSpecific;
        pointSpecific.usedList.add(pointSpecific);
        return pointSpecific;
    }

    public static PointSpecific expiredPoint(Point point, PointSpecific pointSpecific) {
        PointSpecific expiredPoint = PointSpecific.builder()
                .member(point.getMember())
                .point(point)
                .pointState(PointState.EXPIRED)
                .pointKind(PointKind.POINT_EXPIRED)
                .remainPointExpireDate(point.getPointExpiredDate())
                .amount(point.getAmount())
                .build();
        expiredPoint.accumSpecific = pointSpecific;
        expiredPoint.cancelSpecific = pointSpecific;
        pointSpecific.usedList.add(expiredPoint);
        return expiredPoint;
    }

    public PointSpecific usePoint(int remainPoint, Point point) {
        int pointRemain = 0;
        for (PointSpecific pointSpecific : usedList) {
            pointRemain += pointSpecific.getAmount();
        }
        int amount = remainPoint;
        if (pointRemain < remainPoint) {
            amount = pointRemain;
        }
        PointSpecific pointSpecific = PointSpecific.builder()
                .member(point.getMember())
                .point(point)
                .pointState(point.getState())
                .pointKind(point.getKind())
                .remainPointExpireDate(remainPointExpireDate)
                .accumSpecific(this)
                .cancelSpecific(this)
                .amount(-amount)
                .build();
        usedList.add(pointSpecific);
        return pointSpecific;
    }


    public void updatePointAmount(int updatePointAmount) {
        this.amount = updatePointAmount;
    }

    public void delete() {
        point.deletePointSpecific(this);
        point = null;
    }
}
