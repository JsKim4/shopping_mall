package me.kjs.mall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cert.CertService;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberQueryRepository;
import me.kjs.mall.member.SignService;
import me.kjs.mall.member.dto.MemberDetailDto;
import me.kjs.mall.member.dto.sign.*;
import me.kjs.mall.member.exception.login.IllegalPasswordLoginFailedException;
import me.kjs.mall.member.social.SocialConnectorDto;
import me.kjs.mall.member.social.SocialLoginService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class SignApiController {

    private final SignService signService;
    private final SocialLoginService socialLoginService;
    private final CertService certService;
    private final PasswordEncoder passwordEncoder;

    private final MemberQueryRepository memberQueryRepository;

    @PostMapping
    public ResponseDto joinMember(@Validated @RequestBody MemberJoinDto memberJoinDto, Errors errors) {
        hasErrorsThrow(errors);
        certService.checkCertJoinKey(memberJoinDto.getPhoneNumber(), memberJoinDto.getCertKey());
        Member member = signService.join(memberJoinDto);
        MemberDetailDto body = MemberDetailDto.memberToDetailDto(member);
        return ResponseDto.created(body);
    }

    @PutMapping("/withdraw")
    public ResponseDto withdrawMember(@Validated @RequestBody OnlyPasswordDto onlyPasswordDto,
                                      @CurrentMember Member member,
                                      Errors errors) {
        hasErrorsThrow(errors);

        if (!member.isAvailablePassword(onlyPasswordDto.getPassword(), passwordEncoder)) {
            throw new IllegalPasswordLoginFailedException();
        }
        List<SocialConnectorDto> socialConnectors = memberQueryRepository.findSocialTypeAndId(member);
        for (SocialConnectorDto socialConnector : socialConnectors) {
            socialLoginService.disconnect(socialConnector);
        }
        signService.withdraw(member.getId());
        return ResponseDto.noContent();
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody @Validated MemberLoginDto memberLoginDto, Errors errors) {
        hasErrorsThrow(errors);

        TokenDto login = signService.login(memberLoginDto);

        return ResponseDto.created(login);
    }

    @PostMapping("/refresh")
    public ResponseDto refresh(@RequestBody @Validated TokenRefreshDto tokenRefreshDto, Errors errors) {
        hasErrorsThrow(errors);

        TokenDto refreshToken = signService.refreshToken(tokenRefreshDto);

        return ResponseDto.created(refreshToken);
    }

    @PostMapping("/connect")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto connectSocial(@RequestBody @Validated SocialConnectorDto socialConnectorDto, Errors errors, @CurrentMember Member member) {
        hasErrorsThrow(errors);

        signService.connectSocial(socialConnectorDto, member.getId());

        return ResponseDto.created(null);
    }


}
