package com.tcolligan.maximmaker.domain.add;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;

import java.lang.ref.WeakReference;

/**
 * A use case for editing an existing maxim.
 * <p>
 * Created on 6/11/18.
 *
 * @author Thomas Colligan
 */
public class EditMaximUseCase
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private final MaximRepository maximRepository;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public EditMaximUseCase()
    {
        maximRepository = RepositoryManager.getInstance().getMaximRepository();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void findMaximToEdit(long maximId, @NonNull Callback<MaximViewModel> callback)
    {
        final WeakReference<Callback<MaximViewModel>> weakReferenceCallback = new WeakReference<>(callback);

        maximRepository.findMaximById(maximId, new Callback<Maxim>()
        {
            @Override
            public void onSuccess(Maxim maxim)
            {
                MaximViewModel viewModel = MaximViewModelConverter.convertMaximToViewModel(maxim);

                Callback<MaximViewModel> callback = weakReferenceCallback.get();

                if (callback != null)
                {
                    callback.onSuccess(viewModel);
                }
            }
        });
    }

    public void saveEditedMaxim(long maximId,
                                 @NonNull String message,
                                 @Nullable String author,
                                 @Nullable String tags)
    {
        final String trimmedMessage = message.trim();
        final String trimmedAuthor = TextUtils.isEmpty(author) ? null : author.trim();
        final String trimmedTags = TextUtils.isEmpty(tags) ? null : tags.trim();

        maximRepository.findMaximById(maximId, new Callback<Maxim>()
        {
            @Override
            public void onSuccess(Maxim maximToEdit)
            {
                maximToEdit.setMessage(trimmedMessage);
                maximToEdit.setAuthor(trimmedAuthor);
                maximToEdit.setTags(trimmedTags);

                maximRepository.updateMaxim(maximToEdit);
            }
        });
    }
}
