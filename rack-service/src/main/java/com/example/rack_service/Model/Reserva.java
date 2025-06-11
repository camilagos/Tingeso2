package com.example.rack_service.Model;

import java.time.LocalDateTime;

public class Reserva {

    private String rutUsuario;
    private LocalDateTime fechaReserva;
    private int vueltasTiempo;
    private int cantPersonas;
    private int duracion;
    private String detalleGrupo;


    public String getRutUsuario() { return rutUsuario;}
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public int getVueltasTiempo() {
        return vueltasTiempo;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public int getDuracion() { return duracion; }

    public String getDetalleGrupo() {
        return detalleGrupo;
    }
}
