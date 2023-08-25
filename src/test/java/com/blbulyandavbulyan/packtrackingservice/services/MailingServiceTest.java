package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyDeliveredException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyExistsException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
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
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        assertEquals(mailingDTO.id(), mailing.getMailingId(), "id are not equal");
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
        Mockito.verify(mailingRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("create mailing when it's already exist")
    public void createMailingWhenExists(){
        Long postalOfficeId = 1L;
        MailingDTO mailingDTO = new MailingDTO(1L, Mailing.Type.LETTER, new ReceiverDTO(postalOfficeId, "Евгений", "какой-то адрес"));
        Mockito.when(mailingRepository.existsById(mailingDTO.id())).thenReturn(true);
        var actualException = assertThrows(MailingAlreadyExistsException.class, ()-> mailingService.create(mailingDTO));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(mailingRepository, Mockito.only()).existsById(mailingDTO.id());
        Mockito.verify(mailingRepository, Mockito.never()).save(any());
    }
    @Test
    @DisplayName("get by id when mailing exists")
    public void getByIdForExistingMailing(){
        Long mailingId = 1L;
        Mailing expected = new Mailing();
        Mockito.when(mailingRepository.findById(mailingId)).thenReturn(Optional.of(expected));
        Mailing actual = mailingService.getById(mailingId);
        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("get by id when mailing doesn't exist")
    public void getByIdWhenMailingNotExist(){
        Long mailingId = 1L;
        Mockito.when(mailingRepository.findById(mailingId)).thenReturn(Optional.empty());
        var actualException = assertThrows(MailingNotFoundException.class, ()->mailingService.getById(mailingId));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
    }
    @Test
    @DisplayName("get info when mailing exists")
    public void testGetInfoWhenMailingExists(){
        Mailing mailing = new Mailing();
        mailing.setMailingId(1L);
        mailing.setType(Mailing.Type.LETTER);
        mailing.setStatus(Mailing.Status.ON_THE_WAY);
        List<MailingMovement> movements = new ArrayList<>();
        mailing.setMailingMovements(movements);
        {
            MailingMovement mailingMovement = new MailingMovement();
            mailingMovement.setMailing(mailing);
            mailingMovement.setMovementId(1L);
            mailingMovement.setPostalOffice(new PostalOffice());
            mailingMovement.setArrivalDateTime(ZonedDateTime.now().toInstant());
            mailingMovement.setDepartureDateTime(ZonedDateTime.now().plusHours(1).toInstant());
            movements.add(mailingMovement);
        }
        {
            MailingMovement mailingMovement = new MailingMovement();
            mailingMovement.setMailing(mailing);
            mailingMovement.setMovementId(2L);
            mailingMovement.setPostalOffice(new PostalOffice());
            mailingMovement.setArrivalDateTime(ZonedDateTime.now().plusHours(2).toInstant());
            mailingMovement.setDepartureDateTime(ZonedDateTime.now().plusHours(3).toInstant());
            movements.add(mailingMovement);
        }
        {
            MailingMovement mailingMovement = new MailingMovement();
            mailingMovement.setMailing(mailing);
            mailingMovement.setMovementId(2L);
            mailingMovement.setPostalOffice(new PostalOffice());
            mailingMovement.setArrivalDateTime(ZonedDateTime.now().plusHours(4).toInstant());
            movements.add(mailingMovement);
        }
        {
            Receiver receiver = new Receiver();
            receiver.setPostalOffice(new PostalOffice());
            receiver.setName("Андрей");
            receiver.setAddress("какой-то адрес");
            mailing.setReceiver(receiver);
        }
        Mockito.when(mailingRepository.findById(mailing.getMailingId())).thenReturn(Optional.of(mailing));
        MailingInfoDTO mailingInfo = mailingService.getInfo(mailing.getMailingId());
        assertNotNull(mailingInfo);
        assertEquals(mailing.getMailingId(), mailingInfo.mailingId(), "id not equals");
        assertEquals(mailing.getType(), mailingInfo.type(), "type not equals");
        assertEquals(mailing.getStatus(), mailingInfo.status(), "status not equals");
        assertNotNull(mailingInfo.movements());
        Set<MovementDTO> expectedMovements = movements.stream().map(mailingMovement -> new MovementDTO(mailingMovement.getMovementId(), mailingMovement.getMailing().getMailingId(), mailingMovement.getArrivalDateTime(), mailingMovement.getDepartureDateTime())).collect(Collectors.toSet());
        assertTrue(expectedMovements.containsAll(mailingInfo.movements()));
        assertEquals(movements.size(), mailingInfo.movements().size());
    }
    @Test
    @DisplayName("set delivered status when ok")
    public void setDeliveredStatusWhenOk(){
        Long mailingId = 1L;
        Mockito.when(mailingRepository.getMailingStatus(mailingId)).thenReturn(Optional.of(Mailing.Status.ON_THE_WAY));
        assertDoesNotThrow(()->mailingService.setDeliveredStatus(mailingId));
        Mockito.verify(mailingRepository, Mockito.times(1)).getMailingStatus(mailingId);
        Mockito.verify(mailingRepository, Mockito.times(1)).updateStatusById(mailingId, Mailing.Status.DELIVERED);
    }
    @Test
    @DisplayName("set delivered status when mailing already delivered")
    public void setDeliveredStatusWhenMailingAlreadyDelivered(){
        Long mailingId = 1L;
        Mockito.when(mailingRepository.getMailingStatus(mailingId)).thenReturn(Optional.of(Mailing.Status.DELIVERED));
        var actualException = assertThrows(MailingAlreadyDeliveredException.class, ()->mailingService.setDeliveredStatus(mailingId));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus(), "Actual exception doesn't have bad request as its http status!");
        Mockito.verify(mailingRepository, Mockito.only()).getMailingStatus(mailingId);
        Mockito.verify(mailingRepository, Mockito.never()).updateStatusById(eq(mailingId), any(Mailing.Status.class));
    }
    @Test
    @DisplayName("set delivered status for not existing mailing")
    public void setDeliveredStatusWhenMailingDoesNotExist(){
        Long mailingId = 1L;
        Mockito.when(mailingRepository.getMailingStatus(mailingId)).thenReturn(Optional.empty());
        var actualException = assertThrows(MailingNotFoundException.class, ()->mailingService.setDeliveredStatus(mailingId));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus(), "Actual exception doesn't have bad request as its http status!");
        Mockito.verify(mailingRepository, Mockito.only()).getMailingStatus(mailingId);
        Mockito.verify(mailingRepository, Mockito.never()).updateStatusById(eq(mailingId), any(Mailing.Status.class));
    }
}
