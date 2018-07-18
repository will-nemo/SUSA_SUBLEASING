package susa.lambert.william.susa;

import com.google.gson.annotations.SerializedName;

public class School {

    @SerializedName("name")
    private String name;

    @SerializedName("state")
    private String state;

    @SerializedName("city")
    private String city;

    public School(String name, String state, String city) {
        this.name = name;
        this.state = state;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
