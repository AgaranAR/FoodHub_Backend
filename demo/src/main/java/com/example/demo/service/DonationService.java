package com.example.demo.service;

import com.example.demo.entity.Donation;
import com.example.demo.repository.DonationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepo donationRepo;

    public Donation createDonation(Donation donation) {
        return donationRepo.save(donation);
    }

    public List<Donation> getAllDonations() {
        return donationRepo.findAll();
    }

    public List<Donation> getDonationsByDonor(Long donorId) {
        return donationRepo.findByDonorId(donorId);
    }

    public Optional<Donation> updateDonation(Long id, Donation updatedDonation) {
        return donationRepo.findById(id).map(donation -> {
            donation.setFoodItem(updatedDonation.getFoodItem());
            donation.setQuantity(updatedDonation.getQuantity());
            donation.setExpDate(updatedDonation.getExpDate());
            donation.setStatus(updatedDonation.getStatus());
            return donationRepo.save(donation);
        });
    }

    public void deleteDonation(Long id) {
        donationRepo.deleteById(id);
    }
}
