package me.kjs.mall.api.destination;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.destination.Destination;
import me.kjs.mall.destination.dto.DestinationSaveDto;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DestinationApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/destinations 배송지 생성 성공 케이스")
    void createDestinationTest() throws Exception {
        DestinationSaveDto destinationSaveDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();


        TokenDto tokenDto = getUserTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/destinations")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.destinationId").exists())
                .andExpect(jsonPath("data.destinationName").value(destinationSaveDto.getDestinationName()))
                .andExpect(jsonPath("data.recipient").value(destinationSaveDto.getRecipient()))
                .andExpect(jsonPath("data.tel1").value(destinationSaveDto.getTel1()))
                .andExpect(jsonPath("data.tel2").value(destinationSaveDto.getTel2()))
                .andExpect(jsonPath("data.addressSimple").value(destinationSaveDto.getAddressSimple()))
                .andExpect(jsonPath("data.addressDetail").value(destinationSaveDto.getAddressDetail()))
                .andExpect(jsonPath("data.zipcode").value(destinationSaveDto.getZipcode()))
                .andDo(document(
                        "create-destination",
                        requestFields(
                                fieldWithPath("destinationName").description("배송지 이름"),
                                fieldWithPath("recipient").description("수령인 이름"),
                                fieldWithPath("tel1").description("전화번호 1"),
                                fieldWithPath("tel2").description("전화번호 2").optional(),
                                fieldWithPath("addressSimple").description("수령 주소"),
                                fieldWithPath("addressDetail").description("수령 주소 상세"),
                                fieldWithPath("zipcode").description("우편 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 201"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.destinationId").description("배송지 고유 번호"),
                                fieldWithPath("data.destinationName").description("배송지 이름"),
                                fieldWithPath("data.recipient").description("수령인 이름"),
                                fieldWithPath("data.tel1").description("전화번호 1"),
                                fieldWithPath("data.tel2").description("전화번호 2").optional(),
                                fieldWithPath("data.addressSimple").description("수령 주소"),
                                fieldWithPath("data.addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data.zipcode").description("우편 번호")
                        )
                ));
    }

    @Test
    @DisplayName("/api/destinations 배송지 개수 초과로 실패하는 케이스")
    void createDestinationTestFailByOverCount() throws Exception {
        DestinationSaveDto destinationSaveDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();

        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        for (int i = 0; i < 29; i++) {
            destinationService.createDestination(destinationSaveDto, member.getId());
        }
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/destinations")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201));
        int expectedStatus = 4021;
        String expectedMessage = "저장 가능한 배송지 최대 개수를 초과 하였습니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/destinations")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "create-destination-fail-limit-over"
                ));
    }


    @Test
    @DisplayName("/api/destinations/{productId} 배송지 수정 성공 케이스")
    void updateDestinationTest() throws Exception {
        DestinationSaveDto destinationCreateDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();

        DestinationSaveDto destinationUpdateDto = DestinationSaveDto.builder()
                .destinationName("destination name2")
                .recipient("recipient2")
                .tel1("01026420238")
                .tel2("01090217326")
                .addressSimple("gyeonggi SungNam Bundang2")
                .addressDetail("PangyoRoad 222 3333")
                .zipcode("99999")
                .build();


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        Destination destination = destinationService.createDestination(destinationCreateDto, member.getId());


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/destinations/{destinationId}", destination.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-destination",
                        pathParameters(
                                parameterWithName("destinationId").description("배송지 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("destinationName").description("배송지 이름").optional(),
                                fieldWithPath("recipient").description("수령인 이름").optional(),
                                fieldWithPath("tel1").description("전화번호 1").optional(),
                                fieldWithPath("tel2").description("전화번호 2").optional(),
                                fieldWithPath("addressSimple").description("수령 주소").optional(),
                                fieldWithPath("addressDetail").description("수령 주소 상세").optional(),
                                fieldWithPath("zipcode").description("우편 번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 204"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));
        assertEquals(destinationUpdateDto.getDestinationName(), destination.getName());
        assertEquals(destinationUpdateDto.getRecipient(), destination.getRecipient());
        assertEquals(destinationUpdateDto.getTel1(), destination.getTel1());
        assertEquals(destinationUpdateDto.getTel2(), destination.getTel2());
        assertEquals(destinationUpdateDto.getAddressSimple(), destination.getAddressSimple());
        assertEquals(destinationUpdateDto.getAddressDetail(), destination.getAddressDetail());
        assertEquals(destinationUpdateDto.getZipcode(), destination.getZipcode());
    }


    @Test
    @DisplayName("/api/destinations/{productId} 배송지 수정 성공 케이스 - 아무것도 변경되지 않음")
    void updateDestinationNotModifiedTest() throws Exception {
        DestinationSaveDto destinationCreateDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();

        DestinationSaveDto destinationUpdateDto = DestinationSaveDto.builder()
                .destinationName("")
                .recipient("")
                .tel1("")
                .tel2("")
                .addressSimple("")
                .addressDetail("")
                .zipcode("")
                .build();


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        Destination destination = destinationService.createDestination(destinationCreateDto, member.getId());


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/destinations/{destinationId}", destination.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-destination-not-modified",
                        pathParameters(
                                parameterWithName("destinationId").description("배송지 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("destinationName").description("배송지 이름"),
                                fieldWithPath("recipient").description("수령인 이름"),
                                fieldWithPath("tel1").description("전화번호 1"),
                                fieldWithPath("tel2").description("전화번호 2").optional(),
                                fieldWithPath("addressSimple").description("수령 주소"),
                                fieldWithPath("addressDetail").description("수령 주소 상세"),
                                fieldWithPath("zipcode").description("우편 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 204"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )
                ));
        assertEquals(destinationCreateDto.getDestinationName(), destination.getName());
        assertEquals(destinationCreateDto.getRecipient(), destination.getRecipient());
        assertEquals(destinationCreateDto.getTel1(), destination.getTel1());
        assertEquals(destinationCreateDto.getAddressSimple(), destination.getAddressSimple());
        assertEquals(destinationCreateDto.getAddressDetail(), destination.getAddressDetail());
        assertEquals(destinationCreateDto.getZipcode(), destination.getZipcode());
    }


    @Test
    @DisplayName("/api/destinations 배송지 조회")
    void queryDestinationTest() throws Exception {
        DestinationSaveDto destinationCreateDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        for (int i = 0; i < 5; i++) {
            Destination destination = destinationService.createDestination(destinationCreateDto, member.getId());
        }

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/destinations")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("data[0].destinationId").exists())
                .andExpect(jsonPath("data[0].destinationName").exists())
                .andExpect(jsonPath("data[0].recipient").exists())
                .andExpect(jsonPath("data[0].tel1").exists())
                .andExpect(jsonPath("data[0].addressSimple").exists())
                .andExpect(jsonPath("data[0].addressDetail").exists())
                .andExpect(jsonPath("data[0].zipcode").exists())
                .andDo(document(
                        "query-destination",
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[].destinationId").description("배송지 고유 번호"),
                                fieldWithPath("data[].destinationName").description("배송지 이름"),
                                fieldWithPath("data[].recipient").description("수령인 이름"),
                                fieldWithPath("data[].tel1").description("전화번호 1"),
                                fieldWithPath("data[].tel2").description("전화번호 2").optional(),
                                fieldWithPath("data[].addressSimple").description("수령 주소"),
                                fieldWithPath("data[].addressDetail").description("수령 주소 상세"),
                                fieldWithPath("data[].zipcode").description("우편 번호")
                        )
                ));
    }


    @Test
    @DisplayName("/api/destinations/{destinationId} 배송지 삭제")
    void deleteDestinationTest() throws Exception {
        DestinationSaveDto destinationCreateDto = DestinationSaveDto.builder()
                .destinationName("destination name")
                .recipient("recipient")
                .tel1("01026420239")
                .tel2("01090217325")
                .addressSimple("gyeonggi SungNam Bundang")
                .addressDetail("PangyoRoad 111 2222")
                .zipcode("40123")
                .build();

        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        Destination destination = destinationService.createDestination(destinationCreateDto, member.getId());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/destinations/{destinationId}", destination.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "delete-destination",
                        pathParameters(
                                parameterWithName("destinationId").description("배송지 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("Null").optional())
                        )
                );
    }


}