package com.example.descuentoGrupo_service.Repository;

import com.example.descuentoGrupo_service.Entity.DescuentoGrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoGrupoRepository extends JpaRepository<DescuentoGrupoEntity, Integer> {
    DescuentoGrupoEntity findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(int minPersonas, int maxPersonas);
}
