package com.example.descuentoVisitas_service.Service;

import com.example.descuentoVisitas_service.Entity.DescuentoVisitasEntity;
import com.example.descuentoVisitas_service.Repository.DescuentoVisitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DescuentoVisitasService {

    @Autowired
    DescuentoVisitasRepository descuentoVisitasRepository;

    public DescuentoVisitasEntity saveDescuentoVisitas(DescuentoVisitasEntity descuentoVisitas) {
        return descuentoVisitasRepository.save(descuentoVisitas);
    }

    public List<DescuentoVisitasEntity> getAllDescuentosVisitas() {
        return descuentoVisitasRepository.findAll();
    }

    public DescuentoVisitasEntity updateDescuentoVisitas(DescuentoVisitasEntity descuentoVisitas) {
        return descuentoVisitasRepository.save(descuentoVisitas);
    }

    public boolean deleteDescuentoVisitas(int id) throws Exception {
        try {
            descuentoVisitasRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /*
    public DescuentoVisitasEntity buscarDescuentoVisitas(LocalDateTime fechaReserva) {
        LocalDateTime fechaInicio = fechaReserva.withDayOfMonth(1).toLocalDate().atStartOfDay();
        DescuentoVisitasEntity descuentoVisitas = descuentoVisitasRepository.findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(cantVisitas, cantVisitas);
        if (descuentoVisitas != null) {
            return descuentoVisitas;
        } else {
            return null;
        }
    }*/
}
