package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A singleton that keeps track of all the user's Maxims.
 * Handles saving them to disk and loading them from disk.
 *
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class MaximManager
{
    private static final String TAG = "MaximManager";
    private static final String FILE_NAME = "saved_maxims.json";
    private static final MaximManager instance = new MaximManager();
    private final List<Maxim> maximList;

    public static MaximManager getInstance()
    {
        return instance;
    }

    public MaximManager()
    {
        maximList = new ArrayList<>();
    }

    public void loadMaxims(Context context, final MaximsLoadedListener maximsLoadedListener)
    {
        new LoadMaximsAsyncTask(context, FILE_NAME, new LoadMaximsAsyncTask.LoadMaximsListener()
        {
            @Override
            public void onMaximsLoaded(List<Maxim> loadedMaximsList)
            {
                if (loadedMaximsList == null)
                {
                    // Getting a null result back means something went horribly wrong
                    Log.e(TAG, "Error loading maxims from file: " + FILE_NAME);
                }
                else
                {
                    maximList.clear();
                    maximList.addAll(loadedMaximsList);
                    Log.d(TAG, String.format(Locale.US, "Loaded %d maxim(s)", loadedMaximsList.size()));
                }

                if (maximsLoadedListener != null)
                {
                    maximsLoadedListener.onMaximsLoaded(maximList);
                }
            }
        }).execute();
    }

    public void saveMaxims(Context context)
    {
        // Copy ArrayList into array to avoid ConcurrentModifications
        Maxim[] maximsToSaveArray = maximList.toArray(new Maxim[maximList.size()]);
        Log.d(TAG, String.format(Locale.US, "Saving %d maxim(s)", maximList.size()));

        new SaveMaximsAsyncTask(context, FILE_NAME, maximsToSaveArray, new SaveMaximsAsyncTask.SaveMaximsListener()
        {
            @Override
            public void onMaximsSaved(boolean success)
            {
                if (!success)
                {
                    Log.e(TAG, "Error saving maxims to file: " + FILE_NAME);
                }
            }
        }).execute();
    }

    public void addAndSaveMaxim(Context context, Maxim maxim)
    {
        maximList.add(maxim);
        saveMaxims(context);
    }

    public void deleteMaxim(Context context, Maxim maxim)
    {
        boolean didRemove = maximList.remove(maxim);

        if (didRemove)
        {
            saveMaxims(context);
        }
    }

    public List<Maxim> getMaximList()
    {
        return maximList;
    }

    public interface MaximsLoadedListener
    {
        void onMaximsLoaded(List<Maxim> loadedMaximList);
    }
}
