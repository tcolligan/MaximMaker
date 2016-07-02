package com.tcolligan.maximmaker;

import android.content.Context;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedPresenter
{
    private Context context;
    private MaximFeed maximFeed;
    private List<Maxim> maximList;

    public MaximFeedPresenter(Context context, MaximFeed maximFeed)
    {
        this.context = context.getApplicationContext();
        this.maximFeed = maximFeed;

        maximList = new ArrayList<>();
    }

    public void onResume()
    {
        maximFeed.showLoadingState();

        MaximManager.getInstance().loadMaxims(context, new MaximManager.MaximsLoadedListener()
        {
            @Override
            public void onMaximsLoaded(List<Maxim> loadedMaximList)
            {
                maximList.clear();

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
                    maximList.addAll(loadedMaximList);
                    maximFeed.showMaxims(maximList);
                }
            }
        });
    }

    public interface MaximFeed
    {
        void showLoadingState();
        void showEmptyState();
        void showLoadingError();
        void showMaxims(List<Maxim> maximList);
    }
}
