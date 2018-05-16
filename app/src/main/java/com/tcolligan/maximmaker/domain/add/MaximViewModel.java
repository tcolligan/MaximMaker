package com.tcolligan.maximmaker.domain.add;

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

    protected int maximId;
    protected String message;
    protected String author;
    protected String tags;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    protected MaximViewModel(int maximId)
    {
        this.maximId = maximId;
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public int getMaximId()
    {
        return maximId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
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

    public boolean hasAuthor()
    {
        return author != null;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public boolean hasTags()
    {
        return tags != null;
    }
}
