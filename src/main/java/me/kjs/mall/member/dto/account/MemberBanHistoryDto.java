package me.kjs.mall.member.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.member.part.MemberBanHistory;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberBanHistoryDto {

    private Long banHistoryId;
    private String causeMessage;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private CommonStatus commonStatus;


    public static MemberBanHistoryDto memberBanHistoryToDto(MemberBanHistory memberBanHistory) {
        return MemberBanHistoryDto.builder()
                .banHistoryId(memberBanHistory.getId())
                .causeMessage(memberBanHistory.getCauseMessage())
                .beginDate(memberBanHistory.getBeginDate())
                .endDate(memberBanHistory.getEndDate())
                .commonStatus(memberBanHistory.getCommonStatus())
                .build();
    }
}
