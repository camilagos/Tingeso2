package com.example.usuario_service.Service;

import com.example.usuario_service.Entity.UsuarioEntity;
import com.example.usuario_service.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioEntity saveUsuario(UsuarioEntity usuario) {
        UsuarioEntity usuarioRut = usuarioRepository.findByRut(usuario.getRut());
        if (usuarioRut != null) {
            return null;
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioEntity getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public boolean deleteUsuario(Long id) throws Exception {
        try {
            usuarioRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public UsuarioEntity getUsuarioByRut(String rut) {
        return usuarioRepository.findByRut(rut);
    }

    public UsuarioEntity login(UsuarioEntity usuario) {
        UsuarioEntity user = usuarioRepository.findByCorreoAndContrasena(usuario.getCorreo(), usuario.getContrasena());
        if (usuario != null) {
            return usuario;
        } else {
            return null;
        }
    }
}
