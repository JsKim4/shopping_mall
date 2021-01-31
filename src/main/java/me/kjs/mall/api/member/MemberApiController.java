package me.kjs.mall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cert.CertService;
import me.kjs.mall.common.OnlyEmailDto;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.OnlyTokenDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberQueryRepository;
import me.kjs.mall.member.MemberService;
import me.kjs.mall.member.MemberValidator;
import me.kjs.mall.member.dto.MemberDetailDto;
import me.kjs.mall.member.dto.MemberQueryCondition;
import me.kjs.mall.member.dto.MemberUpdateDto;
import me.kjs.mall.member.dto.sign.MemberPasswordUpdateDto;
import me.kjs.mall.member.exception.login.IllegalPasswordLoginFailedException;
import me.kjs.mall.member.part.MemberBanHistory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;
import static me.kjs.mall.member.dto.MemberDetailDto.memberToDetailDto;
import static me.kjs.mall.member.dto.account.MemberBanHistorySimpleDto.memberBanHistoryToSimpleDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberQueryRepository memberQueryRepository;

    private final MemberService memberService;

    private final MemberValidator memberValidator;

    private final CertService certService;

    private final PasswordEncoder passwordEncoder;

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto updateMember(@CurrentMember Member member, @RequestBody @Validated MemberUpdateDto memberUpdateDto, Errors errors) {
        hasErrorsThrow(errors);
        if (!member.isAvailablePassword(memberUpdateDto.getCurrentPassword(), passwordEncoder)) {
            throw new IllegalPasswordLoginFailedException();
        }
        memberService.updateMember(member.getId(), memberUpdateDto);

        return ResponseDto.noContent();
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto currentMember(@CurrentMember Member member) {
        return ResponseDto.ok(memberToDetailDto(member));
    }

    @GetMapping("/current/ban")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto currentMemberBanCause(@CurrentMember Member member) {

        MemberBanHistory memberBanHistory = memberService.findBanCause(member.getId());

        return ResponseDto.ok(memberBanHistoryToSimpleDto(memberBanHistory));
    }

    @PostMapping("/find/email")
    public ResponseDto findEmail(@RequestBody OnlyTokenDto onlyTokenDto) {
        String email = certService.checkCertEmailFindKey(onlyTokenDto.getToken());
        OnlyEmailDto onlyEmailDto = OnlyEmailDto.builder()
                .email(email)
                .build();
        return ResponseDto.created(onlyEmailDto);
    }


    @PutMapping("/password")
    public ResponseDto updateMemberPassword(@RequestBody @Validated MemberPasswordUpdateDto memberPasswordUpdateDto, Errors errors) {
        hasErrorsThrow(errors);

        Member member = certService.checkCertPasswordModify(memberPasswordUpdateDto.getToken());

        memberService.updateMemberPassword(member.getId(), memberPasswordUpdateDto.getPassword());

        return ResponseDto.noContent();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MEMBER_READ')")
    public ResponseDto queryMembers(
            @Validated MemberQueryCondition memberQueryCondition, Errors errors) {
        hasErrorsThrow(errors);
        memberValidator.validator(memberQueryCondition, errors);
        hasErrorsThrow(errors);
        memberQueryCondition.init();

        CommonPage<Member> members = memberQueryRepository.findAllByCondition(memberQueryCondition);

        List<MemberDetailDto> body = members.getContents().stream().map(MemberDetailDto::memberToDetailDto).collect(Collectors.toList());

        CommonPage commonPage = members.updateContent(body);

        return ResponseDto.ok(commonPage);
    }
}
