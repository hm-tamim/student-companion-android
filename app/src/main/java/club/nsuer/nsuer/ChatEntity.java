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
public class ChatEntity {


//    @PrimaryKey(autoGenerate = true)
//    private int uid;

    @PrimaryKey(autoGenerate = false)
    private int msg_id;


    @ColumnInfo(name = "user_from")
    private String user_from;

    @ColumnInfo(name = "user_to")
    private String user_to;

    
    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time")
    private long time;


    @ColumnInfo(name = "from_json")
    private int from_json;




    public int getFrom_json() {
        return from_json;
    }

    public void setFrom_json(int from_json) {
        this.from_json = from_json;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getUser_from() {
        return user_from;
    }

    public void setUser_from(String user_from) {
        this.user_from = user_from;
    }

    public String getUser_to() {
        return user_to;
    }

    public void setUser_to(String user_to) {
        this.user_to = user_to;
    }

}



@Dao
interface ChatDao {

    @RawQuery
    List<ChatEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM chatEntity")
    List<ChatEntity> getAll();



    @Query("SELECT * FROM chatEntity ORDER BY time ASC")
    List<ChatEntity> getAllByTime();


    @Query("SELECT * FROM chatEntity ORDER BY time ASC LIMIT 40")
    List<ChatEntity> getAllByLimit();

    @Query("SELECT * FROM chatEntity WHERE msg_id > :after ORDER BY time ASC")
    List<ChatEntity> getAfterStart(int after);




    @Query("SELECT msg_id FROM chatEntity LIMIT 1")
    int checkEmpty();


    @Query("SELECT msg_id FROM chatEntity ORDER BY msg_id DESC LIMIT 1")
    int getLastID();

    @Query("SELECT msg_id FROM chatEntity WHERE from_json = 1 ORDER BY msg_id DESC LIMIT 1")
    int getLastIDJson();



    @Query("SELECT COUNT(msg_id) from chatEntity")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ChatEntity... chatEntites);

    @Delete
    void delete(ChatEntity chatEntity);

    @Query("DELETE FROM chatEntity")
    public void nukeTable();
}




@Database(entities = {ChatEntity.class}, version = 2, exportSchema = false)
abstract class ChatDatabase extends RoomDatabase {


    private static ChatDatabase INSTANCE;

    public abstract ChatDao chatDao();

    public static ChatDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), ChatDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
