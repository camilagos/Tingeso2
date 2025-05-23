package com.example.tarifa_service.Repository;

import com.example.tarifa_service.Entity.TarifaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<TarifaEntity, Integer> {
    public TarifaEntity findByTiempoVueltas(int tiempoVueltas);
}
