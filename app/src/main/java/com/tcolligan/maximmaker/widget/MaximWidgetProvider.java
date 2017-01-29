package com.tcolligan.maximmaker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.tcolligan.maximmaker.R;
import com.tcolligan.maximmaker.data.Maxim;
import com.tcolligan.maximmaker.data.MaximManager;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created on 1/29/2017.
 *
 * @author Thomas Colligan
 */
public class MaximWidgetProvider extends AppWidgetProvider
{
    private static final Random random = new Random();

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds)
    {
        MaximManager.getInstance().loadMaxims(context, new MaximManager.MaximsLoadedListener()
        {
            @Override
            public void onMaximsLoaded(List<Maxim> loadedMaximList)
            {
                updateWidgets(context, appWidgetManager, appWidgetIds, loadedMaximList);
            }
        });
    }

    private void updateWidgets(final Context context,
                               final AppWidgetManager appWidgetManager,
                               final int[] appWidgetIds,
                               final List<Maxim> loadedMaximList)
    {
        for (int widgetId : appWidgetIds)
        {
            int randomMaximIndex = random.nextInt(loadedMaximList.size());
            Maxim randomMaxim = loadedMaximList.get(randomMaximIndex);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            remoteViews.setTextViewText(R.id.messageTextView, randomMaxim.getMessage());
            remoteViews.setTextViewText(R.id.authorTextView, String.format(Locale.US, "- %s", randomMaxim.getAuthor()));

            Intent intent = new Intent(context, MaximWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.rootWidgetLayout, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
