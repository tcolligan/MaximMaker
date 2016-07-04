package com.tcolligan.maximmaker;

import android.content.Context;
import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.Arrays;
import java.util.List;

/**
 * A presenter class to handle some of the logic for {@link AddMaximActivity}
 *
 * Created on 7/4/2016.
 *
 * @author Thomas Colligan
 */
public class AddMaximPresenter
{
    private static final String TAG = "AddMaximPresenter";
    private Context context;
    private AddMaximView addMaximView;

    public AddMaximPresenter(Context context, AddMaximView addMaximView)
    {
        this.context = context.getApplicationContext();
        this.addMaximView = addMaximView;
    }

    public void onDoneClicked(String maxim, String author, String tags)
    {

        if (TextUtils.isEmpty(maxim))
        {
            addMaximView.showAddMaximErrorDialog();
            return;
        }

        saveNewMaxim(maxim, author, tags);
        addMaximView.finish();
    }

    private void saveNewMaxim(String message, String author, String tags)
    {
        if (TextUtils.isEmpty(message))
        {
            return;
        }

        if (TextUtils.isEmpty(author))
        {
            author = null;
        }

        List<String> tagsList = null;

        if (!TextUtils.isEmpty(tags))
        {
            String[] tagArray = tags.split(",");

            for (int i = 0; i < tagArray.length; i++)
            {
                tagArray[i] = tagArray[i].trim();
            }

            tagsList = Arrays.asList(tagArray);
        }

        Maxim maxim = new Maxim(message, author, tagsList);
        MaximManager.getInstance().addAndSaveMaxim(context, maxim);
    }

    public interface AddMaximView
    {
        void showAddMaximErrorDialog();
        void finish();
    }
}
