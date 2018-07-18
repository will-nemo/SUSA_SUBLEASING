package susa.lambert.william.susa;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("school")
    private School school;

    @SerializedName("location")
    private LocationOf location;

    public School getSchool() {
        return school;
    }

    public LocationOf getLocation() {
        return location;
    }

    public Results() {

    }
}
