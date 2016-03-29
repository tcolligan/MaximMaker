package com.tcolligan.maximmaker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A tag for identifying a specific type of maxim or quote.
 *
 * Created on 3/28/2016.
 *
 * @author Thomas Colligan
 */
public class MaximTag
{
    private static final String KEY_TAG = "kTag";
    private String tag;

    public MaximTag(String tag)
    {
        this.tag = tag;
    }

    public MaximTag(JSONObject jsonObject) throws JSONException
    {
        tag = jsonObject.getString(KEY_TAG);
    }

    public String getTag()
    {
        return tag;
    }

    public JSONObject toJSONObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_TAG, tag);

        return jsonObject;
    }

    @Override
    public String toString()
    {
        return tag;
    }
}
