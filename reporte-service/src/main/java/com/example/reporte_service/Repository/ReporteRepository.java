package com.example.reporte_service.Repository;

import com.example.reporte_service.Entity.ReporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity, Long> {

}
