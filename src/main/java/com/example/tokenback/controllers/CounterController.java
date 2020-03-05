package com.example.tokenback.controllers;

import com.example.tokenback.models.Counter;
import com.example.tokenback.payload.response.MessageResponse;
import com.example.tokenback.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/counters")
public class CounterController {

    @Autowired
    CounterRepository counterRepository;

    // Get All Counters
    @GetMapping("/")
    public List<Counter> getAllCounters() {
        return counterRepository.findAll();
    }

    // Create a new Counter
    @PostMapping("/")
    public Counter createCounter(@Valid @RequestBody Counter counterDetails) {
        Counter counter = new Counter(counterDetails.getName(), counterDetails.getLetter());
        return counterRepository.save(counter);

    }

    // Get a Single Counter
    @GetMapping("/{id}")
    public Counter getCounterById(@PathVariable(value = "id") Long counterId) {
        return counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));
    }

    // Update a Counter
    @PutMapping("/{id}")
    public Counter updateCounter(@PathVariable(value = "id") Long counterId,
                                 @Valid @RequestBody Counter counterDetails) {

        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));

        counter.setName(counterDetails.getName());
        counter.setLetter(counterDetails.getLetter());

        return counterRepository.save(counter);
    }

    // Delete a Counter
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCounter(@PathVariable(value = "id") Long counterId) {
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));

        counterRepository.delete(counter);

        return ResponseEntity.ok(new MessageResponse("Counter deleted successfully!"));
    }

}
