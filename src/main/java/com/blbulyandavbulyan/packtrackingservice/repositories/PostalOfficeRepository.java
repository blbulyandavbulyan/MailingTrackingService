package com.blbulyandavbulyan.packtrackingservice.repositories;

import com.blbulyandavbulyan.packtrackingservice.entities.PostalOffice;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PostalOfficeRepository extends JpaRepository<PostalOffice, Long> {
    @Override
    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PostalOffice> findById(@NonNull Long id);
}