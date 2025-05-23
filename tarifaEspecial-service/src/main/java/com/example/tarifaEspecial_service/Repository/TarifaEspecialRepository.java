package com.example.tarifaEspecial_service.Repository;

import com.example.tarifaEspecial_service.Entity.TarifaEspecialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TarifaEspecialRepository extends JpaRepository<TarifaEspecialEntity, Integer> {
    TarifaEspecialEntity findByFecha(LocalDate fecha);
}
