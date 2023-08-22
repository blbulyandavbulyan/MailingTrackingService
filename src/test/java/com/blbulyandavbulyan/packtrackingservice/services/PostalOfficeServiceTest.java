package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.repositories.PostalOfficeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostalOfficeServiceTest {
    @InjectMocks
    private PostalOfficeService underTest;
    @Mock
    private PostalOfficeRepository postalOfficeRepository;
    @Test
    @DisplayName("exist by id when postal office exists")
    public void existByIdWhenExistsTest(){
        Long postalOfficeIndex = 1L;
        Mockito.when(postalOfficeRepository.existsById(postalOfficeIndex)).thenReturn(true);
        assertTrue(underTest.existByIndex(postalOfficeIndex));
        Mockito.verify(postalOfficeRepository, Mockito.only()).existsById(postalOfficeIndex);
    }
    @Test
    @DisplayName("exist by id when postal office doesn't exists")
    public void existByIdWhenDoesNotExistsTest(){
        Long postalOfficeIndex = 1L;
        Mockito.when(postalOfficeRepository.existsById(postalOfficeIndex)).thenReturn(false);
        assertFalse(underTest.existByIndex(postalOfficeIndex));
        Mockito.verify(postalOfficeRepository, Mockito.only()).existsById(postalOfficeIndex);
    }
    @Test
    @DisplayName("get reference by id")
    public void getReferenceByIdTest(){
        Long postalOfficeIndex = 1L;
        PostalOffice expected = new PostalOffice();
        Mockito.when(postalOfficeRepository.getReferenceById(postalOfficeIndex)).thenReturn(expected);
        PostalOffice actual = underTest.getReferenceById(postalOfficeIndex);
        assertSame(expected, actual);
    }
}
