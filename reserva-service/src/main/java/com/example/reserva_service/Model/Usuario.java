package com.example.reserva_service.Model;

import java.time.LocalDate;

public class Usuario {
    private String nombre;
    private String correo;
    private String rut;
    private String contrasena;
    private LocalDate cumpleanos;
    private boolean admin;

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRut() {
        return rut;
    }

    public String getContrasena() {
        return contrasena;
    }

    public LocalDate getCumpleanos() {
        return cumpleanos;
    }

    public boolean isAdmin() {
        return admin;
    }
}
