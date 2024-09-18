package kz.eospatial.thermalpointwebsocketservice.geo;

import java.util.List;

public class GeoJsonFeatureCollection {
    private String type;
    private List<GeoJsonFeature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GeoJsonFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<GeoJsonFeature> features) {
        this.features = features;
    }
}