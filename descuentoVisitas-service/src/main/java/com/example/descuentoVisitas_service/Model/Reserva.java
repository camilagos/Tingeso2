package com.example.descuentoVisitas_service.Model;

import java.time.LocalDateTime;

public class Reserva {
    private String rutUsuario;
    private String rutsUsuarios;
    private LocalDateTime fechaReserva;
    private int vueltasTiempo;
    private int cantPersonas;

    public String getRutUsuario() {
        return rutUsuario;
    }

    public String getRutsUsuarios() {
        return rutsUsuarios;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public int getVueltasTiempo() {
        return vueltasTiempo;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }
}
