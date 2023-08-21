package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MovementCreatedDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.repositories.MovementRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class MovementService {
    private MovementRepository movementRepository;
    private MailingService mailingService;
    private PostalOfficeService postalOfficeService;
    public MailingMovement create(Long mailingId, Long officeIndex) {
        Mailing mailing = mailingService.getById(mailingId);
        if(mailing.getStatus() != Mailing.Status.DELIVERED){
            Mailing.Status newStatus = officeIndex.equals(mailing.getReceiver().getPostalOffice().getIndex()) ? Mailing.Status.IN_THE_DESTINATION : Mailing.Status.ON_THE_WAY;
            mailing.setStatus(newStatus);//меняем статус на то что посылка ещё в пути, мало ли, может быть изменили пункт назначения
            MailingMovement mailingMovement = new MailingMovement();
            mailingMovement.setMailing(mailing);
            mailingMovement.setPostalOffice(postalOfficeService.getReferenceById(officeIndex));
            try{
                movementRepository.save(mailingMovement);
                return mailingMovement;
            }
            catch (ConstraintViolationException constraintViolationException){
                throw new RuntimeException();//бросить исключение о том что не найден postalOffice
            }
        }
        else throw new RuntimeException();//бросить исключение, что посылка уже доставлена
    }
    @Transactional
    public MailingMovement closeMovement(Long movementId) {
        MailingMovement mailingMovement = movementRepository.findById(movementId).orElseThrow();
        if(mailingMovement.getDepartureDateTime() != null)//бросаем исключение о том что это перемещение уже закрыто
            throw new RuntimeException();
        else{
            mailingMovement.setDepartureDateTime(Instant.now());
            movementRepository.save(mailingMovement);
            return mailingMovement;
        }
    }

    public void closeLastMovement(Long mailingId) {
        movementRepository.updateDepartureDateTimeForLastMovement(mailingId, Instant.now());
    }
}
