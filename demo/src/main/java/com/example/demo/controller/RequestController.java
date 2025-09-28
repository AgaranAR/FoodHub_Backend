package com.example.demo.controller;

import com.example.demo.entity.Request;
import com.example.demo.entity.Status;
import com.example.demo.service.RequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    // Recipient creates request (only users with RECIPIENT role allowed)
    @PreAuthorize("hasRole('RECIPIENT')")
    @PostMapping
    public Request createRequest(@RequestBody Request request) {
        return requestService.createRequest(request);
    }

    // Admin can view all requests
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECIPIENT')")
    @GetMapping("/recipient/{recipientId}")
    public List<Request> getRequestsByRecipient(@PathVariable Long recipientId) {
        return requestService.getRequestsByRecipient(recipientId);
    }

    // Admin updates status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public Optional<Request> updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return requestService.updateStatus(id, status);
    }
}
