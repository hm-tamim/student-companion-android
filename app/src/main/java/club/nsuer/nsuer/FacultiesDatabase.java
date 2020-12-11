package club.nsuer.nsuer;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FacultiesEntity.class}, version = 1, exportSchema = false)
public abstract class FacultiesDatabase extends RoomDatabase {

    private static FacultiesDatabase INSTANCE;

    public abstract FacultiesDao facultiesDao();

    public static FacultiesDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), FacultiesDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}