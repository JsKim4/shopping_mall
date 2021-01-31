package me.kjs.mall.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDto {
    private Long memberId;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birth;
    private AccountStatus accountStatus;
    private long pointsHeld;
    private long accumulatePoint;
    private long pointsUsed;
    private Long accountGroupId;
    private String accountGroupName;
    private String phoneNumber;

    public static MemberDetailDto memberToDetailDto(Member member) {
        return MemberDetailDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .gender(member.getGender())
                .birth(member.getBirthDate())
                .phoneNumber(member.getPhoneNumber())
                .accountStatus(member.getAccountStatus())
                .pointsHeld(member.getPointsHeld())
                .accumulatePoint(member.getAccumulatePoint())
                .pointsUsed(member.getPointsUsed())
                .accountGroupId(member.getAccountGroupId())
                .accountGroupName(member.getAccountGroupName())
                .build();
    }
}
