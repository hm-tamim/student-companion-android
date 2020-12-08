package club.nsuer.nsuer;

public class CgpaRecyclerItem {

    private String course;
    private String credit;
    private String grade;

    public CgpaRecyclerItem(String course, String credit, String grade) {
        this.course = course;
        this.credit = credit;
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCredit() {
        return credit;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }


}
