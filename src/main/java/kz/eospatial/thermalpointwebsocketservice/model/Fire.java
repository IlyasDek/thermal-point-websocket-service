//package kz.eospatial.thermalpointwebsocketservice.model;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Entity
//@Table(name = "fires")
//public class Fire {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private double latitude;
//    private double longitude;
//    private double brightness;
//    private double scan;
//    private double track;
//
//    @Column(name = "acq_date")
//    private LocalDate acquisitionDate;  // Изменено логическое имя столбца
//
//    @Column(name = "acq_time")
//    private LocalTime acquisitionTime;
//
//    @Column(name = "local_time")
//    private LocalTime localTime;
//
//    private String satellite;
//    private String confidence;
//    private String version;
//
//    @Column(name = "bright_t31")
//    private double brightT31;
//
//    private double frp;
//    private String daynight;
//
//    @Column(name = "forestry_id")
//    private Long forestryId;
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
//    public double getBrightness() {
//        return brightness;
//    }
//
//    public void setBrightness(double brightness) {
//        this.brightness = brightness;
//    }
//
//    public double getScan() {
//        return scan;
//    }
//
//    public void setScan(double scan) {
//        this.scan = scan;
//    }
//
//    public double getTrack() {
//        return track;
//    }
//
//    public void setTrack(double track) {
//        this.track = track;
//    }
//
//    public LocalDate getAcquisitionDate() {
//        return acquisitionDate;
//    }
//
//    public void setAcquisitionDate(LocalDate acquisitionDate) {
//        this.acquisitionDate = acquisitionDate;
//    }
//
//    public LocalTime getAcquisitionTime() {
//        return acquisitionTime;
//    }
//
//    public void setAcquisitionTime(LocalTime acquisitionTime) {
//        this.acquisitionTime = acquisitionTime;
//    }
//
//    public LocalTime getLocalTime() {
//        return localTime;
//    }
//
//    public void setLocalTime(LocalTime localTime) {
//        this.localTime = localTime;
//    }
//
//    public String getSatellite() {
//        return satellite;
//    }
//
//    public void setSatellite(String satellite) {
//        this.satellite = satellite;
//    }
//
//    public String getConfidence() {
//        return confidence;
//    }
//
//    public void setConfidence(String confidence) {
//        this.confidence = confidence;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public double getBrightT31() {
//        return brightT31;
//    }
//
//    public void setBrightT31(double brightT31) {
//        this.brightT31 = brightT31;
//    }
//
//    public double getFrp() {
//        return frp;
//    }
//
//    public void setFrp(double frp) {
//        this.frp = frp;
//    }
//
//    public String getDaynight() {
//        return daynight;
//    }
//
//    public void setDaynight(String daynight) {
//        this.daynight = daynight;
//    }
//
//    public Long getForestryId() {
//        return forestryId;
//    }
//
//    public void setForestryId(Long forestryId) {
//        this.forestryId = forestryId;
//    }
//}