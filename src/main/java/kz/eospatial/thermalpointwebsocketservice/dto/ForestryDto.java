package kz.eospatial.thermalpointwebsocketservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.eospatial.thermalpointwebsocketservice.model.GeoCoordinate;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForestryDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Region cannot be blank")
    private String region;

    @NotBlank(message = "Map style URL cannot be blank")
    private String mapStyleUrl;

    @NotNull(message = "Boundaries cannot be null")
    private List<GeoCoordinate> boundaries;

    @NotNull(message = "Center cannot be null")
    private GeoCoordinate center;

    private String mapBoxToken;
    private String token;

    @NotNull(message = "Token expiration date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tokenExpirationDate;

    public ForestryDto(Long id, String name, String region, String mapStyleUrl, List<GeoCoordinate> boundaries,
                       GeoCoordinate center, String mapBoxToken, String token, LocalDate tokenExpirationDate) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.mapStyleUrl = mapStyleUrl;
        this.boundaries = boundaries;
        this.center = center;
        this.mapBoxToken = mapBoxToken;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public ForestryDto(String name, String region, String mapStyleUrl, List<GeoCoordinate> boundaries,
                       GeoCoordinate center, String mapBoxToken, String token, LocalDate tokenExpirationDate) {
        this.name = name;
        this.region = region;
        this.mapStyleUrl = mapStyleUrl;
        this.boundaries = boundaries;
        this.center = center;
        this.mapBoxToken = mapBoxToken;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public ForestryDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Name cannot be blank") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name cannot be blank") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String name) {
        this.name = name;
    }

    public @NotNull(message = "Region cannot be blank") String getRegion() {
        return region;
    }

    public void setRegion(@NotNull(message = "Region cannot be blank") String region) {
        this.region = region;
    }

    public @NotBlank(message = "Map style URL cannot be blank") String getMapStyleUrl() {
        return mapStyleUrl;
    }

    public void setMapStyleUrl(@NotBlank(message = "Map style URL cannot be blank") String mapStyleUrl) {
        this.mapStyleUrl = mapStyleUrl;
    }

    public @NotNull(message = "Boundaries cannot be null") List<GeoCoordinate> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(@NotNull(message = "Boundaries cannot be null") List<GeoCoordinate> boundaries) {
        this.boundaries = boundaries;
    }

    public @NotNull(message = "Center cannot be null") GeoCoordinate getCenter() {
        return center;
    }

    public void setCenter(@NotNull(message = "Center cannot be null") GeoCoordinate center) {
        this.center = center;
    }

    public String getMapBoxToken() {
        return mapBoxToken;
    }

    public void setMapBoxToken(String mapBoxToken) {
        this.mapBoxToken = mapBoxToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public @NotNull(message = "Token expiration date cannot be null") LocalDate getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(@NotNull(message = "Token expiration date cannot be null") LocalDate tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }
}

