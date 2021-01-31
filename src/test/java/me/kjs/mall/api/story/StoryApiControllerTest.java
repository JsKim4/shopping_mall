package me.kjs.mall.api.story;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.PostStatus;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.story.Story;
import me.kjs.mall.story.dto.StorySaveDto;
import me.kjs.mall.story.dto.StorySearchCondition;
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

class StoryApiControllerTest extends BaseTest {
    @Test
    void storyCreateTest() throws Exception {
        TokenDto tokenDto = getTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/stories")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storySaveDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").value(storySaveDto.getBeginDate().toString()))
                .andExpect(jsonPath("data.contents").exists())
                .andExpect(jsonPath("data.title").value(storySaveDto.getTitle()))
                .andExpect(jsonPath("data.thumbnailImage").value(storySaveDto.getThumbnailImage()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andExpect(jsonPath("data.postStatus").value(PostStatus.WAIT.name()))
                .andDo(document("create-story",
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
                                fieldWithPath("contents").description("이벤트 내역 이미지"),
                                fieldWithPath("thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("title").description("이벤트 제목")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.storyId").description("이벤트 고유 번호"),
                                fieldWithPath("data.title").description("이벤트 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 이미지"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS)),
                                fieldWithPath("data.contents").description("이벤트 내용 html")
                        )
                ));
    }


    @Test
    void queryStorys() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        for (int i = 0; i < 10; i++) {
            StorySaveDto storySaveDto = StorySaveDto.builder()
                    .beginDate(LocalDate.now())
                    .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                    .thumbnailImage("ThumbnailImage")
                    .title("story title - " + i)
                    .build();
            Story story = storyService.createStory(storySaveDto);
            story.updateStatus(CommonStatus.USED);
        }

        StorySearchCondition searchCondition = StorySearchCondition.builder()
                .contents(5)
                .page(0)
                .postStatus(PostStatus.PROCESS)
                .status(CommonStatus.USED)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stories")
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
                .andDo(document("query-stories",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "일반 회원은 USED만 가능")),
                                parameterWithName("postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[].storyId").description("이벤트 고유 번호"),
                                fieldWithPath("data.contents[].title").description("이벤트 제목"),
                                fieldWithPath("data.contents[].thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.contents[].beginDate").description("시작일자"),
                                fieldWithPath("data.contents[].visitCount").description("조회수"),
                                fieldWithPath("data.contents[].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[].postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS)),
                                fieldWithPath("data.contents[].contents").description("이벤트 이미지 목록")
                        )
                ));
    }


    @Test
    void queryStory() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        Story story = storyService.createStory(storySaveDto);
        story.updateStatus(CommonStatus.USED);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/stories/{storyId}", story.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.beginDate").exists())
                .andExpect(jsonPath("data.title").exists())
                .andExpect(jsonPath("data.thumbnailImage").exists())
                .andExpect(jsonPath("data.status").exists())
                .andExpect(jsonPath("data.postStatus").exists())
                .andExpect(jsonPath("data.contents").exists())
                .andDo(document("query-story",
                        pathParameters(
                                parameterWithName("storyId").description("이벤트 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.storyId").description("이벤트 고유 번호"),
                                fieldWithPath("data.title").description("이벤트 제목"),
                                fieldWithPath("data.thumbnailImage").description("썸네일 임지ㅣ"),
                                fieldWithPath("data.beginDate").description("시작일자"),
                                fieldWithPath("data.visitCount").description("조회수"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.postStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POST_STATUS)),
                                fieldWithPath("data.contents").description("이벤트 내용 html")
                        )
                ));
    }

    @Test
    void updateStory() throws Exception {
        TokenDto tokenDto = getTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        Story story = storyService.createStory(storySaveDto);
        StorySaveDto storyUpdateDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/stories/{storyId}", story.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storyUpdateDto)))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-story",
                        pathParameters(
                                parameterWithName("storyId").description("이벤트 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("beginDate").description("시작 일자").type("Date"),
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
    void deleteStory() throws Exception {
        TokenDto tokenDto = getTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        Story story = storyService.createStory(storySaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/stories/{storyId}", story.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("delete-story",
                        pathParameters(
                                parameterWithName("storyId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(story.getStatus(), CommonStatus.DELETED);
    }


    @Test
    void useStory() throws Exception {
        TokenDto tokenDto = getTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        Story story = storyService.createStory(storySaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/stories/used/{storyId}", story.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-story-used",
                        pathParameters(
                                parameterWithName("storyId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(story.getStatus(), CommonStatus.USED);
    }

    @Test
    void unUseStory() throws Exception {
        TokenDto tokenDto = getTokenDto();

        StorySaveDto storySaveDto = StorySaveDto.builder()
                .beginDate(LocalDate.now())
                .contents(Arrays.asList("image001", "image002", "image003", "image004").toString())
                .thumbnailImage("ThumbnailImage")
                .title("story title")
                .build();
        Story story = storyService.createStory(storySaveDto);
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/stories/unused/{storyId}", story.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-story-unused",
                        pathParameters(
                                parameterWithName("storyId").description("이벤트 고유 번호")
                        )
                ));
        assertEquals(story.getStatus(), CommonStatus.UN_USED);
    }

}