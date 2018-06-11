package com.tcolligan.maximmaker.domain.feed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * A use case for feed functionality.
 * <p>
 * Created on 6/11/18.
 *
 * @author Thomas Colligan
 */
public class FeedUseCase
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private @NonNull final MaximRepository maximRepository;
    private final BehaviorSubject<List<MaximFeedItemViewModel>> maximViewModelsSubject;
    private @Nullable Disposable disposable;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public FeedUseCase()
    {
        this.maximRepository = RepositoryManager.getInstance().getMaximRepository();
        List<MaximFeedItemViewModel> emptyList = new ArrayList<>();
        this.maximViewModelsSubject = BehaviorSubject.createDefault(emptyList);
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void startSubscribers()
    {
        disposable = maximRepository.getMaximsObservable().subscribe(new Consumer<List<Maxim>>()
        {
            @Override
            public void accept(List<Maxim> maxims)
            {
                List<MaximFeedItemViewModel> viewModels =
                        MaximFeedItemViewModelConverter.convertMaximsToViewModels(maxims);
                maximViewModelsSubject.onNext(viewModels);
            }
        });
    }

    public void clearSubscribers()
    {
        if (disposable != null)
        {
            disposable.dispose();
            disposable = null;
        }
    }

    @NonNull
    public Observable<List<MaximFeedItemViewModel>> getMaximViewModelsObservable()
    {
        return maximViewModelsSubject.observeOn(AndroidSchedulers.mainThread());
    }

    public void loadFeed()
    {
        maximRepository.fetchAllMaxims();
    }

    public void deleteMaxim(long maximId)
    {
        maximRepository.findMaximById(maximId, new Callback<Maxim>()
        {
            @Override
            public void onSuccess(Maxim data)
            {
                maximRepository.deleteMaxim(data);
            }
        });
    }
}
