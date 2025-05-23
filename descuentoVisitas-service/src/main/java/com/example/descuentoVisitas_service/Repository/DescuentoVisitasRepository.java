package com.example.descuentoVisitas_service.Repository;

import com.example.descuentoVisitas_service.Entity.DescuentoVisitasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoVisitasRepository extends JpaRepository<DescuentoVisitasEntity, Integer> {
    DescuentoVisitasEntity findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(int minVisitas, int maxVisitas);
}
