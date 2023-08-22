package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.PostalOfficeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

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
    @Test
    @DisplayName("get by id when postal office exists")
    public void getByIdWhenExistsTest(){
        Long postalOfficeIndex = 1L;
        PostalOffice expected = new PostalOffice();
        expected.setIndex(postalOfficeIndex);
        Mockito.when(postalOfficeRepository.findById(postalOfficeIndex)).thenReturn(Optional.of(expected));
        PostalOffice actual = underTest.getById(postalOfficeIndex);
        assertSame(expected, actual);
        Mockito.verify(postalOfficeRepository, Mockito.only()).findById(postalOfficeIndex);
    }
    @Test
    @DisplayName("get by id when postal office doesn't exist")
    public void getByIdWhenDoesNotExist(){
        Long postalOfficeIndex = 1L;
        Mockito.when(postalOfficeRepository.findById(postalOfficeIndex)).thenReturn(Optional.empty());
        var actualException = assertThrows(PostalOfficeNotFoundException.class, ()->underTest.getById(postalOfficeIndex));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(postalOfficeRepository, Mockito.only()).findById(postalOfficeIndex);
    }
}
