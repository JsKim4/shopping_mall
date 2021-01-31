package me.kjs.mall.product.type;


import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    private int countScore1;
    private int countScore2;
    private int countScore3;
    private int countScore4;
    private int countScore5;

    private int qnaCount;

    public static Evaluation initializeEvaluation() {
        return Evaluation.builder()
                .countScore1(0)
                .countScore2(0)
                .countScore3(0)
                .countScore4(0)
                .countScore5(0)
                .qnaCount(0)
                .build();
    }

    public int getTotalScore() {
        return countScore1 +
                countScore2 * 2 +
                countScore3 * 3 +
                countScore4 * 4 +
                countScore5 * 5;
    }

    public int getScoreCount() {
        return countScore1 +
                countScore2 +
                countScore3 +
                countScore4 +
                countScore5;
    }

    public double getAverageScore() {
        if (getScoreCount() == 0)
            return 0;
        return getTotalScore() * 10 / getScoreCount() / 10.0;
    }

    public void addScore(int score) {
        switch (score) {
            case 1:
                countScore1++;
                break;
            case 2:
                countScore2++;
                break;
            case 3:
                countScore3++;
                break;
            case 4:
                countScore4++;
                break;
            case 5:
                countScore5++;
                break;
        }
    }

    public void addQna() {
        qnaCount++;
    }

    public void deleteQna() {
        qnaCount--;
    }
}