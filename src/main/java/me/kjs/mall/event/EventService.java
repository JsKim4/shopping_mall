package me.kjs.mall.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.event.dto.EventSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public Event createEvent(EventSaveDto eventSaveDto) {
        Event event = Event.createEvent(eventSaveDto);
        return eventRepository.save(event);
    }

    @Transactional
    public void updateEvent(EventSaveDto eventSaveDto, Long eventId) {
        Event event = findEventById(eventId);
        event.updateEvent(eventSaveDto);
    }


    @Transactional
    public void useEvent(Long eventId) {
        Event event = findEventById(eventId);
        event.updateStatus(CommonStatus.USED);
    }

    @Transactional
    public void unUseEvent(Long eventId) {
        Event event = findEventById(eventId);
        event.updateStatus(CommonStatus.UN_USED);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = findEventById(eventId);
        event.delete();
    }

    public Event findEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notAvailableThrow(event);
        return event.loading();
    }


    public void visit(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoExistIdException::new);
        event.visit();
    }
}
