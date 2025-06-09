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
        List<Reserva> reservations = obtenerReservasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(23,59));

        Map<String, Map<String, Double>> intermediate = new TreeMap<>();
        for (Reserva r : reservations) {
            String monthReservation = r.getFechaReserva().getYear() + "-" + String.format("%02d", r.getFechaReserva().getMonthValue());
            String lapsOrTimeReservation = r.getVueltasTiempo() + " vueltas o máx. " + r.getVueltasTiempo() + " minutos";

            double totalReservation = 0;
            try {
                List<List<Object>> detail = mapper.readValue(r.getDetalle(), new TypeReference<List<List<Object>>>() {
                });
                for (List<Object> row : detail) {
                    if (!row.isEmpty()) {
                        Object tarifa = row.get(row.size() - 1);
                        if (tarifa instanceof Number) {
                            totalReservation += ((Number) tarifa).doubleValue();
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

        // Generar lista de todos los meses entre startDate y endDate
        Set<String> allMonths = new TreeSet<>();
        LocalDate current = fechaInicio.withDayOfMonth(1);
        while (!current.isAfter(fechaFin.withDayOfMonth(1))) {
            allMonths.add(current.getYear() + "-" + String.format("%02d", current.getMonthValue()));
            current = current.plusMonths(1);
        }

        Map<String, Map<String, Double>> result = new LinkedHashMap<>();
        Map<String, Double> totalPerMonth = new TreeMap<>();

        // Agregar explícitamente todas las categorías de vueltas/tiempo
        List<String> allLapsCategories = List.of(
                "10 vueltas o máx. 10 minutos",
                "15 vueltas o máx. 15 minutos",
                "20 vueltas o máx. 20 minutos"
        );

        for (String category : allLapsCategories) {
            intermediate.computeIfAbsent(category, k -> new TreeMap<>());
        }


        for (String row : intermediate.keySet()) {
            Map<String, Double> rowData = new LinkedHashMap<>();
            double totalRow = 0;
            for (String month : allMonths) {
                double value = intermediate.get(row).getOrDefault(month, 0.0);
                rowData.put(getMonth(month), value);
                totalPerMonth.put(getMonth(month), totalPerMonth.getOrDefault(getMonth(month), 0.0) + value);
                totalRow += value;
            }
            rowData.put("Total", totalRow);
            result.put(row, rowData);
        }

        Map<String, Double> totalRows = new LinkedHashMap<>();
        double total = 0;
        for (String nameMonth : allMonths.stream().map(this::getMonth).collect(Collectors.toList())) {
            double valorMes = totalPerMonth.getOrDefault(nameMonth, 0.0);
            totalRows.put(nameMonth, valorMes);
            total += valorMes;
        }
        totalRows.put("Total", total);
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
        List<Reserva> reservations = obtenerReservasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(23,59));

        Map<String, Map<String, Double>> intermediate = new TreeMap<>();
        for (Reserva r : reservations) {
            String monthReservation = r.getFechaReserva().getYear() + "-" + String.format("%02d", r.getFechaReserva().getMonthValue());
            int numberPeople = r.getCantPersonas();
            String range;
            if (numberPeople <= 2) range = "1-2 personas";
            else if (numberPeople <= 5) range = "3-5 personas";
            else if (numberPeople <= 10) range = "6-10 personas";
            else range = "11-15 personas";

            double totalReservation = 0;
            try {
                List<List<Object>> detail = mapper.readValue(r.getDetalle(), new TypeReference<List<List<Object>>>() {
                });
                for (List<Object> row : detail) {
                    if (!row.isEmpty()) {
                        Object tarifa = row.get(row.size() - 1);
                        if (tarifa instanceof Number) {
                            totalReservation += ((Number) tarifa).doubleValue();
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

        List<String> allGroupCategories = List.of(
                "1-2 personas",
                "3-5 personas",
                "6-10 personas",
                "11-15 personas"
        );

        for (String row : allGroupCategories) {
            Map<String, Double> rowData = new LinkedHashMap<>();
            double totalRow = 0;
            for (String month : allMonths) {
                double value = intermediate.getOrDefault(row, new TreeMap<>()).getOrDefault(month, 0.0);
                rowData.put(getMonth(month), value);
                totalRow += value;
            }
            rowData.put("Total", totalRow);
            result.put(row, rowData);
        }

        Map<String, Double> totalRows = new LinkedHashMap<>();
        double total = 0;
        for (String nameMonth : allMonths.stream().map(this::getMonth).collect(Collectors.toList())) {
            double monthValue = totalPerMonth.getOrDefault(nameMonth, 0.0);
            totalRows.put(nameMonth, monthValue);
            total += monthValue;
        }
        totalRows.put("Total", total);
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
