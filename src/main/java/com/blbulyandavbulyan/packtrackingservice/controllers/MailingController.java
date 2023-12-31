package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MailingDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MailingInfoDTO;
import com.blbulyandavbulyan.packtrackingservice.services.MailingService;
import com.blbulyandavbulyan.packtrackingservice.services.MovementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mailings")
@AllArgsConstructor
public class MailingController {
    private MailingService mailingService;
    private MovementService movementService;
    @GetMapping("/{mailingId}")
    public MailingInfoDTO getInfo(@PathVariable("mailingId") Long mailingId){
        return mailingService.getInfo(mailingId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody MailingDTO mailingDTO){
        mailingService.create(mailingDTO);
    }
    @Transactional
    @PatchMapping("/{mailingId}/status/delivered")
    public void setDeliveredStatus(@PathVariable Long mailingId){
        mailingService.setDeliveredStatus(mailingId);
        movementService.closeLastMovement(mailingId);
    }
}
