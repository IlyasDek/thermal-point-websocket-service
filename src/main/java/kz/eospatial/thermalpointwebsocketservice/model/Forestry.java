//package kz.eospatial.thermalpointwebsocketservice.model;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Entity
//public class Forestry {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String region;
//    private String mapStyleUrl;
//    private String mapBoxToken;
//    private String token;
//    private LocalDate tokenExpirationDate;
//
//    @OneToMany(mappedBy = "forestry")
//    private List<FireForestRelation> fireForestRelations;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getRegion() {
//        return region;
//    }
//
//    public void setRegion(String region) {
//        this.region = region;
//    }
//
//    public String getMapStyleUrl() {
//        return mapStyleUrl;
//    }
//
//    public void setMapStyleUrl(String mapStyleUrl) {
//        this.mapStyleUrl = mapStyleUrl;
//    }
//
//    public String getMapBoxToken() {
//        return mapBoxToken;
//    }
//
//    public void setMapBoxToken(String mapBoxToken) {
//        this.mapBoxToken = mapBoxToken;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public LocalDate getTokenExpirationDate() {
//        return tokenExpirationDate;
//    }
//
//    public void setTokenExpirationDate(LocalDate tokenExpirationDate) {
//        this.tokenExpirationDate = tokenExpirationDate;
//    }
//
//    public List<FireForestRelation> getFireForestRelations() {
//        return fireForestRelations;
//    }
//
//    public void setFireForestRelations(List<FireForestRelation> fireForestRelations) {
//        this.fireForestRelations = fireForestRelations;
//    }
//
//    // Getters and setters
//}