package susa.lambert.william.susa;

public class LikedBy {

    public String userID;
    public String postID;

    public LikedBy(String userID, String postID) {
        this.userID = userID;
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
