package me.kjs.mall.member.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.point.Point;
import me.kjs.mall.point.PointState;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointManagement {

    private long pointsHeld;
    private long accumulatePoint;
    private long pointsUsed;

    public static PointManagement initialize() {
        return PointManagement.builder()
                .accumulatePoint(0L)
                .accumulatePoint(0L)
                .pointsUsed(0L)
                .build();
    }

    public void accumPoint(Point save) {
        pointsHeld += save.getAmount();
        accumulatePoint += save.getAmount();
    }

    public void usePoint(Point save) {
        pointsHeld += save.getAmount();
        pointsUsed += save.getAmount();
    }

    public void rollbackPoint(Point point) {
        if (point.getState() == PointState.USE) {
            pointsUsed -= point.getAmount();
            pointsHeld -= point.getAmount();
        } else if (point.getState() == PointState.ACCUMULATE) {
            if (point.getState() == PointState.ACCUMULATE) {
                accumulatePoint -= point.getAmount();
                pointsHeld -= point.getAmount();
            }
        }
    }
}
