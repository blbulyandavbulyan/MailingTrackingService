package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovementRepositoryTest {
    @Autowired
    MailingRepository mailingRepository;
    @Autowired
    PostalOfficeRepository postalOfficeRepository;
    @Autowired
    private MovementRepository movementRepository;
    private Mailing mailing;

    @BeforeAll
    public void init() {
        var postalOffice = new PostalOffice();
        postalOffice.setIndex(213324L);
        postalOffice.setTitle("Городская почта");
        postalOffice.setAddress("улица Пушкина");
        postalOfficeRepository.save(postalOffice);
        mailing = new Mailing();
        mailing.setMailingId(1L);
        mailing.setStatus(Mailing.Status.ON_THE_WAY);
        mailing.setType(Mailing.Type.LETTER);
        Receiver receiver = new Receiver();
        receiver.setAddress("test address");
        receiver.setName("test name");
        receiver.setPostalOffice(postalOffice);
        mailing.setReceiver(receiver);
        mailingRepository.save(mailing);
    }

    @AfterEach
    public void clear() {
        movementRepository.deleteAll();
    }

    @Test
    @DisplayName("update departure date time when it is not null")
    public void updateDepartureDateTimeWhenItIsNotNull() {
        MailingMovement beforeUpdate = new MailingMovement();
        beforeUpdate.setMailing(mailing);
        beforeUpdate.setArrivalDateTime(Instant.now());
        beforeUpdate.setDepartureDateTime(Instant.now().plus(1, ChronoUnit.HOURS));
        movementRepository.save(beforeUpdate);
        Instant newDepartueTime = Instant.now();
        movementRepository.updateDepartureDateTimeForLastMovement(beforeUpdate.getMailing().getMailingId(), newDepartueTime);
        MailingMovement afterUpdate = movementRepository.findById(beforeUpdate.getMovementId()).get();
        assertNotEquals(newDepartueTime.truncatedTo(ChronoUnit.MILLIS), beforeUpdate.getDepartureDateTime().truncatedTo(ChronoUnit.MILLIS));
        assertEquals(beforeUpdate.getArrivalDateTime().truncatedTo(ChronoUnit.MILLIS), afterUpdate.getArrivalDateTime().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @DisplayName("update departure date time when it is null")
    public void updateDepartureDateTimeWhenItIsNull() {
        MailingMovement beforeUpdate = new MailingMovement();
        beforeUpdate.setMailing(mailing);
        beforeUpdate.setArrivalDateTime(Instant.now());
        movementRepository.saveAndFlush(beforeUpdate);
        Instant expectedDepartureTime = Instant.now();
        movementRepository.updateDepartureDateTimeForLastMovement(mailing.getMailingId(), expectedDepartureTime);
        MailingMovement afterUpdate = movementRepository.findById(beforeUpdate.getMovementId()).get();
        assertEquals(beforeUpdate.getArrivalDateTime().truncatedTo(ChronoUnit.MILLIS), afterUpdate.getArrivalDateTime().truncatedTo(ChronoUnit.MILLIS));
        assertEquals(expectedDepartureTime.truncatedTo(ChronoUnit.MILLIS), afterUpdate.getDepartureDateTime().truncatedTo(ChronoUnit.MILLIS));
    }
}