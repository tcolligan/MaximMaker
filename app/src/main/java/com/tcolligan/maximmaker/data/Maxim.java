package com.tcolligan.maximmaker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * A model that represents a specific Maxim or quote.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

@Entity(tableName = "maxims")
public class Maxim
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    @PrimaryKey(autoGenerate = true)
    private long id;

    private @NonNull String message;

    private @Nullable String author;

    private @Nullable String tags;

    @ColumnInfo(name = "creation_timestamp")
    private long creationTimestampMilliseconds;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public Maxim(@NonNull String message,
                 @Nullable String author,
                 @Nullable String tags,
                 long creationTimestampMilliseconds)
    {
        this.message = message;
        this.author = author;
        this.tags = tags;
        this.creationTimestampMilliseconds = creationTimestampMilliseconds;
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @NonNull
    public String getMessage()
    {
        return message;
    }

    public void setMessage(@NonNull String message)
    {
        this.message = message;
    }

    @Nullable
    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(@Nullable String author)
    {
        this.author = author;
    }

    @Nullable
    public String getTags()
    {
        return tags;
    }

    public void setTags(@Nullable String tags)
    {
        this.tags = tags;
    }

    public long getCreationTimestampMilliseconds()
    {
        return creationTimestampMilliseconds;
    }

    public void setCreationTimestampMilliseconds(long creationTimestampMilliseconds)
    {
        this.creationTimestampMilliseconds = creationTimestampMilliseconds;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Maxim)
        {
            Maxim maxim = (Maxim) object;
            return id == maxim.id;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US,
                "ID: %d \nMessage: %s \nAuthor: %s \nTags: %s \nCreation Timestamp: %d",
                id, message, author, tags, creationTimestampMilliseconds);
    }
}
