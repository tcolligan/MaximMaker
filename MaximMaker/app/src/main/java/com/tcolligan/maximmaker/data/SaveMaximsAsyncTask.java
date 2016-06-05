package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;

/**
 * An AsyncTask that saves Maxims to a json file.
 *
 * Created on 6/5/2016.
 *
 * @author Thomas Colligan
 */
public class SaveMaximsAsyncTask extends AsyncTask<Void, Void, Boolean>
{
    private static final String TAG = "SaveMaximsAsyncTask";
    private final Context context;
    private final String fileName;
    private final Maxim[] maximsToSaveArray;
    private final SaveMaximsListener saveMaximsListener;

    public SaveMaximsAsyncTask(Context context,
                               String fileName,
                               Maxim[] maximsToSaveArray,
                               SaveMaximsListener saveMaximsListener)
    {
        this.context = context;
        this.fileName = fileName;
        this.maximsToSaveArray = maximsToSaveArray;
        this.saveMaximsListener = saveMaximsListener;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        File file = new File(context.getFilesDir(), fileName);

        // Make sure the file exists before we save to it
        if (!file.exists())
        {
            boolean success = file.mkdirs();

            if (!success)
            {
                return false;
            }
        }

        try
        {
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
            Log.w(TAG, e);
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

    public interface SaveMaximsListener
    {
        void onMaximsSaved(boolean success);
    }
}
