package com.example.demo.repository;

import com.example.demo.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {
    List<Request> findByRecipientId(Long recipientId);
}
