package club.nsuer.nsuer;


import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

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


