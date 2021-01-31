package me.kjs.mall.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.point.dto.PointCreateDto;
import me.kjs.mall.point.dto.PointGiveDto;
import me.kjs.mall.point.dto.PointSpecificAndAmountDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private int amount;
    @Enumerated(EnumType.STRING)
    private PointState pointState;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;
    private LocalDateTime pointCreateDateTime;
    private LocalDate pointExpiredDate;
    @Enumerated(EnumType.STRING)
    private PointKind pointKind;
    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PointSpecific> pointSpecifics = new ArrayList<>();

    private String misc;

    public static Point accumPointCreateByCreateDto(PointCreateDto pointCreateDto) {
        Point point = Point.builder()
                .member(pointCreateDto.getMember())
                .amount(pointCreateDto.getRealAmount())
                .pointState(pointCreateDto.getPointState())
                .orderProduct(pointCreateDto.getOrderProduct())
                .pointExpiredDate(LocalDate.now().plusYears(1))
                .pointCreateDateTime(LocalDateTime.now())
                .pointKind(pointCreateDto.getPointKind())
                .misc(pointCreateDto.getMisc())
                .build();
        PointSpecific pointSpecific = PointSpecific.createAccumPoint(point);
        point.pointSpecifics.add(pointSpecific);
        return point;
    }

    public static Point usePoint(PointCreateDto pointCreateDto, List<PointSpecific> pointSpecifics) {
        Point point = Point.builder()
                .member(pointCreateDto.getMember())
                .amount(pointCreateDto.getRealAmount())
                .pointState(pointCreateDto.getPointState())
                .order(pointCreateDto.getOrder())
                .pointExpiredDate(LocalDate.now().plusYears(1))
                .pointCreateDateTime(LocalDateTime.now())
                .pointKind(pointCreateDto.getPointKind())
                .misc(pointCreateDto.getMisc())
                .build();
        int remainPoint = pointCreateDto.getAmount();
        for (PointSpecific pointSpecific : pointSpecifics) {
            if (pointSpecific.getPointState() == PointState.ACCUMULATE && remainPoint > 0) {
                PointSpecific usePointSpecific = pointSpecific.usePoint(remainPoint, point);
                remainPoint += usePointSpecific.getAmount();
                point.pointSpecifics.add(usePointSpecific);
            }
        }
        return point;
    }

    public static Point givePoint(Member member, PointGiveDto pointGiveDto) {
        Point point = Point.builder()
                .member(member)
                .amount(pointGiveDto.getAmount())
                .pointState(PointState.ACCUMULATE)
                .pointExpiredDate(pointGiveDto.getExpiredDate())
                .pointKind(PointKind.ADMIN_MEDIATE_ACCUMULATE)
                .pointCreateDateTime(LocalDateTime.now())
                .misc(pointGiveDto.getMisc())
                .build();
        PointSpecific pointSpecific = PointSpecific.createAccumPoint(point);
        point.pointSpecifics.add(pointSpecific);
        return point;
    }

    public static Point expiredPoint(PointSpecificAndAmountDto pointSpecificAndAmountDto) {
        Point point = Point.builder()
                .member(pointSpecificAndAmountDto.getMember())
                .misc("포인트 유효기간 만료 소멸")
                .amount(pointSpecificAndAmountDto.getRealAmount())
                .pointState(PointState.EXPIRED)
                .pointKind(PointKind.POINT_EXPIRED)
                .pointExpiredDate(pointSpecificAndAmountDto.getPointExpiredDate())
                .pointCreateDateTime(LocalDateTime.now())
                .build();
        PointSpecific expiredPoint1 = PointSpecific.expiredPoint(point, pointSpecificAndAmountDto.getPointSpecific());
        point.pointSpecifics.add(expiredPoint1);
        return point;
    }

    public Long getId() {
        return id;
    }

    public PointState getState() {
        return pointState;
    }

    public PointKind getKind() {
        return pointKind;
    }

    public int getAmount() {
        return amount;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getPointExpiredDate() {
        return pointExpiredDate;
    }

    public int getRemainExpiredDate() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), pointExpiredDate);
        return Math.max((int) days, 0);
    }

    public String getContent() {
        return "[" + pointState.getDescription() + "] " + misc;
    }

    public LocalDateTime getPointCreateDateTime() {
        return pointCreateDateTime;
    }

    public List<PointSpecific> getUsedList() {
        return pointSpecifics;
    }

    public void updatePointAmount(int amount) {
        this.amount = amount;
    }

    public void deletePointSpecific(PointSpecific pointSpecific) {
        pointSpecifics.remove(pointSpecific);
    }
}
