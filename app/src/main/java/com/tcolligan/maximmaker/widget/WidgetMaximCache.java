package com.tcolligan.maximmaker.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tcolligan.maximmaker.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that keeps track of which maxim has been displayed by the {@link MaximWidgetProvider}.
 *
 * Created on 1/29/2017.
 *
 * @author Thomas Colligan
 */
public class WidgetMaximCache
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final String TAG = "WidgetMaximCache";
    private static final String KEY_DISPLAYED_MAXIM_IDS = "DISPLAYED_MAXIM_IDS";
    private static JSONArray displayedMaximIdsArray = new JSONArray();

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void load(Context context)
    {
        displayedMaximIdsArray = loadDisplayedMaximIdsArray(context);

        if (displayedMaximIdsArray == null)
        {
            displayedMaximIdsArray = new JSONArray();
        }
    }

    private static JSONArray loadDisplayedMaximIdsArray(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.contains(KEY_DISPLAYED_MAXIM_IDS))
        {
            String jsonArray = preferences.getString(KEY_DISPLAYED_MAXIM_IDS, null);

            if (jsonArray != null)
            {
                try
                {
                    return new JSONArray(jsonArray);
                }
                catch (JSONException e)
                {
                    LogUtils.w(TAG, e);
                    return null;
                }
            }
        }

        return null;
    }

    public static List<String> getDisplayedMaximIdsList()
    {
        List<String> displayedMaximIdsList = new ArrayList<>();

        int len = displayedMaximIdsArray.length();

        for (int i = 0; i < len; i++)
        {
            try
            {
                displayedMaximIdsList.add(displayedMaximIdsArray.getString(i));
            }
            catch (JSONException e)
            {
                LogUtils.w(TAG, e);
            }
        }

        return displayedMaximIdsList;
    }

    public static void addDisplayedMaximId(Context context, String maximId)
    {
        displayedMaximIdsArray.put(maximId);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DISPLAYED_MAXIM_IDS, displayedMaximIdsArray.toString());
        editor.apply();
    }

    public static void clear(Context context)
    {
        displayedMaximIdsArray = new JSONArray();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_DISPLAYED_MAXIM_IDS);
        editor.apply();
    }
}
