package com.example.tarifaEspecial_service.Controller;

import com.example.tarifaEspecial_service.Entity.TarifaEspecialEntity;
import com.example.tarifaEspecial_service.Service.TarifaEspecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tarifaEspecial")
public class TarifaEspecialController {

    @Autowired
    TarifaEspecialService tarifaEspecialService;

    @PostMapping()
    public ResponseEntity<TarifaEspecialEntity> save(@RequestBody TarifaEspecialEntity tarifaEspecial) {
        TarifaEspecialEntity tarifaEspecialNew = tarifaEspecialService.saveTarifaEspecial(tarifaEspecial);
        return ResponseEntity.ok(tarifaEspecialNew);
    }

    @GetMapping()
    public ResponseEntity<List<TarifaEspecialEntity>> getAll() {
        List<TarifaEspecialEntity> tarifasEspeciales = tarifaEspecialService.getAllTarifasEspeciales();
        if (tarifasEspeciales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarifasEspeciales);
    }

    @PutMapping()
    public ResponseEntity<TarifaEspecialEntity> update(@RequestBody TarifaEspecialEntity tarifaEspecial) {
        TarifaEspecialEntity tarifaEspecialUpdated = tarifaEspecialService.updateTarifaEspecial(tarifaEspecial);
        return ResponseEntity.ok(tarifaEspecialUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        try {
            tarifaEspecialService.deleteTarifaEspecial(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calcular/{fechaReserva}/{precioBase}")
    public ResponseEntity<Integer> calcularTarifaEspecial(@PathVariable("fechaReserva") String fechaReserva, @PathVariable("precioBase") int precioBase) {
        LocalDate fecha = LocalDate.parse(fechaReserva);
        int precioFinal = tarifaEspecialService.aplicarTarifaEspecial(fecha, precioBase);
        return ResponseEntity.ok(precioFinal);
    }
}
