package com.tcolligan.maximmaker.domain.add;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A ViewModel that represents Maxim data object.
 * <p>
 * Created on 5/16/18.
 *
 * @author Thomas Colligan
 */

public class MaximViewModel
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private final long maximId;
    protected @NonNull String message;
    protected @Nullable String author;
    protected @Nullable String tags;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public MaximViewModel(long maximId, @NonNull String message)
    {
        this.maximId = maximId;
        this.message = message;
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public long getMaximId()
    {
        return maximId;
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

    public boolean hasAuthor()
    {
        return author != null;
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

    public boolean hasTags()
    {
        return tags != null;
    }
}
