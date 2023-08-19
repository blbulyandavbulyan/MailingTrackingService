package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import com.blbulyandavbulyan.packtrackingservice.repositories.PostalOfficeRepository;
import lombok.AllArgsConstructor;
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
}
