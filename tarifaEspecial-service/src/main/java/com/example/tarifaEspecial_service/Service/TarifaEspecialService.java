package com.example.tarifaEspecial_service.Service;

import com.example.tarifaEspecial_service.Entity.TarifaEspecialEntity;
import com.example.tarifaEspecial_service.Model.Usuario;
import com.example.tarifaEspecial_service.Repository.TarifaEspecialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public TarifaEspecialEntity updateTarifaEspecial(int id, TarifaEspecialEntity tarifaEspecial) {
        TarifaEspecialEntity existingTarifa = tarifaEspecialRepository.findById(id).orElse(null);
        if (existingTarifa != null) {
            existingTarifa.setFecha(tarifaEspecial.getFecha());
            existingTarifa.setPorcentajeTarifa(tarifaEspecial.getPorcentajeTarifa());
            existingTarifa.setDescripcion(tarifaEspecial.getDescripcion());
            return tarifaEspecialRepository.save(existingTarifa);
        } else {
            return null; // or throw an exception
        }
    }

    public boolean deleteTarifaEspecial(int id) throws Exception {
        try {
            tarifaEspecialRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean isFinDeSemanaoFeriado(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        // Feriados fijos
        List<MonthDay> feriadosFijos = List.of(
                MonthDay.of(1, 1),   // Año Nuevo
                MonthDay.of(5, 1),   // Día del Trabajador
                MonthDay.of(9, 18),  // Independencia Chile
                MonthDay.of(9, 19),  // Glorias del Ejército
                MonthDay.of(12, 25)  // Navidad
        );

        MonthDay diaActual = MonthDay.from(fecha);

        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY || feriadosFijos.contains(diaActual);
    }

    public int aplicarTarifaEspecial(LocalDate fechaReserva, int precioBase) {
        TarifaEspecialEntity tarifaEspecial = tarifaEspecialRepository.findByFecha(fechaReserva);
        if (tarifaEspecial != null) {
            return precioBase + (precioBase * tarifaEspecial.getPorcentajeTarifa() / 100);
        } else {
            return isFinDeSemanaoFeriado(fechaReserva)
                    ? precioBase + (precioBase * 15 / 100)
                    : precioBase;
        }
    }

    public Set<Usuario> obtenerCumpleaneros(List<Usuario> usuarios, LocalDate fecha) {
        return usuarios.stream()
                .filter(c -> c.getCumpleanos().getMonth() == fecha.getMonth() && c.getCumpleanos().getDayOfMonth() == fecha.getDayOfMonth())
                .collect(Collectors.toSet());
    }

    public int cumpleanosPermitidos(int cantPersonas){
        int birthdayDiscountsAllowed = 0;
        if (cantPersonas >= 3 && cantPersonas <= 5) {
            birthdayDiscountsAllowed = 1;
        } else if (cantPersonas >= 6 && cantPersonas <= 15) {
            birthdayDiscountsAllowed = 2;
        }
        return birthdayDiscountsAllowed;
    }
}
