package com.tcolligan.maximmaker.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * A database access object to pull Maxims out from the Room database.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

@Dao
public interface MaximDao
{
    @Query("SELECT * FROM maxims")
    List<Maxim> getAll();

    @Insert
    long insert(Maxim maxim);

    @Update
    void update(Maxim maxim);

    @Delete
    void delete(Maxim maxim);

    @Query("SELECT * FROM maxims WHERE id = :id LIMIT 1")
    Maxim findById(long id);
}
