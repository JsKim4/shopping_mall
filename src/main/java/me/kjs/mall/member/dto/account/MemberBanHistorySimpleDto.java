package me.kjs.mall.member.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.part.MemberBanHistory;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberBanHistorySimpleDto {

    private String causeMessage;
    private LocalDateTime beginDate;


    public static MemberBanHistorySimpleDto memberBanHistoryToSimpleDto(MemberBanHistory memberBanHistory) {
        return MemberBanHistorySimpleDto.builder()
                .causeMessage(memberBanHistory.getCauseMessage())
                .beginDate(memberBanHistory.getBeginDate())
                .build();
    }
}
