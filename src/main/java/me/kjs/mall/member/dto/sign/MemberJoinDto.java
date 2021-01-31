package me.kjs.mall.member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.social.SocialAccountCreateDto;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.type.Gender;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDto implements SocialAccountCreateDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String certKey;
    private SocialType socialType;
    private String socialId;
    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate birth;
    @NotEmpty
    @Valid
    private List<@NotNull PolicyAndAcceptDto> policies;
}
