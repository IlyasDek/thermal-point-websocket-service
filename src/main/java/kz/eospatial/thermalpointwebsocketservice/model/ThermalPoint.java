package kz.eospatial.thermalpointwebsocketservice.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "fires")
public class ThermalPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double latitude;
    private double longitude;
    private double brightness;
    private double scan;
    private double track;
    private LocalDate acqDate;
    private LocalTime acqTime;
    private LocalTime localTime;
    private String satellite;
    private String confidence;
    private String version;
    private double brightT31;
    private double frp;
    private String daynight;
    private Long forestryId;

    public ThermalPoint(Long id, double latitude, double longitude, double brightness, double scan, double track, LocalDate acqDate, LocalTime acqTime, LocalTime localTime, String satellite, String confidence, String version, double brightT31, double frp, String daynight, Long forestryId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.brightness = brightness;
        this.scan = scan;
        this.track = track;
        this.acqDate = acqDate;
        this.acqTime = acqTime;
        this.localTime = localTime;
        this.satellite = satellite;
        this.confidence = confidence;
        this.version = version;
        this.brightT31 = brightT31;
        this.frp = frp;
        this.daynight = daynight;
        this.forestryId = forestryId;
    }

    public ThermalPoint() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getScan() {
        return scan;
    }

    public void setScan(double scan) {
        this.scan = scan;
    }

    public double getTrack() {
        return track;
    }

    public void setTrack(double track) {
        this.track = track;
    }

    public LocalDate getAcqDate() {
        return acqDate;
    }

    public void setAcqDate(LocalDate acqDate) {
        this.acqDate = acqDate;
    }

    public LocalTime getAcqTime() {
        return acqTime;
    }

    public void setAcqTime(LocalTime acqTime) {
        this.acqTime = acqTime;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public String getSatellite() {
        return satellite;
    }

    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getBrightT31() {
        return brightT31;
    }

    public void setBrightT31(double brightT31) {
        this.brightT31 = brightT31;
    }

    public double getFrp() {
        return frp;
    }

    public void setFrp(double frp) {
        this.frp = frp;
    }

    public String getDaynight() {
        return daynight;
    }

    public void setDaynight(String daynight) {
        this.daynight = daynight;
    }

    public Long getForestryId() {
        return forestryId;
    }

    public void setForestryId(Long forestryId) {
        this.forestryId = forestryId;
    }
}

