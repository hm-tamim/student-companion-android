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
public interface NsuNoticesDao {

    @RawQuery
    List<NsuNoticesEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM nsuNoticesEntity")
    List<NsuNoticesEntity> getAll();

    @Query("SELECT * FROM nsuNoticesEntity WHERE title LIKE :name LIMIT 1")
    NsuNoticesEntity findByTitle(String name);

    @Query("DELETE FROM nsuNoticesEntity WHERE title LIKE :name")
    void deleteByTitle(String name);


    @Query("SELECT title FROM nsuNoticesEntity LIMIT 1")
    String checkEmpty();


    @Query("SELECT COUNT(uid) from nsuNoticesEntity")
    int countFaculties();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NsuNoticesEntity... nsuNoticesEntities);

    @Delete
    void delete(NsuNoticesEntity nsuNoticesEntity);

    @Query("DELETE FROM nsuNoticesEntity")
    public void nukeTable();
}


