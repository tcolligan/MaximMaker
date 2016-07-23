package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.os.AsyncTask;

import com.tcolligan.maximmaker.utils.LogUtils;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;

/**
 * An AsyncTask that saves Maxims to a json file.
 * <p>
 * Created on 6/5/2016.
 *
 * @author Thomas Colligan
 */
class SaveMaximsAsyncTask extends AsyncTask<Void, Void, Boolean>
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final String TAG = "SaveMaximsAsyncTask";
    private final Context context;
    private final Maxim[] maximsToSaveArray;
    private final SaveMaximsListener saveMaximsListener;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public SaveMaximsAsyncTask(Context context,
                               Maxim[] maximsToSaveArray,
                               SaveMaximsListener saveMaximsListener)
    {
        this.context = context;
        this.maximsToSaveArray = maximsToSaveArray;
        this.saveMaximsListener = saveMaximsListener;
    }

    //==============================================================================================
    // Async Task Methods
    //==============================================================================================

    @Override
    protected Boolean doInBackground(Void... params)
    {
        File file = new File(context.getFilesDir(), MaximManager.SAVED_MAXIMS_JSON_FILE_NAME);

        try
        {
            // Make sure the file exists before we save to it
            if (!file.exists())
            {
                boolean success = file.createNewFile();

                if (!success)
                {
                    return false;
                }
            }

            // Take our Maxim data and turn it into JSON data
            JSONArray jsonArray = new JSONArray();

            for (Maxim maxim : maximsToSaveArray)
            {
                jsonArray.put(maxim.toJSONObject());
            }

            // Write the JSON as text to a file
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonArray.toString().getBytes());
            fileOutputStream.close();

            return true;
        }
        catch (Exception e)
        {
            LogUtils.w(TAG, e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        if (saveMaximsListener != null)
        {
            saveMaximsListener.onMaximsSaved(success);
        }
    }

    //==============================================================================================
    // SaveMaximsListener Interface
    //==============================================================================================

    public interface SaveMaximsListener
    {
        void onMaximsSaved(boolean success);
    }
}
