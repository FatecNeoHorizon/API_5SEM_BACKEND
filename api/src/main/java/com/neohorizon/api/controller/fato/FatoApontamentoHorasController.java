package com.neohorizon.api.controller.fato;

import com.neohorizon.api.dto.FatoApontamentoHorasDTO;
import com.neohorizon.api.service.fato.FatoApontamentoHorasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fato-apontamento-horas")
public class FatoApontamentoHorasController {

    private final FatoApontamentoHorasService fatoApontamentoHorasService;

    @Autowired
    public FatoApontamentoHorasController(FatoApontamentoHorasService fatoApontamentoHorasService) {
        this.fatoApontamentoHorasService = fatoApontamentoHorasService;
    }

    @GetMapping
    public ResponseEntity<List<FatoApontamentoHorasDTO>> getAllEntities() {
        List<FatoApontamentoHorasDTO> fatoApontamentoHorasDTOs = fatoApontamentoHorasService.getAllEntities();
        return ResponseEntity.ok(fatoApontamentoHorasDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatoApontamentoHorasDTO> getFatoApontamentoHorasById(@PathVariable Long id) {
        FatoApontamentoHorasDTO fatoApontamentoHorasDTO = fatoApontamentoHorasService.findById(id);
        if (fatoApontamentoHorasDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fatoApontamentoHorasDTO);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<FatoApontamentoHorasDTO>> getApontamentosByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        List<FatoApontamentoHorasDTO> apontamentos = fatoApontamentoHorasService.findByPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(apontamentos);
    }

    @PostMapping("/add")
    public ResponseEntity<FatoApontamentoHorasDTO> addFatoApontamentoHoras(@RequestBody FatoApontamentoHorasDTO fatoApontamentoHorasDTO) {
        FatoApontamentoHorasDTO createdEntity = fatoApontamentoHorasService.create(fatoApontamentoHorasDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FatoApontamentoHorasDTO> updateFatoApontamentoHoras(@PathVariable Long id, @RequestBody FatoApontamentoHorasDTO fatoApontamentoHorasDTO) {
        FatoApontamentoHorasDTO updatedEntity = fatoApontamentoHorasService.update(id, fatoApontamentoHorasDTO);
        if (updatedEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFatoApontamentoHoras(@PathVariable Long id) {
        fatoApontamentoHorasService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
