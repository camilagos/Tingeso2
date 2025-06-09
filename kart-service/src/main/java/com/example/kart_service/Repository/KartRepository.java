package com.example.kart_service.Repository;

import com.example.kart_service.Entity.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KartRepository extends JpaRepository<KartEntity, Long> {
    List<KartEntity> findByDisponible (boolean disponible);
}
