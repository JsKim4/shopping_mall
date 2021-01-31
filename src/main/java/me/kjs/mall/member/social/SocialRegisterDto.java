package me.kjs.mall.member.social;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.type.Gender;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialRegisterDto {
    private String socialId;
    private String name;
    private Gender gender;
    private SocialType socialType;

    public static SocialRegisterDto createSocialRegisterDto(SocialRegisterInterface socialRegisterInterface) {
        return SocialRegisterDto.builder()
                .gender(socialRegisterInterface.getGender())
                .name(socialRegisterInterface.getName())
                .socialId(socialRegisterInterface.getId())
                .socialType(socialRegisterInterface.getSocialType())
                .build();
    }
}
