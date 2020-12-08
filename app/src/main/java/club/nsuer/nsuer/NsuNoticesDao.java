package club.nsuer.nsuer;


import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

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


