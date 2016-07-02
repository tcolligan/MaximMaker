package com.tcolligan.maximmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.List;
import java.util.Locale;

/**
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedAdapter extends RecyclerView.Adapter<MaximFeedAdapter.MaximViewHolder>
{
    private List<Maxim> maximList;

    public MaximFeedAdapter(List<Maxim> maximList)
    {
        this.maximList = maximList;
    }

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
        Maxim maxim = maximList.get(position);

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
    }

    @Override
    public int getItemCount()
    {
        return maximList.size();
    }

    public static class MaximViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageTextView;
        public TextView authorTextView;
        public TextView tagsTextView;

        public MaximViewHolder(View rootView)
        {
            super(rootView);
            messageTextView = (TextView) rootView.findViewById(R.id.messageTextView);
            authorTextView = (TextView) rootView.findViewById(R.id.authorTextView);
            tagsTextView = (TextView) rootView.findViewById(R.id.tagsTextView);
        }
    }
}
