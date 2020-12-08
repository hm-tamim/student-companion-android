package club.nsuer.nsuer;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ScheduleEntity {


    @PrimaryKey(autoGenerate = true)
    private int key;

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "extraNote")
    private String extraNote;


    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "reminderDate")
    private long reminderDate;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "doReminder")
    private boolean doReminder;


    public ScheduleEntity(int id, String title, String type, String extraNote, long date, long reminderDate, int color, boolean doReminder) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.extraNote = extraNote;
        this.date = date;
        this.reminderDate = reminderDate;
        this.color = color;
        this.doReminder = doReminder;
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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


}
