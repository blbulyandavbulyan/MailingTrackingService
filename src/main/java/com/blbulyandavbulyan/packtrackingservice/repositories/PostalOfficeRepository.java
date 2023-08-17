package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostalOfficeRepository extends JpaRepository<PostalOffice, Long> {
}