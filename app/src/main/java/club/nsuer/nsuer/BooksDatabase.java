package club.nsuer.nsuer;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {BooksEntity.class}, version = 1, exportSchema = false)
public abstract class BooksDatabase extends RoomDatabase {

    private static BooksDatabase INSTANCE;

    public abstract BooksDao booksDao();

    public static BooksDatabase getInMemoryDatabase(Context context) {

        if (INSTANCE == null)
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), BooksDatabase.class).allowMainThreadQueries().build();

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}