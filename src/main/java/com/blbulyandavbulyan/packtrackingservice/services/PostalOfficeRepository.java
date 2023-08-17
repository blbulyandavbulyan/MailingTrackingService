package com.blbulyandavbulyan.packtrackingservice.services;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostalOfficeRepository extends JpaRepository<PostalOffice, Long> {
}