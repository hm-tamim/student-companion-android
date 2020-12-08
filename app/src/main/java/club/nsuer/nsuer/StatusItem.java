package club.nsuer.nsuer;

public class StatusItem {

    private String name;
    private String course;
    private String section;
    private String time;
    private String memID;
    private String post;
    private String gender;
    private String picture;
    private int id;
    private boolean liked;
    private int likes;
    private int comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public StatusItem(int id, String name, String course, String picture, String time, String gender, String memID, String post, int likes, int comments, boolean liked) {
        this.id = id;

        this.name = name;
        this.course = course;
        this.time = time;
        this.gender = gender;

        this.memID = memID;
        this.post = post;
        this.picture = picture;
        this.likes = likes;
        this.comments = comments;
        this.liked = liked;

    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
