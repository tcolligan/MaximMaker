package com.tcolligan.maximmaker;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.Arrays;
import java.util.List;

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
            Toast toast = Toast.makeText(getApplicationContext(), R.string.add_maxim_error_toast_text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0 ,0);
            toast.show();
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
}
