package me.kjs.mall.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.notice.dto.NoticeSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NoticeService {


    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice createNotice(NoticeSaveDto noticeSaveDto) {
        Notice notice = Notice.createNotice(noticeSaveDto);
        return noticeRepository.save(notice);
    }

    @Transactional
    public void updateNotice(NoticeSaveDto noticeSaveDto, Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        notice.updateNotice(noticeSaveDto);
    }


    @Transactional
    public void useNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        notice.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        notice.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        notice.delete();
    }

    public Notice findNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notAvailableThrow(notice);
        return notice;
    }

    @Transactional
    public void visit(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoExistIdException::new);
        notice.visit();
    }
}
