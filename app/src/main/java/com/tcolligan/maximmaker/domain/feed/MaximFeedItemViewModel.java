package com.tcolligan.maximmaker.domain.feed;

import android.view.View;

import com.tcolligan.maximmaker.domain.add.MaximViewModel;

import java.util.Locale;

/**
 * A view model class that is used to help display what a Maxim looks like
 * inside of the Maxim feed.
 * <p>
 * Created on 7/24/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedItemViewModel extends MaximViewModel
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximFeedItemViewModel(int id)
    {
        super(id);
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public String getMessageText()
    {
        return message;
    }

    public String getAuthorText()
    {
        if (hasAuthor())
        {
            return (String.format(DEFAULT_LOCALE, "- %s", author));
        }

        return "";
    }

    public String getTagsText()
    {
        if (hasTags())
        {
            return tags;
        }

        return "";
    }

    public int getAuthorTextViewVisibility()
    {
        if (hasAuthor())
        {
            return View.VISIBLE;
        }

        return View.GONE;
    }

    public int getTagsTextViewVisibility()
    {
        if (hasTags())
        {
            return View.VISIBLE;
        }

        return View.GONE;
    }
}
