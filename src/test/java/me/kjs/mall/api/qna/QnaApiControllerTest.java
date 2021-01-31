package me.kjs.mall.api.qna;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyContentDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import me.kjs.mall.qna.Qna;
import me.kjs.mall.qna.dto.QnaCreateDto;
import me.kjs.mall.qna.dto.QnaSearchCondition;
import me.kjs.mall.qna.dto.QnaUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class QnaApiControllerTest extends BaseTest {

    @Test
    void createQnaTest() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Long productId = products.get(0).getId();
        QnaCreateDto qnaCreateDto = QnaCreateDto.builder()
                .content("qna create")
                .secret(YnType.N)
                .productId(productId)
                .build();

        TokenDto userTokenDto = getUserTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/qnas")
                .header("X-AUTH-TOKEN", userTokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(qnaCreateDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.qnaId").exists())
                .andExpect(jsonPath("data.questionContent").value(qnaCreateDto.getContent()))
                .andExpect(jsonPath("data.secret").value(qnaCreateDto.getSecret().name()))
                .andExpect(jsonPath("data.comment").value(YnType.N.name()))
                .andExpect(jsonPath("data.mine").value(YnType.Y.name()))
                .andExpect(jsonPath("data.postDate").exists())
                .andDo(document("create-qna-question",
                        requestFields(
                                fieldWithPath("content").description("질문 내용"),
                                fieldWithPath("secret").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "비밀글 여부")),
                                fieldWithPath("productId").description("상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.qnaId").description("qna 질문 고유 번호"),
                                fieldWithPath("data.questionContent").description("질문 내용"),
                                fieldWithPath("data.secret").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "비밀글 여부")),
                                fieldWithPath("data.comment").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "코멘트 여부")),
                                fieldWithPath("data.mine").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "작성자 여부")),
                                fieldWithPath("data.answerContent").description("답변 내용").optional().type("String"),
                                fieldWithPath("data.postDate").description("게시 일자").type("Date"),
                                fieldWithPath("data.productId").description("상품 고유 번호"),
                                fieldWithPath("data.productName").description("상품 이름"),
                                fieldWithPath("data.reviewer").description("qna 작성자")
                        )
                ));
    }

    @Test
    void queryQnaTest() throws Exception {

        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Long productId = products.get(0).getId();
        TokenDto userTokenDto = getUserTokenDto();
        for (int i = 0; i < 5; i++) {
            QnaCreateDto qnaCreateDto = QnaCreateDto.builder()
                    .content("qna create " + i)
                    .secret(YnType.N)
                    .build();
            qnaService.createQna(qnaCreateDto, productId, memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new));
        }

        QnaSearchCondition qnaSearchCondition = QnaSearchCondition.builder()
                .contents(5)
                .page(0)
                .queryCurrent(YnType.N)
                .productId(productId)
                .answer(YnType.N)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/qnas/")
                .param("contents", String.valueOf(qnaSearchCondition.getContents()))
                .param("page", String.valueOf(qnaSearchCondition.getPage()))
                .param("productId", String.valueOf(productId))
                .param("answer", String.valueOf(qnaSearchCondition.getAnswer()))
                .param("queryCurrent", String.valueOf(qnaSearchCondition.getQueryCurrent())))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.contents[*].qnaId").exists())
                .andExpect(jsonPath("data.contents[*].questionContent").exists())
                .andExpect(jsonPath("data.contents[*].secret").exists())
                .andExpect(jsonPath("data.contents[*].comment").exists())
                .andExpect(jsonPath("data.contents[*].mine").exists())
                .andExpect(jsonPath("data.contents[*].postDate").exists())
                .andDo(document("query-qna-question",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작"),
                                parameterWithName("queryCurrent").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "내 질문 조회 여부")),
                                parameterWithName("productId").description("상품 고유 번호"),
                                parameterWithName("answer").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "답변 여부"))
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("요청한 콘텐츠 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.hasNext").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다음 페이지 존재 여부")),
                                fieldWithPath("data.contents[*].qnaId").description("qna 질문 고유 번호"),
                                fieldWithPath("data.contents[*].questionContent").description("질문 내용"),
                                fieldWithPath("data.contents[*].secret").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "비밀글 여부")),
                                fieldWithPath("data.contents[*].comment").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "코멘트 여부")),
                                fieldWithPath("data.contents[*].mine").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "작성자 여부")),
                                fieldWithPath("data.contents[*].answerContent").description("답변 내용").optional().type("String"),
                                fieldWithPath("data.contents[*].postDate").description("게시 일자").type("Date"),
                                fieldWithPath("data.contents[*].productId").description("상품 고유 번호"),
                                fieldWithPath("data.contents[*].productName").description("상품 이름"),
                                fieldWithPath("data.contents[*].reviewer").description("qna 작성자")
                        )
                ));
    }


    @Test
    void updateQnaTest() throws Exception {
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Long productId = products.get(0).getId();
        TokenDto userTokenDto = getUserTokenDto();
        QnaCreateDto qnaCreateDto = QnaCreateDto.builder()
                .content("qna create")
                .secret(YnType.N)
                .build();
        Qna qna = qnaService.createQna(qnaCreateDto, productId, memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new));

        QnaUpdateDto qnaUpdateDto = QnaUpdateDto.builder()
                .content("qna update")
                .secret(YnType.Y)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/qnas/{qnaId}", qna.getId())
                .header("X-AUTH-TOKEN", userTokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(qnaUpdateDto)))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-qna-question",
                        pathParameters(
                                parameterWithName("qnaId").description("상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").description("질문 내용"),
                                fieldWithPath("secret").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "비밀글 여부"))
                        )
                ));
        assertEquals(qna.getQuestionContent(), qnaUpdateDto.getContent());
        assertEquals(qna.getSecret(), qnaUpdateDto.getSecret());
    }

    @Test
    void deleteQnaTest() throws Exception {
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Long productId = products.get(0).getId();
        TokenDto userTokenDto = getUserTokenDto();
        QnaCreateDto qnaCreateDto = QnaCreateDto.builder()
                .content("qna create")
                .secret(YnType.N)
                .build();
        Qna qna = qnaService.createQna(qnaCreateDto, productId, memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new));


        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/qnas/{qnaId}", qna.getId())
                .header("X-AUTH-TOKEN", userTokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("delete-qna",
                        pathParameters(
                                parameterWithName("qnaId").description("상품 고유 번호")
                        )
                ));
        assertFalse(qna.isAvailable());
    }

    @Test
    void answerQuestion() throws Exception {
        List<Product> products = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        Long productId = products.get(0).getId();
        TokenDto userTokenDto = getUserTokenDto();
        QnaCreateDto qnaCreateDto = QnaCreateDto.builder()
                .content("qna create")
                .secret(YnType.N)
                .build();
        Qna qna = qnaService.createQna(qnaCreateDto, productId, memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new));
        TokenDto tokenDto = getTokenDto();
        OnlyContentDto onlyContentDto = OnlyContentDto.builder()
                .content("qna update")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/qnas/answer/{qnaId}", qna.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyContentDto))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-qna-answer",
                        pathParameters(
                                parameterWithName("qnaId").description("상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("content").description("qna 대답 내용")
                        )
                ));

        assertEquals(qna.getComment(), YnType.Y);
        assertEquals(qna.getAnswerContent(), onlyContentDto.getContent());
    }
}
