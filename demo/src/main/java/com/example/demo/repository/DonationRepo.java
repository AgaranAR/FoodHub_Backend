package com.example.demo.repository;

import com.example.demo.entity.Donation;
import com.example.demo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DonationRepo extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorId(Long donorId);
    List<Donation> findByStatus(Status status);
    List<Donation> findByExpDateBefore(LocalDate date);
}
