package com.tcolligan.maximmaker.ui.feedscreen;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tcolligan.maximmaker.R;

/**
 * An ItemDecoration used for the RecyclerView in {@link MaximFeedActivity}
 * <p/>
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
class MaximFeedItemDecorator extends RecyclerView.ItemDecoration
{
    //region ItemDecoration Methods
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        // Add top margin to the first item in the list
        if (parent.getChildAdapterPosition(view) == 0)
        {
            outRect.top = (int) view.getContext().getResources().getDimension(R.dimen.half_default_margin);
        }

        // The last item in the list needs extra separation so that it is not blocked by the FAB
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1)
        {
            outRect.bottom = (int) view.getContext().getResources().getDimension(R.dimen.feed_fab_padding);
        }
        else
        {
            outRect.bottom = (int) view.getContext().getResources().getDimension(R.dimen.half_default_margin);
        }
    }
    //endregion
}
