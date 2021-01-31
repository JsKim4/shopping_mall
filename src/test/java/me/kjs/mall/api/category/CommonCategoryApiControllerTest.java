package me.kjs.mall.api.category;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.category.Category;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.dto.sign.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonCategoryApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/categories 카테고리 조회")
    public void queryCategory() throws Exception {
        TokenDto tokenDto = getTokenDto();
        List<Category> categories = categoryService.findAllCategoriesFetch();
        categoryService.removeCategory(categories.get(0).getId());
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data[0].categoryId").exists())
                .andExpect(jsonPath("data[0].categoryName").exists())
                .andExpect(jsonPath("data[0].childCategories").isArray())
                .andExpect(jsonPath("data[0].childCategories[0].childCategories").doesNotExist())
                .andDo(document("query-category",
                        relaxedResponseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[0].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("data[0].categoryName").description("카테고리 이름"),
                                fieldWithPath("data[0].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data[0].childCategories[]").description("하위 카테고리 리스트")
                        )));
    }

}