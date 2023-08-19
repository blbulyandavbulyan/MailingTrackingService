package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.services.MailingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    private final FieldDescriptor mailingTypeField = fieldWithPath("type").description("The type of the letter").attributes(
            key("constraints").value("Must be one of: LETTER, PACKAGE, WRAPPER, POSTCARD")
    );

    @Test
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
                                pathParameters(
                                        parameterWithName("id").description("The id of the mailing, which info will be got")
                                )
                        )
                );
        Mockito.verify(mailingService, Mockito.times(1)).getInfo(mailingInfoDTO.mailingId());
    }

}
