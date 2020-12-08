package club.nsuer.nsuer;

public class ClassesItem {

    private String time;
    private String event;
    private String color;

    public ClassesItem(String time, String event, String color) {
        this.time = time;
        this.event = event;
        this.color = color;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
