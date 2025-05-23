package com.example.usuario_service.Repository;

import com.example.usuario_service.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    UsuarioEntity findByRut(String rut);
    UsuarioEntity findByCorreoAndContrasena(String correo, String contrasena);
    List<UsuarioEntity> findAllByRutIn(List<String> ruts);
}
