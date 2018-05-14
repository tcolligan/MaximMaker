package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.os.AsyncTask;

import com.tcolligan.maximmaker.core.utils.FileUtils;
import com.tcolligan.maximmaker.core.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An AsyncTask that loads maxims from a json file.
 * <p/>
 * Created on 6/5/2016.
 *
 * @author Thomas Colligan
 */
class LoadMaximsAsyncTask extends AsyncTask<Void, Void, List<Maxim>>
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final String TAG = "LoadMaximsAsyncTask";
    private final Context context;
    private final LoadMaximsListener loadMaximsListener;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public LoadMaximsAsyncTask(Context context, LoadMaximsListener loadMaximsListener)
    {
        this.context = context;
        this.loadMaximsListener = loadMaximsListener;
    }

    //==============================================================================================
    // AsyncTask Methods
    //==============================================================================================

    @Override
    protected List<Maxim> doInBackground(Void... params)
    {
        File file = new File(context.getFilesDir(), MaximManager.SAVED_MAXIMS_JSON_FILE_NAME);

        try
        {
            if (file.exists())
            {
                String fileDataAsString = FileUtils.retrieveFileDataAsText(file);
                return convertJsonStringToMaximList(fileDataAsString);
            }
            else
            {
                LogUtils.d(TAG, MaximManager.SAVED_MAXIMS_JSON_FILE_NAME + " does not exist");
            }
        }
        catch (Exception e)
        {
            // Something went really wrong just now...
            LogUtils.w(TAG, e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Maxim> loadedMaximsList)
    {
        if (loadMaximsListener != null)
        {
            loadMaximsListener.onMaximsLoaded(loadedMaximsList);
        }
    }

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    private static List<Maxim> convertJsonStringToMaximList(String jsonString) throws JSONException
    {
        List<Maxim> maximList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        int len = jsonArray.length();

        for (int i = 0; i < len; i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Maxim maxim = new Maxim(jsonObject);
            maximList.add(maxim);
        }

        return maximList;
    }

    //==============================================================================================
    // LoadMaximsListener Interface
    //==============================================================================================

    public interface LoadMaximsListener
    {
        void onMaximsLoaded(List<Maxim> loadedMaximsList);
    }

}
