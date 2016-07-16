package com.tcolligan.maximmaker.ui.feedscreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.data.Maxim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An adapter used to display CardViews with Maxims on the in {@link MaximFeedActivity}
 * <p/>
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedAdapter extends RecyclerView.Adapter<MaximFeedAdapter.MaximViewHolder>
{
    //region Class Properties
    private List<Maxim> maximList;
    private final MaximViewHolderListener maximViewHolderListener;
    //endregion

    //region Constructor
    public MaximFeedAdapter(MaximViewHolderListener maximViewHolderListener)
    {
        this.maximList = new ArrayList<>();
        this.maximViewHolderListener = maximViewHolderListener;
    }
    //endregion

    //region Class Instance Methods
    public void setMaximList(List<Maxim> maximList)
    {
        this.maximList = maximList;
    }
    //endregion

    //region RecyclerView.Adapter Methods
    @Override
    public MaximViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_maxim, parent, false);

        return new MaximViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MaximViewHolder holder, int position)
    {
        final Maxim maxim = maximList.get(position);

        holder.messageTextView.setText(maxim.getMessage());
        if (maxim.hasAuthor())
        {
            holder.authorTextView.setText(String.format(Locale.getDefault(), "- %s", maxim.getAuthor()));
            holder.authorTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.authorTextView.setVisibility(View.GONE);
        }

        if (maxim.hasTags())
        {
            holder.tagsTextView.setText(maxim.getTagsCommaSeparated());
            holder.tagsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tagsTextView.setVisibility(View.GONE);
        }

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (maximViewHolderListener != null)
                {
                    maximViewHolderListener.onLongClick(maxim);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return maximList.size();
    }
    //endregion

    //region MaximViewHolder Class
    public static class MaximViewHolder extends RecyclerView.ViewHolder
    {
        public final View rootView;
        public final TextView messageTextView;
        public final TextView authorTextView;
        public final TextView tagsTextView;

        public MaximViewHolder(View rootView)
        {
            super(rootView);

            this.rootView = rootView;
            messageTextView = (TextView) rootView.findViewById(R.id.messageTextView);
            authorTextView = (TextView) rootView.findViewById(R.id.authorTextView);
            tagsTextView = (TextView) rootView.findViewById(R.id.tagsTextView);
        }
    }
    //endregion

    //region MaximViewHolderListener Interface
    public interface MaximViewHolderListener
    {
        void onLongClick(Maxim maxim);
    }
    //endregion
}
