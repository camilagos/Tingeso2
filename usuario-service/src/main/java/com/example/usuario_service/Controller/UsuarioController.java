package com.example.usuario_service.Controller;

import com.example.usuario_service.Entity.UsuarioEntity;
import com.example.usuario_service.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/")
    public ResponseEntity<UsuarioEntity> saveUser(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity user = usuarioService.saveUsuario(usuario);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getUserById(@PathVariable Long id) {
        UsuarioEntity user = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUserById(@PathVariable Long id) throws Exception {
        var isDeleted = usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<UsuarioEntity> getUserByRut(@PathVariable String rut) {
        UsuarioEntity user = usuarioService.getUsuarioByRut(rut);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioEntity> login(@RequestBody UsuarioEntity usuario) {
        return ResponseEntity.ok(usuarioService.login(usuario));
    }
}
