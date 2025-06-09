package com.example.rack_service.Model;

import java.time.LocalDateTime;

public class Reserva {

    private LocalDateTime fechaReserva;
    private int cantPersonas;
    private String vueltasTiempo;
    private String detalle;

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public String getVueltasTiempo() {
        return vueltasTiempo;
    }

    public String getDetalle() {
        return detalle;
    }
}
