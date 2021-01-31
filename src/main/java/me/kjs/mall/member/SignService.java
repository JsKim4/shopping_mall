package me.kjs.mall.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.dto.sign.MemberJoinDto;
import me.kjs.mall.member.dto.sign.MemberLoginDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.dto.sign.TokenRefreshDto;
import me.kjs.mall.member.exception.join.AlreadyExistEmailException;
import me.kjs.mall.member.exception.login.IllegalPasswordLoginFailedException;
import me.kjs.mall.member.exception.login.NoExistEmailException;
import me.kjs.mall.member.exception.login.NotFoundMemberByRefreshTokenException;
import me.kjs.mall.member.exception.login.RefreshTokenExpiredException;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.part.AccountGroupRepository;
import me.kjs.mall.member.social.SocialConnectorDto;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.social.exception.AlreadyExistSocialException;
import me.kjs.mall.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountGroupRepository accountGroupRepository;

    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    public Member join(MemberJoinDto memberJoinDto) {

        validation(memberJoinDto);

        AccountGroup accountGroup = accountGroupRepository.findDefault().orElseThrow(EntityNotFoundException::new);

        Member member = Member.join(memberJoinDto, accountGroup, passwordEncoder);

        Member saveMember = memberRepository.save(member);

        return saveMember;
    }

    @Transactional
    public TokenDto login(MemberLoginDto memberLoginDto) {
        Member member = memberRepository.findByEmail(memberLoginDto.getEmail()).orElseThrow(NoExistEmailException::new);
        if (!member.isAvailablePassword(memberLoginDto.getPassword(), passwordEncoder)) {
            throw new IllegalPasswordLoginFailedException();
        }
        String token = member.login(jwtTokenProvider);
        return TokenDto.tokenAndMemberToTokenDto(token, jwtTokenProvider.getSecurityProperties().getTokenValidSecond(), member);
    }

    @Transactional
    public TokenDto login(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        String token = member.login(jwtTokenProvider);
        return TokenDto.tokenAndMemberToTokenDto(token, jwtTokenProvider.getSecurityProperties().getTokenValidSecond(), member);
    }

    @Transactional
    public TokenDto refreshToken(TokenRefreshDto tokenRefreshDto) {
        Member member = memberRepository.findByRefreshToken(tokenRefreshDto.getRefreshToken()).orElseThrow(NotFoundMemberByRefreshTokenException::new);

        if (member.isExpiredRefreshToken()) {
            throw new RefreshTokenExpiredException();
        }

        String token = member.tokenRefresh(jwtTokenProvider);
        return TokenDto.tokenAndMemberToTokenDto(token, jwtTokenProvider.getSecurityProperties().getTokenValidSecond(), member);
    }


    @Transactional
    public void connectSocial(SocialConnectorDto socialConnectorDto, Long memberId) {
        Optional<Member> socialForm = memberQueryRepository.findBySocialTypeAndId(SocialType.KAKAO, socialConnectorDto.getSocialId());
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        socialForm.ifPresent(value -> {
            if (value != member) {
                throw new AlreadyExistSocialException();
            }
        });
        member.connectSocialAccount(socialConnectorDto);
    }

    private void validation(MemberJoinDto memberJoinDto) {
        if (memberRepository.existsByEmail(memberJoinDto.getEmail())) {
            throw new AlreadyExistEmailException();
        }
    }

    @Transactional
    public void withdraw(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        member.withdraw();
    }
}
