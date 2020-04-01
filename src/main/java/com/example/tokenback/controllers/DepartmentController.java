package com.example.tokenback.controllers;

import com.example.tokenback.models.Department;
import com.example.tokenback.models.Display;
import com.example.tokenback.models.Token;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.DisplayRepository;
import com.example.tokenback.repository.TokenRepository;
import com.example.tokenback.schema.CustomDepartment;
import com.example.tokenback.schema.ResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DisplayRepository displayRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Get All Departments
    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Create a new Department
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createDepartment(@Valid @RequestBody CustomDepartment departmentDetails) {

        // check the department name and letter should be different
        if (departmentRepository.existsByName(departmentDetails.getName())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "400");
            map.put("message","Error: Department Name is already in exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        if (departmentRepository.existsByLetter(departmentDetails.getLetter())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "400");
            map.put("message","Error: Department Letter is already in exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        Department department = new Department(departmentDetails.getName(),
                departmentDetails.getLetter(), departmentDetails.getStart_number(),
                departmentDetails.getColor());

        return ResponseEntity.ok(departmentRepository.save(department));
    }

    // Get a Single Department
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable(value = "id") Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));
    }

    // Update a Department
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Department updateDepartment(@PathVariable(value = "id") Long departmentId,
                                       @Valid @RequestBody CustomDepartment departmentDetails) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));

        department.setName(departmentDetails.getName() != null ? departmentDetails.getName() : department.getName());
        department.setLetter(departmentDetails.getLetter() != null ? departmentDetails.getLetter() : department.getLetter());
        department.setStart_number(departmentDetails.getStart_number() != null ? departmentDetails.getStart_number() : department.getStart_number());
        department.setColor(departmentDetails.getColor() != null ? departmentDetails.getColor() : department.getColor());

        return departmentRepository.save(department);
    }

    @PutMapping("/reset/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Department resetDepartment(@PathVariable(value = "id") Long departmentId, @Valid @RequestBody ResetToken resetToken) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));

        department.setStart_number(1);

        Department resetDepartment = departmentRepository.save(department);

        tokenRepository.saveAll(resetToken.getTokens());

        this.simpMessagingTemplate.convertAndSend("/topics/tokens/reset", resetDepartment);


        return resetDepartment;
    }

    // Delete a Department
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Department deleteDepartment(@PathVariable(value = "id") Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));

        // first check
        if (department.getStart_number() != 1)
            throw new RuntimeException("Error: Reset the department first.");

        // get all the tokens which contains the department
        // token.setDepartment = null
        // save the tokens
        // get all the display which contains this department
        // display.departments remove the department
        // save the display
        List<Token> tokens = tokenRepository.findByDepartment(department);
        for (Token token : tokens) {
            token.setDepartment(null);
        }

        List<Display> displays = displayRepository.findAll();

        for (Display display : displays) {
            display.setDepartments(display.getDepartments().stream().filter(department1 -> !department1.getId().equals(department.getId())).collect(Collectors.toSet()));
        }

        departmentRepository.delete(department);

        return department;
    }

}
