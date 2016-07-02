package com.tcolligan.maximmaker;

import android.content.Context;
import android.util.Log;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A presenter class to handle some of the logic for {@link MaximFeedActivity}
 *
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedPresenter
{
    private Context context;
    private MaximFeed maximFeed;
    private MaximManager maximManager;
    private boolean didShowLoadingState;

    public MaximFeedPresenter(Context context, MaximFeed maximFeed)
    {
        this.context = context.getApplicationContext();
        this.maximFeed = maximFeed;
        this.maximManager = MaximManager.getInstance();
    }

    public void onResume(String searchText, boolean isSearching)
    {
        Log.d("TESTER", String.format(Locale.ENGLISH, "Text: %s isSearching: %s", searchText, Boolean.toString(isSearching)));
        if (didShowLoadingState)
        {
            if (isSearching)
            {
                // We don't want those feed states to show while searching
                onSearchForText(searchText);
            }
            else
            {
                showMaximsWithFeedStates(maximManager.getMaximList());
            }
        }
        else
        {
            // The initial loading screen gets show the first time around
            maximFeed.showLoadingState();
            didShowLoadingState = true;

            maximManager.loadMaxims(context, new MaximManager.MaximsLoadedListener()
            {
                @Override
                public void onMaximsLoaded(List<Maxim> loadedMaximList)
                {
                    showMaximsWithFeedStates(loadedMaximList);
                }
            });
        }
    }

    public void onDeleteMaxim(Maxim maxim)
    {
        MaximManager.getInstance().deleteMaxim(context, maxim);
        showMaximsWithFeedStates(maximManager.getMaximList());
    }

    public void onSearchForText(String text)
    {
        Log.d("TESTER", text);
        if (text.isEmpty())
        {
            maximFeed.showMaxims(maximManager.getMaximList());
        }
        else
        {
            List<Maxim> searchResults = new ArrayList<>();
            text = text.toLowerCase();

            for (Maxim maxim : maximManager.getMaximList())
            {
                if (maxim.getMessage().toLowerCase().contains(text) ||
                        (maxim.hasAuthor() && maxim.getAuthor().toLowerCase().contains(text)) ||
                        (maxim.hasTags() && maxim.getTagsCommaSeparated().toLowerCase().contains(text)))
                {
                    searchResults.add(maxim);
                }
            }

            maximFeed.showMaxims(searchResults);
        }
    }

    public void onSearchClosed()
    {
        showMaximsWithFeedStates(maximManager.getMaximList());
    }

    private void showMaximsWithFeedStates(List<Maxim> maximList)
    {
        if (maximList == null)
        {
            maximFeed.showLoadingError();
        }
        else if (maximList.size() == 0)
        {
            maximFeed.showEmptyState();
        }
        else
        {
            maximFeed.showMaxims(maximList);
        }
    }

    public interface MaximFeed
    {
        void showLoadingState();
        void showEmptyState();
        void showLoadingError();
        void showMaxims(List<Maxim> maximList);
    }
}
