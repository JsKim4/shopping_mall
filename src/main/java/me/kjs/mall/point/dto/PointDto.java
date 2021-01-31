package me.kjs.mall.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.point.Point;
import me.kjs.mall.point.PointKind;
import me.kjs.mall.point.PointState;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {

    private Long pointId;
    private PointState pointState;
    private PointKind pointKind;
    private int amount;
    private LocalDate expiredDate;
    private int remainExpiredDate;
    private String content;
    private LocalDateTime pointCreateDateTime;

    public static PointDto pointToDto(Point point) {
        return PointDto.builder()
                .pointId(point.getId())
                .pointState(point.getState())
                .pointKind(point.getKind())
                .amount(point.getAmount())
                .expiredDate(point.getPointExpiredDate())
                .remainExpiredDate(point.getRemainExpiredDate())
                .content(point.getContent())
                .pointCreateDateTime(point.getPointCreateDateTime())
                .build();
    }
}
