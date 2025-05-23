package com.example.tarifaEspecial_service.Service;

import com.example.tarifaEspecial_service.Entity.TarifaEspecialEntity;
import com.example.tarifaEspecial_service.Repository.TarifaEspecialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class TarifaEspecialService {

    @Autowired
    TarifaEspecialRepository tarifaEspecialRepository;

    public TarifaEspecialEntity saveTarifaEspecial(TarifaEspecialEntity tarifaEspecial) {
        return tarifaEspecialRepository.save(tarifaEspecial);
    }

    public List<TarifaEspecialEntity> getAllTarifasEspeciales() {
        return tarifaEspecialRepository.findAll();
    }

    public TarifaEspecialEntity updateTarifaEspecial(TarifaEspecialEntity tarifaEspecial) {
        return tarifaEspecialRepository.save(tarifaEspecial);
    }

    public boolean deleteTarifaEspecial(int id) throws Exception {
        try {
            tarifaEspecialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean isFinDeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    public int aplicarTarifaEspecial(LocalDate fechaReserva, int precioBase) {
        TarifaEspecialEntity tarifaEspecial = tarifaEspecialRepository.findByFecha(fechaReserva);
        if (tarifaEspecial != null) {
            return precioBase + (precioBase * tarifaEspecial.getPorcentajeTarifa() / 100);
        } else {
            return isFinDeSemana(fechaReserva)
                    ? precioBase + (precioBase * 15 / 100)
                    : precioBase;
        }
    }
}
