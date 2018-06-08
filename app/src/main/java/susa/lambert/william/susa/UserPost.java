package susa.lambert.william.susa;

public class UserPost {

    public int price;
    public int likes;
    public String address;
    public String status;
    public String location;
    public String availability;
    public String desc,userID,timeOf;
    public String post_image;
    public String post_image2;
    public String post_image3;
    public String email;
    public String title;
    public String postID;


    public UserPost(int price, String address, String location, String desc,
                    String userID, String timeOf, String post_image, String post_image2,
                    String post_image3,String email, String availability, String title, String postID) {
        this.price = price;
        this.likes = 0;
        this.status = "VACANT";
        this.address = address;
        this.location = location;
        this.availability = availability;
        this.desc = desc;
        this.userID = userID;
        this.timeOf = timeOf;
        this.post_image = post_image;
        this.post_image2 = post_image2;
        this.post_image3 = post_image3;
        this.email = email;
        this.title = title;
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserPost(){


    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(String timeOf) {
        this.timeOf = timeOf;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_image2() {
        return post_image2;
    }

    public void setPost_image2(String post_image2) {
        this.post_image2 = post_image2;
    }

    public String getPost_image3() {
        return post_image3;
    }

    public void setPost_image3(String post_image3) {
        this.post_image3 = post_image3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
