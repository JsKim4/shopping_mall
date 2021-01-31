package me.kjs.mall.member.social.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccount {
    @JsonProperty("profile_needs_agreement")
    private String profileNeedsAgreement;
    @JsonProperty("profile")
    private KakaoProfile profile;
    @JsonProperty("email_needs_agreement")
    private boolean emailNeedsAgreement;
    @JsonProperty("is_email_valid")
    private boolean isEmailValid;
    @JsonProperty("is_email_verified")
    private boolean isEmailVerified;
    private String email = "sample@sample.com";
    @JsonProperty("age_range_needs_agreement")
    private boolean ageRangeNeedsAgreement;
    @JsonProperty("age_range")
    private String ageRage = "20~29";
    @JsonProperty("birthday_needs_agreement")
    private boolean birthdayNeedsAgreement;
    private String birthday = "1130";
    @JsonProperty("gender_needs_agreement")
    private boolean genderNeedsAgreement;
    private String gender = "female";
}

