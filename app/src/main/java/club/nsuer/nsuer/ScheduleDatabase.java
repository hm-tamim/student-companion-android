package club.nsuer.nsuer;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ScheduleEntity.class}, version = 3, exportSchema = false)
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