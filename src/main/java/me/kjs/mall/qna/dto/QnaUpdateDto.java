package me.kjs.mall.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaUpdateDto {
    @NotEmpty
    @Length(min = 10, max = 500)
    private String content;
    @NotNull
    private YnType secret = YnType.N;
}
