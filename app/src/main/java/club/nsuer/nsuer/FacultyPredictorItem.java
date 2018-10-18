package club.nsuer.nsuer;

public class FacultyPredictorItem {

    String faculty;
    String time;
    String section;
    String previousSections;

    public FacultyPredictorItem(String faculty, String section, String time, String previousSections) {
        this.faculty = faculty;
        this.time = time;
        this.section = section;
        this.previousSections = previousSections;
    }

    public String getFaculty() {

        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPreviousSections() {
        return previousSections;
    }

    public void setPreviousSections(String previousSections) {
        this.previousSections = previousSections;
    }
}
