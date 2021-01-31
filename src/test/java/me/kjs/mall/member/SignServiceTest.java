package me.kjs.mall.member;


import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.dto.sign.MemberJoinDto;
import me.kjs.mall.member.dto.sign.MemberLoginDto;
import me.kjs.mall.member.dto.sign.PolicyAndAcceptDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.exception.join.AlreadyExistEmailException;
import me.kjs.mall.member.exception.login.IllegalPasswordLoginFailedException;
import me.kjs.mall.member.exception.login.MemberLoginWithdrawFailException;
import me.kjs.mall.member.exception.login.NoExistEmailException;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SignServiceTest extends BaseTest {
    List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
            PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
    private MemberJoinDto memberJoinDto = MemberJoinDto.builder()
            .email("jskim9")
            .password("a123456")
            .name("김준섭")
            .birth(LocalDate.of(1970, 1, 1))
            .certKey("certKey")
            .phoneNumber("01000000013")
            .gender(Gender.MALE)
            .policies(policyAndAcceptDtos)
            .build();


    @Test
    @DisplayName("Join Method_정상 가입의 경우_정상 생성됨")
    void joinEmployeeMember() {
        SIgnService.join(memberJoinDto);
        Member member = memberRepository.findByEmail(memberJoinDto.getEmail()).orElseThrow(EntityNotFoundException::new);
        assertNotNull(member.getId());
        assertEquals(member.getEmail(), memberJoinDto.getEmail());
        assertNotNull(member.getPassword());
        assertEquals(member.getName(), memberJoinDto.getName());
        assertEquals(member.getAccountStatus(), AccountStatus.ALLOW);
        assertNull(member.getWithdrawDateTime());
        assertTrue(member.getAccountRoles().contains(AccountRole.USER));
        assertNull(member.getLoginInfo().getLastLoginDateTime());
        assertNull(member.getLoginInfo().getLastTokenIssueDateTime());
        assertNotNull(member.getLoginInfo().getRefreshToken());
        assertNotNull(member.getLoginInfo().getRefreshTokenExpireDateTime());
        assertEquals(member.getPointManagement().getAccumulatePoint(), 0);
        assertEquals(member.getPointManagement().getPointsHeld(), 0);
        assertEquals(member.getPointManagement().getPointsUsed(), 0);
    }

    @Test
    @DisplayName("Join Method_이미 존재하는 이메일일 경우_AlreadyExistEmailException 발생")
    void joinEmployeeMemberAlreadyEmailFail() {
        SIgnService.join(memberJoinDto);
        AlreadyExistEmailException alreadyExistEmailException = assertThrows(AlreadyExistEmailException.class, () -> {
            SIgnService.join(memberJoinDto);
        });

        assertEquals(alreadyExistEmailException.getMessage(), "이미 등록된 이메일 입니다.");
    }


    @Test
    @DisplayName("Login Method_성공케이스")
    void loginSuccess() {
        SIgnService.join(memberJoinDto);
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();
        TokenDto tokenDto = SIgnService.login(memberLoginDto);

        assertNotNull(tokenDto.getToken());
        assertEquals(tokenDto.getTokenExpireSecond(), securityProperties.getTokenValidSecond());
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        assertEquals(member.getLoginInfo().getRefreshToken(), tokenDto.getRefreshToken());
        assertTrue(tokenDto.getRefreshTokenExpireSecond() - member.getRefreshTokenExpireSecond(LocalDateTime.now()) < 10);
        assertTrue(tokenDto.getRefreshTokenExpireSecond() > securityProperties.getRefreshTokenExpiredSecond() - 100);
        assertTrue(tokenDto.getRefreshTokenExpireSecond() <= securityProperties.getRefreshTokenExpiredSecond());
    }

    @Test
    @DisplayName("Login Method_존재하지 않는 회원_NoExistEmailException 발생")
    void loginFailNotFound() {

        SIgnService.join(memberJoinDto);
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email("NotFoundEmail@!@@@@@")
                .password(memberJoinDto.getPassword())
                .build();
        NoExistEmailException noExistEmailException = assertThrows(NoExistEmailException.class, () -> {
            TokenDto tokenDto = SIgnService.login(memberLoginDto);
        });
        assertEquals(noExistEmailException.getMessage(), "비밀번호 혹은 이메일을 확인해주세요.");
    }

    @Test
    @DisplayName("Login Mehod_비밀번호 다름_IllegalPasswordLoginFailedException 발생")
    void loginFailIllegalPassword() {
        SIgnService.join(memberJoinDto);
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password("IllegalPasswordddddddd")
                .build();
        IllegalPasswordLoginFailedException illegalPasswordLoginFailedException = assertThrows(IllegalPasswordLoginFailedException.class, () -> {
            SIgnService.login(memberLoginDto);
        });
        assertEquals(illegalPasswordLoginFailedException.getMessage(), "비밀번호 혹은 이메일을 확인해주세요.");
    }

    @Test
    @DisplayName("Login Method_회원 탈퇴 상태_MemberLoginWithdrawFailException")
    void loginFailWithdraw() {
        SIgnService.join(memberJoinDto);
        Member member = memberRepository.findByEmail(memberJoinDto.getEmail()).orElseThrow(EntityExistsException::new);
        member.memberWithdraw();
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();

        MemberLoginWithdrawFailException memberLoginWithdrawFailException = assertThrows(MemberLoginWithdrawFailException.class, () -> {
            SIgnService.login(memberLoginDto);
        });

        assertEquals(memberLoginWithdrawFailException.getMessage(), "탈퇴한 회원입니다.");
    }

}