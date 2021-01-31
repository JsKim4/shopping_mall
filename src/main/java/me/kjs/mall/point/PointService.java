package me.kjs.mall.point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.point.dto.PointCreateDto;
import me.kjs.mall.point.dto.PointGiveDto;
import me.kjs.mall.point.dto.PointSpecificAndAmountDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PointService {

    private final PointRepository pointRepository;
    private final PointQueryRepository pointQueryRepository;
    private final PointSpecificQueryRepository pointSpecificQueryRepository;
    private final MemberRepository memberRepository;
    private final PointSpecificRepository pointSpecificRepository;

    public List<Point> findPointByMember(Member member) {
        List<Point> points = pointRepository.findAllByMember(member);
        return points;
    }

    @Transactional
    public Point accumulatePoint(PointCreateDto pointCreateDto) {
        Point point = Point.accumPointCreateByCreateDto(pointCreateDto);
        Point savePoint = pointRepository.save(point);
        Member member = pointCreateDto.getMember();
        member.accumulatePoint(savePoint);
        return savePoint;
    }

    @Transactional
    public Point usePointAndRemainPoint(PointCreateDto pointCreateDto) {
        List<PointSpecific> pointSpecifics = pointSpecificQueryRepository.findAllByUsablePointSpecifics(pointCreateDto.getMember());
        Point point = Point.usePoint(pointCreateDto, pointSpecifics);
        Point savePoint = pointRepository.save(point);
        Member member = pointCreateDto.getMember();
        member.usePoint(savePoint);
        return savePoint;
    }

    public int availableUsePoint(Member member) {
        return pointSpecificQueryRepository.findUsablePoint(member);
    }

    @Transactional
    public void expiredPoint() {
        List<PointSpecificAndAmountDto> expiredPoints = pointSpecificQueryRepository.findExpiredPoint();
        for (PointSpecificAndAmountDto expiredPoint : expiredPoints) {
            Point point = Point.expiredPoint(expiredPoint);
            pointRepository.save(point);
            Member member = point.getMember();
            member.usePoint(point);
        }
    }

    @Transactional
    public Point givePoint(PointGiveDto pointGiveDto) {
        Member member = memberRepository.findById(pointGiveDto.getMemberId()).orElseThrow(NoExistIdException::new);
        Point point = Point.givePoint(member, pointGiveDto);
        return pointRepository.save(point);
    }

    @Transactional
    public void rollbackUsePoint(Point point, int usePoint) {
        int realPoint = usePoint;
        List<PointSpecific> usedList = point.getUsedList();
        List<PointSpecific> deleteList = new ArrayList<>();
        for (PointSpecific pointSpecific : usedList) {
            if (pointSpecific.getPointState() != PointState.USE) {
                continue;
            }
            if (pointSpecific.getAmount() * (-1) <= realPoint) {
                deleteList.add(pointSpecific);
                realPoint += pointSpecific.getAmount();
            } else {
                int updatePoint = pointSpecific.getAmount() + realPoint;
                pointSpecific.updatePointAmount(updatePoint);
                break;
            }
        }
        for (PointSpecific pointSpecific : deleteList) {
            pointSpecific.delete();
            pointSpecificRepository.deleteById(pointSpecific.getId());
        }
        Member member = point.getMember();
        member.rollbackPoint(point);
        point.updatePointAmount(point.getAmount() + usePoint);
        if (point.getAmount() == 0) {
            pointRepository.deleteById(point.getId());
        }
    }

    @Transactional
    public void orderCancel(OrderSpecific orderSpecific) {
        Optional<Point> pointForm = pointQueryRepository.findByOrderSpecific(orderSpecific);
        if (pointForm.isPresent()) {
            rollbackUsePoint(pointForm.get(), orderSpecific.getUsePoint());
        }
    }
}
