package com.example.rack_service.Service;

import com.example.rack_service.Model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RackService {

    @Autowired
    RestTemplate restTemplate;

    public List<Reserva> obtenerReservas() {
        String url = "http://reserva-service/reservas/all";
        ResponseEntity<List<Reserva>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Reserva>>() {}
        );
        return response.getBody();
    }
}
