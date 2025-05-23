package com.example.tarifa_service.Service;


import com.example.tarifa_service.Entity.TarifaEntity;
import com.example.tarifa_service.Repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaService {

    @Autowired
    TarifaRepository tarifaRepository;

    public TarifaEntity saveTarifa(TarifaEntity tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public List<TarifaEntity> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    public TarifaEntity updateTarifa(TarifaEntity tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public boolean deleteTarifa(int id) throws Exception {
        try {
            tarifaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public TarifaEntity buscarTarifa (int tiempoVueltas) {
        TarifaEntity tarifa = tarifaRepository.findByTiempoVueltas(tiempoVueltas);
        if (tarifa != null) {
            return tarifa;
        } else {
            return null;
        }
    }
}
