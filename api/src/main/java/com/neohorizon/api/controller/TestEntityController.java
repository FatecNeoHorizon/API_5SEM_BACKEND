package com.neohorizon.api.controller;

import com.neohorizon.api.dto.TestEntityDTO;
import com.neohorizon.api.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-entity")
public class TestEntityController {

    private final TestEntityService testEntityService;

    @Autowired
    public TestEntityController(TestEntityService testEntityService) {
        this.testEntityService = testEntityService;
    }

    @GetMapping
    public ResponseEntity<List<TestEntityDTO>> getAllTestEntities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        List<TestEntityDTO> testEntityDTOs = testEntityService.getAllTestEntities();
        return ResponseEntity.ok(testEntityDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestEntityDTO> getTestEntityById(@PathVariable Long id) {
        TestEntityDTO testEntityDTO = testEntityService.findById(id);
        if (testEntityDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(testEntityDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<TestEntityDTO> addTestEntity(@RequestBody TestEntityDTO testEntityDTO) {
        TestEntityDTO createdEntity = testEntityService.save(testEntityDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestEntityDTO> updateTestEntity(@PathVariable Long id, @RequestBody TestEntityDTO testEntityDTO) {
        TestEntityDTO updatedEntity = testEntityService.update(id, testEntityDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestEntity(@PathVariable Long id) {
        testEntityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}