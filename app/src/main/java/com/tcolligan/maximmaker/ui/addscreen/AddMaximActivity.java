package com.tcolligan.maximmaker.ui.addscreen;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.data.Maxim;
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

    public static final String KEY_EDIT_MAXIM_UUID = "kEditMaximUuid";
    private EditText maximEditText;
    private EditText authorEditText;
    private EditText tagsEditText;
    private AddMaximPresenter addMaximPresenter;

    //==============================================================================================
    // Life-cycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maxim);

        maximEditText = (EditText) findViewById(R.id.maximEditText);
        authorEditText = (EditText) findViewById(R.id.authorEditText);
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addMaximPresenter = new AddMaximPresenter(getApplicationContext(), this);

        // Users can also edit an existing maxim if the want
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            String uuid = extras.getString(KEY_EDIT_MAXIM_UUID);
            addMaximPresenter.onMaximToEditUuidFound(uuid);
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
        addMaximPresenter = null;
    }

    //==============================================================================================
    // Button Click Methods
    //==============================================================================================

    private void onSaveClicked()
    {
        String maxim = maximEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String tags = tagsEditText.getText().toString();

        addMaximPresenter.onSaveClicked(maxim, author, tags);
    }

    //==============================================================================================
    // AddMaximView Implementation Methods
    //==============================================================================================

    @Override
    public void showMaxim(Maxim maxim)
    {
        maximEditText.setText(maxim.getMessage());

        if (maxim.hasAuthor())
        {
            authorEditText.setText(maxim.getAuthor());
        }

        if (maxim.hasTags())
        {
            tagsEditText.setText(maxim.getTagsCommaSeparated());
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
