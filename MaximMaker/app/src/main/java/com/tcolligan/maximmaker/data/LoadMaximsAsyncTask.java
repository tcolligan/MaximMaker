package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An AsyncTask that loads maxims from a json file.
 *
 * Created on 6/5/2016.
 *
 * @author Thomas Colligan
 */
public class LoadMaximsAsyncTask extends AsyncTask<Void, Void, List<Maxim>>
{
    private static final String TAG = "LoadMaximsAsyncTask";
    private final Context context;
    private final String fileName;
    private final LoadMaximsListener loadMaximsListener;

    public LoadMaximsAsyncTask(Context context, String fileName, LoadMaximsListener loadMaximsListener)
    {
        this.context = context;
        this.fileName = fileName;
        this.loadMaximsListener = loadMaximsListener;
    }

    @Override
    protected List<Maxim> doInBackground(Void... params)
    {
        File file = new File(context.getFilesDir(), fileName);
        List<Maxim> loadedMaximsList = new ArrayList<>();

        try
        {
            if (file.exists())
            {
                // Read the json text from the file
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                bufferedReader.close();

                // Convert the text to JSON and Maxim objects
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                int len = jsonArray.length();

                for (int i = 0; i < len; i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Maxim maxim = new Maxim(jsonObject);
                    loadedMaximsList.add(maxim);
                }
            }
            else
            {
                Log.d(TAG, fileName + " does not exist");
            }
        }
        catch (Exception e)
        {
            // Something went really wrong just now...
            Log.w(TAG, e);
            return null;
        }

        return loadedMaximsList;
    }

    @Override
    protected void onPostExecute(List<Maxim> loadedMaximsList)
    {
        if (loadMaximsListener != null)
        {
            loadMaximsListener.onMaximsLoaded(loadedMaximsList);
        }
    }

    public interface LoadMaximsListener
    {
        void onMaximsLoaded(List<Maxim> loadedMaximsList);
    }
}
