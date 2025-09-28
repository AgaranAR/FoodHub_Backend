package com.example.demo.controller;

import com.example.demo.entity.Donation;
import com.example.demo.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PostMapping
    public Donation createDonation(@RequestBody Donation donation) {
        return donationService.createDonation(donation);
    }

    @GetMapping
    public List<Donation> getAllDonations() {
        return donationService.getAllDonations();
    }

    @GetMapping("/donor/{donorId}")
    public List<Donation> getDonationsByDonor(@PathVariable Long donorId) {
        return donationService.getDonationsByDonor(donorId);
    }

    @PutMapping("/{id}")
    public Optional<Donation> updateDonation(@PathVariable Long id, @RequestBody Donation donation) {
        return donationService.updateDonation(id, donation);
    }

    @DeleteMapping("/{id}")
    public void deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
    }
}
