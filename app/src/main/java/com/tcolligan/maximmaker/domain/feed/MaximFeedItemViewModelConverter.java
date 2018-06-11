package com.tcolligan.maximmaker.domain.feed;

import android.support.annotation.NonNull;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that converts data objects into view models.
 * <p>
 * Created on 5/16/18.
 *
 * @author Thomas Colligan
 */

public class MaximFeedItemViewModelConverter
{
    @NonNull
    static MaximFeedItemViewModel convertMaximToViewModel(@NonNull Maxim maxim)
    {
        MaximFeedItemViewModel viewModel = new MaximFeedItemViewModel(maxim.getId(),
                maxim.getMessage());

        viewModel.setAuthor(maxim.getAuthor());
        viewModel.setTags(maxim.getTags());

        return viewModel;
    }

    @NonNull
    static List<MaximFeedItemViewModel> convertMaximsToViewModels(@NonNull List<Maxim> maxims)
    {
        List<MaximFeedItemViewModel> viewModels = new ArrayList<>();

        for (Maxim maxim : maxims)
        {
            viewModels.add(convertMaximToViewModel(maxim));
        }

        return viewModels;
    }
}
