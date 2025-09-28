package com.example.demo.service;

import com.example.demo.entity.Donation;
import com.example.demo.entity.Request;
import com.example.demo.entity.Status;
import com.example.demo.repository.DonationRepo;
import com.example.demo.repository.RequestRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchingService {

    private final DonationRepo donationRepo;
    private final RequestRepo requestRepository;

    public MatchingService(DonationRepo donationRepo, RequestRepo requestRepository) {
        this.donationRepo = donationRepo;
        this.requestRepository = requestRepository;
    }

    // Manual allocation - transactional to avoid partial updates
    @Transactional
    public String allocateDonation(Long donationId, Long requestId) {
        Donation donation = donationRepo.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (donation.getStatus() == Status.EXPIRED || donation.getExpDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Donation expired");
        }

        if (!donation.getFoodItem().equalsIgnoreCase(request.getFoodItem())) {
            throw new RuntimeException("Food item mismatch");
        }
        if (donation.getQuantity() < request.getQuantityNeeded()) {
            throw new RuntimeException("Not enough quantity in donation");
        }

        // reduce donation quantity
        donation.setQuantity(donation.getQuantity() - request.getQuantityNeeded());
        if (donation.getQuantity() == 0) {
            donation.setStatus(Status.DISTRIBUTED);
        } else {
            donation.setStatus(Status.AVAILABLE); // partially used, still available
        }
        donationRepo.save(donation);

        request.setStatus(Status.FULFILLED);
        requestRepository.save(request);

        return "Allocated donation " + donationId + " -> request " + requestId;
    }

    // Basic auto allocator: iterate requests, match to first suitable donation
    @Transactional
    public List<String> autoAllocate() {
        List<String> results = new ArrayList<>();
        List<Request> pendingRequests = requestRepository.findAll().stream()
                .filter(r -> r.getStatus() == Status.PENDING).toList();

        List<Donation> donations = donationRepo.findAll().stream()
                .filter(d -> d.getStatus() == Status.AVAILABLE && !d.getExpDate().isBefore(LocalDate.now()))
                .toList();

        for (Request req : pendingRequests) {
            for (Donation don : donations) {
                if (don.getFoodItem().equalsIgnoreCase(req.getFoodItem()) && don.getQuantity() >= req.getQuantityNeeded()) {
                    results.add(allocateDonation(don.getId(), req.getId()));
                    break; // move to next request
                }
            }
        }
        return results;
    }
}
