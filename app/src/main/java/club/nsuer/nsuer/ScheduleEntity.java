package club.nsuer.nsuer;


import android.arch.persistence.room.Entity;

@Entity
public class ScheduleEntity {

    private int id;
    private String title;
    private String type;
    private String extraNote;
    private int date;
    private int reminderDate;
    private int color;
    private boolean doReminder;
    private boolean passed;


    public ScheduleEntity(int id, String title, String type, String extraNote, int date, int color, boolean doReminder, boolean passed) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.extraNote = extraNote;
        this.date = date;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(int reminderDate) {
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
