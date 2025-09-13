package com.neohorizon.api.service;

import com.neohorizon.api.dto.TestEntityDTO;
import com.neohorizon.api.entity.TestEntity;
import com.neohorizon.api.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestEntityService {

    private final TestEntityRepository testEntityRepository;

    @Autowired
    public TestEntityService(TestEntityRepository testEntityRepository) {
        this.testEntityRepository = testEntityRepository;
    }

    public List<TestEntityDTO> getAllTestEntities() {
        return testEntityRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TestEntityDTO findById(Long id) {
        return testEntityRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public TestEntityDTO save(TestEntityDTO testEntityDTO) {
        TestEntity testEntity = convertToEntity(testEntityDTO);
        TestEntity savedEntity = testEntityRepository.save(testEntity);
        return convertToDTO(savedEntity);
    }

    public TestEntityDTO update(Long id, TestEntityDTO testEntityDTO) {
        TestEntity existingEntity = testEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test entity with ID " + id + " not found."));

        existingEntity.setName(testEntityDTO.getName());
        existingEntity.setDescription(testEntityDTO.getDescription());

        TestEntity updatedEntity = testEntityRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        testEntityRepository.deleteById(id);
    }

    private TestEntityDTO convertToDTO(TestEntity entity) {
        if (entity == null) {
            return null;
        }
        TestEntityDTO dto = new TestEntityDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private TestEntity convertToEntity(TestEntityDTO dto) {
        if (dto == null) {
            return null;
        }
        TestEntity entity = new TestEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}