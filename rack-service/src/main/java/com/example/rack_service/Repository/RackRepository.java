package com.example.rack_service.Repository;

import com.example.rack_service.Entity.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<RackEntity, Long> {

}
