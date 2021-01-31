package me.kjs.mall.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSaveDto {
    private LocalDate beginDate;
    private String title;
    private String thumbnailImage;
    private String content;
}
