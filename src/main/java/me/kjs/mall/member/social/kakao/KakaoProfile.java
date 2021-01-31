package me.kjs.mall.member.social.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoProfile {
    private String nickname;
    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}
