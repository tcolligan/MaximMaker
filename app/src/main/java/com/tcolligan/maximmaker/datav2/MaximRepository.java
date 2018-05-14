package com.tcolligan.maximmaker.datav2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tcolligan.maximmaker.core.AppExecutors;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
    private final List<Maxim> cachedMaxims;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximRepository(AppExecutors appExecutors, MaximDatabase maximDatabase)
    {
        this.appExecutors = appExecutors;
        this.maximDatabase = maximDatabase;
        this.cachedMaxims = new ArrayList<>();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void fetchAllMaxims(@Nullable Callback<List<Maxim>> callback)
    {
        if (!cachedMaxims.isEmpty() && callback != null)
        {
            callback.onSuccess(cachedMaxims);
            return;
        }

        final WeakReference<Callback<List<Maxim>>> weakReferenceCallback = new WeakReference<>(callback);

        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                final List<Maxim> allMaxims = maximDatabase.maximDao().getAll();

                appExecutors.mainThread().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        cachedMaxims.clear();
                        cachedMaxims.addAll(allMaxims);
                        Callback<List<Maxim>> callback = weakReferenceCallback.get();

                        if (callback != null)
                        {
                            callback.onSuccess(cachedMaxims);
                        }
                    }
                });
            }
        });
    }

    public void addMaxim(@NonNull final Maxim maxim)
    {
        cachedMaxims.add(maxim);

        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                maximDatabase.maximDao().insert(maxim);
            }
        });
    }

    public void deleteMaxim(@NonNull final Maxim maxim)
    {
        cachedMaxims.remove(maxim);

        appExecutors.diskIO().execute(new Runnable()
        {
            @Override
            public void run()
            {
                maximDatabase.maximDao().insert(maxim);
            }
        });
    }

    public void findMaximById(final int id, @Nullable Callback<Maxim> callback)
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

}
