package com.tcolligan.maximmaker.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.data.Callback;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximRepository;
import com.tcolligan.maximmaker.data.RepositoryManager;
import com.tcolligan.maximmaker.data.WidgetMaximCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * An AppWidgetProvider that is used to display a widget containing a random Maxim.
 * <p/>
 * Created on 1/29/2017.
 *
 * @author Thomas Colligan
 */
public class MaximWidgetProvider extends AppWidgetProvider
{
    private static final Random random = new Random();
    private static final MaximRepository maximRepository = RepositoryManager.getInstance().getMaximRepository();

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds)
    {
        maximRepository.fetchAllMaxims(new Callback<List<Maxim>>()
        {
            @Override
            public void onSuccess(List<Maxim> data)
            {
                updateWidgets(context, appWidgetManager, appWidgetIds, data);
            }
        });
    }

    private void updateWidgets(final Context context,
                               final AppWidgetManager appWidgetManager,
                               final int[] appWidgetIds,
                               final List<Maxim> loadedMaximList)
    {
        WidgetMaximCache.load(context);

        for (int widgetId : appWidgetIds)
        {
            List<Integer> displayedMaximIdsList = WidgetMaximCache.getDisplayedMaximIdsList();
            Maxim maximToDisplay = retrieveNewRandomMaximToDisplay(loadedMaximList, displayedMaximIdsList);

            if (maximToDisplay == null)
            {
                WidgetMaximCache.clear(context);
                displayedMaximIdsList = WidgetMaximCache.getDisplayedMaximIdsList();
                maximToDisplay = retrieveNewRandomMaximToDisplay(loadedMaximList, displayedMaximIdsList);
            }

            updateWidget(context, appWidgetManager, widgetId, maximToDisplay);

            if (maximToDisplay != null)
            {
                WidgetMaximCache.addDisplayedMaximId(context, maximToDisplay.getId());
            }
        }
    }

    private Maxim retrieveNewRandomMaximToDisplay(final List<Maxim> loadedMaximList, List<Integer> displayedMaximIdsList)
    {
        final List<Maxim> availableMaximsList = new ArrayList<>(loadedMaximList);

        for (Integer displayedMaximId : displayedMaximIdsList)
        {
            for (int i = 0; i < availableMaximsList.size(); i++)
            {
                Maxim availableMaxim = availableMaximsList.get(i);

                if (availableMaxim.getId() == displayedMaximId)
                {
                    availableMaximsList.remove(availableMaxim);
                    break;
                }
            }
        }

        if (availableMaximsList.size() == 0)
        {
            return null;
        }

        int randomMaximIndex = random.nextInt(availableMaximsList.size());
        return availableMaximsList.get(randomMaximIndex);
    }

    private void updateWidget(final Context context,
                              final AppWidgetManager appWidgetManager,
                              int widgetId,
                              Maxim maximToDisplay)
    {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

        if (maximToDisplay == null)
        {
            remoteViews.setViewVisibility(R.id.noMaximsTextView, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.messageTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.authorTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.tagsTextView, View.GONE);
        }
        else
        {
            remoteViews.setTextViewText(R.id.messageTextView, maximToDisplay.getMessage());
            remoteViews.setTextViewText(R.id.authorTextView, maximToDisplay.getAuthor() != null ?
                    String.format(Locale.US, "- %s", maximToDisplay.getAuthor()) : "");

            remoteViews.setTextViewText(R.id.tagsTextView, maximToDisplay.getTags() != null ?
                    maximToDisplay.getTags() : "");

            remoteViews.setViewVisibility(R.id.noMaximsTextView, View.GONE);
            remoteViews.setViewVisibility(R.id.messageTextView, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.authorTextView, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.tagsTextView, View.VISIBLE);
        }

        Intent intent = new Intent(context, MaximWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.rootWidgetLayout, pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
}
