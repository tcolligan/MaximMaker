package com.tcolligan.maximmaker.ui.feedscreen;

import android.content.Context;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;
import com.tcolligan.maximmaker.ui.feedscreen.MaximFeedAdapter.MaximViewHolderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A presenter class to handle some of the logic for {@link MaximFeedActivity}
 * <p>
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
class MaximFeedPresenter implements MaximViewHolderListener
{
    //region Class Properties
    private final Context context;
    private final MaximFeedView maximFeedView;
    private final MaximManager maximManager;
    private boolean didShowLoadingState;
    private String searchText;
    private boolean isSearching;
    //endregion

    //region Constructor
    public MaximFeedPresenter(Context context, MaximFeedView maximFeedView)
    {
        this.context = context.getApplicationContext();
        this.maximFeedView = maximFeedView;
        this.maximManager = MaximManager.getInstance();
        this.searchText = "";
    }
    //endregion

    //region Presenter Action Methods
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
            maximFeedView.showLoadingState();
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

    public void onAddMaximButtonClicked()
    {
        maximFeedView.showAddMaximScreen();
    }

    public void onEditMaxim(Maxim maxim)
    {
        maximFeedView.showEditMaximScreen(maxim);
    }

    public void onDeleteMaxim(Maxim maxim)
    {
        maximFeedView.showConfirmMaximDeletionDialog(maxim);
    }

    public void onDeleteMaximConfirmed(Maxim maximToDelete)
    {
        MaximManager.getInstance().deleteMaxim(context, maximToDelete);
        showMaximsWithFeedStates(maximManager.getMaximList());
    }

    public void onSearchOpened()
    {
        // While searching, we do not want any of the empty or error states in the list to show.
        // So even if there are now maxims in the list, just show an empty list.
        isSearching = true;
        maximFeedView.showMaxims(maximManager.getMaximList());
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
            maximFeedView.showMaxims(maximManager.getMaximList());
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
    //endregion

    //region MaximFeedView Helper Methods
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

        maximFeedView.showMaxims(searchResults);
    }

    private void showMaximsWithFeedStates(List<Maxim> maximList)
    {
        if (maximList == null)
        {
            maximFeedView.showLoadingError();
        }
        else if (maximList.size() == 0)
        {
            maximFeedView.showEmptyState();
        }
        else
        {
            maximFeedView.showMaxims(maximList);
        }
    }
    //endregion

    //region MaximViewHolderListener Implementation
    @Override
    public void onLongClick(final Maxim maxim)
    {
        maximFeedView.showEditOrDeleteMaximDialog(maxim);
    }
    //endregion

    //region MaximFeedView Interface
    public interface MaximFeedView
    {
        void showLoadingState();

        void showEmptyState();

        void showLoadingError();

        void showMaxims(List<Maxim> maximList);

        void showEditOrDeleteMaximDialog(Maxim maxim);

        void showConfirmMaximDeletionDialog(Maxim maxim);

        void showAddMaximScreen();

        void showEditMaximScreen(Maxim maximToEdit);
    }
    //endregion
}
