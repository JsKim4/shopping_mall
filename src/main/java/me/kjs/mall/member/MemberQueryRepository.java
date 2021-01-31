package me.kjs.mall.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.member.dto.MemberQueryCondition;
import me.kjs.mall.member.social.QSocialAccount;
import me.kjs.mall.member.social.SocialConnectorDto;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberQueryRepository {

    private final EntityManager em;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void setUp() {
        queryFactory = new JPAQueryFactory(em);
    }

    public boolean existAdmin() {
        try {
            Member member = queryFactory.select(QMember.member)
                    .from(QMember.member)
                    .where(QMember.member.accountGroup.roles.contains(AccountRole.ADMIN))
                    .orderBy(QMember.member.id.asc())
                    .fetchFirst();
            return member != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Member findAdmin() {

        Member fetch = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.accountGroup.roles.contains(AccountRole.ADMIN))
                .orderBy(QMember.member.id.asc())
                .fetchFirst();

        return fetch;
    }

    public CommonPage<Member> findAllByCondition(MemberQueryCondition memberQueryCondition) {

        BooleanBuilder condition = new BooleanBuilder();

        if (memberQueryCondition.isContainCondition(MemberQueryCondition.Condition.ConditionType.AccountStatus)) {
            String value = memberQueryCondition.getContainValue(MemberQueryCondition.Condition.ConditionType.AccountStatus);
            condition.and(QMember.member.accountStatus.eq(AccountStatus.valueOf(value.toUpperCase())));
        }

        if (memberQueryCondition.isBlank() == false) {
            condition.and(QMember.member.name.contains(memberQueryCondition.getSearchWord()));
        }


        QueryResults<Member> results = queryFactory.select(QMember.member)
                .from(QMember.member)
                .join(QMember.member.accountGroup)
                .where(condition)
                .orderBy(QMember.member.id.desc())
                .offset(memberQueryCondition.getOffset())
                .limit(memberQueryCondition.getLimit())
                .fetchJoin()
                .fetchResults();

        CommonPage result = new CommonPage(results, memberQueryCondition.getPage());

        return result;
    }

    public Optional<Member> findBySocialTypeAndId(SocialType socialType, String id) {
        return Optional.ofNullable(queryFactory.select(QSocialAccount.socialAccount.member)
                .from(QSocialAccount.socialAccount)
                .join(QSocialAccount.socialAccount.member)
                .where(QSocialAccount.socialAccount.socialId.eq(id).and(
                        QSocialAccount.socialAccount.socialType.eq(socialType)
                ))
                .fetchFirst());
    }

    public List<SocialConnectorDto> findSocialTypeAndId(Member member) {
        List<Tuple> fetch = queryFactory.select(QSocialAccount.socialAccount.socialId, QSocialAccount.socialAccount.socialType)
                .from(QSocialAccount.socialAccount)
                .where(QSocialAccount.socialAccount.member.eq(member))
                .fetch();
        return fetch.stream().map(t -> SocialConnectorDto.builder().socialId(t.get(0, String.class)).socialType(t.get(1, SocialType.class)).build()).collect(Collectors.toList());
    }
}
