package club.nsuer.nsuer;

public class AcademicCalendarItem {

    private String date;
    private String month;
    private String day;
    private String event;
    private String color;
    private boolean isPassed;

    public AcademicCalendarItem() {

    }

    public AcademicCalendarItem(String date, String month, String day, String event, String color, boolean isPassed) {
        this.date = date;
        this.month = month;
        this.day = day;
        this.event = event;
        this.color = color;
        this.isPassed = isPassed;
    }

    public boolean isPassed() {

        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
