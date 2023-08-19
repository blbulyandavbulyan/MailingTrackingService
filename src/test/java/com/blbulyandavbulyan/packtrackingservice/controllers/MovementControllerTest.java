package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MovementCreatedDTO;
import com.blbulyandavbulyan.packtrackingservice.services.MovementService;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.ZonedDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class MovementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovementService movementService;

    @Test
    @DisplayName("test create movement")
    public void testCreateMovement() throws Exception {
        Long mailingId = 1L;
        Long officeIndex = 12345L;
        MovementCreatedDTO movementCreatedDTO = new MovementCreatedDTO();
        movementCreatedDTO.setMovementId(1L);
        movementCreatedDTO.setMailingId(mailingId);
        movementCreatedDTO.setPostalIndex(officeIndex);
        movementCreatedDTO.setArrivalDateTime(Instant.now());
        Mockito.when(movementService.create(mailingId, officeIndex)).thenReturn(movementCreatedDTO);
        mockMvc.perform(post("/api/v1/movements")
                        .param("mailing_id", mailingId.toString())
                        .param("office_index", officeIndex.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("mailingId").value(mailingId))
                .andExpect(jsonPath("postalIndex").value(officeIndex))
                .andExpect(jsonPath("movementId").value(movementCreatedDTO.getMovementId()))
                .andExpect(jsonPath("arrivalDateTime").value(movementCreatedDTO.getArrivalDateTime().toString()))
                .andDo(
                        document(
                                "Create movement",
                                formParameters(
                                        parameterWithName("mailing_id").description("The id of the mailing"),
                                        parameterWithName("office_index").description("The index of the postal office, where this mailing is")
                                ),
                                responseFields(
                                        fieldWithPath("movementId").description("The id of created movement"),
                                        fieldWithPath("mailingId").description("Mailing id, given in the request"),
                                        fieldWithPath("postalIndex").description("The postal index, given in the request"),
                                        fieldWithPath("arrivalDateTime").description("Date and time of arrival")

                                )
                        )
                );

    }
}