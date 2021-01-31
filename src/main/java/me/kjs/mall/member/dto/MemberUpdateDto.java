package me.kjs.mall.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.type.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String password;
    @NotBlank
    private String currentPassword;
    @NotNull
    private Gender gender;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birth;
}
