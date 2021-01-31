package me.kjs.mall.member.social;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.Member;

import javax.persistence.*;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_account_id")
    private Long id;
    private String socialId;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static SocialAccount createSocialAccount(SocialAccountCreateDto socialAccountCreateDto, Member member) {
        return SocialAccount.builder()
                .socialId(socialAccountCreateDto.getSocialId())
                .socialType(socialAccountCreateDto.getSocialType())
                .member(member)
                .build();
    }

    public void disconnect() {
        member = null;
    }
}
