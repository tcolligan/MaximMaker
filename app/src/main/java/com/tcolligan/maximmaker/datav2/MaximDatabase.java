package com.tcolligan.maximmaker.datav2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Abstract class that will be used to create out database.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

@Database(entities = {Maxim.class}, version = 1)
public abstract class MaximDatabase extends RoomDatabase
{
    private static MaximDatabase INSTANCE;

    public static MaximDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (MaximDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MaximDatabase.class, "maxim_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MaximDao maximDao();
}
