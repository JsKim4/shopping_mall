package me.kjs.mall.api;

import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.social.SocialRegisterDto;
import me.kjs.mall.member.type.Gender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DocumentationTestController {
    @GetMapping("/test/kakao/response/login")
    public ResponseDto responseLoginInterface() {
        TokenDto tokenDto = TokenDto.builder()
                .refreshToken(UUID.randomUUID().toString())
                .refreshTokenExpireSecond(60 * 60 * 24 * 14)
                .token("jsdhkasjd.asdhkjhdkjas.asdlkhaksd")
                .tokenExpireSecond(60 * 60 * 2)
                .build();
        return ResponseDto.ok(tokenDto);
    }

    @GetMapping("/test/kakao/response/register")
    public ResponseDto responseRegisterInterface() {
        SocialRegisterDto socialRegisterDto = SocialRegisterDto.builder()
                .gender(Gender.MALE)
                .name("홍길동")
                .socialId("11111111")
                .build();

        return ResponseDto.builder()
                .message(ExceptionStatus.KAKAO_NOT_REGISTER_USER.getMessage())
                .status(ExceptionStatus.KAKAO_NOT_REGISTER_USER.getStatus())
                .data(socialRegisterDto)
                .build();
    }


}
