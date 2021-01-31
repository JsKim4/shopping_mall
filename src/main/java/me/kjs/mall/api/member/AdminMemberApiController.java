package me.kjs.mall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.member.MemberService;
import me.kjs.mall.member.dto.MemberBanCauseDto;
import me.kjs.mall.member.dto.sign.OnlyPasswordDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
@PreAuthorize("hasRole('ROLE_MEMBER_MODIFY')")
public class AdminMemberApiController {

    private final MemberService memberService;

    @PutMapping("/password/{memberId}")
    public ResponseDto initializePassword(@PathVariable("memberId") Long memberId, @RequestBody @Validated OnlyPasswordDto password, Errors errors) {
        hasErrorsThrow(errors);
        memberService.initializePassword(memberId, password.getPassword());
        return ResponseDto.noContent();
    }

    @PutMapping("/ban/{memberId}")
    public ResponseDto banMember(@PathVariable("memberId") Long memberId, @RequestBody @Validated MemberBanCauseDto memberBanCauseDto, Errors errors) {
        hasErrorsThrow(errors);
        memberService.banMember(memberId, memberBanCauseDto);

        return ResponseDto.noContent();
    }

    @PutMapping("/free/{memberId}")
    public ResponseDto freeMember(@PathVariable("memberId") Long memberId) {
        memberService.freeMember(memberId);

        return ResponseDto.noContent();
    }


}
