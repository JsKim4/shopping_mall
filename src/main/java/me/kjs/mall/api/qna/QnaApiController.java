package me.kjs.mall.api.qna;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.dto.OnlyContentDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.qna.Qna;
import me.kjs.mall.qna.QnaService;
import me.kjs.mall.qna.dto.QnaCreateDto;
import me.kjs.mall.qna.dto.QnaDto;
import me.kjs.mall.qna.dto.QnaSearchCondition;
import me.kjs.mall.qna.dto.QnaUpdateDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
@Slf4j
public class QnaApiController {

    private final QnaService qnaService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto createQuestion(@RequestBody @Validated QnaCreateDto qnaCreateDto,
                                      @CurrentMember Member member,
                                      Errors errors) {
        hasErrorsThrow(errors);
        Qna qna = qnaService.createQna(qnaCreateDto, qnaCreateDto.getProductId(), member);
        QnaDto qnaDto = QnaDto.qnaToDto(qna);
        return ResponseDto.created(qnaDto);
    }

    @GetMapping
    public ResponseDto queryQnas(
            @Validated QnaSearchCondition qnaSearchCondition,
            @CurrentMember Member member,
            Errors errors) {
        hasErrorsThrow(errors);
        CommonSlice<Qna> qnas = qnaService.findQnaBySearchCondition(qnaSearchCondition, member);
        CommonSlice commonSlice = qnas.updateContent(qnas.getContents().stream().map(QnaDto::qnaToDto).collect(Collectors.toList()));
        return ResponseDto.ok(commonSlice);
    }

    @PutMapping("/{qnaId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto updateQna(@PathVariable("qnaId") Long qnaId,
                                 @RequestBody @Validated QnaUpdateDto qnaUpdateDto,
                                 @CurrentMember Member member,
                                 Errors errors) {
        hasErrorsThrow(errors);
        qnaService.updateQna(qnaUpdateDto, qnaId, member.getId());
        return ResponseDto.noContent();
    }

    @DeleteMapping("/{qnaId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto deleteQna(@PathVariable("qnaId") Long qnaId,
                                 @CurrentMember Member member) {
        qnaService.deleteQna(qnaId, member.getId());
        return ResponseDto.noContent();
    }

    @PutMapping("/answer/{qnaId}")
    @PreAuthorize("hasRole('ROLE_QNA')")
    public ResponseDto updateQueryAnswer(@PathVariable("qnaId") Long qnaId,
                                         @RequestBody @Validated OnlyContentDto onlyContentDto,
                                         Errors errors) {
        hasErrorsThrow(errors);
        qnaService.updateQnaAnswer(qnaId, onlyContentDto);
        return ResponseDto.noContent();
    }

}
