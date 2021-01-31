package me.kjs.mall.member.part;

import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.MemberBanCauseDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberBanHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_ban_history_id")
    private Long id;

    private String causeMessage;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private CommonStatus commonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberBanHistory createMemberBan(Member member, MemberBanCauseDto memberBanCauseDto, LocalDateTime startDate) {
        return MemberBanHistory.builder()
                .causeMessage(memberBanCauseDto.getCauseMessage())
                .commonStatus(CommonStatus.USED)
                .beginDate(startDate)
                .member(member)
                .build();
    }

    public void expire() {
        commonStatus = CommonStatus.UN_USED;
        endDate = LocalDateTime.now();
    }

    public boolean isAvailableCause() {
        return commonStatus == CommonStatus.USED;
    }
}
