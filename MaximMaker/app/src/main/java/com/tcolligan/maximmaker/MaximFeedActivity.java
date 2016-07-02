package com.tcolligan.maximmaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity that displays all of the Maxims that the user has saved in a feed style UI.
 *
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedActivity extends AppCompatActivity implements MaximFeedPresenter.MaximFeed, MaximFeedAdapter.MaximFeedListener
{
    private ProgressBar progressBar;
    private TextView messageTextView;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private MaximFeedPresenter maximFeedPresenter;
    private MaximFeedAdapter maximFeedAdapter;
    private List<Maxim> maximList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxim_feed);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MaximFeedItemDecorator());

        maximFeedPresenter = new MaximFeedPresenter(getApplicationContext(), this);
        maximList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maxim_feed_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                maximFeedPresenter.onSearchForText(newText);
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                maximFeedPresenter.onSearchClosed();
                return true;
            }
        });

        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        String currentSearch = searchView == null ? "" : searchView.getQuery().toString();
        boolean isSearching = searchView != null && !searchView.isIconified();
        maximFeedPresenter.onResume(currentSearch, isSearching);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        maximFeedPresenter = null;
    }

    public void onAddMaximButtonClicked(View v)
    {
        Intent intent = new Intent(this, AddMaximActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLongClick(final Maxim maxim)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_maxim_dialog_message)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onDeleteMaxim(maxim);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void showLoadingState()
    {
        progressBar.setVisibility(View.VISIBLE);
        messageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyState()
    {
        messageTextView.setText(R.string.no_maxims_text);

        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingError()
    {
        messageTextView.setText(R.string.error_loading_maxims_text);

        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showMaxims(List<Maxim> maximList)
    {
        this.maximList.clear();
        this.maximList.addAll(maximList);

        if (maximFeedAdapter == null)
        {
            maximFeedAdapter = new MaximFeedAdapter(this.maximList, this);
            recyclerView.setAdapter(maximFeedAdapter);
        }
        else
        {
            maximFeedAdapter.notifyDataSetChanged();
        }

        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
