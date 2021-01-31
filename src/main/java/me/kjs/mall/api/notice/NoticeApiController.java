package me.kjs.mall.api.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.notice.Notice;
import me.kjs.mall.notice.NoticeQueryRepository;
import me.kjs.mall.notice.NoticeService;
import me.kjs.mall.notice.dto.NoticeDetailDto;
import me.kjs.mall.notice.dto.NoticeSaveDto;
import me.kjs.mall.notice.dto.NoticeSearchCondition;
import me.kjs.mall.notice.dto.NoticeSimpleDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
@Slf4j
public class NoticeApiController {

    private final NoticeService noticeService;
    private final NoticeQueryRepository noticeQueryRepository;

    @GetMapping
    public ResponseDto queryStories(@Validated NoticeSearchCondition noticeSearchCondition,
                                    Errors errors,
                                    @CurrentMember Member member) {
        hasErrorsThrow(errors);
        if (noticeSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "delete notice don't query");
        }
        if (!hasRole(member, AccountRole.NOTICE)) {
            if (noticeSearchCondition.getStatus() == CommonStatus.UN_USED ||
                    noticeSearchCondition.getStatus() == CommonStatus.CREATED) {
                errors.rejectValue("status", "wrong value", "not admin don't query un_used or created");
            }
            if (noticeSearchCondition.getPostStatus() != PostStatus.PROCESS) {
                errors.rejectValue("postStatus", "wrong value", "user query postStatus only PROCESS");
            }
        }
        hasErrorsThrow(errors);

        CommonPage<Notice> notices = noticeQueryRepository.findNoticeBySearchCondition(noticeSearchCondition);

        CommonPage body = notices.updateContent(notices.getContents().stream().map(NoticeSimpleDto::noticeToSimpleDto).collect(Collectors.toList()));

        return ResponseDto.ok(body);
    }

    /**
     * 멱등성 보장 안됨
     */
    @GetMapping("/{noticeId}")
    public ResponseDto queryNotice(@PathVariable("noticeId") Long noticeId,
                                   @CurrentMember Member member) {
        Notice notice = noticeService.findNoticeById(noticeId);
        if (!hasRole(member, AccountRole.NOTICE)) {
            if (!notice.isUsed()) {
                throw new NoExistIdException();
            }
        }
        noticeService.visit(notice.getId());

        NoticeDetailDto body = NoticeDetailDto.noticeToDetailDto(notice);
        return ResponseDto.ok(body);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto createNotice(@RequestBody @Validated NoticeSaveDto noticeSaveDto,
                                    Errors errors) {

        hasErrorsThrow(errors);

        Notice notice = noticeService.createNotice(noticeSaveDto);

        NoticeDetailDto body = NoticeDetailDto.noticeToDetailDto(notice);
        return ResponseDto.created(body);
    }


    @PutMapping("/{noticeId}")
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto updateNotice(@PathVariable("noticeId") Long noticeId,
                                    @RequestBody @Validated NoticeSaveDto noticeSaveDto,
                                    Errors errors) {
        hasErrorsThrow(errors);

        noticeService.updateNotice(noticeSaveDto, noticeId);

        return ResponseDto.noContent();
    }

    @PutMapping("/used/{noticeId}")
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto useNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.useNotice(noticeId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{noticeId}")
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto unUseNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.unUseNotice(noticeId);
        return ResponseDto.noContent();
    }


    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto deleteNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseDto.noContent();
    }
}
