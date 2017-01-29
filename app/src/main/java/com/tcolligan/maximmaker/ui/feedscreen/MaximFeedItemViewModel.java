package com.tcolligan.maximmaker.ui.feedscreen;

import android.view.View;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.Locale;

/**
 * A view model class that is used in {@link MaximFeedAdapter} to help display what a Maxim
 * looks like inside of the Maxim feed.
 *
 * Created on 7/24/2016.
 *
 * @author Thomas Colligan
 */
class MaximFeedItemViewModel
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private Maxim maxim;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public MaximFeedItemViewModel(Maxim maxim)
    {
        this.maxim = maxim;
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public Maxim getMaxim()
    {
        return maxim;
    }

    public String getMessageText()
    {
        return maxim.getMessage();
    }

    public String getAuthorText()
    {
        if (maxim.hasAuthor())
        {
            return (String.format(DEFAULT_LOCALE, "- %s", maxim.getAuthor()));
        }

        return "";
    }

    public String getTagsText()
    {
        if (maxim.hasTags())
        {
            return maxim.getTagsCommaSeparated();
        }

        return "";
    }

    public int getAuthorTextViewVisibility()
    {
        if (maxim.hasAuthor())
        {
            return View.VISIBLE;
        }

        return View.GONE;
    }

    public int getTagsTextViewVisibility()
    {
        if (maxim.hasTags())
        {
            return View.VISIBLE;
        }

        return View.GONE;
    }
}
