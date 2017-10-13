package com.wks.nearby.data.places;

import com.google.gson.annotations.SerializedName;

/**
 * Created by waqqassheikh on 13/10/2017.
 */

public class Geometry {

    public static class Location {
        @SerializedName("lat")
        private final double latitude;
        @SerializedName("lon")
        private final double longitude;

        private Location(){
            this.latitude = 0;
            this.longitude = 0;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    @SerializedName("location")
    private final Location location;

    private Geometry(){
        this.location = null;
    }

    public Location getLocation() {
        return location;
    }

    public Double getLatitude(){
        return location == null? null : location.getLatitude();
    }

    public Double getLongitude(){
        return location == null? null : location.getLongitude();
    }
}
