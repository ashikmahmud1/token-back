package com.example.tokenback.controllers;

import com.example.tokenback.models.*;
import com.example.tokenback.repository.*;
import com.example.tokenback.schema.CustomToken;
import com.example.tokenback.schema.DepartmentReport;
import com.example.tokenback.schema.UserReport;
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
    UserRepository userRepository;

    @Autowired
    CounterRepository counterRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //Get All Tokens Created By Today
    @GetMapping("/today")
    public List<Token> getAllTokensToday() {
        return tokenRepository.findByCreatedAtAndStatusOrStatus(new Date(), ETokenStatus.TOKEN_CREATED, ETokenStatus.TOKEN_CALLED);
    }

    // Get All Tokens
    @GetMapping("/")
    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    // Create a new Token
    @PostMapping("/")
    public Token createToken(@Valid @RequestBody CustomToken customToken) {

        // custom token has department_id, customer_id
//        System.out.println(customToken.toString());

        Customer customer = customerRepository.findById(customToken.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Error: Please assign customer to the token."));

        Department department = departmentRepository.findById(customToken.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Error: Please assign department to the token."));

        // token number should generate by department letter + start_number
        // after creating the token we should also increase the value of the start_number by one
        String token_number = department.getLetter() + " - " + department.getStart_number();
        Token token = new Token(token_number,
                department,
                customToken.getPriority(), customToken.getStatus(), customer);

        if (customToken.getUser_id() != null){
            // get the user by user_id
            User user = userRepository.findById(customToken.getUser_id())
                    .orElseThrow(() -> new RuntimeException("Error: User is not found."));
            token.setUser(user);
        }
        Token savedToken = tokenRepository.save(token);

        department.setStart_number(department.getStart_number() + 1);
        // update the department
        departmentRepository.save(department);
        // send the newly created token using the socket channel /topic/tokens/created
        this.simpMessagingTemplate.convertAndSend("/topics/tokens/created", savedToken);

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
        if (customToken.getUser_id() != null) {
            User user = userRepository.findById(customToken.getUser_id())
                    .orElseThrow(() -> new RuntimeException("Error: User not found."));
            token.setUser(user);
        }

        // check if there is counter_id in the customToken
        // get the counter by the counter_id and set token.setCounter(counter)
        if (customToken.getCounter_id() != null) {
            Counter counter = counterRepository.findById(customToken.getCounter_id())
                    .orElseThrow(() -> new RuntimeException("Error: Counter not found."));
            token.setCounter(counter);
        }

        token.setPriority(customToken.getPriority() != null ? customToken.getPriority() : token.getPriority());
        token.setToken_number(customToken.getToken_number() != null ? customToken.getToken_number() : token.getToken_number());
        token.setStatus(customToken.getStatus() != null ? customToken.getStatus() : token.getStatus());
        token.setType(customToken.getType() != null ? customToken.getType() : token.getType());
        token.setServing_start(customToken.getServing_start() != null ? customToken.getServing_start() : token.getServing_start());
        token.setServing_end(customToken.getServing_end() != null ? customToken.getServing_end() : token.getServing_end());

        // send the updated token using the socket channel /topic/tokens/updated
        Token updatedToken = tokenRepository.save(token);

        this.simpMessagingTemplate.convertAndSend("/topics/tokens/updated", updatedToken);

        return updatedToken;
    }

    // Delete a Token
    @DeleteMapping("/{id}")
    public Token deleteToken(@PathVariable(value = "id") Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));

        tokenRepository.delete(token);
        // send the deleted token using the socket channel /topic/tokens/deleted
        this.simpMessagingTemplate.convertAndSend("/topics/tokens/deleted", token);

        return token;
    }

    @PostMapping("/served-report")
    public List<DepartmentReport> departmentReport(@Valid @RequestBody CustomToken customToken) {
        return tokenRepository.findDepartmentReport(null, null, ETokenStatus.TOKEN_SERVED);
    }

    @GetMapping("/user-report")
    public UserReport userReport() {
        List<User> users = userRepository.findAll();
        List<Token> tokens = tokenRepository.findByStatus(ETokenStatus.TOKEN_SERVED);

        return new UserReport(users, tokens);
    }

}
