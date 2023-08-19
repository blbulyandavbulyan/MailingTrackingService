package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.MailingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MailingServiceTest {
    @InjectMocks
    private MailingService mailingService;
    @Mock
    private MailingRepository mailingRepository;
    @Mock
    private PostalOfficeService postalOfficeService;

    @Test
    @DisplayName("normal creating mailing(when everything is ok)")
    public void normalCreateMailing() {
        PostalOffice postalOffice = new PostalOffice();
        postalOffice.setIndex(121334L);
        postalOffice.setTitle("Городская почта");
        postalOffice.setAddress("Улица пушкина");
        Mockito.when(postalOfficeService.existByIndex(postalOffice.getIndex())).thenReturn(true);
        Mockito.when(postalOfficeService.getReferenceById(postalOffice.getIndex())).thenReturn(postalOffice);
        MailingDTO mailingDTO = new MailingDTO(1L, Mailing.Type.LETTER, new ReceiverDTO(postalOffice.getIndex(), "Евгений", "какой-то адрес"));
        mailingService.create(mailingDTO);
        ArgumentCaptor<Mailing> mailingArgumentCaptor = ArgumentCaptor.forClass(Mailing.class);
        Mockito.verify(mailingRepository).save(mailingArgumentCaptor.capture());
        Mailing mailing = mailingArgumentCaptor.getValue();
        assertEquals(Mailing.Status.ON_THE_WAY, mailing.getStatus(), "Mailing status is not \"ON_THE_WAY\"");
        assertEquals(mailingDTO.type(), mailing.getType(), "Mailing type doesn't match");
        assertNotNull(mailing.getReceiver(), "Receiver is null in mailing!");
        Receiver receiver = mailing.getReceiver();
        assertEquals(mailingDTO.receiver().name(), receiver.getName(), "Receivers names are not equal");
        assertEquals(mailingDTO.receiver().address(), receiver.getAddress(), "Addresses in mailing receivers are not equal!");
        assertEquals(postalOffice, receiver.getPostalOffice(), "Postal office isn't equal to expected in receiver");
    }
    @Test
    @DisplayName("create mailing when postal office doesn't exist")
    public void createMailingWhenPostalOfficeDoesNotExist(){
        Long postalOfficeId = 1L;
        MailingDTO mailingDTO = new MailingDTO(1L, Mailing.Type.LETTER, new ReceiverDTO(postalOfficeId, "Евгений", "какой-то адрес"));
        Mockito.when(postalOfficeService.existByIndex(postalOfficeId)).thenReturn(false);
        assertThrows(PostalOfficeNotFoundException.class, ()->mailingService.create(mailingDTO));
        Mockito.verify(mailingRepository, Mockito.never()).save(Mockito.any());
    }
}
