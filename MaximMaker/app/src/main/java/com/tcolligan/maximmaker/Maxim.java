package com.tcolligan.maximmaker;

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
    private static final String TO_STRING_SEPARATOR = ", ";

    private String message;
    private String author;
    private List<MaximTag> maximTags;
    private long creationTimestamp;

    public Maxim(String message, String author, List<MaximTag> maximTags)
    {
        this.message = message;
        this.author = author;
        this.maximTags = maximTags;
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
            maximTags = new ArrayList<>();

            for (int i = 0; i < tagsArray.length(); i++)
            {
                JSONObject jsonObjectTag = tagsArray.getJSONObject(i);
                MaximTag maximTag = new MaximTag(jsonObjectTag);
                maximTags.add(maximTag);
            }
        }
    }

    public boolean hasAuthor()
    {
        return author != null;
    }

    public boolean hasTags()
    {
        return maximTags != null;
    }

    public String getMessage()
    {
        return message;
    }

    public String getAuthor()
    {
        return author;
    }

    public List<MaximTag> getMaximTags()
    {
        return maximTags;
    }

    public long getCreationTimestamp()
    {
        return creationTimestamp;
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

        if (maximTags != null)
        {
            JSONArray jsonArray = new JSONArray();

            for (MaximTag maximTag : maximTags)
            {
                jsonArray.put(maximTag.toJSONObject());
            }

            jsonObject.put(KEY_TAGS, jsonArray);
        }

        return jsonObject;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(message);
        stringBuilder.append(TO_STRING_SEPARATOR);

        if (author != null)
        {
            stringBuilder.append(author);
            stringBuilder.append(TO_STRING_SEPARATOR);
        }

        if (maximTags != null)
        {
            int len = maximTags.size();
            for (int i = 0; i < len; i++)
            {
                MaximTag maximTag = maximTags.get(i);
                if (i == len - 1)
                {
                    stringBuilder.append(maximTag.toString());
                }
                else
                {
                    stringBuilder.append(maximTag.toString());
                    stringBuilder.append(TO_STRING_SEPARATOR);
                }
            }
        }

        stringBuilder.append(new Date(creationTimestamp).toString());

        return stringBuilder.toString();
    }
}
