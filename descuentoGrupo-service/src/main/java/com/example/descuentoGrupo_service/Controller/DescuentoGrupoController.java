package com.example.descuentoGrupo_service.Controller;

import com.example.descuentoGrupo_service.Entity.DescuentoGrupoEntity;
import com.example.descuentoGrupo_service.Service.DescuentoGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentoGrupo")
public class DescuentoGrupoController {

    @Autowired
    DescuentoGrupoService descuentoGrupoService;

    @PostMapping()
    public ResponseEntity<DescuentoGrupoEntity> save(@RequestBody DescuentoGrupoEntity descuentoGrupo) {
        DescuentoGrupoEntity descuentoGrupoNew = descuentoGrupoService.saveDescuentoGrupo(descuentoGrupo);
        return ResponseEntity.ok(descuentoGrupoNew);
    }

    @GetMapping()
    public ResponseEntity<List<DescuentoGrupoEntity>> getAll() {
        List<DescuentoGrupoEntity> descuentosGrupo = descuentoGrupoService.getAllDescuentosGrupo();
        if (descuentosGrupo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(descuentosGrupo);
    }

    @PutMapping()
    public ResponseEntity<DescuentoGrupoEntity> update(@RequestBody DescuentoGrupoEntity descuentoGrupo) {
        DescuentoGrupoEntity descuentoGrupoUpdated = descuentoGrupoService.updateDescuentoGrupo(descuentoGrupo);
        return ResponseEntity.ok(descuentoGrupoUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        try {
            descuentoGrupoService.deleteDescuentoGrupo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calcular/{cantPersonas}")
    public ResponseEntity<DescuentoGrupoEntity> seleccionarDescuentoGrupo(@PathVariable("cantPersonas") int cantPersonas) {
        DescuentoGrupoEntity descuentoGrupo = descuentoGrupoService.buscarDescuentoGrupo(cantPersonas);
        if (descuentoGrupo != null) {
            return ResponseEntity.ok(descuentoGrupo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
