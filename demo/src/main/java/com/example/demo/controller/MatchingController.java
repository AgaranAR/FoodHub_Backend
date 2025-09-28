package com.example.demo.controller;

import com.example.demo.service.MatchingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    // Admin/manual allocation
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/allocate/{donationId}/{requestId}")
    public String allocateManually(@PathVariable Long donationId, @PathVariable Long requestId) {
        return matchingService.allocateDonation(donationId, requestId);
    }

    // Auto allocate (admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/allocate/auto")
    public List<String> autoAllocate() {
        return matchingService.autoAllocate();
    }
}
