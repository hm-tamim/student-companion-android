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
public interface FacultiesDao {

    @RawQuery
    List<FacultiesEntity> getUserViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM facultiesEntity")
    List<FacultiesEntity> getAll();

    @Query("SELECT * FROM facultiesEntity WHERE initial LIKE :name LIMIT 1")
    FacultiesEntity findByInitial(String name);

    @Query("DELETE FROM facultiesEntity WHERE initial LIKE :name")
    void deleteByInitial(String name);

    @Query("SELECT course FROM facultiesEntity where initial LIKE :initial")
    String getCourseByFaculty(String initial);

    @Query("SELECT section FROM facultiesEntity where course LIKE :course")
    String getSectionByCourse(String course);

    @Query("DELETE FROM facultiesEntity WHERE course LIKE :name")
    void deleteByCourse(String name);

    @Query("SELECT COUNT(uid) from facultiesEntity")
    int countFaculties();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FacultiesEntity... facultiesEntities);

    @Delete
    void delete(FacultiesEntity facultiesEntity);

    @Query("DELETE FROM facultiesEntity")
    public void nukeTable();
}


