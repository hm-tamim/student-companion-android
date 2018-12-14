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
public class NotificationEntity {

//    @PrimaryKey(autoGenerate = true)
//    private int uid;

    @PrimaryKey(autoGenerate = true)
    private int msg_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "senderMemID")
    private String senderMemID;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "typeExtra")
    private String typeExtra;

    @ColumnInfo(name = "typeExtra2")
    private String typeExtra2;

    @ColumnInfo(name = "time")
    private long time;


    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @ColumnInfo(name = "seen")
    private boolean seen;


    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSenderMemID() {
        return senderMemID;
    }

    public void setSenderMemID(String senderMemID) {
        this.senderMemID = senderMemID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeExtra() {
        return typeExtra;
    }

    public void setTypeExtra(String typeExtra) {
        this.typeExtra = typeExtra;
    }

    public String getTypeExtra2() {
        return typeExtra2;
    }

    public void setTypeExtra2(String typeExtra2) {
        this.typeExtra2 = typeExtra2;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}



@Dao
interface NotificationDao {

    @RawQuery
    List<NotificationEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM NotificationEntity")
    List<NotificationEntity> getAll();

    @Query("SELECT * FROM NotificationEntity ORDER BY time ASC")
    List<NotificationEntity> getAllByTime();

    @Query("SELECT * FROM NotificationEntity ORDER BY time DESC LIMIT 40")
    List<NotificationEntity> getAllByLimit();

    @Query("SELECT * FROM NotificationEntity WHERE msg_id > :after ORDER BY time ASC")
    List<NotificationEntity> getAfterStart(int after);

    @Query("SELECT msg_id FROM NotificationEntity LIMIT 1")
    int checkEmpty();


    @Query("DELETE FROM NotificationEntity WHERE time LIKE :idd")
    int deleteByTime(long idd);

    @Query("UPDATE NotificationEntity SET seen = 1  WHERE msg_id = :idd")
    int setSeen(int idd);


    @Query("SELECT msg_id FROM NotificationEntity ORDER BY msg_id DESC LIMIT 1")
    int getLastID();

    @Query("SELECT COUNT(msg_id) from NotificationEntity")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NotificationEntity... notificationEntities);

    @Delete
    void delete(NotificationEntity NotificationEntity);

    @Query("DELETE FROM NotificationEntity")
    public void nukeTable();
}




@Database(entities = {NotificationEntity.class}, version = 2, exportSchema = false)
abstract class NotificationDatabase extends RoomDatabase {


    private static NotificationDatabase INSTANCE;

    public abstract NotificationDao notificationDao();

    public static NotificationDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), NotificationDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

