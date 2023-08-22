package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
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
import java.time.temporal.ChronoUnit;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
    public void successfulCreatingMovement() throws Exception {
        Long mailingId = 1L;
        Long officeIndex = 12345L;
        MailingMovement mailingMovement = new MailingMovement();
        mailingMovement.setMovementId(1L);
        Mailing mailing = new Mailing();
        mailing.setType(Mailing.Type.LETTER);
        mailing.setMailingId(mailingId);
        PostalOffice postalOffice = new PostalOffice();
        postalOffice.setIndex(officeIndex);
        mailingMovement.setMailing(mailing);
        mailingMovement.setPostalOffice(postalOffice);
        mailingMovement.setArrivalDateTime(Instant.now());
        Mockito.when(movementService.create(mailingId, officeIndex)).thenReturn(mailingMovement);
        mockMvc.perform(post("/api/v1/movements")
                        .param("mailing_id", mailingId.toString())
                        .param("office_index", officeIndex.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("mailingId").value(mailingId))
                .andExpect(jsonPath("postalIndex").value(officeIndex))
                .andExpect(jsonPath("movementId").value(mailingMovement.getMovementId()))
                .andExpect(jsonPath("arrivalDateTime").value(mailingMovement.getArrivalDateTime().toString()))
                .andDo(
                        document(
                                "{class-name}/{method-name}",
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

    @Test
    @DisplayName("close movement test")
    public void closeMovement() throws Exception {
        Long mailingId = 23423422342L;
        Long movementId = 232L;
        Long postalOfficeIndex = 21413123L;
        MailingMovement mailingMovement = new MailingMovement();
        PostalOffice postalOffice = new PostalOffice();
        postalOffice.setIndex(postalOfficeIndex);
        mailingMovement.setPostalOffice(postalOffice);
        mailingMovement.setMovementId(movementId);
        mailingMovement.setArrivalDateTime(Instant.now());
        mailingMovement.setDepartureDateTime(Instant.now().plus(2, ChronoUnit.HOURS));
        Mailing mailing = new Mailing();
        mailing.setMailingId(mailingId);
        mailingMovement.setMailing(mailing);
        Mockito.when(movementService.closeMovement(movementId)).thenReturn(mailingMovement);
        mockMvc.perform(patch("/api/v1/movements/{movementId}", movementId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("movementId").value(movementId))
                .andExpect(jsonPath("mailingId").value(mailingId))
                .andExpect(jsonPath("departureDateTime").value(mailingMovement.getDepartureDateTime().toString()))
                .andExpect(jsonPath("arrivalDateTime").value(mailingMovement.getArrivalDateTime().toString()))
                .andDo(document(
                                "{class-name}/{method-name}",
                                pathParameters(parameterWithName("movementId").description("The id of the closing movement")),
                                responseFields(
                                        fieldWithPath("movementId").description("The id of the closing movement"),
                                        fieldWithPath("mailingId").description("Mailing id which associated with this movement"),
                                        fieldWithPath("arrivalDateTime").description("The date and time of arrival in the postal office"),
                                        fieldWithPath("departureDateTime").description("The date and time of closing this arrival, and in this date and time the mailing leaved the postal office")
                                )
                        )
                );
        Mockito.verify(movementService, Mockito.only()).closeMovement(movementId);
    }
}
