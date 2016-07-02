package com.tcolligan.maximmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An adapter used to display CardViews with Maxims on the in {@link MaximFeedActivity}
 *
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedAdapter extends RecyclerView.Adapter<MaximFeedAdapter.MaximViewHolder>
{
    private List<Maxim> maximList;
    private List<Maxim> visibleMaximsList;
    private MaximFeedListener maximFeedListener;

    public MaximFeedAdapter(List<Maxim> maximList, MaximFeedListener maximFeedListener)
    {
        this.maximList = maximList;
        this.maximFeedListener = maximFeedListener;

        visibleMaximsList = new ArrayList<>();
        visibleMaximsList.addAll(maximList);
    }

    public void filter(String text)
    {
        if (text.isEmpty())
        {
            visibleMaximsList.clear();
            visibleMaximsList.addAll(maximList);
        }
        else
        {
            List<Maxim> searchResults = new ArrayList<>();
            text = text.toLowerCase();

            for (Maxim maxim : maximList)
            {
                if (maxim.getMessage().toLowerCase().contains(text) ||
                        (maxim.hasAuthor() && maxim.getAuthor().toLowerCase().contains(text)) ||
                        (maxim.hasTags() && maxim.getTagsCommaSeparated().toLowerCase().contains(text)))
                {
                    searchResults.add(maxim);
                }
            }

            visibleMaximsList.clear();
            visibleMaximsList.addAll(searchResults);
        }

        notifyDataSetChanged();
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
        final Maxim maxim = visibleMaximsList.get(position);

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
                if (maximFeedListener != null)
                {
                    maximFeedListener.onLongClick(maxim);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return visibleMaximsList.size();
    }

    public static class MaximViewHolder extends RecyclerView.ViewHolder
    {
        public View rootView;
        public TextView messageTextView;
        public TextView authorTextView;
        public TextView tagsTextView;

        public MaximViewHolder(View rootView)
        {
            super(rootView);

            this.rootView = rootView;
            messageTextView = (TextView) rootView.findViewById(R.id.messageTextView);
            authorTextView = (TextView) rootView.findViewById(R.id.authorTextView);
            tagsTextView = (TextView) rootView.findViewById(R.id.tagsTextView);
        }
    }

    public interface MaximFeedListener
    {
        void onLongClick(Maxim maxim);
    }
}
