package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MovementCreatedDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.repositories.MovementRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class MovementService {
    private MovementRepository movementRepository;
    private MailingService mailingService;
    private PostalOfficeService postalOfficeService;
    private ModelMapper modelMapper;
    public MovementCreatedDTO create(Long mailingId, Long officeIndex) {
        Mailing mailing = mailingService.getById(mailingId);
        if(mailing.getStatus() != Mailing.Status.DELIVERED){
            Mailing.Status newStatus = officeIndex.equals(mailing.getReceiver().getPostalOffice().getIndex()) ? Mailing.Status.IN_THE_DESTINATION : Mailing.Status.ON_THE_WAY;
            mailing.setStatus(newStatus);//меняем статус на то что посылка ещё в пути, мало ли, может быть изменили пункт назначения
            MailingMovement mailingMovement = new MailingMovement();
            mailingMovement.setMailing(mailing);
            mailingMovement.setPostalOffice(postalOfficeService.getReferenceById(officeIndex));
            try{
                mailingMovement =  movementRepository.save(mailingMovement);
                return modelMapper.map(mailingMovement, MovementCreatedDTO.class);
            }
            catch (ConstraintViolationException constraintViolationException){
                throw new RuntimeException();//бросить исключение о том что не найден postalOffice
            }
        }
        else throw new RuntimeException();//бросить исключение, что посылка уже доставлена
    }
    @Transactional
    public MovementDTO closeMovement(Long movementId) {
        MailingMovement mailingMovement = movementRepository.findById(movementId).orElseThrow();
        if(mailingMovement.getDepartureDateTime() != null)//бросаем исключение о том что это перемещение уже закрыто
            throw new RuntimeException();
        else{
            mailingMovement.setDepartureDateTime(Instant.now());
            return modelMapper.map(mailingMovement, MovementDTO.class);
        }
    }

    public void closeLastMovement(Long mailingId) {
        movementRepository.updateDepartureDateTimeForLastMovement(mailingId, Instant.now());
    }
}
