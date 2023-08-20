package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyExistsException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.services.MailingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class MailingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MailingService mailingService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResponseFieldsSnippet errorSnippet = responseFields(
            fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("http status of error"),
            fieldWithPath("timestamp").type(JsonFieldType.STRING).description("timestamp, when error happened"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("Message, which describes the error")
    );
    private final FieldDescriptor mailingTypeField = fieldWithPath("type").description("The type of the letter").attributes(
            key("constraints").value("Must be one of: LETTER, PACKAGE, WRAPPER, POSTCARD")
    );

    @Test
    @DisplayName("create mailing when everything is ok")
    public void testCreateMailing() throws Exception {
        MailingDTO mailingDTO = new MailingDTO(1L, Mailing.Type.LETTER, new ReceiverDTO(324456L, "Unknown", "Somewhere"));
        mockMvc.perform(
                post("/api/v1/mailings").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailingDTO))
        ).andExpect(status().isCreated()).andDo(
                document("Successful create new mailing",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Identifier of mailing"),
                                mailingTypeField,
                                fieldWithPath("receiver.index").type(JsonFieldType.NUMBER).description("The postal index of receiver"),
                                fieldWithPath("receiver.name").type(JsonFieldType.STRING).description("The name of the receiver"),
                                fieldWithPath("receiver.address").type(JsonFieldType.STRING).description("The address of the receiver")
                        )
                )
        );
        Mockito.verify(mailingService, Mockito.times(1)).create(mailingDTO);
    }

    @Test
    @DisplayName("getting info when mailing exist")
    public void testGettingInfoAboutMailing() throws Exception {
        MailingInfoDTO mailingInfoDTO = new MailingInfoDTO(1L, Mailing.Type.LETTER, Mailing.Status.ON_THE_WAY,
                List.of(
                        new MovementDTO(1L, 1L, ZonedDateTime.now().toInstant(), ZonedDateTime.now().plusHours(1).toInstant()),
                        new MovementDTO(2L, 1L, ZonedDateTime.now().plusHours(2).toInstant(), ZonedDateTime.now().plusHours(3).toInstant()),
                        new MovementDTO(3L, 1L, ZonedDateTime.now().plusHours(4).toInstant(), null)
                )

        );
        Mockito.when(mailingService.getInfo(1L)).thenReturn(mailingInfoDTO);
        mockMvc.perform(get("/api/v1/mailings/{id}", mailingInfoDTO.mailingId()))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document(
                                "Getting info about mailing",
                                responseFields(
                                        fieldWithPath("mailingId").type(JsonFieldType.NUMBER).description("The identifier of the mailing of this movement"),
                                        mailingTypeField,
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("The status of the mailing").attributes(
                                                key("constraints").value("Must be one of: ON_THE_WAY, IN_THE_DESTINATION, DELIVERED")
                                        ),
                                        fieldWithPath("movements").type(JsonFieldType.ARRAY).description("The movements of the mailing"),
                                        fieldWithPath("movements[].movementId").type(JsonFieldType.NUMBER).description("The id of the movement"),
                                        fieldWithPath("movements[].mailingId").type(JsonFieldType.NUMBER).description("The id of the mailing"),
                                        fieldWithPath("movements[].arrivalDateTime").type(JsonFieldType.STRING).description("The arrival time of the mailing"),
                                        fieldWithPath("movements[].departureDateTime").type(JsonFieldType.STRING).optional().description("The departure time of the mailing")
                                ),
                                pathParameters(parameterWithName("id").description("The id of the mailing, which info will be got"))
                        )
                );
        Mockito.verify(mailingService, Mockito.times(1)).getInfo(mailingInfoDTO.mailingId());
    }

    @Test
    @DisplayName("getting info when mailing doesn't exist")
    public void testGettingInfoForNotExistingMailing() throws Exception {
        Long mailingId = 1L;
        Mockito.when(mailingService.getInfo(mailingId)).then((invocation) -> {
            throw new MailingNotFoundException("Mailing with id " + mailingId + " not found!", HttpStatus.NOT_FOUND);
        });
        mockMvc.perform(get("/api/v1/mailings/{id}", mailingId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isString())
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(document("Getting info for not existing mailing", errorSnippet));
        Mockito.verify(mailingService, Mockito.only()).getInfo(mailingId);
    }

    @Test
    @DisplayName("create mailing when mailing with this id exists")
    public void testCreateMailingWhenMailingWithThisIdExists() throws Exception {
        MailingDTO mailingDTO = new MailingDTO(1L, Mailing.Type.LETTER, new ReceiverDTO(324456L, "Unknown", "Somewhere"));
        Mockito.doAnswer((invocationOnMock -> {
            throw new MailingAlreadyExistsException("Mailing with id " + mailingDTO.id() + " already exists!", HttpStatus.BAD_REQUEST);
        })).when(mailingService).create(mailingDTO);
        mockMvc.perform(post("/api/v1/mailings").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(mailingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isString())
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(document("create new mailing if it already exists", errorSnippet));
        Mockito.verify(mailingService, Mockito.times(1)).create(mailingDTO);
    }

    @Test
    @DisplayName("updating delivered status everything is ok")
    public void updateDeliveredStatus() throws Exception {
        Long mailingId = 1L;
        mockMvc.perform(patch("/api/v1/mailings/{id}/delivered", mailingId))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "update delivered status when ok",
                                pathParameters(parameterWithName("id").description("The id of the mailing, which status will be set to delivered"))
                        )
                );
        Mockito.verify(mailingService, Mockito.only()).setDeliveredStatus(mailingId);
    }

}
