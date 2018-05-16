package com.tcolligan.maximmaker.domain.feed;

import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;
import com.tcolligan.maximmaker.domain.add.MaximViewModel;

import java.util.List;

/**
 * A presenter class to handle some of the logic for MaximFeedActivity.
 * <p/>
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedPresenter
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private MaximFeedView view;
    private final MaximRepository maximRepository;
    private boolean didShowLoadingState;
    private String searchText;
    private boolean isSearching;
    private List<MaximFeedItemViewModel> maximViewModels;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public MaximFeedPresenter()
    {
        this.maximRepository = RepositoryManager.getInstance().getMaximRepository();
        this.searchText = "";
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void attachView(MaximFeedView maximFeedView)
    {
        this.view = maximFeedView;
    }

    public void detachView()
    {
        this.view = null;
    }

    //==============================================================================================
    // Presenter Action Methods
    //==============================================================================================

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
                showMaximsWithFeedStates(maximViewModels);
            }
        }
        else
        {
            // The initial loading screen gets shown the first time onResume is called.
            // It will probably load super fast, so the user wont see it.
            // But it if the user has a lot of maxims to load, it will be helpful then.
            view.showLoadingState();
            didShowLoadingState = true;

            maximRepository.fetchAllMaxims(new Callback<List<Maxim>>()
            {
                @Override
                public void onSuccess(List<Maxim> data)
                {
                    maximViewModels = MaximFeedItemViewModelConerter.convertMaximsToViewModels(data);
                    showMaximsWithFeedStates(maximViewModels);
                }
            });
        }
    }

    public void onAddMaximButtonClicked()
    {
        view.showAddMaximScreen();
    }

    public void onEditMaxim(int maximId)
    {
        view.showEditMaximScreen(maximId);
    }

    public void onDeleteMaxim(int maximId)
    {
        view.showConfirmMaximDeletionDialog(maximId);
    }

    public void onDeleteMaximConfirmed(int maximId)
    {
        maximRepository.findMaximById(maximId, new Callback<Maxim>()
        {
            @Override
            public void onSuccess(Maxim data)
            {
                maximRepository.deleteMaxim(data);
                // TODO: Refresh list
            }
        });
    }

    public void onSearchOpened()
    {
        // While searching, we do not want any of the empty or error states in the list to show.
        // So even if there are now maxims in the list, just show an empty list.
        isSearching = true;
        //view.showMaxims(maximManager.getMaximList());
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
            //view.showMaxims(maximManager.getMaximList());
        }
        else
        {
            showMaximsForSearchText();
        }
    }

    public void onSearchClosed()
    {
        isSearching = false;
        //showMaximsWithFeedStates(maximManager.getMaximList());
    }

    public void onExportClicked()
    {
        // TODO: Export
    }

    public void onMaximLongClick(MaximFeedItemViewModel viewModel)
    {
        view.showEditOrDeleteMaximDialog(viewModel.getMaximId());
    }

    //==============================================================================================
    // MaximFeedView Helper Methods
    //==============================================================================================

    private void showMaximsForSearchText()
    {
        /*Locale defaultLocale = Locale.getDefault();
        List<Maxim> searchResults = new ArrayList<>();
        searchText = searchText.toLowerCase(defaultLocale);

        for (Maxim maxim : maximManager.getMaximList())
        {
            if (maxim.getMessage().toLowerCase(defaultLocale).contains(searchText) ||
                    (maxim.hasAuthor() && maxim.getAuthor().toLowerCase(defaultLocale).contains(searchText)) ||
                    (maxim.hasTags() && maxim.getTagsCommaSeparated().toLowerCase(defaultLocale).contains(searchText)))
            {
                searchResults.add(maxim);
            }
        }

        view.showMaxims(searchResults);*/
    }

    private void showMaximsWithFeedStates(List<MaximFeedItemViewModel> viewModels)
    {
        if (viewModels == null)
        {
            view.showLoadingError();
        }
        else if (viewModels.isEmpty())
        {
            view.showEmptyState();
        }
        else
        {
            view.showMaxims(viewModels);
        }
    }

    //==============================================================================================
    // MaximFeedView Interface
    //==============================================================================================

    public interface MaximFeedView
    {
        void showLoadingState();

        void showEmptyState();

        void showLoadingError();

        void showMaxims(List<MaximFeedItemViewModel> viewModels);

        void showEditOrDeleteMaximDialog(int maximId);

        void showConfirmMaximDeletionDialog(int maximId);

        void showAddMaximScreen();

        void showEditMaximScreen(int maximId);
    }

}
