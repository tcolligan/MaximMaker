package com.tcolligan.maximmaker.domain.add;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;

/**
 * A use case for adding a brand new maxim.
 * <p>
 * Created on 6/11/18.
 *
 * @author Thomas Colligan
 */
public class AddMaximUseCase
{
    private final MaximRepository maximRepository;

    public AddMaximUseCase()
    {
        maximRepository = RepositoryManager.getInstance().getMaximRepository();
    }

    public void addNewMaxim(@NonNull String message,
                            @Nullable String author,
                            @Nullable String tags,
                            long timestamp)
    {
        message = message.trim();
        author = TextUtils.isEmpty(author) ? null : author.trim();
        tags = TextUtils.isEmpty(tags) ? null : tags.trim();

        Maxim maxim = new Maxim(message, author, tags, timestamp);
        maximRepository.addMaxim(maxim);
    }
}
