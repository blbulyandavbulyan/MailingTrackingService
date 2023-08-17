package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MailingRepository extends JpaRepository<Mailing, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Mailing m SET m.status = :status WHERE m.mailingId = :mailingId")
    void updateStatusById(@Param("mailingId") Long mailingId, @Param("status") Mailing.Status status);
}
