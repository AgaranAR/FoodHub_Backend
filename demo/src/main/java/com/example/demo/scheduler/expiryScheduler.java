package com.example.demo.scheduler;

import com.example.demo.entity.Donation;
import com.example.demo.entity.Status;
import com.example.demo.repository.DonationRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class expiryScheduler {

    private final DonationRepo donationRepo;

    public expiryScheduler(DonationRepo donationRepo) {
        this.donationRepo = donationRepo;
    }

    // runs every day at 02:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkExpiries() {
        LocalDate today = LocalDate.now();
        List<Donation> all = donationRepo.findAll();
        for (Donation d : all) {
            if (d.getExpDate() != null) {
                if (d.getExpDate().isBefore(today) && d.getStatus() != Status.EXPIRED) {
                    d.setStatus(Status.EXPIRED);
                } else if (!d.getExpDate().isBefore(today) && d.getExpDate().minusDays(3).isBefore(today)
                        && d.getStatus() == Status.AVAILABLE) {
                    // mark near expiry if within 3 days
                    d.setStatus(Status.NEAR_EXPIRY);
                }
            }
        }
        donationRepo.saveAll(all);
    }
}
