package com.tcolligan.maximmaker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * An activity that allows users to add their own custom Maxims.
 *
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class AddMaximActivity extends AppCompatActivity implements AddMaximPresenter.AddMaximView
{
    public static final String KEY_MAXIM_UUID = "kMaximUuid";

    private EditText maximEditText;
    private EditText authorEditText;
    private EditText tagsEditText;

    private AddMaximPresenter addMaximPresenter;

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
            case R.id.done:
                onDoneClicked();
                return true;
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

    private void onDoneClicked()
    {
        String maxim = maximEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String tags = tagsEditText.getText().toString();

        addMaximPresenter.onDoneClicked(maxim, author, tags);
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
