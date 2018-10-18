package club.nsuer.nsuer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ScheduleEntity.class}, version = 1, exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static ScheduleDatabase INSTANCE;

    public abstract ScheduleDao scheduleDao();

    public static ScheduleDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), ScheduleDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}