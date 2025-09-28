package com.example.demo.service;

import com.example.demo.entity.Request;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.repository.RequestRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepo requestRepository;
    private final UserRepo userRepo;

    public RequestService(RequestRepo requestRepository, UserRepo userRepo) {
        this.requestRepository = requestRepository;
        this.userRepo = userRepo;
    }

    // Validate recipient exists & role
    public Request createRequest(Request request) {
        if (request.getQuantityNeeded() <= 0) {
            throw new IllegalArgumentException("quantityNeeded must be > 0");
        }
        User recipient = userRepo.findById(request.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));
        // Optionally ensure role == RECIPIENT
        if (recipient.getRole() != com.example.demo.entity.Role.RECIPIENT) {
            throw new IllegalArgumentException("User is not a recipient");
        }
        request.setStatus(Status.PENDING);
        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<Request> getRequestsByRecipient(Long recipientId) {
        return requestRepository.findByRecipientId(recipientId);
    }

    @Transactional
    public Optional<Request> updateStatus(Long id, Status status) {
        Optional<Request> opt = requestRepository.findById(id);
        opt.ifPresent(r -> {
            r.setStatus(status);
            requestRepository.save(r);
        });
        return opt;
    }
}
