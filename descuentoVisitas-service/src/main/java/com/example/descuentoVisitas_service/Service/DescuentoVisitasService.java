package com.example.descuentoVisitas_service.Service;

import com.example.descuentoVisitas_service.Entity.DescuentoVisitasEntity;
import com.example.descuentoVisitas_service.Model.Reserva;
import com.example.descuentoVisitas_service.Model.Usuario;
import com.example.descuentoVisitas_service.Repository.DescuentoVisitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DescuentoVisitasService {

    @Autowired
    DescuentoVisitasRepository descuentoVisitasRepository;

    @Autowired
    RestTemplate restTemplate;

    public DescuentoVisitasEntity saveDescuentoVisitas(DescuentoVisitasEntity descuentoVisitas) {
        return descuentoVisitasRepository.save(descuentoVisitas);
    }

    public List<DescuentoVisitasEntity> getAllDescuentosVisitas() {
        return descuentoVisitasRepository.findAll();
    }

    public DescuentoVisitasEntity updateDescuentoVisitas(int id, DescuentoVisitasEntity descuentoVisitas) {
        DescuentoVisitasEntity existingDescuento = descuentoVisitasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescuentoVisitas not found with id: " + id));

        existingDescuento.setCategoria(descuentoVisitas.getCategoria());
        existingDescuento.setMinVisitas(descuentoVisitas.getMinVisitas());
        existingDescuento.setMaxVisitas(descuentoVisitas.getMaxVisitas());
        existingDescuento.setDescuento(descuentoVisitas.getDescuento());

        return descuentoVisitasRepository.save(existingDescuento);
    }

    public boolean deleteDescuentoVisitas(int id) throws Exception {
        try {
            descuentoVisitasRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public int buscarDescuentoVisitas(String rut, LocalDateTime fechaReserva) {
        LocalDateTime fechaInicio = fechaReserva.withDayOfMonth(1).toLocalDate().atStartOfDay();
        List<Reserva> reservas = restTemplate.exchange("http://reserva-service/reserva/entreFechas/"
                        + "/" + fechaInicio + "/" + fechaReserva,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Reserva>>() {
                }).getBody();
        int contador = 0;

        for (Reserva reserva : reservas) {
            boolean isMainCustomer = rut.equals(reserva.getRutUsuario());

            boolean isInParticipants = false;
            if (reserva.getRutsUsuarios() != null && !reserva.getRutsUsuarios().isBlank()) {
                isInParticipants = Arrays.stream(reserva.getRutsUsuarios().split(","))
                        .map(String::trim)
                        .anyMatch(rut::equals);
            }

            if (isMainCustomer || isInParticipants) {
                contador++;
            }
        }

        DescuentoVisitasEntity desc = descuentoVisitasRepository.findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(contador, contador);
        return desc.getDescuento();
    }
}
