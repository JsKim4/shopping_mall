package me.kjs.mall.api.event;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.event.Event;
import me.kjs.mall.event.EventStatus;
import me.kjs.mall.event.dto.EventSaveDto;
import me.kjs.mall.event.dto.EventSearchCondition;
import me.kjs.mall.member.dto.sign.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class EventApiControllerTest extends BaseTest {
    @Test
    void eventCreateTest() throws Exception {
        TokenDto tokenDto = getTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/events")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventSaveDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").value(eventSaveDto.getBeginDate().toString()))
                .andExpect(jsonPath("data.endDate").value(eventSaveDto.getBeginDate().toString()))
                .andExpect(jsonPath("data.contents").isArray())
                .andExpect(jsonPath("data.title").value(eventSaveDto.getTitle()))
                .andExpect(jsonPath("data.thumbnailImage").value(eventSaveDto.getThumbnailImage()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andExpect(jsonPath("data.eventStatus").value(EventStatus.WAIT.name()))
                .andDo(document("create-event",
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
                                fieldWithPath("endDate").description("종료 일자").type("Date"),
                                fieldWithPath("contents").description("이벤트 내역 이미지"),
                                fieldWithPath("thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("title").description("이벤트 제목")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.eventId").description("이벤트 고유 번호"),
                                fieldWithPath("data.title").description("이벤트 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.endDate").description("종료일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.eventStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.EVENT_STATUS)),
                                fieldWithPath("data.contents").description("이벤트 이미지 목록")
                        )
                ));
    }


    @Test
    void queryEvents() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        for (int i = 0; i < 10; i++) {
            EventSaveDto eventSaveDto = EventSaveDto.builder()
                    .beginDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                    .thumbnailImage("ThumbnailImage")
                    .title("event title - " + i)
                    .build();
            Event event = eventService.createEvent(eventSaveDto);
            event.updateStatus(CommonStatus.USED);
        }

        EventSearchCondition searchCondition = EventSearchCondition.builder()
                .contents(5)
                .page(0)
                .status(CommonStatus.USED)
                .eventStatus(EventStatus.PROCESS)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/events")
                .param("contents", String.valueOf(searchCondition.getContents()))
                .param("page", String.valueOf(searchCondition.getPage()))
                .param("status", String.valueOf(searchCondition.getStatus()))
                .param("eventStatus", String.valueOf(searchCondition.getEventStatus()))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.contents[*].beginDate").exists())
                .andExpect(jsonPath("data.contents[*].endDate").exists())
                .andExpect(jsonPath("data.contents[*].title").exists())
                .andExpect(jsonPath("data.contents[*].thumbnailImage").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andExpect(jsonPath("data.contents[*].eventStatus").exists())
                .andDo(document("query-events",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "일반 회원은 USED만 가능")),
                                parameterWithName("eventStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.EVENT_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[].eventId").description("이벤트 고유 번호"),
                                fieldWithPath("data.contents[].title").description("이벤트 제목"),
                                fieldWithPath("data.contents[].thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.contents[].beginDate").description("시작일자"),
                                fieldWithPath("data.contents[].endDate").description("종료일자"),
                                fieldWithPath("data.contents[].visitCount").description("조회수"),
                                fieldWithPath("data.contents[].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[].eventStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.EVENT_STATUS))
                        )
                ));
    }


    @Test
    void queryEvent() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        Event event = eventService.createEvent(eventSaveDto);
        event.updateStatus(CommonStatus.USED);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/events/{eventId}", event.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").exists())
                .andExpect(jsonPath("data.endDate").exists())
                .andExpect(jsonPath("data.title").exists())
                .andExpect(jsonPath("data.thumbnailImage").exists())
                .andExpect(jsonPath("data.status").exists())
                .andExpect(jsonPath("data.eventStatus").exists())
                .andExpect(jsonPath("data.contents").isArray())
                .andDo(document("query-event",
                        pathParameters(
                                parameterWithName("eventId").description("이벤트 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.eventId").description("이벤트 고유 번호"),
                                fieldWithPath("data.title").description("이벤트 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.endDate").description("종료일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.eventStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.EVENT_STATUS)),
                                fieldWithPath("data.contents").description("이벤트 이미지 목록")
                        )
                ));
    }

    @Test
    void updateEvent() throws Exception {
        TokenDto tokenDto = getTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        Event event = eventService.createEvent(eventSaveDto);
        EventSaveDto eventUpdateDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/{eventId}", event.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventUpdateDto)))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-event",
                        pathParameters(
                                parameterWithName("eventId").description("이벤트 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
                                fieldWithPath("endDate").description("종료 일자").type("Date"),
                                fieldWithPath("contents").description("이벤트 내역 이미지"),
                                fieldWithPath("thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("title").description("이벤트 제목")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 204),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    void deleteEvent() throws Exception {
        TokenDto tokenDto = getTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        Event event = eventService.createEvent(eventSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/events/{eventId}", event.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-event",
                        pathParameters(
                                parameterWithName("eventId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(event.getStatus(), CommonStatus.DELETED);
    }


    @Test
    void useEvent() throws Exception {
        TokenDto tokenDto = getTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        Event event = eventService.createEvent(eventSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/used/{eventId}", event.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-event-used",
                        pathParameters(
                                parameterWithName("eventId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(event.getStatus(), CommonStatus.USED);
    }

    @Test
    void unUseEvent() throws Exception {
        TokenDto tokenDto = getTokenDto();

        EventSaveDto eventSaveDto = EventSaveDto.builder()
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004"))
                .thumbnailImage("ThumbnailImage")
                .title("event title")
                .build();
        Event event = eventService.createEvent(eventSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/events/unused/{eventId}", event.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-event-unused",
                        pathParameters(
                                parameterWithName("eventId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(event.getStatus(), CommonStatus.UN_USED);
    }


}