package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MovementCreatedDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.entities.MailingMovement;
import com.blbulyandavbulyan.packtrackingservice.services.MovementService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movements")
@AllArgsConstructor
public class MovementController {
    private MovementService movementService;
    private ModelMapper modelMapper;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementCreatedDTO create(@RequestParam("mailing_id") Long mailingId, @RequestParam("office_index") Long officeIndex){
        MailingMovement mailingMovement = movementService.create(mailingId, officeIndex);
        return modelMapper.map(mailingMovement, MovementCreatedDTO.class);
    }
    @PatchMapping("/{movementId}")
    public MovementDTO closeMovement(@PathVariable("movementId") Long movementId){
        MailingMovement mailingMovement = movementService.closeMovement(movementId);
        return modelMapper.map(mailingMovement, MovementDTO.class);
    }
}
