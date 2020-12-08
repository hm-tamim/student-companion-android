package club.nsuer.nsuer;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {NsuNoticesEntity.class}, version = 1, exportSchema = false)
public abstract class NsuNoticesDatabase extends RoomDatabase {

    private static NsuNoticesDatabase INSTANCE;

    public abstract NsuNoticesDao nsuNoticesDao();

    public static NsuNoticesDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), NsuNoticesDatabase.class).allowMainThreadQueries().build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}