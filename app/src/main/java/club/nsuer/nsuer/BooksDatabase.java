package club.nsuer.nsuer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
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