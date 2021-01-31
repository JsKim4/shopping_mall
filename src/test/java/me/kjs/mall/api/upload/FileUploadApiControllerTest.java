package me.kjs.mall.api.upload;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.connect.ConnectService;
import me.kjs.mall.connect.UploadPath;
import me.kjs.mall.member.dto.sign.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileUploadApiControllerTest extends BaseTest {

    @MockBean
    private ConnectService connectService;


    @Test
    void fileUploadMockingTest() throws Exception {
        String fileDir = "/tmp";
        String fileName = "import_target.xls";
        String fileFullPath = String.format("%s/%s", fileDir, fileName);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[1024]
        );

        String imagePath = "upload/" + LocalDate.now().toString() + "/" + UUID.randomUUID().toString();
        given(connectService.uploadImage(file, UploadPath.EXCHANGE)).willReturn(imagePath);

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(multipart("/api/upload/exchange")
                .file(file)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").value(imagePath))
                .andDo(document("create-file-upload-exchange",
                        requestParts(
                                partWithName("file").description("업로드 파일")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("상태 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("업로드 이미지 PATH")
                        )));
    }

    @Test
    void fileUploadBaseProductMockingTest() throws Exception {
        String fileDir = "/tmp";
        String fileName = "import_target.xls";
        String fileFullPath = String.format("%s/%s", fileDir, fileName);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[1024]
        );

        String imagePath = "upload/" + LocalDate.now().toString() + "/" + UUID.randomUUID().toString();
        given(connectService.uploadImage(file, UploadPath.BASE_PRODUCT)).willReturn(imagePath);

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(multipart("/api/upload/base")
                .file(file)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").value(imagePath))
                .andDo(document("create-file-upload-base",
                        requestParts(
                                partWithName("file").description("업로드 파일")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("상태 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("업로드 이미지 PATH")
                        )));
    }

}