package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import com.blbulyandavbulyan.packtrackingservice.repositories.MailingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailingService {
    private MailingRepository mailingRepository;
    public void create(MailingDTO mailingDTO) {
        // TODO: 17.08.2023 Реализовать метод создания отправления
        throw new UnsupportedOperationException();
    }

    public MailingInfoDTO getInfo(Long mailingId) {
        // TODO: 17.08.2023 Реализовать метод получения информации об отправлении
        throw new UnsupportedOperationException();
    }

    public void setDeliveredStatus(Long mailingId) {
        if(mailingRepository.existsById(mailingId)){
            mailingRepository.updateStatusById(mailingId, Mailing.Status.DELIVERED);
        }
        else throw new RuntimeException();
    }

    public Mailing getById(Long mailingId) {
        return mailingRepository.findById(mailingId).orElseThrow();
    }

    public void save(Mailing mailing) {
        mailingRepository.save(mailing);
    }
}
