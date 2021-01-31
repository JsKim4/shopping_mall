package me.kjs.mall.member;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.dto.MemberQueryCondition;
import me.kjs.mall.member.social.SocialType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


class MemberQueryRepositoryTest extends BaseTest {

    @Test
    void query_test() {
        memberQueryRepository.findAdmin();
    }


    @Test
    void findAllByConditionTest() {
        MemberQueryCondition.Condition condition = MemberQueryCondition.Condition.builder()
                .type(MemberQueryCondition.Condition.ConditionType.CompanyRank)
                .value("DIRECTOR")
                .build();

        MemberQueryCondition memberQueryCondition = MemberQueryCondition.builder()
                .contents(10)
                .page(0)
                .searchWord("ADM")
                .conditions(Arrays.asList(condition))
                .build();


        List<Member> result = memberQueryRepository.findAllByCondition(memberQueryCondition).getContents();
        assertTrue(result.size() > 0);
    }

    @Test
    void queryTest() {
        Optional<Member> bySocialTypeAndId = memberQueryRepository.findBySocialTypeAndId(SocialType.KAKAO, "1");

    }
}