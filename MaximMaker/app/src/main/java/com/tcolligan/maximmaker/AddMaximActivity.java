package com.tcolligan.maximmaker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.Arrays;
import java.util.List;

/**
 * An activity that allows users to add their own custom Maxims.
 *
 * Created on 5/30/2016.
 *
 * @author Thomas Colligan
 */
public class AddMaximActivity extends AppCompatActivity
{
    private static final String TAG = "AddMaximActivity";
    private EditText maximEditText;
    private EditText authorEditText;
    private EditText tagsEditText;

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

    private void onDoneClicked()
    {
        String maxim = maximEditText.getText().toString();
        String author = authorEditText.getText().toString();
        String tags = tagsEditText.getText().toString();

        if (TextUtils.isEmpty(maxim))
        {
            showAddMaximErrorDialog();
            return;
        }

        saveNewMaxim(maxim, author, tags);
        finish();
    }

    private void saveNewMaxim(String message, String author, String tags)
    {
        if (TextUtils.isEmpty(message))
        {
            return;
        }

        if (TextUtils.isEmpty(author))
        {
            author = null;
        }

        List<String> tagsList = null;

        if (!TextUtils.isEmpty(tags))
        {
            String[] tagArray = tags.split(",");

            for (int i = 0; i < tagArray.length; i++)
            {
                tagArray[i] = tagArray[i].trim();
            }

            tagsList = Arrays.asList(tagArray);
        }

        Maxim maxim = new Maxim(message, author, tagsList);
        MaximManager.getInstance().addAndSaveMaxim(getApplicationContext(), maxim);
        Log.d(TAG, "Added maxim: " + maxim.toString());
    }

    private void showAddMaximErrorDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.add_maxim_error_text)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }
}
