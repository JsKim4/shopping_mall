package me.kjs.mall.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {
    @NotEmpty
    private String content;
    @Min(1)
    @Max(5)
    private int score;
    private List<@NotEmpty String> images;
}
