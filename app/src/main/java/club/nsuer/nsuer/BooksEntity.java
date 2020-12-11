package club.nsuer.nsuer;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BooksEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "books")
    private String books;

    @Override
    public String toString() {
        return "BooksEntity{" +
                "uid=" + uid +
                ", course='" + course + '\'' +
                ", books='" + books + '\'' +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }
}