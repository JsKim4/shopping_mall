package me.kjs.mall.point;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.point.dto.PointSpecificAndAmountDto;
import org.junit.jupiter.api.Test;

import java.util.List;

class PointSpecificQueryRepositoryTest extends BaseTest {
    @Test
    void pointQueryTest() {
        TokenDto user004 = getTokenDto("user002");
        Member member = memberRepository.findByRefreshToken(user004.getRefreshToken()).orElseThrow(NoExistIdException::new);
        List<PointSpecific> allByUsablePointSpecifics = pointSpecificQueryRepository.findAllByUsablePointSpecifics(member);
    }

    @Test
    void expiredQueryTest() {
        List<PointSpecificAndAmountDto> expiredPoint = pointSpecificQueryRepository.findExpiredPoint();
    }

}