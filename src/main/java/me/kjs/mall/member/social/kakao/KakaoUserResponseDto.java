package me.kjs.mall.member.social.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.social.SocialRegisterInterface;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.type.Gender;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserResponseDto implements SocialRegisterInterface {
    private String id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Gender getGender() {
        return "female".equals(kakaoAccount.getGender()) ? Gender.FEMALE : Gender.MALE;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
