package com.example.reporte_service.Controller;

import com.example.reporte_service.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

    @Autowired
    ReporteService reporteService;

    @GetMapping("/porTiempo")
    public ResponseEntity<Map<String, Map<String, Double>>> getIncomeFromLapsOrTime(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Map<String, Double>> result = reporteService.incomeFromLapsOrTime(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/porCantPersonas")
    public ResponseEntity<Map<String, Map<String, Double>>> getIncomePerPerson(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Map<String, Double>> result = reporteService.incomePerPerson(startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
