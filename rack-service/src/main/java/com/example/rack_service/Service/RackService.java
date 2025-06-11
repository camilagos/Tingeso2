package com.example.rack_service.Service;

import com.example.rack_service.Model.Reserva;
import com.example.rack_service.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RackService {

    @Autowired
    RestTemplate restTemplate;

    public List<Reserva> obtenerReservas() {
        String url = "http://reserva-service/reserva/all";
        ResponseEntity<List<Reserva>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Reserva>>() {}
        );
        return response.getBody();
    }

    public List<Map<String, Object>> getAllReservationsByDuration() {
        List<Reserva> reservations = obtenerReservas();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Reserva r : reservations) {
            LocalDateTime start = r.getFechaReserva();
            int laps = r.getVueltasTiempo();
            ResponseEntity<Integer> response = restTemplate.exchange(
                    "http://tarifa-service/tarifa/tiempo/" + laps,
                    HttpMethod.GET,
                    null,
                    Integer.class
            );

            if (response.getBody() == null) {
                throw new IllegalStateException("No se pudo obtener el tiempo de reserva: respuesta vacía");
            }

            int duration = response.getBody();

            LocalDateTime end = start.plusMinutes(duration);

            ResponseEntity<Usuario> response2 = restTemplate.exchange(
                    "http://usuario-service/usuario/rut/" + r.getRutUsuario(),
                    HttpMethod.GET,
                    null,
                    Usuario.class
            );

            Optional<Usuario> user = Optional.ofNullable(response2.getBody());


            Map<String, Object> reservation = new HashMap<>();
            reservation.put("start", start.toString());
            reservation.put("end", end.toString());
            reservation.put("title", user.map(Usuario::getNombre).orElse(r.getRutUsuario()));

            result.add(reservation);
        }

        return result;
    }
}
