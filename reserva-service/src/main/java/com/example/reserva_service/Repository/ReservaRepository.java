package com.example.reserva_service.Repository;

import com.example.reserva_service.Entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    ReservaEntity findByFechaReserva(LocalDateTime fechaReserva);

    List<ReservaEntity> findByFechaReservaBetween(LocalDateTime startDate, LocalDateTime endDate);
}
