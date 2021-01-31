package me.kjs.mall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.type.Evaluation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private int countScore1;
    private int countScore2;
    private int countScore3;
    private int countScore4;
    private int countScore5;
    private int scoreCount;
    private int qnaCount;
    private double averageScore;

    public static EvaluationDto evaluationToDto(Evaluation evaluation) {
        return EvaluationDto.builder()
                .qnaCount(evaluation.getQnaCount())
                .countScore1(evaluation.getCountScore1())
                .countScore2(evaluation.getCountScore2())
                .countScore3(evaluation.getCountScore3())
                .countScore4(evaluation.getCountScore4())
                .countScore5(evaluation.getCountScore5())
                .scoreCount(evaluation.getScoreCount())
                .averageScore(evaluation.getAverageScore())
                .build();
    }

}
