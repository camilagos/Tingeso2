package com.example.kart_service.Controller;

import com.example.kart_service.Entity.KartEntity;
import com.example.kart_service.Service.KartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kart")
public class KartController {

    @Autowired
    KartService kartService;

    @PostMapping("/save")
    public ResponseEntity<KartEntity> saveKart(@RequestBody KartEntity kart) {
        KartEntity savedKart = kartService.saveKart(kart);
        return ResponseEntity.ok(savedKart);
    }

    @GetMapping("/disponible/{disponible}")
    public ResponseEntity<List<KartEntity>> getKartsByDisponibilidad(@PathVariable boolean disponible) {
        List<KartEntity> karts = kartService.getKartsByDisponibilidad(disponible);
        return ResponseEntity.ok(karts);
    }

    @PutMapping("/update")
    public ResponseEntity<KartEntity> updateKart(@RequestBody KartEntity kart) {
        KartEntity updatedKart = kartService.updateKart(kart);
        return ResponseEntity.ok(updatedKart);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteKart(@PathVariable Long id) throws Exception {
        boolean isDeleted = kartService.deleteKart(id);
        return ResponseEntity.ok(isDeleted);
    }
}
