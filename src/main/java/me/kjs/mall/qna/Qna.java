package me.kjs.mall.qna;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;
import me.kjs.mall.qna.dto.QnaCreateDto;
import me.kjs.mall.qna.dto.QnaUpdateDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends BaseEntity implements OwnerCheck, AvailableCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private String questionContent;
    @Enumerated(EnumType.STRING)
    private YnType secret;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;
    private LocalDateTime postDateTime;
    private String reviewer;
    @Enumerated(EnumType.STRING)
    private YnType answer;
    private String answerContent;
    private LocalDateTime qnaAnswerDateTime;

    @Transient
    private YnType mine = YnType.Y;

    public static Qna qnaCreate(QnaCreateDto qnaCreateDto, Product product, Member member) {
        product.addQna();
        return Qna.builder()
                .member(member)
                .product(product)
                .questionContent(qnaCreateDto.getContent())
                .secret(qnaCreateDto.getSecret())
                .status(CommonStatus.USED)
                .postDateTime(LocalDateTime.now())
                .reviewer(member.getMaskingName())
                .answerContent(null)
                .qnaAnswerDateTime(null)
                .mine(YnType.Y)
                .answer(YnType.N)
                .build();
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED || status == CommonStatus.UN_USED;
    }

    @Override
    public boolean isOwner(Member member) {
        return this.member == member;
    }

    public Long getId() {
        return id;
    }

    public String getQuestionContent() {
        if (secret == YnType.Y && mine == YnType.N) {
            return "";
        }
        return questionContent;
    }

    public YnType getSecret() {
        return secret;
    }

    public YnType getComment() {
        return answerContent != null ? YnType.Y : YnType.N;
    }

    public YnType getMine() {
        return mine;
    }

    public String getAnswerContent() {
        if (secret == YnType.Y && mine == YnType.N) {
            return "";
        }
        return answerContent;
    }

    public LocalDate getPostDate() {
        return postDateTime.toLocalDate();
    }

    public void checkMine(Member member) {
        if (secret == YnType.Y) {
            if (this.member == member) {
                mine = YnType.Y;
                return;
            }
        }
        mine = YnType.N;

    }

    public void update(QnaUpdateDto qnaUpdateDto) {
        questionContent = qnaUpdateDto.getContent();
        secret = qnaUpdateDto.getSecret();
    }

    public void delete() {
        product.deleteQna();
        status = CommonStatus.DELETED;
    }

    public void updateAnswer(String content) {
        answerContent = content;
        qnaAnswerDateTime = LocalDateTime.now();
        answer = YnType.Y;
    }

    public Qna loading() {
        product.loading();
        return this;
    }

    public Long getProductId() {
        return product.getId();
    }

    public String getProductName() {
        return product.getBaseProductName();
    }

    public String getMaskingReviewer() {
        return reviewer;
    }
}
