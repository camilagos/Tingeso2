package com.example.reserva_service.Controller;

import com.example.reserva_service.Entity.ReservaEntity;
import com.example.reserva_service.Service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reserva")
@CrossOrigin("*")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @GetMapping("/entreFechas")
    public ResponseEntity<List<ReservaEntity>> getReservasEntreFechas(LocalDateTime startDate, LocalDateTime endDate) {
        List<ReservaEntity> reservas = reservaService.getReservasEntreFechas(startDate, endDate);
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }
}
