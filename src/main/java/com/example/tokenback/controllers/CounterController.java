package com.example.tokenback.controllers;

import com.example.tokenback.models.Counter;
import com.example.tokenback.models.Token;
import com.example.tokenback.repository.CounterRepository;
import com.example.tokenback.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/counters")
public class CounterController {

    @Autowired
    CounterRepository counterRepository;

    @Autowired
    TokenRepository tokenRepository;

    // Get All Counters
    @GetMapping("/")
    public List<Counter> getAllCounters() {
        return counterRepository.findAll();
    }

    // Create a new Counter
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCounter(@Valid @RequestBody Counter counterDetails) {
        // check the counter name and letter already exist in the database
        if (counterRepository.existsByName(counterDetails.getName())){
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "400");
            map.put("message","Error: Counter Name is already in exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        if (counterRepository.existsByLetter(counterDetails.getLetter())){
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "400");
            map.put("message","Error: Counter Letter is already in exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        Counter counter = new Counter(counterDetails.getName(), counterDetails.getLetter());
        return ResponseEntity.ok(counterRepository.save(counter));

    }

    // Get a Single Counter
    @GetMapping("/{id}")
    public Counter getCounterById(@PathVariable(value = "id") Long counterId) {
        return counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));
    }

    // Update a Counter
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Counter updateCounter(@PathVariable(value = "id") Long counterId,
                                 @Valid @RequestBody Counter counterDetails) {

        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));

        counter.setName(counterDetails.getName() != null ? counterDetails.getName() : counter.getName());
        counter.setLetter(counterDetails.getLetter() != null ? counterDetails.getLetter() : counter.getLetter());

        return counterRepository.save(counter);
    }

    // Delete a Counter
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Counter deleteCounter(@PathVariable(value = "id") Long counterId) {

        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Error: Counter not found."));

        // first get all the tokens by the counter
        // set the token.counter = null
        // now delete the counter
        List<Token> tokens = tokenRepository.findByCounter(counter);
        for (Token token:tokens){
            token.setCounter(null);
        }

        counterRepository.delete(counter);

        return counter;
    }

}
