package com.tcolligan.maximmaker.ui.feedscreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.ui.addscreen.AddMaximActivity;

import java.util.List;

/**
 * An activity that displays all of the Maxims that the user has saved in a feed style UI.
 * <p>
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedActivity extends AppCompatActivity implements MaximFeedPresenter.MaximFeedView
{
    //region Class Properties
    private ProgressBar progressBar;
    private TextView messageTextView;
    private RecyclerView recyclerView;
    private MaximFeedAdapter maximFeedAdapter;
    private MaximFeedPresenter maximFeedPresenter;
    //endregion

    //region Life-Cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxim_feed);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MaximFeedItemDecorator());

        maximFeedPresenter = new MaximFeedPresenter(getApplicationContext(), this);
        maximFeedAdapter = new MaximFeedAdapter(maximFeedPresenter);
        recyclerView.setAdapter(maximFeedAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maxim_feed_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

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
                maximFeedPresenter.onSearch(newText);
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                maximFeedPresenter.onSearchOpened();
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
        maximFeedPresenter.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        maximFeedPresenter = null;
    }
    //endregion


    //region Button onClick Methods
    @SuppressWarnings("UnusedParameters")
    public void onAddMaximButtonClicked(View v)
    {
        maximFeedPresenter.onAddMaximButtonClicked();
    }
    //endregion

    //region MapFeedView Implementation
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
        maximFeedAdapter.setMaximList(maximList);
        maximFeedAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEditOrDeleteMaximDialog(final Maxim maxim)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.edit_of_delete_maxim_message)
                .setPositiveButton(R.string.edit_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onEditMaxim(maxim);
                    }
                })
                .setNegativeButton(R.string.delete_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onDeleteMaxim(maxim);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showConfirmMaximDeletionDialog(final Maxim maxim)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_maxim_dialog_message)
                .setPositiveButton(R.string.delete_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onDeleteMaximConfirmed(maxim);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void showAddMaximScreen()
    {
        Intent intent = new Intent(this, AddMaximActivity.class);
        startActivity(intent);
    }

    @Override
    public void showEditMaximScreen(Maxim maximToEdit)
    {
        Intent intent = new Intent(this, AddMaximActivity.class);
        intent.putExtra(AddMaximActivity.KEY_EDIT_MAXIM_UUID, maximToEdit.getUuid());
        startActivity(intent);
    }
    //endregion
}
