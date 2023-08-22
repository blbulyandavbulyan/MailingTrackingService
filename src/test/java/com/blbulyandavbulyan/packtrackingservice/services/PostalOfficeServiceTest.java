package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.repositories.PostalOfficeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PostalOfficeServiceTest {
    @InjectMocks
    private PostalOfficeService underTest;
    @Mock
    private PostalOfficeRepository postalOfficeRepository;
    @Test
    @DisplayName("exist by id when postal office exists")
    public void existByIdWhenExistsTest(){
        Long mailingId = 1L;
        Mockito.when(postalOfficeRepository.existsById(mailingId)).thenReturn(true);
        assertTrue(underTest.existByIndex(mailingId));
        Mockito.verify(postalOfficeRepository, Mockito.only()).existsById(mailingId);
    }
    @Test
    @DisplayName("exist by id when postal office doesn't exists")
    public void existByIdWhenDoesNotExistsTest(){
        Long mailingId = 1L;
        Mockito.when(postalOfficeRepository.existsById(mailingId)).thenReturn(false);
        assertFalse(underTest.existByIndex(mailingId));
        Mockito.verify(postalOfficeRepository, Mockito.only()).existsById(mailingId);
    }
}
