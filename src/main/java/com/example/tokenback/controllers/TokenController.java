package com.example.tokenback.controllers;

import com.example.tokenback.models.CustomToken;
import com.example.tokenback.models.Customer;
import com.example.tokenback.models.Department;
import com.example.tokenback.models.Token;
import com.example.tokenback.payload.response.MessageResponse;
import com.example.tokenback.repository.CustomerRepository;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Get All Tokens
    @GetMapping("/")
    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    // Create a new Token
    @PostMapping("/")
    public Token createToken(@Valid @RequestBody CustomToken customToken) {

        // custom token has department_id, customer_id
        System.out.println(customToken.toString());

        Customer customer = customerRepository.findById(customToken.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Error: Please assign customer to the token."));

        Department department = departmentRepository.findById(customToken.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Error: Please assign department to the token."));

        System.out.println();

        Token token = new Token(customToken.getToken_number(),
                customer,
                department,
                customToken.getPriority());

        // save the token
        Token savedToken = tokenRepository.save(token);
        // send the newly created token using the socket channel /topic/tokens/created
        this.simpMessagingTemplate.convertAndSend("/topic/tokens/created", savedToken);

        return savedToken;

    }

    // Get a Single Token
    @GetMapping("/{id}")
    public Token getTokenById(@PathVariable(value = "id") Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));
    }

    // Update a Token
    @PutMapping("/{id}")
    public Token updateToken(@PathVariable(value = "id") Long tokenId,
                             @Valid @RequestBody Token tokenDetails) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));

        token.setPriority(tokenDetails.getPriority());
        token.setToken_number(tokenDetails.getToken_number());
        token.setServing_start(tokenDetails.getServing_start());
        token.setServing_end(tokenDetails.getServing_end());

        // send the updated token using the socket channel /topic/tokens/updated

        return tokenRepository.save(token);
    }

    // Delete a Token
    @DeleteMapping("/{id}")
    public Token deleteToken(@PathVariable(value = "id") Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));

        // send the deleted token using the socket channel /topic/tokens/deleted
        tokenRepository.delete(token);

        return token;
    }

}
