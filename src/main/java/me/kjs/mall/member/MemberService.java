package me.kjs.mall.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cert.CertRepository;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.member.dto.MemberBanCauseDto;
import me.kjs.mall.member.dto.MemberUpdateDto;
import me.kjs.mall.member.exception.account.NotAvailableBanException;
import me.kjs.mall.member.exception.account.NotAvailableFreeException;
import me.kjs.mall.member.part.MemberBanHistory;
import me.kjs.mall.member.type.AccountStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final CertRepository certRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void updateMemberPassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        member.updatePassword(password, passwordEncoder);
    }

    @Transactional
    public void initializePassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);

        member.updatePassword(password, passwordEncoder);
    }

    @Transactional
    public void banMember(Long memberId, MemberBanCauseDto memberBanCauseDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        if (member.getAccountStatus() == AccountStatus.ALLOW) {
            member.ban(memberBanCauseDto);
        } else {
            throw new NotAvailableBanException();
        }
    }

    @Transactional
    public void freeMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);

        if (member.getAccountStatus() == AccountStatus.BAN) {
            member.free();
        } else {
            throw new NotAvailableFreeException();
        }

    }

    public MemberBanHistory findBanCause(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        return member.getBanCause();
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        member.update(memberUpdateDto);
        if (!CollectionTextUtil.isBlank(memberUpdateDto.getPassword())) {
            member.updatePassword(memberUpdateDto.getPassword(), passwordEncoder);
        }
    }
}
