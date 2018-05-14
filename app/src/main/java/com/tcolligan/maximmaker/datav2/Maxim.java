package com.tcolligan.maximmaker.datav2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
    private int id;

    private String message;

    private String author;

    private String tags;

    @ColumnInfo(name = "creation_timestamp")
    private long creationTimestampMilliseconds;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public Maxim(@NonNull String message,
                 String author,
                 String tags,
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(@NonNull String message)
    {
        this.message = message;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
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
}
