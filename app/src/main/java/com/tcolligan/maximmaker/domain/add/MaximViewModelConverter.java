package com.tcolligan.maximmaker.domain.add;

import com.tcolligan.maximmaker.data.Maxim;

/**
 * A class that converts data objects into view models.
 * <p>
 * Created on 5/16/18.
 *
 * @author Thomas Colligan
 */

class MaximViewModelConverter
{
    static MaximViewModel convertMaximToViewModel(Maxim maxim)
    {
        MaximViewModel viewModel = new MaximViewModel(maxim.getId());

        viewModel.setMessage(maxim.getMessage());
        viewModel.setAuthor(maxim.getAuthor());
        viewModel.setTags(maxim.getTags());

        return viewModel;
    }
}
