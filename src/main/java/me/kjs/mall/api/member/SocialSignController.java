package me.kjs.mall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.configs.properties.KakaoProperties;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberQueryRepository;
import me.kjs.mall.member.SignService;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.social.SocialLoginService;
import me.kjs.mall.member.social.SocialRegisterDto;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.social.exception.KakaoNotRegisterUserException;
import me.kjs.mall.member.social.kakao.KakaoOauthResponseDto;
import me.kjs.mall.member.social.kakao.KakaoTokenResponseDto;
import me.kjs.mall.member.social.kakao.KakaoUserResponseDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SocialSignController {
    private final SocialLoginService socialLoginService;

    private final KakaoProperties kakaoProperties;

    private final SignService signService;

    private final MemberQueryRepository memberQueryRepository;

    @GetMapping("/login/kakao")
    public ModelAndView requestKakaoLogin(ModelAndView modelAndView) {
        modelAndView.addObject("clientId", kakaoProperties.getClientId());
        modelAndView.addObject("redirectUrl", kakaoProperties.getRedirectUrl());
        modelAndView.addObject("responseType", "code");
        modelAndView.setViewName("kakao_login");
        return modelAndView;
    }

    @GetMapping(value = "/login/kakao/redirect")
    @CrossOrigin("*")
    public ModelAndView kakaoLogin(KakaoOauthResponseDto kakaoOauthResponseDto) {
        KakaoTokenResponseDto kakaoTokenResponseDto = socialLoginService.connectKakao(kakaoOauthResponseDto);
        log.info("kakaoTokenResponseDto {} ", kakaoTokenResponseDto);
        KakaoUserResponseDto kakaoUserResponseDto = socialLoginService.connectKakaoUser(kakaoTokenResponseDto.getAccessToken());

        Member member = memberQueryRepository.findBySocialTypeAndId(SocialType.KAKAO, kakaoUserResponseDto.getId())
                .orElseThrow(() -> new KakaoNotRegisterUserException(SocialRegisterDto.createSocialRegisterDto(kakaoUserResponseDto)));
        TokenDto login = signService.login(member.getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("kakao_login_result");
        modelAndView.addObject("status", ExceptionStatus.SUCCESS.getStatus());
        modelAndView.addObject("message", ExceptionStatus.SUCCESS.getMessage());
        modelAndView.addObject("data", login);
        return modelAndView;
    }
}
