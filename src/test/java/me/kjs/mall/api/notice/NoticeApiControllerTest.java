package me.kjs.mall.api.notice;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.notice.Notice;
import me.kjs.mall.notice.dto.NoticeSaveDto;
import me.kjs.mall.notice.dto.NoticeSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class NoticeApiControllerTest extends BaseTest {
    @Test
    void noticeCreateTest() throws Exception {
        TokenDto tokenDto = getTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("notice content")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/notices")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noticeSaveDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").value(noticeSaveDto.getBeginDate().toString()))
                .andExpect(jsonPath("data.content").exists())
                .andExpect(jsonPath("data.title").value(noticeSaveDto.getTitle()))
                .andExpect(jsonPath("data.thumbnailImage").value(noticeSaveDto.getThumbnailImage()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andExpect(jsonPath("data.postStatus").value(PostStatus.WAIT.name()))
                .andDo(document("create-notice",
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
                                fieldWithPath("content").description("공지 내역 이미지"),
                                fieldWithPath("thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("title").description("공지 제목")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.noticeId").description("공지 고유 번호"),
                                fieldWithPath("data.title").description("공지 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS)),
                                fieldWithPath("data.content").description("공지 이미지 목록")
                        )
                ));
    }


    @Test
    void queryNotices() throws Exception {
        TokenDto tokenDto = getTokenDto();

        for (int i = 0; i < 10; i++) {
            NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                    .beginDate(LocalDate.now())
                    .content("content text text text text text text")
                    .thumbnailImage("ThumbnailImage")
                    .title("notice title - " + i)
                    .build();
            Notice notice = noticeService.createNotice(noticeSaveDto);
            notice.updateStatus(CommonStatus.USED);
        }

        NoticeSearchCondition searchCondition = NoticeSearchCondition.builder()
                .contents(5)
                .page(0)
                .postStatus(PostStatus.PROCESS)
                .status(CommonStatus.USED)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/notices")
                .param("contents", String.valueOf(searchCondition.getContents()))
                .param("page", String.valueOf(searchCondition.getPage()))
                .param("status", String.valueOf(searchCondition.getStatus()))
                .param("postStatus", String.valueOf(searchCondition.getPostStatus()))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.contents[*].beginDate").exists())
                .andExpect(jsonPath("data.contents[*].title").exists())
                .andExpect(jsonPath("data.contents[*].thumbnailImage").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andExpect(jsonPath("data.contents[*].postStatus").exists())
                .andDo(document("query-notices",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "일반 회원은 USED만 가능")),
                                parameterWithName("postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 공지 개수"),
                                fieldWithPath("data.totalCount").description("전체 공지 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[].noticeId").description("공지 고유 번호"),
                                fieldWithPath("data.contents[].title").description("공지 제목"),
                                fieldWithPath("data.contents[].thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.contents[].beginDate").description("시작일자"),
                                fieldWithPath("data.contents[].visitCount").description("조회수"),
                                fieldWithPath("data.contents[].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[].postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS))
                        )
                ));
    }


    @Test
    void queryNotice() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        Notice notice = noticeService.createNotice(noticeSaveDto);
        notice.updateStatus(CommonStatus.USED);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/notices/{noticeId}", notice.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").exists())
                .andExpect(jsonPath("data.title").exists())
                .andExpect(jsonPath("data.thumbnailImage").exists())
                .andExpect(jsonPath("data.status").exists())
                .andExpect(jsonPath("data.postStatus").exists())
                .andExpect(jsonPath("data.content").exists())
                .andDo(document("query-notice",
                        pathParameters(
                                parameterWithName("noticeId").description("공지 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.noticeId").description("공지 고유 번호"),
                                fieldWithPath("data.title").description("공지 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS)),
                                fieldWithPath("data.content").description("공지 이미지 목록")
                        )
                ));
    }

    @Test
    void updateNotice() throws Exception {
        TokenDto tokenDto = getTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        Notice notice = noticeService.createNotice(noticeSaveDto);
        NoticeSaveDto noticeUpdateDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/notices/{noticeId}", notice.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noticeUpdateDto)))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-notice",
                        pathParameters(
                                parameterWithName("noticeId").description("공지 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
                                fieldWithPath("content").description("공지 내역 이미지"),
                                fieldWithPath("thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("title").description("공지 제목")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 204),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    void deleteNotice() throws Exception {
        TokenDto tokenDto = getTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        Notice notice = noticeService.createNotice(noticeSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/notices/{noticeId}", notice.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-notice",
                        pathParameters(
                                parameterWithName("noticeId").description("공지 고유 번호")
                        )
                ));
        assertEquals(notice.getStatus(), CommonStatus.DELETED);
    }


    @Test
    void useNotice() throws Exception {
        TokenDto tokenDto = getTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        Notice notice = noticeService.createNotice(noticeSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/notices/used/{noticeId}", notice.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-notice-used",
                        pathParameters(
                                parameterWithName("noticeId").description("공지 고유 번호")
                        )
                ));
        assertEquals(notice.getStatus(), CommonStatus.USED);
    }

    @Test
    void unUseNotice() throws Exception {
        TokenDto tokenDto = getTokenDto();

        NoticeSaveDto noticeSaveDto = NoticeSaveDto.builder()
                .beginDate(LocalDate.now())
                .content("content text text text text text text")
                .thumbnailImage("ThumbnailImage")
                .title("notice title")
                .build();
        Notice notice = noticeService.createNotice(noticeSaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/notices/unused/{noticeId}", notice.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-notice-unused",
                        pathParameters(
                                parameterWithName("noticeId").description("공지 고유 번호")
                        )
                ));
        assertEquals(notice.getStatus(), CommonStatus.UN_USED);
    }

}