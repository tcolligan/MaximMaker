package com.tcolligan.maximmaker.ui.feed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tcolligan.maximmaker.domain.feed.FeedUseCase;
import com.tcolligan.maximmaker.domain.feed.MaximFeedItemViewModel;
import com.tcolligan.maximmaker.domain.feed.SearchUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
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
    private @NonNull final List<MaximFeedItemViewModel> feedViewModels;
    private @NonNull final List<MaximFeedItemViewModel> searchViewModels;
    private @NonNull final MaximFeedView dummyView;
    private @NonNull String searchText;
    private @Nullable MaximFeedView view;
    private boolean didShowLoadingState;
    private boolean isSearching;
    private @Nullable Disposable feedDisposable;
    private @Nullable Disposable searchDisposable;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximFeedPresenter()
    {
        feedUseCase = new FeedUseCase();
        feedViewModels = new ArrayList<>();
        searchViewModels = new ArrayList<>();
        searchText = "";

        dummyView = new MaximFeedView()
        {
            @Override
            public void showLoadingState()
            {
            }

            @Override
            public void showEmptyState()
            {
            }

            @Override
            public void showMaxims(List<MaximFeedItemViewModel> viewModels)
            {
            }

            @Override
            public void showEditOrDeleteMaximDialog(long maximId)
            {
            }

            @Override
            public void showConfirmMaximDeletionDialog(long maximId)
            {
            }

            @Override
            public void showAddMaximScreen()
            {
            }

            @Override
            public void showEditMaximScreen(long maximId)
            {
            }
        };
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================


    @NonNull
    public MaximFeedView getView()
    {
        return view == null ? dummyView : view;
    }

    public void attachView(@NonNull MaximFeedView maximFeedView)
    {
        this.view = maximFeedView;
        this.feedUseCase.startSubscribers();
        subscribeFeedObserver();
    }

    private void subscribeFeedObserver()
    {
        feedDisposable = this.feedUseCase.getMaximViewModelsObservable().subscribe(new Consumer<List<MaximFeedItemViewModel>>()
        {
            @Override
            public void accept(List<MaximFeedItemViewModel> maximFeedItemViewModels)
            {
                feedViewModels.clear();
                searchViewModels.clear();
                feedViewModels.addAll(maximFeedItemViewModels);
                searchViewModels.addAll(maximFeedItemViewModels);

                if (isSearching)
                {
                    showMaximsForSearchText();
                }
                else
                {
                    showMaximsWithFeedStates();
                }
            }
        });
    }

    public void detachView()
    {
        this.feedUseCase.clearSubscribers();

        if (feedDisposable != null)
        {
            feedDisposable.dispose();
            feedDisposable = null;
        }

        if (searchDisposable != null)
        {
            searchDisposable.dispose();
            searchDisposable = null;
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
            getView().showLoadingState();
            didShowLoadingState = true;
            feedUseCase.loadFeed();
        }
    }

    public void onAddMaximButtonClicked()
    {
        getView().showAddMaximScreen();
    }

    public void onEditMaxim(long maximId)
    {
        getView().showEditMaximScreen(maximId);
    }

    public void onDeleteMaxim(long maximId)
    {
        getView().showConfirmMaximDeletionDialog(maximId);
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
        showMaximsForSearchText();
    }

    public void onSearch(String searchText)
    {
        if (!isSearching)
        {
            return;
        }

        this.searchText = searchText;

        showMaximsForSearchText();
    }

    public void onSearchClosed()
    {
        isSearching = false;
        showMaximsWithFeedStates();
    }

    public void onExportClicked()
    {
        // TODO: Export
    }

    public void onMaximLongClick(MaximFeedItemViewModel viewModel)
    {
        getView().showEditOrDeleteMaximDialog(viewModel.getMaximId());
    }

    //==============================================================================================
    // MaximFeedView Helper Methods
    //==============================================================================================

    private void showMaximsForSearchText()
    {
        if (searchDisposable != null)
        {
            searchDisposable.dispose();
            searchDisposable = null;
        }

        SearchUseCase.search(searchText, feedViewModels)
                .subscribe(new SingleObserver<List<MaximFeedItemViewModel>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        searchDisposable = d;
                    }

                    @Override
                    public void onSuccess(List<MaximFeedItemViewModel> viewModels)
                    {
                        searchViewModels.clear();
                        searchViewModels.addAll(viewModels);
                        getView().showMaxims(searchViewModels);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                    }
                });
    }

    private void showMaximsWithFeedStates()
    {
        if (feedViewModels.isEmpty())
        {
            getView().showEmptyState();
        }
        else
        {
            getView().showMaxims(feedViewModels);
        }
    }

    //==============================================================================================
    // MaximFeedView Interface
    //==============================================================================================

    public interface MaximFeedView
    {
        void showLoadingState();

        void showEmptyState();

        void showMaxims(@NonNull List<MaximFeedItemViewModel> viewModels);

        void showEditOrDeleteMaximDialog(long maximId);

        void showConfirmMaximDeletionDialog(long maximId);

        void showAddMaximScreen();

        void showEditMaximScreen(long maximId);
    }

}
