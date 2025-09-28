package com.example.demo.service;

import com.example.demo.entity.Donation;
import com.example.demo.entity.Status;
import com.example.demo.repository.DonationRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final DonationRepo donationRepo;

    public InventoryService(DonationRepo donationRepo) {
        this.donationRepo = donationRepo;
    }

    // returns all donations but ensures expired ones are marked
    @Transactional
    public List<Donation> getAllInventory() {
        List<Donation> expired = donationRepo.findByExpDateBefore(LocalDate.now());
        expired.forEach(d -> d.setStatus(Status.EXPIRED));
        donationRepo.saveAll(expired);
        return donationRepo.findAll();
    }

    public List<Donation> getAvailableItems() {
        // make sure we update expired before returning available
        List<Donation> expired = donationRepo.findByExpDateBefore(LocalDate.now());
        expired.forEach(d -> d.setStatus(Status.EXPIRED));
        donationRepo.saveAll(expired);

        return donationRepo.findByStatus(Status.AVAILABLE);
    }

    public List<Donation> getExpiredItems() {
        return donationRepo.findByStatus(Status.EXPIRED);
    }

    // distribute (whole donation) - admin/donor
    @Transactional
    public Optional<Donation> distributeItem(Long id) {
        Optional<Donation> opt = donationRepo.findById(id);
        opt.ifPresent(d -> {
            d.setStatus(Status.DISTRIBUTED);
            donationRepo.save(d);
        });
        return opt;
    }

    // reduce quantity by amount; if zero mark DISTRIBUTED
    @Transactional
    public Optional<Donation> reserveQuantity(Long id, int amount) {
        Optional<Donation> opt = donationRepo.findById(id);
        opt.ifPresent(d -> {
            if (d.getQuantity() < amount) {
                throw new IllegalArgumentException("Not enough quantity");
            }
            d.setQuantity(d.getQuantity() - amount);
            if (d.getQuantity() == 0) d.setStatus(Status.DISTRIBUTED);
            donationRepo.save(d);
        });
        return opt;
    }
}
