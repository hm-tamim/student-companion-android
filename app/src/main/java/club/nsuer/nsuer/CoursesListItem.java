package club.nsuer.nsuer;

public class CoursesListItem {

    private String name;
    private String start;
    private String end;
    private String room;
    private String faculty;
    private String day;
    private String section;

    public CoursesListItem(String name, String section, String start, String end, String room, String faculty, String day) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.room = room;
        this.faculty = faculty;
        this.day = day;
        this.section = section;
    }


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}