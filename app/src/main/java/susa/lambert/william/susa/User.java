package susa.lambert.william.susa;

public class User {
    public String name;
    public String email;
    public String college;
    public String pImage;
    public long timeJoined;

    public User(){

    }

    public User(String name, String email, String college, String pImage, long timeJoined) {
        this.name = name;
        this.email = email;
        this.pImage = pImage;
        this.college = college;
        this.timeJoined = timeJoined;
    }

    public long getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(long timeJoined) {
        this.timeJoined = timeJoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
