package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.ReceiverDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.exceptions.MailingNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.MailingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        else throw new PostalOfficeNotFoundException("Нет почтового офиса с index " + receiverDTO.index());
    }

    public MailingInfoDTO getInfo(Long mailingId) {
        // TODO: 17.08.2023 Реализовать метод получения информации об отправлении
        throw new UnsupportedOperationException();
    }

    public void setDeliveredStatus(Long mailingId) {
        if(mailingRepository.existsById(mailingId)){
            mailingRepository.updateStatusById(mailingId, Mailing.Status.DELIVERED);
        }
        else throw new MailingNotFoundException("Отправление с id " + mailingId + " не найдено!");
    }

    public Mailing getById(Long mailingId) {
        return mailingRepository.findById(mailingId).orElseThrow();
    }

    public void save(Mailing mailing) {
        mailingRepository.save(mailing);
    }
}
