package com.tcolligan.maximmaker.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A model that represents a specific maxim or quote.
 *
 * Created on 3/28/2016.
 *
 * @author Thomas Colligan
 */
public class Maxim
{
    private static final String KEY_MESSAGE = "kMessage";
    private static final String KEY_AUTHOR = "kAuthor";
    private static final String KEY_TAGS = "kTags";
    private static final String KEY_CREATION_TIMESTAMP = "kCreationTimestamp";
    private static final String TO_STRING_SEPARATOR = "\n\n";

    private String message;
    private String author;
    private List<String> tagsList;
    private long creationTimestamp;

    public Maxim(String message, String author, List<String> tagsList)
    {
        this.message = message;
        this.author = author;
        this.tagsList = tagsList;
        this.creationTimestamp = System.currentTimeMillis();
    }

    public Maxim(JSONObject jsonObject) throws JSONException
    {
        message = jsonObject.getString(KEY_MESSAGE);
        author = jsonObject.optString(KEY_AUTHOR, null);
        creationTimestamp = jsonObject.getInt(KEY_CREATION_TIMESTAMP);

        JSONArray tagsArray = jsonObject.optJSONArray(KEY_TAGS);

        if (tagsArray != null)
        {
            tagsList = new ArrayList<>();

            for (int i = 0; i < tagsArray.length(); i++)
            {
                String tag = tagsArray.getString(i);
                tagsList.add(tag);
            }
        }
    }

    public boolean hasAuthor()
    {
        return author != null;
    }

    public boolean hasTags()
    {
        return tagsList != null;
    }

    public String getMessage()
    {
        return message;
    }

    public String getAuthor()
    {
        return author;
    }

    public List<String> getTagsList()
    {
        return tagsList;
    }

    public long getCreationTimestamp()
    {
        return creationTimestamp;
    }

    public String getTagsCommaSeparated()
    {
        return TextUtils.join(",", tagsList);
    }

    public JSONObject toJSONObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(KEY_MESSAGE, message);

        if (author != null)
        {
            jsonObject.put(KEY_AUTHOR, author);
        }

        jsonObject.put(KEY_CREATION_TIMESTAMP, creationTimestamp);

        if (tagsList != null)
        {
            JSONArray jsonArray = new JSONArray();

            for (String tag : tagsList)
            {
                jsonArray.put(tag);
            }

            jsonObject.put(KEY_TAGS, jsonArray);
        }

        return jsonObject;
    }

    @Override
    public String toString()
    {
        // For debugging only
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(message);
        stringBuilder.append(TO_STRING_SEPARATOR);

        if (author != null)
        {
            stringBuilder.append(author);
            stringBuilder.append(TO_STRING_SEPARATOR);
        }

        if (tagsList != null)
        {
            int len = tagsList.size();
            for (int i = 0; i < len; i++)
            {
                String tag = tagsList.get(i);
                if (i == len - 1)
                {
                    stringBuilder.append(tag);
                }
                else
                {
                    stringBuilder.append(getTagsCommaSeparated());
                    stringBuilder.append(TO_STRING_SEPARATOR);
                }
            }
        }

        stringBuilder.append(new Date(creationTimestamp).toString());

        return stringBuilder.toString();
    }
}
