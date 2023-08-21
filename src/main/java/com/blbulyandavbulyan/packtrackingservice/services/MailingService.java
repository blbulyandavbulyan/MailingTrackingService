package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingAlreadyDeliveredException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.MailingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MailingService {
    private MailingRepository mailingRepository;
    private PostalOfficeService postalOfficeService;
    public void create(MailingDTO mailingDTO) {
        Mailing mailing = new Mailing();
        mailing.setMailingId(mailing.getMailingId());
        mailing.setType(mailingDTO.type());
        Receiver receiver = new Receiver();
        mailing.setReceiver(receiver);
        ReceiverDTO receiverDTO = mailingDTO.receiver();
        receiver.setAddress(receiverDTO.address());
        receiver.setName(receiverDTO.name());
        if(postalOfficeService.existByIndex(receiverDTO.index())){
            receiver.setPostalOffice(postalOfficeService.getReferenceById(receiverDTO.index()));
            mailingRepository.save(mailing);
        }
        else throw new PostalOfficeNotFoundException("Нет почтового офиса с индексом " + receiverDTO.index(), HttpStatus.BAD_REQUEST);
    }

    public MailingInfoDTO getInfo(Long mailingId) {
        Mailing mailing = mailingRepository.findById(mailingId).orElseThrow(()-> new MailingNotFoundException("Отправление с id " + mailingId + " не найдено!", HttpStatus.NOT_FOUND));
        return new MailingInfoDTO(
                mailing.getMailingId(), mailing.getType(), mailing.getStatus(),
                mailing.getMailingMovements().stream().map(
                        mailingMovement -> new MovementDTO(
                                mailingMovement.getMovementId(), mailingMovement.getMailing().getMailingId(),
                                mailingMovement.getArrivalDateTime(), mailingMovement.getDepartureDateTime()
                        )
                ).toList()
        );
    }
    @Transactional
    public void setDeliveredStatus(Long mailingId) {
        Mailing.Status oldStatus = mailingRepository.getMailingStatus(mailingId)
                .orElseThrow(()-> new MailingNotFoundException("Отправление с id " + mailingId + " не найдено!", HttpStatus.BAD_REQUEST));
        if(oldStatus != Mailing.Status.DELIVERED)
            mailingRepository.updateStatusById(mailingId, Mailing.Status.DELIVERED);
        else throw new MailingAlreadyDeliveredException("Отправление с id " + mailingId + " уже было доставлено!");
    }

    public Mailing getById(Long mailingId) {
        // TODO: 19.08.2023 Возможно понадобится возвращать отсюда optional, чтобы те кто используют сервис сами решали что делать с пустым optional
        return mailingRepository.findById(mailingId).orElseThrow(() -> new MailingNotFoundException("Отправление с id " + mailingId + " не найдено!", HttpStatus.BAD_REQUEST));
    }

    public void save(Mailing mailing) {
        mailingRepository.save(mailing);
    }
}
