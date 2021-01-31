package me.kjs.mall.story.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorySaveDto {
    @NotNull
    private LocalDate beginDate;
    @NotEmpty
    private String title;
    @NotEmpty
    private String thumbnailImage;
    @NotEmpty
    private String contents;
}
