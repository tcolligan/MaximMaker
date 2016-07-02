package com.tcolligan.maximmaker;

import android.content.Context;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.List;

/**
 * A presenter class to handle some of the logic for {@link MaximFeedActivity}
 *
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedPresenter
{
    private Context context;
    private MaximFeed maximFeed;
    private MaximManager maximManager;
    private boolean didShowLoadingState;

    public MaximFeedPresenter(Context context, MaximFeed maximFeed)
    {
        this.context = context.getApplicationContext();
        this.maximFeed = maximFeed;
        this.maximManager = MaximManager.getInstance();
    }

    public void onResume()
    {
        if (!didShowLoadingState)
        {
            maximFeed.showLoadingState();
            didShowLoadingState = true;
        }

        maximManager.loadMaxims(context, new MaximManager.MaximsLoadedListener()
        {
            @Override
            public void onMaximsLoaded(List<Maxim> loadedMaximList)
            {
                if (loadedMaximList == null)
                {
                    maximFeed.showLoadingError();
                }
                else if (loadedMaximList.size() == 0)
                {
                    maximFeed.showEmptyState();
                }
                else
                {
                    maximFeed.showMaxims(loadedMaximList);
                }
            }
        });
    }

    public void onDeleteMaxim(Maxim maxim)
    {
        MaximManager.getInstance().deleteMaxim(context, maxim);
        maximFeed.showMaxims(maximManager.getMaximList());
    }

    public interface MaximFeed
    {
        void showLoadingState();
        void showEmptyState();
        void showLoadingError();
        void showMaxims(List<Maxim> maximList);
    }
}
