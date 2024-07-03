//package kz.eospatial.thermalpointwebsocketservice.repository;
//
//import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface ThermalPointRepository extends JpaRepository<ThermalPoint, Long> {
//
//    @Query("SELECT t FROM ThermalPoint t JOIN t.fireForestRelation r WHERE r.forestry.id = :forestryId")
//    List<ThermalPoint> findByForestryId(@Param("forestryId") Long forestryId);
//}