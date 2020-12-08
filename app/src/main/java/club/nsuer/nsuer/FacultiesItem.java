package club.nsuer.nsuer;

public class FacultiesItem {

    private String name;
    private String rank;
    private String image;
    private String initial;
    private String course;
    private String section;
    private String email;
    private String phone;
    private String ext;
    private String department;
    private String office;
    private String url;

    public FacultiesItem(String name, String rank, String image, String initial, String course, String section, String email, String phone, String ext, String department, String office, String url) {
        this.name = name;
        this.rank = rank;
        this.image = image;
        this.initial = initial;
        this.course = course;
        this.section = section;
        this.email = email;
        this.phone = phone;
        this.ext = ext;
        this.department = department;
        this.office = office;
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {

        return name;
    }

    public String getRank() {
        return rank;
    }

    public String getImage() {
        return image;
    }

    public String getInitial() {
        return initial;
    }

    public String getCourse() {
        return course;
    }

    public String getSection() {
        return section;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getOffice() {
        return office;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }


    public FacultiesItem() {

    }
}
