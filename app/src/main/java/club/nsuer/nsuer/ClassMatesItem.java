package club.nsuer.nsuer;

public class ClassMatesItem {

    private String name;
    private String course;
    private String section;
    private String memID;
    private String image;
    private String gender;
    private String email;




    public ClassMatesItem(String name, String course, String memID, String image, String gender, String email) {
        this.name = name;
        this.course = course;
        this.memID = memID;
        this.image = image;
        this.gender = gender;
        this.email = email;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
