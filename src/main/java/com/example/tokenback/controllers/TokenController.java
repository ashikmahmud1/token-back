package com.example.tokenback.controllers;

import com.example.tokenback.models.CustomToken;
import com.example.tokenback.models.Customer;
import com.example.tokenback.models.Department;
import com.example.tokenback.models.Token;
import com.example.tokenback.repository.CustomerRepository;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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

    // Send the token that created by today.
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
                             @Valid @RequestBody CustomToken customToken) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));

        // check if there is user_id in the customToken
        // get the user by the user_id and set token.setUser(user)


        // check if there is counter_id in the customToken
        // get the counter by the counter_id and set token.setCounter(counter)

        token.setPriority(customToken.getPriority());
        token.setToken_number(customToken.getToken_number());
        token.setServing_start(customToken.getServing_start());
        token.setServing_end(customToken.getServing_end());

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

    @PostMapping("/department/report")
    public List<Object[]> departmentReport(@Valid @RequestBody CustomToken customToken) {
        return tokenRepository.findDepartmentReport(new Date(), new Date());
    }

}
