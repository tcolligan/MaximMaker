package com.tcolligan.maximmaker.domain.feed;

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

public class MaximFeedItemViewModelConerter
{
    static MaximFeedItemViewModel convertMaximToViewModel(Maxim maxim)
    {
        MaximFeedItemViewModel viewModel = new MaximFeedItemViewModel(maxim.getId());

        viewModel.setMessage(maxim.getMessage());
        viewModel.setAuthor(maxim.getAuthor());
        viewModel.setTags(maxim.getTags());

        return viewModel;
    }

    static List<MaximFeedItemViewModel> convertMaximsToViewModels(List<Maxim> maxims)
    {
        List<MaximFeedItemViewModel> viewModels = new ArrayList<>();

        for (Maxim maxim : maxims)
        {
            viewModels.add(convertMaximToViewModel(maxim));
        }

        return viewModels;
    }
}
