package club.nsuer.nsuer;

public class ScheduleItem {

    private int id;
    private String title;
    private String type;
    private String extraNote;
    private long date;
    private long reminderDate;
    private int color;
    private boolean doReminder;
    private boolean passed;


    public ScheduleItem(int id, String title, String type, String extraNote, long date, long reminderDate, int color, boolean doReminder, boolean passed) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.extraNote = extraNote;
        this.date = date;
        this.reminderDate = reminderDate;
        this.color = color;
        this.doReminder = doReminder;
        this.passed = passed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtraNote() {
        return extraNote;
    }

    public void setExtraNote(String extraNote) {
        this.extraNote = extraNote;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDoReminder() {
        return doReminder;
    }

    public void setDoReminder(boolean doReminder) {
        this.doReminder = doReminder;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }


}
