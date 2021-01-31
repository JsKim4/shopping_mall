package me.kjs.mall.api.point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.point.Point;
import me.kjs.mall.point.PointQueryRepository;
import me.kjs.mall.point.PointService;
import me.kjs.mall.point.dto.PointDto;
import me.kjs.mall.point.dto.PointGiveDto;
import me.kjs.mall.point.dto.PointSearchCondition;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
@Slf4j
public class PointApiController {
    private final PointService pointService;

    private final PointQueryRepository pointQueryRepository;

    @GetMapping("/members/current")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto queryCurrentMember(@CurrentMember Member member,
                                          @Validated PointSearchCondition pointSearchCondition,
                                          Errors errors) {
        hasErrorsThrow(errors);
        CommonSlice<Point> points = pointQueryRepository.findPointByMemberAndSearchCondition(member, pointSearchCondition);
        CommonSlice<PointDto> body = points.updateContent(points.getContents().stream().map(PointDto::pointToDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_POINT')")
    public ResponseDto creatPoint(@RequestBody @Validated PointGiveDto pointGiveDto,
                                  Errors errors) {
        hasErrorsThrow(errors);
        Point point = pointService.givePoint(pointGiveDto);
        return ResponseDto.ok(PointDto.pointToDto(point));
    }
}
