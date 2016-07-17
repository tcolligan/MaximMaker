package com.tcolligan.maximmaker.ui.addscreen;

import android.content.Context;
import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.Arrays;
import java.util.List;

/**
 * A presenter class to handle some of the logic for {@link AddMaximActivity}
 * <p/>
 * Created on 7/4/2016.
 *
 * @author Thomas Colligan
 */
class AddMaximPresenter
{
    //region Class Properties
    private static final String TAGS_SEPARATOR = ",";
    private final Context context;
    private final AddMaximView addMaximView;
    private final MaximManager maximManager;
    private Maxim maximToEdit;
    //endregion

    //region Constructor
    public AddMaximPresenter(Context context, AddMaximView addMaximView)
    {
        this.context = context.getApplicationContext();
        this.addMaximView = addMaximView;
        this.maximManager = MaximManager.getInstance();
    }
    //endregion

    //region Presenter Action Methods
    public void onMaximToEditUuidFound(String maximToEditUuid)
    {
        if (maximToEditUuid != null)
        {
            maximToEdit = maximManager.findMaximWithUuid(maximToEditUuid);
            addMaximView.showMaxim(maximToEdit);
        }
    }

    public void onSaveClicked(String maxim, String author, String tags)
    {
        if (TextUtils.isEmpty(maxim))
        {
            addMaximView.showAddMaximErrorDialog();
            return;
        }

        if (maximToEdit == null)
        {
            saveNewMaxim(maxim, author, tags);
        }
        else
        {
            saveEditedMaxim(maxim, author, tags);
        }

        addMaximView.finish();
    }
    //endregion

    //region Class Instance Methods
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

        Maxim maxim = new Maxim(message, author, tagsToList(tags));
        maximManager.addAndSaveMaxim(context, maxim);
    }

    private void saveEditedMaxim(String message, String author, String tags)
    {
        if (TextUtils.isEmpty(message))
        {
            return;
        }

        if (TextUtils.isEmpty(author))
        {
            author = null;
        }

        maximToEdit.setMessage(message);
        maximToEdit.setAuthor(author);
        maximToEdit.setTagsList(tagsToList(tags));

        maximManager.saveMaxims(context);
    }
    //endregion

    //region Helper Methods
    private static List<String> tagsToList(String tags)
    {
        List<String> tagsList = null;

        if (!TextUtils.isEmpty(tags))
        {
            String[] tagArray = tags.split(TAGS_SEPARATOR);

            for (int i = 0; i < tagArray.length; i++)
            {
                tagArray[i] = tagArray[i].trim();
            }

            tagsList = Arrays.asList(tagArray);
        }

        return tagsList;
    }
    //endregion

    //region AddMaximView Interface
    public interface AddMaximView
    {
        void showMaxim(Maxim maxim);

        void showAddMaximErrorDialog();

        void finish();
    }
    //endregion
}
