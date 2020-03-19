package com.example.tokenback.controllers;

import com.example.tokenback.models.Department;
import com.example.tokenback.models.Display;
import com.example.tokenback.repository.DepartmentRepository;
import com.example.tokenback.repository.DisplayRepository;
import com.example.tokenback.schema.CustomDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<Display> getAllDisplays() {
        return displayRepository.findAll();
    }

    // Create a new Display
    @PostMapping("/")
    public Display createDisplay(@Valid @RequestBody CustomDisplay displayDetails) {

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

        return displayRepository.save(display);
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
    public Display deleteDisplay(@PathVariable(value = "id") Long displayId) {
        Display display = displayRepository.findById(displayId)
                .orElseThrow(() -> new RuntimeException("Error: Display not found."));

        displayRepository.delete(display);

        return display;
    }
}
