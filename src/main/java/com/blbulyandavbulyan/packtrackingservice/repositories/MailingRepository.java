package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MailingRepository extends JpaRepository<Mailing, Long> {
    @Transactional
    @Modifying
    void updateStatusById(Long mailingId, Mailing.Status status);
}
