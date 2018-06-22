package com.tcolligan.maximmaker.ui.feed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.domain.feed.MaximFeedItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter used to display CardViews with Maxims on the in {@link MaximFeedActivity}
 * <p>
 * Created on 7/2/2016.
 *
 * @author Thomas Colligan
 */
class MaximFeedAdapter extends RecyclerView.Adapter<MaximFeedAdapter.MaximViewHolder>
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private List<MaximFeedItemViewModel> maximFeedItemViewModelList;
    private final MaximViewHolderListener maximViewHolderListener;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    MaximFeedAdapter(MaximViewHolderListener maximViewHolderListener)
    {
        this.maximFeedItemViewModelList = new ArrayList<>();
        this.maximViewHolderListener = maximViewHolderListener;
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    void setMaximFeedItemViewModelList(List<MaximFeedItemViewModel> maximFeedItemViewModelList)
    {
        this.maximFeedItemViewModelList = maximFeedItemViewModelList;
    }

    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================

    @NonNull
    @Override
    public MaximViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_maxim, parent, false);

        return new MaximViewHolder(view);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MaximViewHolder holder, int position)
    {
        final MaximFeedItemViewModel maximFeedItemViewModel = maximFeedItemViewModelList.get(position);

        holder.messageTextView.setText(maximFeedItemViewModel.getMessageText());
        holder.authorTextView.setText(maximFeedItemViewModel.getAuthorText());
        holder.tagsTextView.setText(maximFeedItemViewModel.getTagsText());

        holder.authorTextView.setVisibility(maximFeedItemViewModel.getAuthorTextViewVisibility());
        holder.tagsTextView.setVisibility(maximFeedItemViewModel.getTagsTextViewVisibility());

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (maximViewHolderListener != null)
                {
                    maximViewHolderListener.onLongClick(maximFeedItemViewModel);
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return maximFeedItemViewModelList.size();
    }

    //==============================================================================================
    // MaximViewHolder Class
    //==============================================================================================

    static class MaximViewHolder extends RecyclerView.ViewHolder
    {
        final View rootView;
        final TextView messageTextView;
        final TextView authorTextView;
        final TextView tagsTextView;

        MaximViewHolder(View rootView)
        {
            super(rootView);

            this.rootView = rootView;
            messageTextView = rootView.findViewById(R.id.messageTextView);
            authorTextView = rootView.findViewById(R.id.authorTextView);
            tagsTextView = rootView.findViewById(R.id.tagsTextView);
        }
    }

    //==============================================================================================
    // MaximViewHolderListener Interface
    //==============================================================================================

    public interface MaximViewHolderListener
    {
        void onLongClick(MaximFeedItemViewModel viewModel);
    }

}
