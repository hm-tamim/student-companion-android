package club.nsuer.nsuer;

public class NotificationItem {

    private String title;
    private String body;
    private String type;
    private String typeExtra;
    private String typeExtra2;
    private String senderMemID;
    private long time;
    private boolean seen;

    public NotificationItem(String title, String body, String type, String typeExtra, String typeExtra2, String senderMemID, long time, boolean seen) {
        this.title = title;
        this.body = body;
        this.type = type;
        this.typeExtra = typeExtra;
        this.typeExtra2 = typeExtra2;
        this.senderMemID = senderMemID;
        this.time = time;
        this.seen = seen;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeExtra() {
        return typeExtra;
    }

    public void setTypeExtra(String typeExtra) {
        this.typeExtra = typeExtra;
    }

    public String getTypeExtra2() {
        return typeExtra2;
    }

    public void setTypeExtra2(String typeExtra2) {
        this.typeExtra2 = typeExtra2;
    }

    public String getSenderMemID() {
        return senderMemID;
    }

    public void setSenderMemID(String senderMemID) {
        this.senderMemID = senderMemID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
