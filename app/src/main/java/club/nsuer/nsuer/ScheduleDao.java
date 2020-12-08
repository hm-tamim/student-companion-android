package club.nsuer.nsuer;


import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {

    @RawQuery
    List<ScheduleEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM scheduleEntity ORDER BY date ASC")
    List<ScheduleEntity> getAll();

    @Query("SELECT * FROM scheduleEntity WHERE title LIKE :name LIMIT 1")
    ScheduleEntity findByTitle(String name);

    @Query("SELECT * FROM scheduleEntity WHERE id = :id LIMIT 1")
    ScheduleEntity findById(int id);

    @Query("DELETE FROM scheduleEntity WHERE title LIKE :name")
    void deleteByTitle(String name);

    @Query("DELETE FROM scheduleEntity WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT title FROM scheduleEntity LIMIT 1")
    String isEmpty();


    @Query("SELECT COUNT(id) from scheduleEntity")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(ScheduleEntity... scheduleEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(ScheduleEntity... scheduleEntities);

    @Delete
    void delete(ScheduleEntity scheduleEntity);

    @Query("DELETE FROM scheduleEntity")
    public void nukeTable();
}


