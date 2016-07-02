package com.tcolligan.maximmaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcolligan.maximmaker.data.Maxim;

import java.util.List;

public class MaximFeedActivity extends AppCompatActivity implements MaximFeedPresenter.MaximFeed
{
    private ProgressBar progressBar;
    private TextView messageTextView;
    private RecyclerView recyclerView;
    private MaximFeedPresenter maximFeedPresenter;

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

        maximFeedPresenter = new MaximFeedPresenter(getApplicationContext(), this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        maximFeedPresenter.onResume();
    }

    public void onAddMaximButtonClicked(View v)
    {
        Intent intent = new Intent(this, AddMaximActivity.class);
        startActivity(intent);
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
        progressBar.setVisibility(View.GONE);
        messageTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
}
