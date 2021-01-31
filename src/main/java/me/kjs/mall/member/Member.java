package me.kjs.mall.member;


import lombok.*;
import me.kjs.mall.common.BaseEntityDateTime;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.destination.Destination;
import me.kjs.mall.member.dto.MemberBanCauseDto;
import me.kjs.mall.member.dto.MemberUpdateDto;
import me.kjs.mall.member.dto.sign.MemberJoinDto;
import me.kjs.mall.member.exception.login.MemberLoginWithdrawFailException;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.part.LoginInfo;
import me.kjs.mall.member.part.MemberBanHistory;
import me.kjs.mall.member.part.PointManagement;
import me.kjs.mall.member.social.SocialAccount;
import me.kjs.mall.member.social.SocialAccountCreateDto;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import me.kjs.mall.point.Point;
import me.kjs.mall.security.JwtTokenProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntityDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @NotBlank
    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birth;

    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    @Builder.Default
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    @Builder.Default
    private List<MemberPolicy> memberPolicies = new ArrayList<>();
    @Embedded
    private LoginInfo loginInfo;

    private LocalDateTime withdrawDateTime;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_group_id")
    private AccountGroup accountGroup;
    @Embedded
    private PointManagement pointManagement;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    @Builder.Default
    private List<MemberBanHistory> memberBanHistories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    @Builder.Default
    private List<Destination> destinations = new ArrayList<>();

    public static Member join(MemberJoinDto memberJoinDto, AccountGroup accountGroup, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .name(memberJoinDto.getName())
                .email(memberJoinDto.getEmail())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .pointManagement(PointManagement.initialize())
                .withdrawDateTime(null)
                .accountStatus(AccountStatus.ALLOW)
                .accountGroup(accountGroup)
                .loginInfo(LoginInfo.initialize())
                .pointManagement(PointManagement.initialize())
                .gender(memberJoinDto.getGender())
                .birth(memberJoinDto.getBirth().toString().replace("-", ""))
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .build();
        List<MemberPolicy> memberPolicies = memberJoinDto.getPolicies().stream().map(mp -> MemberPolicy.createPolicy(mp, member)).collect(Collectors.toList());
        member.memberPolicies = memberPolicies;
        if (memberJoinDto.getSocialType() != null) {
            member.connectSocialAccount(memberJoinDto);
        }

        return member;
    }

    public void connectSocialAccount(SocialAccountCreateDto socialAccountCreateDto) {
        socialAccounts.add(SocialAccount.createSocialAccount(socialAccountCreateDto, this));
    }

    public List<String> getAccountRoleNames() {
        return accountGroup.getAccountRoleNames();
    }

    public Collection<? extends GrantedAuthority> getAuthorityRoles() {
        Collection<SimpleGrantedAuthority> result = accountGroup.getAuthorityRoles();
        if (accountStatus == AccountStatus.ALLOW) {
            SimpleGrantedAuthority role_allow = new SimpleGrantedAuthority("ROLE_ALLOW");
            result.add(role_allow);
        }
        return result;
    }

    public String tokenRefresh(JwtTokenProvider jwtTokenProvider) {
        String newToken = jwtTokenProvider.createToken(email, accountGroup.getAccountRoleNames());
        loginInfo = loginInfo.tokenRefresh();
        return newToken;
    }

    public String login(JwtTokenProvider jwtTokenProvider) {
        accountStatusThrow();
        loginInfo = loginInfo.login(jwtTokenProvider.getSecurityProperties());
        String newToken = jwtTokenProvider.createToken(email, accountGroup.getAccountRoleNames());
        return newToken;
    }


    private void accountStatusThrow() {
        switch (accountStatus) {
            case WITHDRAW:
                throw new MemberLoginWithdrawFailException();
        }
    }


    private boolean isAvailable() {
        switch (accountStatus) {
            case WITHDRAW:
            case RETIRED:
            case BAN:
                return false;
        }
        return true;
    }

    public String getRefreshToken() {
        return loginInfo.getRefreshToken();
    }

    public long getRefreshTokenExpireSecond(LocalDateTime now) {
        LocalDate expireDate = loginInfo.getRefreshTokenExpireDateTime().toLocalDate();
        LocalTime expireTime = loginInfo.getRefreshTokenExpireDateTime().toLocalTime();
        LocalDate standDate = now.toLocalDate();
        LocalTime standTime = now.toLocalTime();
        Period period = Period.between(standDate, expireDate);
        Duration duration = Duration.between(standTime, expireTime);
        return period.getDays() * 24 * 60 * 60 + duration.getSeconds();
    }


    public boolean isAvailableAcceptOrReject() {
        return accountStatus == AccountStatus.REQUEST || accountStatus == AccountStatus.RE_REQUEST;
    }

    public void memberWithdraw() {
        accountStatus = AccountStatus.WITHDRAW;
    }


    public long getPointsHeld() {
        return pointManagement.getPointsHeld();
    }

    public long getAccumulatePoint() {
        return pointManagement.getAccumulatePoint();
    }

    public long getPointsUsed() {
        return pointManagement.getPointsUsed();
    }


    public Set<AccountRole> getAccountRoles() {
        return accountGroup.getRoles();
    }


    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean isExpiredRefreshToken() {
        if (loginInfo == null)
            return true;

        return loginInfo.isExpiredRefreshToken();

    }

    public void refreshTokenExpire() {
        loginInfo.refreshTokenExpire();
    }

    public void updateGroup(AccountGroup accountGroup) {
        this.accountGroup = accountGroup;
    }

    public void ban(MemberBanCauseDto memberBanCauseDto) {
        banCausesExpire();
        MemberBanHistory memberBan = MemberBanHistory.createMemberBan(this, memberBanCauseDto, LocalDateTime.now());
        memberBanHistories.add(memberBan);
        accountStatus = AccountStatus.BAN;
    }

    public MemberBanHistory getBanCause() {
        if (accountStatus != AccountStatus.BAN) {
            return MemberBanHistory.builder().causeMessage("이용제한 회원이 아닙니다.").build();
        }
        MemberBanHistory result = MemberBanHistory.builder().causeMessage("이용제한 사유가 존재하지 않습니다. 관리자에게 문의 부탁드립니다.").build();
        for (MemberBanHistory memberBanHistory : memberBanHistories) {
            if (memberBanHistory.isAvailableCause()) {
                result = memberBanHistory;
                break;
            }
        }
        return result;
    }

    public void free() {
        banCausesExpire();
        accountStatus = AccountStatus.ALLOW;
    }

    private void banCausesExpire() {
        for (MemberBanHistory memberBanHistory : memberBanHistories) {
            if (memberBanHistory.isAvailableCause()) {
                memberBanHistory.expire();
            }
        }
    }

    public boolean hasRole(AccountRole accountRole) {
        return accountGroup.isContainRolesTo(accountRole);
    }

    public Long getAccountGroupId() {
        return accountGroup.getId();
    }

    public String getAccountGroupName() {
        return accountGroup.getName();
    }

    public boolean isAvailableAddDestination() {
        if (destinations.size() < 30) {
            return true;
        }
        return false;
    }

    public void addDestination(Destination saveDestination) {
        destinations.add(saveDestination);
    }

    public boolean isAvailableOrder() {
        return accountStatus == AccountStatus.ALLOW;
    }

    public void accumulatePoint(Point save) {
        pointManagement.accumPoint(save);
    }

    public void usePoint(Point save) {
        pointManagement.usePoint(save);
    }

    public String getMaskingName() {
        String memberName = name;
        String name = memberName.charAt(0) + "";
        int length = memberName.length();
        for (int i = 1; i < length - 1; i++) {
            name += "*";
        }
        if (length > 2) {
            name += memberName.charAt(length - 1);
        }
        return name;
    }

    public void rollbackPoint(Point point) {
        pointManagement.rollbackPoint(point);
    }

    public boolean isAvailablePassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public void withdraw() {
        accountStatus = AccountStatus.WITHDRAW;
        name = "삭제된 회원";
        withdrawDateTime = LocalDateTime.now();
        birth = LocalDate.of(1970, 1, 1).toString().replace("-", "");
        email = UUID.randomUUID().toString();
        phoneNumber = UUID.randomUUID().toString();
        loginInfo.expired();
        for (SocialAccount socialAccount : socialAccounts) {
            socialAccount.disconnect();
        }
        socialAccounts = new ArrayList<>();
    }

    public int getAge() {
        LocalDate localDate = DateTimeUtil.formatYYYYMMDDToLocalDate(birth);
        Period period = Period.between(localDate, LocalDate.now());
        return period.getYears();
    }

    public String getTargetName() {
        if (getAge() < 13) {
            return "어린이용";
        }
        if (getAge() < 19) {
            return "청소년용";
        }
        if (getAge() > 60) {
            return "노년용";
        }
        if (gender == Gender.FEMALE) {
            return "여성용";
        } else {
            return "남성용";
        }
    }

    public void update(MemberUpdateDto memberUpdateDto) {
        if (memberUpdateDto.getBirth() != null) {
            birth = memberUpdateDto.getBirth().toString().replace("-", "");
        }
        if (memberUpdateDto.getGender() != null) {
            gender = memberUpdateDto.getGender();
        }
        if (!CollectionTextUtil.isBlank(memberUpdateDto.getName())) {
            name = memberUpdateDto.getName();
        }
    }

    public LocalDate getBirthDate() {
        return DateTimeUtil.formatYYYYMMDDToLocalDate(birth);
    }
}