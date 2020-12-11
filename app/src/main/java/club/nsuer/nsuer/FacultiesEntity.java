package club.nsuer.nsuer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FacultiesEntity {


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "rank")
    private String rank;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "initial")
    private String initial;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "section")
    private String section;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "department")
    private String department;

    @ColumnInfo(name = "office")
    private String office;


    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "ext")
    private String ext;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "FacultiesItem{" +
                "name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", image='" + image + '\'' +
                ", initial='" + initial + '\'' +
                ", course='" + course + '\'' +
                ", section='" + section + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", office='" + office + '\'' +
                '}';
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

}
