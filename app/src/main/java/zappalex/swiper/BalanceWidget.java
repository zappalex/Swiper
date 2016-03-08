package zappalex.swiper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import zappalex.swiper.utils.FormatManager;
import zappalex.swiper.utils.SharedPreferencesManager;
import zappalex.swiper.utils.ValueResources;

public class BalanceWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.balance_widget);
        TextView editBtn = new TextView(context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        ComponentName myWidget = new ComponentName(context, BalanceWidget.class);

        views.setOnClickPendingIntent(R.id.total_balance_view, pendingIntent);
        views.setOnClickPendingIntent(R.id.edit_btn, pendingIntent);
        appWidgetManager.updateAppWidget(myWidget, views);

        SharedPreferencesManager sharedPreferences = SharedPreferencesManager.getInstance(context);
        FormatManager formatManager = FormatManager.getInstance();

        views.setTextViewText(R.id.total_balance_view, formatManager.formatBalance(sharedPreferences.getSharedPrefDbl(ValueResources.BALANCE_SHARED_PREF, ValueResources.DEFAULT_BALANCE_VAL)));
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}