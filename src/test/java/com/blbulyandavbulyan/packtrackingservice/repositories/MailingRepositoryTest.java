package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.Mailing;
import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.entities.Receiver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MailingRepositoryTest {
    @Autowired
    MailingRepository mailingRepository;
    @Autowired
    PostalOfficeRepository postalOfficeRepository;
    private Long mailingId;
    @AfterEach
    public void clearDB(){
        mailingRepository.deleteAll();
    }
    @BeforeAll
    public void init(){
        var postalOffice = new PostalOffice();
        postalOffice.setIndex(213324L);
        postalOffice.setTitle("Городская почта");
        postalOffice.setAddress("улица Пушкина");
        postalOfficeRepository.save(postalOffice);
        Mailing mailing = new Mailing();
        mailing.setMailingId(1L);
        mailing.setStatus(Mailing.Status.ON_THE_WAY);
        mailing.setType(Mailing.Type.LETTER);
        Receiver receiver = new Receiver();
        receiver.setAddress("test address");
        receiver.setName("test name");
        receiver.setPostalOffice(postalOffice);
        mailing.setReceiver(receiver);
        mailingRepository.save(mailing);
        mailingId = mailing.getMailingId();
    }
    @Test
    @DisplayName("updating mailing status")
    public void updatingMailingStatus(){
        mailingRepository.updateStatusById(mailingId, Mailing.Status.DELIVERED);
        Mailing mailing = mailingRepository.findById(mailingId).get();
        assertEquals(Mailing.Status.DELIVERED, mailing.getStatus());
    }
    @Test
    @DisplayName("getting mailing status when mailing exist")
    public void gettingMailingStatus(){
        Mailing mailing = mailingRepository.findById(mailingId).get();
        var optionalStatus = mailingRepository.getMailingStatus(mailingId);
        assertTrue(optionalStatus.isPresent());
        var mailingStatus = optionalStatus.get();
        assertEquals(mailing.getStatus(), mailingStatus);
    }
    @Test
    @DisplayName("getting mailing status when mailing doesn't exist")
    public void gettingMailingStatusForNotExistingMailing(){
        var optionalStatus = mailingRepository.getMailingStatus(-1L);
        assertTrue(optionalStatus.isEmpty());
    }
}