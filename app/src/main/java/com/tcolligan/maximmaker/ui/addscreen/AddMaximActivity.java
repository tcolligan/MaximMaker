package com.tcolligan.maximmaker.ui.addscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.domain.add.MaximViewModel;
import com.tcolligan.maximmaker.ui.addscreen.AddMaximPresenter.AddMaximView;

/**
 * An activity that allows users to add their own custom Maxims.
 * <p>
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class AddMaximActivity extends AppCompatActivity implements AddMaximView
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private static final String KEY_EDIT_MAXIM_ID = "KEY_EDIT_MAXIM_ID";
    private ProgressBar progressBar;
    private ScrollView contentScrollView;
    private EditText maximEditText;
    private EditText authorEditText;
    private EditText tagsEditText;
    private AddMaximPresenter presenter;

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, AddMaximActivity.class);
        context.startActivity(starter);
    }

    public static void startToEditMaxim(Context context, long maximId)
    {
        Intent starter = new Intent(context, AddMaximActivity.class);
        starter.putExtra(AddMaximActivity.KEY_EDIT_MAXIM_ID, maximId);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Life-cycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maxim);

        findViews();
        setupActionBar();

        presenter = new AddMaximPresenter();
        presenter.attachView(this);

        checkForEditableMaximId();
    }

    private void checkForEditableMaximId()
    {
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            long id = extras.getLong(KEY_EDIT_MAXIM_ID);
            presenter.onHasMaximToEdit(id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_maxim_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                onSaveClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    //==============================================================================================
    // Button Click Methods
    //==============================================================================================

    private void onSaveClicked()
    {
        String maxim = maximEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String tags = tagsEditText.getText().toString();

        presenter.onSaveClicked(maxim, author, tags, System.currentTimeMillis());
    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    private void findViews()
    {
        progressBar = findViewById(R.id.progressBar);
        contentScrollView = findViewById(R.id.contentScrollView);
        maximEditText = findViewById(R.id.maximEditText);
        authorEditText = findViewById(R.id.authorEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //==============================================================================================
    // AddMaximView Implementation Methods
    //==============================================================================================

    @Override
    public void showLoading()
    {
        progressBar.setVisibility(View.VISIBLE);
        contentScrollView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading()
    {
        progressBar.setVisibility(View.GONE);
        contentScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMaxim(MaximViewModel viewModel)
    {
        maximEditText.setText(viewModel.getMessage());

        if (viewModel.hasAuthor())
        {
            authorEditText.setText(viewModel.getAuthor());
        }

        if (viewModel.hasTags())
        {
            tagsEditText.setText(viewModel.getTags());
        }
    }

    @Override
    public void showMaximRequiresMessageErrorDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.add_maxim_error_text)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }
}
