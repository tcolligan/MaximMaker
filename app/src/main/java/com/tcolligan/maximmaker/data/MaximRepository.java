package com.tcolligan.maximmaker.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tcolligan.maximmaker.core.AppExecutors;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * A repository for accessing Maxims.
 * <p>
 * Created on 5/14/18.
 *
 * @author Thomas Colligan
 */

public class MaximRepository
{
    //==============================================================================================
    // Class Property
    //==============================================================================================

    private AppExecutors appExecutors;
    private MaximDatabase maximDatabase;
    private final BehaviorSubject<List<Maxim>> maximsSubject;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximRepository(@NonNull AppExecutors appExecutors, @NonNull MaximDatabase maximDatabase)
    {
        this.appExecutors = appExecutors;
        this.maximDatabase = maximDatabase;

        List<Maxim> emptyList = new ArrayList<>();
        this.maximsSubject = BehaviorSubject.createDefault(emptyList);
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    @NonNull
    public Observable<List<Maxim>> getMaximsObservable()
    {
        return maximsSubject.observeOn(AndroidSchedulers.mainThread());
    }

    public void fetchAllMaxims()
    {
        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<Maxim> maxims = maximDatabase.maximDao().getAll();
                maximsSubject.onNext(maxims);
            }
        });
    }

    public void addMaxim(@NonNull final Maxim maxim)
    {
        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                long id = maximDatabase.maximDao().insert(maxim);
                maxim.setId(id);

                maximsSubject.getValue().add(maxim);
                maximsSubject.onNext(maximsSubject.getValue());
            }
        });
    }

    public void deleteMaxim(@NonNull final Maxim maxim)
    {
        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                maximDatabase.maximDao().delete(maxim);
                maximsSubject.getValue().remove(maxim);
                maximsSubject.onNext(maximsSubject.getValue());
            }
        });
    }

    public void findMaximById(final long id, @Nullable Callback<Maxim> callback)
    {
        final WeakReference<Callback<Maxim>> weakReferenceCallback = new WeakReference<>(callback);

        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                final Maxim maxim = maximDatabase.maximDao().findById(id);

                appExecutors.mainThread().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Callback<Maxim> callback = weakReferenceCallback.get();

                        if (callback != null)
                        {
                            callback.onSuccess(maxim);
                        }
                    }
                });
            }
        });
    }

    public void updateMaxim(@NonNull final Maxim maxim)
    {
        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                maximDatabase.maximDao().update(maxim);

                List<Maxim> maxims = maximsSubject.getValue();
                maxims.remove(maxim);
                maxims.add(maxim);
                maximsSubject.onNext(maxims);
            }
        });
    }

}
