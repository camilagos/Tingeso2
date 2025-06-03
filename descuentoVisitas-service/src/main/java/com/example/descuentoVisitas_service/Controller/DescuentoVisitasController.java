package com.example.descuentoVisitas_service.Controller;

import com.example.descuentoVisitas_service.Entity.DescuentoVisitasEntity;
import com.example.descuentoVisitas_service.Service.DescuentoVisitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentoVisitas")
public class DescuentoVisitasController {

    @Autowired
    DescuentoVisitasService descuentoVisitasService;

    @PostMapping()
    public ResponseEntity<DescuentoVisitasEntity> save(@RequestBody DescuentoVisitasEntity descuentoVisitas) {
        DescuentoVisitasEntity descuentoVisitasNew = descuentoVisitasService.saveDescuentoVisitas(descuentoVisitas);
        return ResponseEntity.ok(descuentoVisitasNew);
    }

    @GetMapping()
    public ResponseEntity<List<DescuentoVisitasEntity>> getAll() {
        List<DescuentoVisitasEntity> descuentosVisitas = descuentoVisitasService.getAllDescuentosVisitas();
        if (descuentosVisitas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(descuentosVisitas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoVisitasEntity> update(
            @PathVariable("id") int id,
            @RequestBody DescuentoVisitasEntity descuentoVisitas) {
        DescuentoVisitasEntity descuentoVisitasUpdated = descuentoVisitasService.updateDescuentoVisitas(id, descuentoVisitas);
        return ResponseEntity.ok(descuentoVisitasUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        try {
            descuentoVisitasService.deleteDescuentoVisitas(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*
    @GetMapping("/calcular/{cantVisitas}")
    public ResponseEntity<DescuentoVisitasEntity> seleccionarDescuentoVisitas(@PathVariable("cantVisitas") int cantVisitas) {
        DescuentoVisitasEntity descuentoVisitas = descuentoVisitasService.buscarDescuentoVisitas(cantVisitas);
        if (descuentoVisitas != null) {
            return ResponseEntity.ok(descuentoVisitas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}
