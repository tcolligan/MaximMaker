package com.tcolligan.maximmaker;

import android.content.Context;
import android.util.Log;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.ArrayList;
import java.util.List;

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

    private String searchText;
    private boolean isSearching;

    public MaximFeedPresenter(Context context, MaximFeed maximFeed)
    {
        this.context = context.getApplicationContext();
        this.maximFeed = maximFeed;
        this.maximManager = MaximManager.getInstance();
        this.searchText = "";
    }

    public void onResume()
    {
        if (didShowLoadingState)
        {
            if (isSearching)
            {
                // We don't want those feed states to show while searching
                showMaximsForSearchText();
            }
            else
            {
                showMaximsWithFeedStates(maximManager.getMaximList());
            }
        }
        else
        {
            // The initial loading screen gets shown the first time onResume is called.
            // It will probably load super fast, so the user wont see it.
            // But it if the user has a lot of maxims to load, it will be helpful then.
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

    public void onSearchOpened()
    {
        // While searching, we do not want any of the empty or error states in the list to show.
        // So even if there are now maxims in the list, just show an empty list.
        isSearching = true;
        maximFeed.showMaxims(maximManager.getMaximList());
    }

    public void onSearch(String searchText)
    {
        if (!isSearching)
        {
            return;
        }

        this.searchText = searchText;

        if (searchText.isEmpty())
        {
            maximFeed.showMaxims(maximManager.getMaximList());
        }
        else
        {
            showMaximsForSearchText();
        }
    }

    public void onSearchClosed()
    {
        isSearching = false;
        showMaximsWithFeedStates(maximManager.getMaximList());
    }

    private void showMaximsForSearchText()
    {
        List<Maxim> searchResults = new ArrayList<>();
        searchText = searchText.toLowerCase();

        for (Maxim maxim : maximManager.getMaximList())
        {
            if (maxim.getMessage().toLowerCase().contains(searchText) ||
                    (maxim.hasAuthor() && maxim.getAuthor().toLowerCase().contains(searchText)) ||
                    (maxim.hasTags() && maxim.getTagsCommaSeparated().toLowerCase().contains(searchText)))
            {
                searchResults.add(maxim);
            }
        }

        maximFeed.showMaxims(searchResults);
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
