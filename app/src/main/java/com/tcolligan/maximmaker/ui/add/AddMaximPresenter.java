package com.tcolligan.maximmaker.ui.add;

import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.domain.add.AddMaximUseCase;
import com.tcolligan.maximmaker.domain.add.EditMaximUseCase;
import com.tcolligan.maximmaker.domain.add.MaximViewModel;

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

    private final AddMaximUseCase addMaximUseCase;
    private final EditMaximUseCase editMaximUseCase;
    private AddMaximView view;
    private long maximToEditId;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    AddMaximPresenter()
    {
        addMaximUseCase = new AddMaximUseCase();
        editMaximUseCase = new EditMaximUseCase();
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

    public void onHasMaximToEdit(long maximToEditId)
    {
        this.maximToEditId = maximToEditId;
        view.showLoading();

        editMaximUseCase.findMaximToEdit(maximToEditId, new Callback<MaximViewModel>()
        {
            @Override
            public void onSuccess(MaximViewModel maximViewModel)
            {
                if (view != null)
                {
                    view.dismissLoading();
                    view.showMaxim(maximViewModel);
                }
            }
        });
    }

    public void onSaveClicked(String message, String author, String tags, long timestamp)
    {
        if (TextUtils.isEmpty(message))
        {
            view.showMaximRequiresMessageErrorDialog();
            return;
        }

        if (maximToEditId == 0)
        {
            addMaximUseCase.addNewMaxim(message, author, tags, timestamp);
        }
        else
        {
            editMaximUseCase.saveEditedMaxim(maximToEditId, message, author, tags);
        }

        view.finish();
    }

    //==============================================================================================
    // AddMaximView Interface
    //==============================================================================================

    public interface AddMaximView
    {
        void showLoading();

        void dismissLoading();

        void showMaxim(MaximViewModel viewModel);

        void showMaximRequiresMessageErrorDialog();

        void finish();
    }

}
