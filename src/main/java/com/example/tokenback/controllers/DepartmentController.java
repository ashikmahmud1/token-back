package com.example.tokenback.controllers;

import com.example.tokenback.models.Department;
import com.example.tokenback.models.Token;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.DisplayRepository;
import com.example.tokenback.repository.TokenRepository;
import com.example.tokenback.schema.CustomDepartment;
import com.example.tokenback.schema.ResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    // Get All Departments
//    @GetMapping("/{display_id}/departments")
//    public List<Department> getAllDepartmentsByDisplay(@PathVariable Long display_id) {
//        return departmentRepository.findByDisplay_Id(display_id);
//    }

    // Create a new Department
    @PostMapping("/")
    public Department createDepartment(@Valid @RequestBody CustomDepartment departmentDetails) {

        Department department = new Department(departmentDetails.getName(),
                departmentDetails.getLetter(), departmentDetails.getStart_number(),
                departmentDetails.getColor());

        return departmentRepository.save(department);
    }

    // Get a Single Department
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable(value = "id") Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));
    }

    // Update a Department
    @PutMapping("/{id}")
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
    public Department resetDepartment(@PathVariable(value = "id") Long departmentId, @Valid @RequestBody ResetToken resetToken) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));

        department.setStart_number(1);

        Department resetDepartment = departmentRepository.save(department);

        tokenRepository.saveAll(resetToken.getTokens());

        for (Token token : resetToken.getTokens()) {
            System.out.println(token.toString());
        }
        this.simpMessagingTemplate.convertAndSend("/topics/tokens/reset", resetDepartment);


        return resetDepartment;
    }

    // Delete a Department
    @DeleteMapping("/{id}")
    public Department deleteDepartment(@PathVariable(value = "id") Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department not found."));

        departmentRepository.delete(department);

        return department;
    }

}
