//package kz.eospatial.thermalpointwebsocketservice.model;
//
//import jakarta.persistence.*;
//
//@Entity
//public class FireForestRelation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "fire_id", nullable = false)
//    private ThermalPoint fire;
//
//    @ManyToOne
//    @JoinColumn(name = "forestry_id", nullable = false)
//    private Forestry forestry;
//
//    // Getters and setters
//}