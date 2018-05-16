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
import android.widget.EditText;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.domain.add.AddMaximPresenter;
import com.tcolligan.maximmaker.domain.add.AddMaximPresenter.AddMaximView;
import com.tcolligan.maximmaker.domain.add.MaximViewModel;

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

    public static void startToEditMaxim(Context context, int maximId)
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
            int id = extras.getInt(KEY_EDIT_MAXIM_ID);
            presenter.onMaximToEditUuidFound(id);
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
            {
                onBackPressed();
                return true;
            }
            case R.id.save:
            {
                onSaveClicked();
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
        // TODO: Do this
    }

    @Override
    public void dismissLoading()
    {
        // TODO: Do this
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
    public void showAddMaximErrorDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.add_maxim_error_text)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }
}
