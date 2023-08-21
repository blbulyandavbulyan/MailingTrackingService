package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.exceptions.PostalOfficeNotFoundException;
import com.blbulyandavbulyan.packtrackingservice.repositories.PostalOfficeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostalOfficeService {
    private PostalOfficeRepository postalOfficeRepository;
    public PostalOffice getReferenceById(Long officeIndex) {
        return postalOfficeRepository.getReferenceById(officeIndex);
    }

    public boolean existByIndex(Long index) {
        return postalOfficeRepository.existsById(index);
    }

    public PostalOffice getById(Long postalOfficeIndex) {
        return postalOfficeRepository.findById(postalOfficeIndex).orElseThrow(()->new PostalOfficeNotFoundException("Postal office with index + " + postalOfficeIndex + " not found!", HttpStatus.BAD_REQUEST));
    }
}
