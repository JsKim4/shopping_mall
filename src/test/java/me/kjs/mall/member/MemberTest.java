package me.kjs.mall.member;

import me.kjs.mall.common.type.YnType;
import me.kjs.mall.configs.properties.SecurityProperties;
import me.kjs.mall.member.dto.sign.MemberJoinDto;
import me.kjs.mall.member.dto.sign.PolicyAndAcceptDto;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import me.kjs.mall.security.JwtTokenProvider;
import me.kjs.mall.security.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Member Class is")
class MemberTest {
    @Nested
    @DisplayName("Join Method")
    class join_member {

        @Nested
        @DisplayName("정상 가입의 경우")
        class current_join {


            List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                    PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
            private AccountGroup accountGroup = AccountGroup.builder().build();

            private Set<AccountRole> accountRoles = new HashSet<>();
            private MemberJoinDto successJoinDto = MemberJoinDto.builder()
                    .email("jskim")
                    .password("a123456")
                    .name("김준섭")
                    .policies(policyAndAcceptDtos)
                    .birth(LocalDate.of(1970, 1, 1))
                    .certKey("certKey")
                    .phoneNumber("01000000012")
                    .gender(Gender.MALE)
                    .build();

            @Test
            @DisplayName("정상 생성됨")
            void joinMember() {
                accountRoles.add(AccountRole.USER);
                accountGroup.getRoles().addAll(accountRoles);
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                Member member = Member.join(successJoinDto, accountGroup, passwordEncoder);
                assertNull(member.getId());
                assertEquals(member.getEmail(), successJoinDto.getEmail());
                assertTrue(passwordEncoder.matches(successJoinDto.getPassword(), member.getPassword()));
                assertNotNull(member.getPassword());
                assertEquals(member.getName(), successJoinDto.getName());
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
        }
    }


    @Nested
    @DisplayName("TokenRefresh Method")
    class TokenRefresh {
        Member employee = null;
        Member friend = null;
        String password = "a123456";
        PasswordEncoder passwordEncoder;

        LoginService loginService = new LoginService(null);

        SecurityProperties securityProperties = new SecurityProperties();

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(securityProperties, loginService);

        private AccountGroup accountGroup = AccountGroup.builder().build();

        @BeforeEach
        void setUp() {
            List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                    PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
            passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                    .name("normaluser")
                    .email("emailnotFounaibale")
                    .password("a123456")
                    .birth(LocalDate.of(1970, 1, 1))
                    .certKey("certKey")
                    .phoneNumber("01000000012")
                    .gender(Gender.MALE)
                    .policies(policyAndAcceptDtos)
                    .build();
            employee = Member.join(memberJoinDto, accountGroup, passwordEncoder);
        }

        @Test
        @DisplayName("토큰 재발급 / 실패 없음")
        void tokenRefreshTest() {
            LocalDateTime lastLoginDateTime = employee.getLoginInfo().getLastLoginDateTime();
            LocalDateTime lastTokenIssueDateTime = employee.getLoginInfo().getLastTokenIssueDateTime();
            String refreshToken = employee.getLoginInfo().getRefreshToken();
            LocalDateTime refreshTokenExpireDateTime = employee.getLoginInfo().getRefreshTokenExpireDateTime();
            String token = employee.tokenRefresh(jwtTokenProvider);
            assertNotNull(token);
            assertEquals(lastLoginDateTime, employee.getLoginInfo().getLastLoginDateTime());
            assertNotEquals(lastTokenIssueDateTime, employee.getLoginInfo().getLastTokenIssueDateTime());
            assertEquals(refreshToken, employee.getLoginInfo().getRefreshToken());
            assertEquals(refreshTokenExpireDateTime, employee.getLoginInfo().getRefreshTokenExpireDateTime());
        }
    }
}