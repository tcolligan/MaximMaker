package com.tcolligan.maximmaker;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedItemDecorator extends RecyclerView.ItemDecoration
{
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        // Add top margin to the first item in the list
        if(parent.getChildAdapterPosition(view) == 0)
        {
            outRect.top = (int) view.getContext().getResources().getDimension(R.dimen.half_default_margin);
        }

        // The last item in the list needs extra separation so that it is not blocked by the FAB
        if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1)
        {
            outRect.bottom = (int) view.getContext().getResources().getDimension(R.dimen.feed_fab_padding);
        }
        else
        {
            outRect.bottom = (int) view.getContext().getResources().getDimension(R.dimen.half_default_margin);
        }
    }
}
