package me.kjs.mall.api.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.event.Event;
import me.kjs.mall.event.EventQueryRepository;
import me.kjs.mall.event.EventService;
import me.kjs.mall.event.dto.EventDetailDto;
import me.kjs.mall.event.dto.EventSaveDto;
import me.kjs.mall.event.dto.EventSearchCondition;
import me.kjs.mall.event.dto.EventSimpleDto;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.type.AccountRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.AvailableUtil.hasRole;
import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Slf4j
public class EventApiController {

    private final EventService eventService;

    private final EventQueryRepository eventQueryRepository;

    @GetMapping
    public ResponseDto queryEvents(@Validated EventSearchCondition eventSearchCondition,
                                   Errors errors,
                                   @CurrentMember Member member) {
        hasErrorsThrow(errors);
        if (eventSearchCondition.getStatus() == CommonStatus.DELETED) {
            errors.rejectValue("status", "wrong value", "delete event don't query");
        }
        if (!hasRole(member, AccountRole.EVENT)) {
            if (eventSearchCondition.getStatus() != CommonStatus.USED) {
                errors.rejectValue("status", "wrong value", "not admin don't query un_used or created");
            }
        }
        hasErrorsThrow(errors);

        CommonPage<Event> events = eventQueryRepository.findEventBySearchCondition(eventSearchCondition);

        CommonPage body = events.updateContent(events.getContents().stream().map(EventSimpleDto::eventToSimpleDto).collect(Collectors.toList()));

        return ResponseDto.ok(body);
    }

    /**
     * 멱등성 보장 안됨
     */
    @GetMapping("/{eventId}")
    public ResponseDto queryEvent(@PathVariable("eventId") Long eventId,
                                  @CurrentMember Member member) {
        Event event = eventService.findEventById(eventId);
        if (!hasRole(member, AccountRole.EVENT)) {
            if (!event.isUsed()) {
                throw new NoExistIdException();
            }
        }
        eventService.visit(event.getId());
        EventDetailDto body = EventDetailDto.eventToDetailDto(event);
        return ResponseDto.ok(body);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto createEvent(@RequestBody @Validated EventSaveDto eventSaveDto,
                                   Errors errors) {

        hasErrorsThrow(errors);
        validation(eventSaveDto, errors);
        hasErrorsThrow(errors);

        Event event = eventService.createEvent(eventSaveDto);

        EventDetailDto body = EventDetailDto.eventToDetailDto(event);

        return ResponseDto.created(body);
    }

    private void validation(EventSaveDto eventSaveDto, Errors errors) {
        if (eventSaveDto.getBeginDate().isAfter(eventSaveDto.getEndDate())) {
            errors.rejectValue("beginDate", "wrong value", "beginDate don't after endDate");
        }
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto updateEvent(@PathVariable("eventId") Long eventId,
                                   @RequestBody @Validated EventSaveDto eventSaveDto,
                                   Errors errors) {
        hasErrorsThrow(errors);
        validation(eventSaveDto, errors);
        hasErrorsThrow(errors);

        eventService.updateEvent(eventSaveDto, eventId);

        return ResponseDto.noContent();
    }

    @PutMapping("/used/{eventId}")
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto useEvent(@PathVariable("eventId") Long eventId) {
        eventService.useEvent(eventId);
        return ResponseDto.noContent();
    }

    @PutMapping("/unused/{eventId}")
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto unUseEvent(@PathVariable("eventId") Long eventId) {
        eventService.unUseEvent(eventId);
        return ResponseDto.noContent();
    }


    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto deleteEvent(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseDto.noContent();
    }


}
