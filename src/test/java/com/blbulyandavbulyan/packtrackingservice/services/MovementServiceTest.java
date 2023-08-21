package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyDeliveredException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.MovementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    public void createMovementWhenEverythingOk(){
        Long mailingId = 1L;
        Mailing mailing = new Mailing();
        mailing.setStatus(Mailing.Status.DELIVERED);
        Mockito.when(mailingService.getById(mailingId)).thenReturn(mailing);
        var actualException = assertThrows(MailingAlreadyDeliveredException.class, ()->movementService.create(mailingId, 1242L));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(mailingService, Mockito.only()).getById(mailingId);
    }
    @Test
    @DisplayName("create movement when mailing does not exist")
    public void createMovementWhenMailingDoesNotExist(){
        Long mailingId = 1L;
        Mockito.when(mailingService.getById(mailingId)).then((invocationOnMock -> {
            throw new MailingNotFoundException("Mailing with id + " + mailingId + " not found!", HttpStatus.BAD_REQUEST);
        }));
        Mockito.verify(mailingService, Mockito.only()).getById(mailingId);
    }
}
