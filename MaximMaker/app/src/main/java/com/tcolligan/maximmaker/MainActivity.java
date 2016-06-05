package com.tcolligan.maximmaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tcolligan.maximmaker.data.MaximManager;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaximManager.getInstance().loadMaxims(getApplicationContext(), new MaximManager.MaximsLoadedListener()
        {
            @Override
            public void onMaximsLoaded(boolean success)
            {
                if (success)
                {
                    // TODO: Refresh list of maxims
                }
                else
                {
                    // TODO: Show error toast
                }
            }
        });
    }

    public void onAddMaximButtonClicked(View v)
    {
        Intent intent = new Intent(this, AddMaximActivity.class);
        startActivity(intent);
    }
}
