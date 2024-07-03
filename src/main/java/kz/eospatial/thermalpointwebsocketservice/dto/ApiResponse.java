package kz.eospatial.thermalpointwebsocketservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ApiResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("forestry")
    private ForestryDto forestry;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ForestryDto getForestry() {
        return forestry;
    }

    public void setForestry(ForestryDto forestry) {
        this.forestry = forestry;
    }
}