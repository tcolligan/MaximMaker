package com.tcolligan.maximmaker.data;

import android.content.Context;
import android.os.AsyncTask;

import com.tcolligan.maximmaker.core.utils.FileUtils;
import com.tcolligan.maximmaker.core.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An AsyncTask that saves Maxims to a json file.
 * <p/>
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
            if (!FileUtils.createNewFileIfFileDoesNotExist(file))
            {
                return false;
            }

            JSONArray jsonArray = convertMaximArrayToJsonArray(maximsToSaveArray);
            writeJsonArrayToFile(jsonArray, file);

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
    // Static Class Instance Methods
    //==============================================================================================

    private static JSONArray convertMaximArrayToJsonArray(Maxim[] maximArray) throws JSONException
    {
        JSONArray jsonArray = new JSONArray();

        for (Maxim maxim : maximArray)
        {
            jsonArray.put(maxim.toJSONObject());
        }

        return jsonArray;
    }

    private static void writeJsonArrayToFile(JSONArray jsonArray, File file) throws IOException
    {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(jsonArray.toString().getBytes());
        fileOutputStream.close();
    }

    //==============================================================================================
    // SaveMaximsListener Interface
    //==============================================================================================

    public interface SaveMaximsListener
    {
        void onMaximsSaved(boolean success);
    }
}
