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
public class MessageEntity {


//    @PrimaryKey(autoGenerate = true)
//    private int uid;


    @PrimaryKey(autoGenerate = false)
    private int chat_id;


    @ColumnInfo(name = "msg_id")
    private int msg_id;


    @ColumnInfo(name = "user_from")
    private String user_from;

    @ColumnInfo(name = "user_to")
    private String user_to;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "user_gender")
    private String user_gender;

    @ColumnInfo(name = "user_picture")
    private String user_picture;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time")
    private long time;


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

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }
}


@Dao
interface MessageDao {

    @RawQuery
    List<MessageEntity> getViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM messageEntity")
    List<MessageEntity> getAll();


    @Query("SELECT * FROM messageEntity ORDER BY time DESC")
    List<MessageEntity> getAllByTime();

    @Query("SELECT chat_id FROM messageEntity LIMIT 1")
    int checkEmpty();


    @Query("SELECT COUNT(chat_id) from messageEntity")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MessageEntity... messageEntites);

    @Delete
    void delete(MessageEntity messageEntity);

    @Query("DELETE FROM messageEntity")
    public void nukeTable();
}


@Database(entities = {MessageEntity.class}, version = 1, exportSchema = false)
abstract class MessageDatabase extends RoomDatabase {


    private static MessageDatabase INSTANCE;

    public abstract MessageDao messageDao();

    public static MessageDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), MessageDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
