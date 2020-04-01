package com.example.tokenback.controllers;

import com.example.tokenback.models.Department;
import com.example.tokenback.models.Display;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.DisplayRepository;
import com.example.tokenback.schema.CustomDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/displays")
public class DisplayController {

    @Autowired
    DisplayRepository displayRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    // Get All Displays
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_TOKENIST') or hasRole('ROLE_ADMIN') or hasRole('ROLE_STAFF')")
    public List<Display> getAllDisplays() {
        return displayRepository.findAll();
    }

    // Create a new Display
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_TOKENIST') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createDisplay(@Valid @RequestBody CustomDisplay displayDetails) {

        if (displayRepository.existsByName(displayDetails.getName())){
            HashMap<String, String> map = new HashMap<>();
            map.put("status", "400");
            map.put("message","Error: Display Name is already in exist!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
        Set<Long> departmentIds = displayDetails.getDepartments();
        Set<Department> departments = new HashSet<>();

        Display display = new Display(displayDetails.getName(), displayDetails.getFrom_queue(), displayDetails.getTo_queue());
        if (departmentIds != null) {
            departmentIds.forEach(dept_id -> {
                Department department = departmentRepository.findById(dept_id).
                        orElseThrow(() -> new RuntimeException("Error: Department is not found."));
                departments.add(department);
            });
            display.setDepartments(departments);
        }

        return ResponseEntity.ok(displayRepository.save(display));
    }

    @GetMapping("/number/{name}")
    public Display getDisplayByNumber(@PathVariable(value = "name") String name) {
        return displayRepository.findByName(name).
                orElseThrow(() -> new RuntimeException("Error: Display is not found."));
    }

    // Get a Single Display
    @GetMapping("/{id}")
    public Display getDisplayById(@PathVariable(value = "id") Long displayId) {
        return displayRepository.findById(displayId)
                .orElseThrow(() -> new RuntimeException("Error: Display not found."));
    }

    // Update a Display
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TOKENIST') or hasRole('ROLE_ADMIN')")
    public Display updateDisplay(@PathVariable(value = "id") Long displayId,
                                 @Valid @RequestBody CustomDisplay displayDetails) {

        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new RuntimeException("Error: Display not found."));
        Set<Long> departmentIds = displayDetails.getDepartments();
        Set<Department> departments = new HashSet<>();

        if (departmentIds != null) {
            departmentIds.forEach(dept_id -> {
                Department department = departmentRepository.findById(dept_id).
                        orElseThrow(() -> new RuntimeException("Error: Department is not found."));
                departments.add(department);
            });
            display.setDepartments(departments);
        }
        display.setName(displayDetails.getName() != null ? displayDetails.getName() : display.getName());
        display.setFrom_queue(displayDetails.getFrom_queue() != null ? displayDetails.getFrom_queue() : display.getFrom_queue());
        display.setTo_queue(displayDetails.getTo_queue() != null ? displayDetails.getTo_queue() : display.getTo_queue());

        return displayRepository.save(display);
    }

    // Delete a Display
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TOKENIST') or hasRole('ROLE_ADMIN')")
    public Display deleteDisplay(@PathVariable(value = "id") Long displayId) {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new RuntimeException("Error: Display not found."));

        displayRepository.delete(display);

        return display;
    }
}
