package com.tcolligan.maximmaker.domain.add;

import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;

/**
 * A presenter class to handle some of the logic for AddMaximActivity
 * <p/>
 * Created on 7/4/2016.
 *
 * @author Thomas Colligan
 */
public class AddMaximPresenter
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private AddMaximView view;
    private final MaximRepository maximRepository;
    private Maxim maximToEdit;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public AddMaximPresenter()
    {
        this.maximRepository = RepositoryManager.getInstance().getMaximRepository();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void attachView(AddMaximView view)
    {
        this.view = view;
    }

    public void detachView()
    {
        this.view = null;
    }

    //==============================================================================================
    // Presenter Action Methods
    //==============================================================================================

    public void onMaximToEditUuidFound(int maximToEditUuid)
    {
        view.showLoading();

        maximRepository.findMaximById(maximToEditUuid, new Callback<Maxim>()
        {
            @Override
            public void onSuccess(Maxim data)
            {
                maximToEdit = data;
                MaximViewModel viewModel = MaximViewModelConverter.convertMaximToViewModel(maximToEdit);

                if (view != null)
                {
                    view.dismissLoading();
                    view.showMaxim(viewModel);
                }
            }
        });
    }

    public void onSaveClicked(String maxim, String author, String tags, long timestamp)
    {
        if (TextUtils.isEmpty(maxim))
        {
            view.showAddMaximErrorDialog();
            return;
        }

        if (maximToEdit == null)
        {
            saveNewMaxim(maxim, author, tags, timestamp);
        }
        else
        {
            saveEditedMaxim(maxim, author, tags);
        }

        view.finish();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    private void saveNewMaxim(String message, String author, String tags, long timestamp)
    {
        message = message.trim();
        author = author.trim();
        tags = tags.trim();

        if (TextUtils.isEmpty(message))
        {
            return;
        }

        if (TextUtils.isEmpty(author))
        {
            author = null;
        }

        Maxim maxim = new Maxim(message, author, tags, timestamp);
        maximRepository.addMaxim(maxim);
    }

    private void saveEditedMaxim(String message, String author, String tags)
    {
        message = message.trim();
        author = author.trim();
        tags = tags.trim();

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
        maximToEdit.setTags(tags);
        maximRepository.updateMaxim(maximToEdit);
    }

    //==============================================================================================
    // AddMaximView Interface
    //==============================================================================================

    public interface AddMaximView
    {
        void showLoading();

        void dismissLoading();

        void showMaxim(MaximViewModel viewModel);

        void showAddMaximErrorDialog();

        void finish();
    }

}
