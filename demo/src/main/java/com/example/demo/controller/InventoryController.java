package com.example.demo.controller;

import com.example.demo.entity.Donation;
import com.example.demo.service.InventoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECIPIENT','DONOR')")
    @GetMapping
    public List<Donation> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECIPIENT','DONOR')")
    @GetMapping("/available")
    public List<Donation> getAvailable() {
        return inventoryService.getAvailableItems();
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECIPIENT','DONOR')")
    @GetMapping("/expired")
    public List<Donation> getExpired() {
        return inventoryService.getExpiredItems();
    }

    @PreAuthorize("hasAnyRole('ADMIN','DONOR')")
    @PatchMapping("/{id}/distribute")
    public Optional<Donation> distribute(@PathVariable Long id) {
        return inventoryService.distributeItem(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DONOR')")
    @PatchMapping("/{id}/reserve")
    public Optional<Donation> reserve(@PathVariable Long id, @RequestParam int amount) {
        return inventoryService.reserveQuantity(id, amount);
    }
}
