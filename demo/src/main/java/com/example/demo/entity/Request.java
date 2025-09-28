package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId; // link to the user who requested

    private String foodItem;

    private int quantityNeeded;

    @Enumerated(EnumType.STRING)
    private Status status; // PENDING, APPROVED, REJECTED, FULFILLED
}
