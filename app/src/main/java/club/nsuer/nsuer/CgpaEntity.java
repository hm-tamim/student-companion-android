package club.nsuer.nsuer;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;


@Entity
public class CgpaEntity {


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "credit")
    private String credit;

    @ColumnInfo(name = "grade")
    private String grade;


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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }



}



@Dao
interface CgpaDao {

    @RawQuery
    List<CgpaEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM cgpaEntity")
    List<CgpaEntity> getAll();

    @Query("DELETE FROM cgpaEntity WHERE course LIKE :name")
    void deleteByCourseName(String name);

    @Query("SELECT course FROM cgpaEntity LIMIT 1")
    String checkEmpty();


    @Query("SELECT COUNT(uid) from cgpaEntity")
    int countFaculties();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CgpaEntity... cgpaEntities);

    @Delete
    void delete(CgpaEntity cgpaEntity);

    @Query("DELETE FROM cgpaEntity")
    public void nukeTable();
}




@Database(entities = {CgpaEntity.class}, version = 1, exportSchema = false)
abstract class CgpaDatabase extends RoomDatabase {


    private static CgpaDatabase INSTANCE;

    public abstract CgpaDao cgpaDao();

    public static CgpaDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CgpaDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
