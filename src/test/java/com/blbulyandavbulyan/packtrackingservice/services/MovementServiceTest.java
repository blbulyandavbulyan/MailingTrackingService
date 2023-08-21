package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyDeliveredException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.MovementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
public class MovementServiceTest {
    @InjectMocks
    private MovementService movementService;
    @Mock
    private MovementRepository movementRepository;
    @Mock
    private PostalOfficeService postalOfficeService;
    @Mock
    private MailingService mailingService;
    @Test
    @DisplayName("create movement when mailing is already delivered")
    public void createMovementWhenMailingAlreadyDelivered(){
        Long mailingId = 1L;
        Mailing mailing = new Mailing();
        mailing.setStatus(Mailing.Status.DELIVERED);
        mailing.setMailingId(mailingId);
        Mockito.when(mailingService.getById(mailingId)).thenReturn(mailing);
        var actualException = assertThrows(MailingAlreadyDeliveredException.class, ()->movementService.create(mailingId, 1242L));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(mailingService, Mockito.only()).getById(mailingId);
        Mockito.verify(movementRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    @DisplayName("create movement when mailing does not exist")
    public void createMovementWhenMailingDoesNotExist(){
        Long mailingId = 1L;
        Mockito.when(mailingService.getById(mailingId)).then((invocationOnMock -> {
            throw new MailingNotFoundException("Mailing with id + " + mailingId + " not found!", HttpStatus.BAD_REQUEST);
        }));
        var actualException = assertThrows(MailingNotFoundException.class, ()->movementService.create(mailingId, 1243L));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(mailingService, Mockito.only()).getById(mailingId);
        Mockito.verify(movementRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    @DisplayName("create movement when postal office does not exists")
    public void createMovementWhenPostalOfficeDoesNotExist(){
        Long mailingId = 1L;
        Long postalOfficeIndex = 234234L;
        Mailing mailing = new Mailing();
        mailing.setMailingId(mailingId);
        mailing.setStatus(Mailing.Status.IN_THE_DESTINATION);
        PostalOffice postalOffice = new PostalOffice();
        postalOffice.setIndex(postalOfficeIndex);
        Receiver receiver = new Receiver();
        receiver.setPostalOffice(postalOffice);
        mailing.setReceiver(receiver);
        Mockito.when(postalOfficeService.getById(postalOfficeIndex)).then((invocationOnMock)->{
           throw new PostalOfficeNotFoundException("Postal office with index " +postalOfficeIndex +" not found!", HttpStatus.BAD_REQUEST);
        });
        Mockito.when(mailingService.getById(mailingId)).thenReturn(mailing);
        var actualException = assertThrows(PostalOfficeNotFoundException.class, ()-> movementService.create(mailingId, postalOfficeIndex));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(mailingService, Mockito.times(1)).getById(mailingId);
        Mockito.verify(postalOfficeService, Mockito.times(1)).getById(postalOfficeIndex);
        Mockito.verify(movementRepository, Mockito.never()).save(Mockito.any());
    }
}
