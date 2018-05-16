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
import com.tcolligan.maximmaker.domain.feed.MaximFeedItemViewModel;
import com.tcolligan.maximmaker.domain.feed.MaximFeedPresenter;
import com.tcolligan.maximmaker.ui.addscreen.AddMaximActivity;

import java.util.List;

/**
 * An activity that displays all of the Maxims that the user has saved in a feed style UI.
 * <p/>
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class MaximFeedActivity extends AppCompatActivity implements MaximFeedPresenter.MaximFeedView, MaximFeedAdapter.MaximViewHolderListener
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private ProgressBar progressBar;
    private TextView messageTextView;
    private RecyclerView recyclerView;
    private MaximFeedAdapter maximFeedAdapter;
    private MaximFeedPresenter maximFeedPresenter;

    //==============================================================================================
    // Life-Cycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxim_feed);

        findAllViews();
        setupRecyclerView();
        setupPresenterAndAdapter();
    }

    private void findAllViews()
    {
        progressBar = findViewById(R.id.progressBar);
        messageTextView = findViewById(R.id.messageTextView);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView()
    {
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MaximFeedItemDecorator());
    }

    private void setupPresenterAndAdapter()
    {
        maximFeedPresenter = new MaximFeedPresenter();
        maximFeedPresenter.attachView(this);

        maximFeedAdapter = new MaximFeedAdapter(this);
        recyclerView.setAdapter(maximFeedAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maxim_feed_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        setupSearchItemListener(searchItem);
        setupSearchQueryListener(searchView);

        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        maximFeedPresenter.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.export:
            {
                maximFeedPresenter.onExportClicked();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        maximFeedPresenter.detachView();
        maximFeedPresenter = null;
    }

    //==============================================================================================
    // Button onClick Methods
    //==============================================================================================

    @SuppressWarnings("UnusedParameters")
    public void onAddMaximButtonClicked(View v)
    {
        maximFeedPresenter.onAddMaximButtonClicked();
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================


    private void setupSearchQueryListener(SearchView searchView)
    {
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
    }

    private void setupSearchItemListener(MenuItem searchItem)
    {
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
    }

    //==============================================================================================
    // MaximViewHolderListener Implementation
    //==============================================================================================

    @Override
    public void onLongClick(final MaximFeedItemViewModel viewModel)
    {
        maximFeedPresenter.onMaximLongClick(viewModel);
    }

    //==============================================================================================
    // MapFeedView Implementation
    //==============================================================================================

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
    public void showMaxims(List<MaximFeedItemViewModel> viewModels)
    {
        maximFeedAdapter.setMaximFeedItemViewModelList(viewModels);
        maximFeedAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEditOrDeleteMaximDialog(final int maximId)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.edit_of_delete_maxim_message)
                .setPositiveButton(R.string.edit_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onEditMaxim(maximId);
                    }
                })
                .setNegativeButton(R.string.delete_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onDeleteMaxim(maximId);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showConfirmMaximDeletionDialog(final int maximId)
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_maxim_dialog_message)
                .setPositiveButton(R.string.delete_button_text, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        maximFeedPresenter.onDeleteMaximConfirmed(maximId);
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
    public void showEditMaximScreen(int maximId)
    {
        AddMaximActivity.startToEditMaxim(this, maximId);
    }

    /*@Override
    public void exportMaximsToEmail(List<Maxim> maximList)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Maxim maxim : maximList)
        {
            stringBuilder.append(convertMaximToExportText(getApplicationContext(), maxim));
            stringBuilder.append("\n\n");
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());

        if (intent.resolveActivity(getPackageManager()) == null)
        {
            Toast toast = Toast.makeText(this, R.string.no_email_client_installed, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else
        {
            startActivity(intent);
        }
    }*/

    //==============================================================================================
    // Helper Methods
    //==============================================================================================

    /*private static  String convertMaximToExportText(Context context, Maxim maxim)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(maxim.getMessage());
        stringBuilder.append(EXPORT_SEPARATOR);

        if (maxim.hasAuthor())
        {
            stringBuilder.append(context.getString(R.string.export_author_label));
            stringBuilder.append(" ");
            stringBuilder.append(maxim.getAuthor());
            stringBuilder.append(EXPORT_SEPARATOR);
        }

        if (maxim.hasTags())
        {
            stringBuilder.append(context.getString(R.string.export_tags_label));
            stringBuilder.append(" ");
            stringBuilder.append(maxim.getTagsCommaSeparated());
            stringBuilder.append(EXPORT_SEPARATOR);
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        simpleDateFormat.setTimeZone(timeZone);
        Date date = new Date(maxim.getCreationTimestamp());

        stringBuilder.append(context.getString(R.string.export_recorded_date_label));
        stringBuilder.append(" ");
        stringBuilder.append(simpleDateFormat.format(date));

        return stringBuilder.toString();
    }*/

}
