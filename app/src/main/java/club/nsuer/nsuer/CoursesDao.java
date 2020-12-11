package club.nsuer.nsuer;


import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

import java.util.List;

@Dao
public interface CoursesDao {


    @RawQuery
    List<CoursesEntity> getUserViaQuery(SupportSQLiteQuery query);


    @Query("SELECT * FROM coursesEntity")
    List<CoursesEntity> getAll();

    @Query("SELECT * FROM coursesEntity ORDER BY startTime")
    List<CoursesEntity> getAllByTime();

    @Query("SELECT course FROM coursesEntity where faculty LIKE :initial")
    String getCourseByFaculty(String initial);

    @Query("SELECT section FROM coursesEntity where faculty LIKE :initial")
    String getSectionByFaculty(String initial);

    @Query("SELECT section FROM coursesEntity where course LIKE :course")
    String getSectionByCourse(String course);

    @Query("SELECT * FROM coursesEntity WHERE uid IN (:userIds)")
    List<CoursesEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM coursesEntity WHERE course LIKE :name LIMIT 1")
    CoursesEntity findByName(String name);

    @Query("DELETE FROM coursesEntity WHERE course LIKE :name")
    void deleteByCourseName(String name);

    @Query("SELECT COUNT(uid) from coursesEntity")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CoursesEntity... coursesEntities);

    @Delete
    void delete(CoursesEntity coursesEntity);

    @Query("DELETE FROM CoursesEntity")
    public void nukeTable();
}


