package me.kjs.mall.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.Member;
import me.kjs.mall.point.PointSpecific;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointSpecificAndAmountDto {
    private PointSpecific pointSpecific;
    private int amount;

    public Member getMember() {
        return pointSpecific.getMember();
    }

    public LocalDate getPointExpiredDate() {
        return pointSpecific.getRemainPointExpireDate();
    }

    public int getRealAmount() {
        return -amount;
    }
}
