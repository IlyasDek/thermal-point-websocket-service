package kz.eospatial.thermalpointwebsocketservice.geo;

public class GeoJsonGeometry {
    private String type;
    private double[] coordinates;

    // getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}