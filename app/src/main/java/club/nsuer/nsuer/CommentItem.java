package club.nsuer.nsuer;

public class CommentItem {

    private String name;
    private String gender;
    private String picture;


    private int commentID;
    private String memID;
    private String text;
    private String time;


    public String getMemID() {
        return memID;
    }

    public CommentItem(String name, String gender, String picture, int commentID, String memID, String text, String time) {
        this.name = name;
        this.gender = gender;
        this.picture = picture;
        this.commentID = commentID;
        this.memID = memID;
        this.text = text;
        this.time = time;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public int getCommentID() {

        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
