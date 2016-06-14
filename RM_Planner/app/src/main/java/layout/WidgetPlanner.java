package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.cs246.rmgroup.rmplanner.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetPlanner extends AppWidgetProvider {
    String[] strHours = {"7:00", "8:00", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00",
            "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00"};

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_planner);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_planner);
        views.setOnClickPendingIntent(R.id.button, pendingIntent);
        gLayout = (GridLayout) findViewById(R.id.gridLayout);

        gLayout.setColumnCount(2);
        gLayout.setRowCount(strHours.length);

        /************************************
         * Bring in all hours in left column
         ***********************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(0, GridLayout.CENTER));
            params.setMargins(5, 0, 0, 0);
            params.setGravity(Gravity.FILL);
            TextView tv = new TextView(null);
            //tv.setText(Integer.toString(hours[i]) + ((i < 5) ? "AM" : "PM"));
            tv.setText(strHours[i]);
            tv.setTextSize(12);
            tv.setPadding(10,10,10,10);
            gLayout.addView(tv, params);
        }

        /*******************************
         * Bring in all editTexts
         ******************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(1, GridLayout.LEFT));
            params.setGravity(Gravity.FILL_HORIZONTAL);
            EditText et = new EditText(null);
            et.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            et.setTextSize(12);
            et.setText(" ");
            et.setPadding(1, 3, 3, 5);
            gLayout.addView(et, params);
        }

        /******************************************************/
        //Apply drawable to all
        int count = 0;
        count = gLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = gLayout.getChildAt(i);
            v.setBackgroundResource(R.drawable.draw_back_left);
            //v.setBackgroundColor(Integer.parseInt("ACACAC", 16) + (50 * i));
            if (gLayout.getChildAt(i) instanceof EditText) {
                EditText e = (EditText) gLayout.getChildAt(i);
                e.setBackgroundResource(R.drawable.draw_back);
            }
        }
        /******************************************************/
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

