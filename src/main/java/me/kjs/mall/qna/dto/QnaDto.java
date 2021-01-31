package me.kjs.mall.qna.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.qna.Qna;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaDto {
    private Long qnaId;
    private String questionContent;
    private YnType secret;
    private YnType comment;
    private YnType mine;
    private String answerContent;
    private LocalDate postDate;
    private Long productId;
    private String productName;
    private String reviewer;

    public static QnaDto qnaToDto(Qna qna) {
        return QnaDto.builder()
                .qnaId(qna.getId())
                .questionContent(qna.getQuestionContent())
                .secret(qna.getSecret())
                .comment(qna.getComment())
                .mine(qna.getMine())
                .answerContent(qna.getAnswerContent())
                .postDate(qna.getPostDate())
                .productId(qna.getProductId())
                .productName(qna.getProductName())
                .reviewer(qna.getMaskingReviewer())
                .build();
    }
}
