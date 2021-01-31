package me.kjs.mall.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;
import me.kjs.mall.review.dto.ReviewCreateDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity implements OwnerCheck, AvailableCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String reviewer;

    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    private LocalDateTime reviewDateTime;

    private int score;

    public static Review createReview(ReviewCreateDto reviewCreateDto, OrderProduct orderProduct, Member member) {
        Review review = Review.builder()
                .content(reviewCreateDto.getContent())
                .images(reviewCreateDto.getImages())
                .product(orderProduct.getProduct())
                .status(CommonStatus.USED)
                .score(reviewCreateDto.getScore())
                .member(member)
                .reviewer(member.getMaskingName())
                .reviewDateTime(LocalDateTime.now())
                .build();
        return review;
    }

    @Override
    public boolean isOwner(Member member) {
        return this.member == member;
    }

    @Override
    public boolean isAvailable() {
        return status != CommonStatus.DELETED;
    }

    @Override
    public boolean isUsed() {
        return status == CommonStatus.USED;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        if (status == CommonStatus.UN_USED)
            return "";
        return content;
    }

    public List<String> getImages() {
        if (status == CommonStatus.UN_USED) {
            return Collections.EMPTY_LIST;
        }
        return images;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getReviewDateTime() {
        return reviewDateTime;
    }

    public String getReviewerName() {
        return reviewer;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void updateStatus(CommonStatus status) {
        this.status = status;
    }

    public void loading() {
        product.loading();
    }

    public String getProductName() {
        return product.getBaseProductName();
    }

    public Long getProductId() {
        return product.getId();
    }

    public Product getProduct() {
        return product;
    }
}

