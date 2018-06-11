package com.tcolligan.maximmaker.ui.feedscreen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tcolligan.maximmaker.domain.feed.FeedUseCase;
import com.tcolligan.maximmaker.domain.feed.MaximFeedItemViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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

    private @NonNull final FeedUseCase feedUseCase;
    private @NonNull String searchText;
    private @Nullable MaximFeedView view;
    private boolean didShowLoadingState;
    private boolean isSearching;
    private @Nullable Disposable disposable;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximFeedPresenter()
    {
        feedUseCase = new FeedUseCase();
        searchText = "";
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void attachView(MaximFeedView maximFeedView)
    {
        this.view = maximFeedView;
        this.feedUseCase.startSubscribers();
        subscribeFeedObserver();
    }

    private void subscribeFeedObserver()
    {
        disposable = this.feedUseCase.getMaximViewModelsObservable().subscribe(new Consumer<List<MaximFeedItemViewModel>>()
        {
            @Override
            public void accept(List<MaximFeedItemViewModel> maximFeedItemViewModels)
            {
                if (view != null)
                {
                    if (isSearching)
                    {
                        showMaximsForSearchText();
                    }
                    else
                    {
                        showMaximsWithFeedStates(maximFeedItemViewModels);
                    }
                }
            }
        });
    }

    public void detachView()
    {
        this.feedUseCase.clearSubscribers();

        if (disposable != null)
        {
            disposable.dispose();
            disposable = null;
        }

        this.view = null;
    }

    //==============================================================================================
    // Presenter Action Methods
    //==============================================================================================

    public void onResume()
    {
        if (!didShowLoadingState)
        {
            view.showLoadingState();
            didShowLoadingState = true;
            feedUseCase.loadFeed();
        }
    }

    public void onAddMaximButtonClicked()
    {
        view.showAddMaximScreen();
    }

    public void onEditMaxim(long maximId)
    {
        view.showEditMaximScreen(maximId);
    }

    public void onDeleteMaxim(long maximId)
    {
        view.showConfirmMaximDeletionDialog(maximId);
    }

    public void onDeleteMaximConfirmed(long maximId)
    {
        feedUseCase.deleteMaxim(maximId);
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

    private void showMaximsWithFeedStates(@Nullable List<MaximFeedItemViewModel> viewModels)
    {
        if (view == null)
        {
            return;
        }

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

        void showEditOrDeleteMaximDialog(long maximId);

        void showConfirmMaximDeletionDialog(long maximId);

        void showAddMaximScreen();

        void showEditMaximScreen(long maximId);
    }

}
