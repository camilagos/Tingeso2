package com.example.reserva_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rutUsuario;
    private String rutsUsuarios;
    private LocalDateTime fechaReserva;
    private int vueltasTiempo;
    private int cantPersonas;

    @Column(name = "detalle_grupo", columnDefinition = "TEXT")
    private String detalleGrupo;

    public Long getId() {
        return id;
    }

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

    public String getDetalleGrupo() {
        return detalleGrupo;
    }

    public ReservaEntity(String rutUsuario, String rutsUsuarios, LocalDateTime fechaReserva, int vueltasTiempo, int cantPersonas, String detalleGrupo) {
        this.rutUsuario = rutUsuario;
        this.rutsUsuarios = rutsUsuarios;
        this.fechaReserva = fechaReserva;
        this.vueltasTiempo = vueltasTiempo;
        this.cantPersonas = cantPersonas;
        this.detalleGrupo = detalleGrupo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRutUsuario(String rutUsuario) {
        this.rutUsuario = rutUsuario;
    }

    public void setRutsUsuarios(String rutsUsuarios) {
        this.rutsUsuarios = rutsUsuarios;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public void setVueltasTiempo(int vueltasTiempo) {
        this.vueltasTiempo = vueltasTiempo;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public void setDetalleGrupo(String detalleGrupo) {
        this.detalleGrupo = detalleGrupo;
    }
}
