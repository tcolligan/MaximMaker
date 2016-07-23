package com.tcolligan.maximmaker.data;

import android.content.Context;

import com.tcolligan.maximmaker.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A singleton that keeps track of all the user's Maxims.
 * Handles saving them to disk and loading them from disk.
 * <p>
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class MaximManager
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    public static final String SAVED_MAXIMS_JSON_FILE_NAME = "saved_maxims.json";
    private static final String TAG = "MaximManager";
    private static final MaximManager instance = new MaximManager();
    private final List<Maxim> maximList;

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static MaximManager getInstance()
    {
        return instance;
    }

    //==============================================================================================
    // Constructor
    //==============================================================================================

    private MaximManager()
    {
        maximList = new ArrayList<>();
    }

    //==============================================================================================
    // Load, Save, Add, & Delete Methods
    //==============================================================================================

    public void loadMaxims(Context context, final MaximsLoadedListener maximsLoadedListener)
    {
        new LoadMaximsAsyncTask(context, new LoadMaximsAsyncTask.LoadMaximsListener()
        {
            @Override
            public void onMaximsLoaded(List<Maxim> loadedMaximsList)
            {
                if (loadedMaximsList == null)
                {
                    // Getting a null result back means something went horribly wrong
                    LogUtils.e(TAG, "Error loading maxims from file: " + SAVED_MAXIMS_JSON_FILE_NAME);
                }
                else
                {
                    maximList.clear();
                    maximList.addAll(loadedMaximsList);
                    LogUtils.d(TAG, String.format(Locale.US, "Loaded %d maxim(s)", loadedMaximsList.size()));
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
        LogUtils.d(TAG, String.format(Locale.US, "Saving %d maxim(s)", maximList.size()));

        new SaveMaximsAsyncTask(context, maximsToSaveArray, new SaveMaximsAsyncTask.SaveMaximsListener()
        {
            @Override
            public void onMaximsSaved(boolean success)
            {
                if (!success)
                {
                    LogUtils.e(TAG, "Error saving maxims to file: " + SAVED_MAXIMS_JSON_FILE_NAME);
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

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public Maxim findMaximWithUuid(String uuid)
    {
        for (Maxim maxim : maximList)
        {
            if (maxim.getUuid().equals(uuid))
            {
                return maxim;
            }
        }

        return null;
    }

    public List<Maxim> getMaximList()
    {
        return maximList;
    }

    //==============================================================================================
    // MaximsLoadListener Interface
    //==============================================================================================

    public interface MaximsLoadedListener
    {
        void onMaximsLoaded(List<Maxim> loadedMaximList);
    }

}
