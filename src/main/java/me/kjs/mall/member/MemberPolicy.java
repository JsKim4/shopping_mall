package me.kjs.mall.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntityDateTime;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.dto.sign.PolicyAndAcceptDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPolicy extends BaseEntityDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_policy_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PolicyType policyType;
    @Enumerated(EnumType.STRING)
    private YnType acceptType;
    private LocalDateTime responseDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberPolicy createPolicy(PolicyAndAcceptDto policyAndAcceptDto, Member member) {
        return MemberPolicy.builder()
                .acceptType(policyAndAcceptDto.getAcceptType())
                .policyType(policyAndAcceptDto.getPolicyType())
                .responseDateTime(LocalDateTime.now())
                .member(member)
                .build();
    }
}
