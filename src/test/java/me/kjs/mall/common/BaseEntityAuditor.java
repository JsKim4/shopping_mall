package me.kjs.mall.common;

import lombok.RequiredArgsConstructor;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class BaseEntityAuditor implements AuditorAware<BaseEntityDateTime> {

    private final MemberRepository memberRepository;

    private Member member;

    @Override
    public Optional<BaseEntityDateTime> getCurrentAuditor() {
        return Optional.of(member);
    }

    public void setAuditorByRefreshToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("테스트 Auditing 세팅 도중 회원정보를 찾을수 없어 오류가 발생하였습니다. By Token : " + refreshToken));
        setMember(member);
    }

    public void setAuditorByEmail(String email) {
        Member member = memberRepository.findByRefreshToken(email)
                .orElseThrow(() -> new EntityNotFoundException("테스트 Auditing 세팅 도중 회원정보를 찾을수 없어 오류가 발생하였습니다. By Email : " + email));
        setMember(member);
    }

    private void setMember(Member member) {
        this.member = member;
    }
}
