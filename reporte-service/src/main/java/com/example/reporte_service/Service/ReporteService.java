package com.example.reporte_service.Service;

import com.example.reporte_service.Entity.ReporteEntity;
import com.example.reporte_service.Model.Reserva;
import com.example.reporte_service.Repository.ReporteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    ReporteRepository reporteRepository;

    @Autowired
    RestTemplate restTemplate;

    public final ObjectMapper mapper = new ObjectMapper();

    public String getMonth(String yyyyMM) {
        Month month = Month.of(Integer.parseInt(yyyyMM.substring(5)));
        return month.getDisplayName(TextStyle.FULL, new Locale("es"));
    }

    public List<Reserva> obtenerReservasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String url = String.format("http://reserva-service/reserva/entreFechas/%s/%s",
                fechaInicio.format(formatter),
                fechaFin.format(formatter));

        ResponseEntity<List<Reserva>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Reserva>>() {}
        );

        return response.getBody();
    }


    public Map<String, Map<String, Double>> incomeFromLapsOrTime(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reserva> reservations = obtenerReservasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59));

        Map<String, Map<String, Double>> intermediate = new TreeMap<>();
        for (Reserva r : reservations) {
            String monthReservation = r.getFechaReserva().getYear() + "-" + String.format("%02d", r.getFechaReserva().getMonthValue());
            String lapsOrTimeReservation = r.getVueltasTiempo() + " vueltas o máx. " + r.getVueltasTiempo() + " minutos";

            double totalReservation = 0;
            try {
                List<List<Object>> detail = mapper.readValue(r.getDetalleGrupo(), new TypeReference<List<List<Object>>>() {});
                for (List<Object> row : detail) {
                    if (!row.isEmpty()) {
                        Object tarifa = row.get(row.size() - 1);
                        if (tarifa instanceof Number) {
                            totalReservation += Math.round(((Number) tarifa).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }

            intermediate.computeIfAbsent(lapsOrTimeReservation, k -> new TreeMap<>());
            intermediate.get(lapsOrTimeReservation).put(monthReservation,
                    intermediate.get(lapsOrTimeReservation).getOrDefault(monthReservation, 0.0) + totalReservation);
        }

        Set<String> allMonths = new TreeSet<>();
        LocalDate current = fechaInicio.withDayOfMonth(1);
        while (!current.isAfter(fechaFin.withDayOfMonth(1))) {
            allMonths.add(current.getYear() + "-" + String.format("%02d", current.getMonthValue()));
            current = current.plusMonths(1);
        }

        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        Map<String, Double> totalPerMonth = new TreeMap<>();

        Set<String> allLapsCategories = new TreeSet<>(intermediate.keySet());

        for (String row : allLapsCategories) {
            Map<String, Double> rowData = new LinkedHashMap<>();
            double totalRow = 0;
            for (String month : allMonths) {
                double value = intermediate.get(row).getOrDefault(month, 0.0);
                double roundedValue = Math.round(value);
                rowData.put(getMonth(month), roundedValue);
                totalPerMonth.put(getMonth(month), totalPerMonth.getOrDefault(getMonth(month), 0.0) + roundedValue);
                totalRow += roundedValue;
            }
            rowData.put("Total", (double) Math.round(totalRow));
            result.put(row, rowData);
        }

        Map<String, Double> totalRows = new LinkedHashMap<>();
        double total = 0;
        for (String nameMonth : allMonths.stream().map(this::getMonth).collect(Collectors.toList())) {
            double valorMes = totalPerMonth.getOrDefault(nameMonth, 0.0);
            double roundedValorMes = Math.round(valorMes);
            totalRows.put(nameMonth, roundedValorMes);
            total += roundedValorMes;
        }
        totalRows.put("Total", (double) Math.round(total));
        result.put("TOTAL", totalRows);

        try {
            String jsonDetalle = mapper.writeValueAsString(result);
            ReporteEntity reporte = new ReporteEntity();
            reporte.setTipo("Número de Vueltas o Tiempo Máximo");
            reporte.setFechaInicio(fechaInicio);
            reporte.setFechaFin(fechaFin);
            reporte.setDetalle(jsonDetalle);
            reporteRepository.save(reporte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, Map<String, Double>> incomePerPerson(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reserva> reservations = obtenerReservasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59));

        Map<String, Map<String, Double>> intermediate = new TreeMap<>();
        for (Reserva r : reservations) {
            String monthReservation = r.getFechaReserva().getYear() + "-" + String.format("%02d", r.getFechaReserva().getMonthValue());

            // Se toma el rango guardado en la reserva:
            String range = r.getRangoPersonas();
            if (range == null || range.isBlank()) {
                range = "Sin rango definido";
            }

            double totalReservation = 0;
            try {
                List<List<Object>> detail = mapper.readValue(r.getDetalleGrupo(), new TypeReference<List<List<Object>>>() {});
                for (List<Object> row : detail) {
                    if (!row.isEmpty()) {
                        Object tarifa = row.get(row.size() - 1);
                        if (tarifa instanceof Number) {
                            totalReservation += Math.round(((Number) tarifa).doubleValue());
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }

            intermediate.computeIfAbsent(range, k -> new TreeMap<>());
            intermediate.get(range).put(monthReservation,
                    intermediate.get(range).getOrDefault(monthReservation, 0.0) + totalReservation);
        }

        Set<String> allMonths = new TreeSet<>();
        LocalDate current = fechaInicio.withDayOfMonth(1);
        while (!current.isAfter(fechaFin.withDayOfMonth(1))) {
            allMonths.add(current.getYear() + "-" + String.format("%02d", current.getMonthValue()));
            current = current.plusMonths(1);
        }

        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        Map<String, Double> totalPerMonth = new TreeMap<>();

        Set<String> allGroupCategories = new TreeSet<>(intermediate.keySet());

        for (String row : allGroupCategories) {
            Map<String, Double> rowData = new LinkedHashMap<>();
            double totalRow = 0;
            for (String month : allMonths) {
                double value = intermediate.get(row).getOrDefault(month, 0.0);
                double roundedValue = Math.round(value);
                rowData.put(getMonth(month), roundedValue);
                totalPerMonth.put(getMonth(month), totalPerMonth.getOrDefault(getMonth(month), 0.0) + roundedValue);
                totalRow += roundedValue;
            }
            rowData.put("Total", (double) Math.round(totalRow));
            result.put(row, rowData);
        }

        Map<String, Double> totalRows = new LinkedHashMap<>();
        double total = 0;
        for (String nameMonth : allMonths.stream().map(this::getMonth).collect(Collectors.toList())) {
            double monthValue = totalPerMonth.getOrDefault(nameMonth, 0.0);
            double roundedMonthValue = Math.round(monthValue);
            totalRows.put(nameMonth, roundedMonthValue);
            total += roundedMonthValue;
        }
        totalRows.put("Total", (double) Math.round(total));
        result.put("TOTAL", totalRows);

        try {
            String jsonDetalle = mapper.writeValueAsString(result);
            ReporteEntity reporte = new ReporteEntity();
            reporte.setTipo("Número de Personas");
            reporte.setFechaInicio(fechaInicio);
            reporte.setFechaFin(fechaFin);
            reporte.setDetalle(jsonDetalle);
            reporteRepository.save(reporte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
