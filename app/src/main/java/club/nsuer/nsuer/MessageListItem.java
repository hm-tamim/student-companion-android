package club.nsuer.nsuer;

public class MessageListItem {


    private int id;
    private String userTo;
    private String userFrom;
    private String message;
    private long time;
    private String name;
    private String gender;
    private String picture;
    private int seen;

    public MessageListItem(int id, String userTo, String userFrom, String message, long time, int seen, String name, String gender, String picture) {
        this.id = id;
        this.userTo = userTo;
        this.userFrom = userFrom;
        this.message = message;
        this.time = time;
        this.seen = seen;
        this.name = name;
        this.gender = gender;
        this.picture = picture;
    }

    public int getSeen() {
        return seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


}
