package club.nsuer.nsuer;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CoursesEntity.class}, version = 1, exportSchema = false)
public abstract class CoursesDatabase extends RoomDatabase {

    private static CoursesDatabase INSTANCE;

    public abstract CoursesDao coursesDao();

    public static CoursesDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CoursesDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}