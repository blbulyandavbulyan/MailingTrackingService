package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public interface MovementRepository extends JpaRepository<MailingMovement, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} m SET m.departureDateTime = :departureDateTime WHERE m.arrivalDateTime = " +
            "(SELECT MAX(m2.arrivalDateTime) FROM #{#entityName} m2 WHERE m2.mailing.mailingId = :mailingId)")
    void updateDepartureDateTimeForLastMovement(@Param("mailingId") Long mailingId, @Param("departureDateTime") Instant departureDateTime);
}
