package kz.eospatial.thermalpointwebsocketservice.repo;

import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThermalPointRepository {
    List<ThermalPoint> findAll();
    List<ThermalPoint> findByForestryId(Long forestryId);
}

