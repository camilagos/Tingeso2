package com.example.reporte_service.Model;

import java.time.LocalDateTime;

public class Reserva {


    private LocalDateTime fechaReserva;
    private int vueltasTiempo;
    private int cantPersonas;
    private String rangoPersonas;
    private String detalleGrupo;

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public int getVueltasTiempo() {
        return vueltasTiempo;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public String getRangoPersonas() { return rangoPersonas; }

    public String getDetalleGrupo() {
        return detalleGrupo;
    }
}
