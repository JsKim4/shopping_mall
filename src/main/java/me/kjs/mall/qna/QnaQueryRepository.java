package me.kjs.mall.qna;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.product.Product;
import me.kjs.mall.qna.dto.QnaSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QnaQueryRepository {
    private final EntityManager em;

    private JPAQueryFactory jpaQueryFactory;

    @PostConstruct
    private void setUp() {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    public CommonSlice<Qna> findQnaBySearchCondition(Product product, QnaSearchCondition qnaSearchCondition, Member member) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QQna.qna.status.notIn(CommonStatus.DELETED));
        if (product != null) {
            booleanBuilder.and(QQna.qna.product.eq(product));
        }
        if (qnaSearchCondition.getQueryCurrent() == YnType.Y) {
            booleanBuilder.and(QQna.qna.member.eq(member));
        }
        if (qnaSearchCondition.getAnswer() == YnType.Y) {
            booleanBuilder.and(QQna.qna.answer.eq(YnType.Y));
        } else if (qnaSearchCondition.getAnswer() == YnType.N) {
            booleanBuilder.and(QQna.qna.answer.eq(YnType.N));
        }
        List<Qna> qnaQueryResults = jpaQueryFactory.select(QQna.qna)
                .from(QQna.qna)
                .where(booleanBuilder)
                .limit(qnaSearchCondition.getSliceLimit())
                .offset(qnaSearchCondition.getOffset())
                .orderBy(QQna.qna.id.desc())
                .fetch();
        CommonSlice commonSlice = new CommonSlice(qnaQueryResults, qnaSearchCondition);
        return commonSlice;
    }

}
