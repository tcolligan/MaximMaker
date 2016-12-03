package com.tcolligan.maximmaker.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A model that represents a specific maxim or quote.
 * <p/>
 * Created on 3/28/2016.
 *
 * @author Thomas Colligan
 */
public class Maxim
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final String KEY_UUID = "kUuid";
    private static final String KEY_MESSAGE = "kMessage";
    private static final String KEY_AUTHOR = "kAuthor";
    private static final String KEY_TAGS = "kTags";
    private static final String KEY_CREATION_TIMESTAMP = "kCreationTimestamp";
    private static final String COMMA_SEPARATOR = ", ";
    private static final String TO_STRING_SEPARATOR = " | ";

    private String uuid;
    private String message;
    private String author;
    private List<String> tagsList;
    private long creationTimestamp;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    public Maxim(String message, String author, List<String> tagsList)
    {
        this.message = message;
        this.author = author;
        this.tagsList = tagsList;

        this.uuid = UUID.randomUUID().toString();
        this.creationTimestamp = System.currentTimeMillis();
    }

    public Maxim(JSONObject jsonObject) throws JSONException
    {
        uuid = jsonObject.getString(KEY_UUID);
        message = jsonObject.getString(KEY_MESSAGE);
        author = jsonObject.optString(KEY_AUTHOR, null);
        creationTimestamp = jsonObject.getLong(KEY_CREATION_TIMESTAMP);

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

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public boolean hasAuthor()
    {
        return author != null;
    }

    public boolean hasTags()
    {
        return tagsList != null;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setTagsList(List<String> tagsList)
    {
        this.tagsList = tagsList;
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
        return TextUtils.join(COMMA_SEPARATOR, tagsList);
    }

    public JSONObject toJSONObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(KEY_UUID, uuid);
        jsonObject.put(KEY_MESSAGE, message);

        if (hasAuthor())
        {
            jsonObject.put(KEY_AUTHOR, author);
        }

        jsonObject.put(KEY_CREATION_TIMESTAMP, creationTimestamp);

        if (hasTags())
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
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(uuid);
        stringBuilder.append(TO_STRING_SEPARATOR);

        stringBuilder.append(message);
        stringBuilder.append(TO_STRING_SEPARATOR);

        if (hasAuthor())
        {
            stringBuilder.append(author);
            stringBuilder.append(TO_STRING_SEPARATOR);
        }

        if (hasTags())
        {
            stringBuilder.append(getTagsCommaSeparated());
            stringBuilder.append(TO_STRING_SEPARATOR);
        }

        stringBuilder.append(new Date(creationTimestamp).toString());

        return stringBuilder.toString();
    }

}
