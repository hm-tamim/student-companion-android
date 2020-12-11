package club.nsuer.nsuer;


import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

import java.util.List;

@Dao
public interface BooksDao {

    @RawQuery
    List<BooksEntity> getUserViaQuery(SupportSQLiteQuery query);

    @Query("SELECT * FROM BooksEntity")
    List<BooksEntity> getAll();

    @Query("SELECT * FROM booksEntity WHERE course LIKE :name LIMIT 1")
    BooksEntity findByName(String name);

    @Query("DELETE FROM booksEntity WHERE course LIKE :name")
    void deleteByCourseName(String name);


    @Query("SELECT COUNT(uid) from booksEntity")
    int countBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(BooksEntity... booksEntities);

    @Delete
    void delete(BooksEntity booksEntity);

    @Query("DELETE FROM booksEntity")
    public void nukeTable();
}


