package com.example.reserva_service.Controller;

import com.example.reserva_service.Entity.ReservaEntity;
import com.example.reserva_service.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reserva")
@CrossOrigin("*")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @GetMapping("/entreFechas/{startDate}/{endDate}")
    public ResponseEntity<List<ReservaEntity>> getReservasEntreFechas(@PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                      @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ReservaEntity> reservas = reservaService.getReservasEntreFechas(startDate, endDate);
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/")
    public ResponseEntity<ReservaEntity> crearReserva(@RequestBody ReservaEntity reserva) {
        ReservaEntity nuevaReserva = reservaService.saveReserva(reserva);
        return ResponseEntity.ok(nuevaReserva);
    }

    @GetMapping("/{fecha}")
    public ResponseEntity<ReservaEntity> getReservaByFecha(@PathVariable LocalDateTime fecha) {
        ReservaEntity reserva = reservaService.getReservaByFechaReserva(fecha);
        if (reserva == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservaEntity>> getAllReservas() {
        List<ReservaEntity> reservas = reservaService.getAllReservas();
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }

}
