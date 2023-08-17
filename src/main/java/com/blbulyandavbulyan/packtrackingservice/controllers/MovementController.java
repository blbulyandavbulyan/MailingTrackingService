package com.blbulyandavbulyan.packtrackingservice.controllers;

import com.blbulyandavbulyan.packtrackingservice.dtos.MovementCreatedDTO;
import com.blbulyandavbulyan.packtrackingservice.dtos.MovementDTO;
import com.blbulyandavbulyan.packtrackingservice.services.MovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movements")
@AllArgsConstructor
public class MovementController {
    private MovementService movementService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementCreatedDTO create(@RequestParam("mailingId") Long mailingId, @RequestParam("officeIndex") Long officeIndex){
        return movementService.create(mailingId, officeIndex);
    }
    @PatchMapping("/{movementId}")
    public MovementDTO closeMovement(@PathVariable("movementId") Long movementId){
        return movementService.closeMovement(movementId);
    }
}
