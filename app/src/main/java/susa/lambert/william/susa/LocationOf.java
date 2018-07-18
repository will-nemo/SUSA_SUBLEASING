package susa.lambert.william.susa;

import com.google.gson.annotations.SerializedName;

public class LocationOf {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public LocationOf(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
