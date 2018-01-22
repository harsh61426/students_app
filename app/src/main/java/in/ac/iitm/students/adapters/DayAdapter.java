package in.ac.iitm.students.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created by Sarath on 8/6/17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    //ArrayList<String> day_dataset, date_dataset, desc_dataset, holiday_dataset;
    ArrayList<Calendar_Event> month_events = new ArrayList<>();
    private Context context;


    public DayAdapter(ArrayList<Calendar_Event> month_events, Context context) {
        this.month_events = month_events;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_card, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String day = null;
        switch (month_events.get(position).getDay()) {
            case "Sunday":
                day = "Sun";
                break;
            case "Monday":
                day = "Mon";
                break;
            case "Tuesday":
                day = "Tue";
                break;
            case "Wednesday":
                day = "Wed";
                break;
            case "Thursday":
                day = "Thu";
                break;
            case "Friday":
                day = "Fri";
                break;
            case "Saturday":
                day = "Sat";
                break;
            default:
                day = "";
        }

        holder.tv_day.setText(day);
        holder.tv_date.setText(String.valueOf(month_events.get(position).getDate()));
        holder.tv_desc.setText(month_events.get(position).getDetails());


        if (month_events.get(position).isHoliday()){
            holder.cardView.setBackgroundColor(Color.parseColor("#607D8B"));
            holder.tv_date.setTextColor(Color.parseColor("#2196F3"));
            holder.tv_day.setTextColor(Color.BLACK);
        }else{
            holder.cardView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            holder.tv_date.setTextColor(Color.parseColor("#2196F3"));
            holder.tv_day.setTextColor(Color.parseColor("#3F51B5"));
        }

        if(Objects.equals(month_events.get(position).eventDisplay1, "")){
            holder.reminderCardView.setVisibility(View.GONE);
        }
        if(!Objects.equals(month_events.get(position).eventDisplay1, "")){
            holder.reminderCardView.setVisibility(View.VISIBLE);
            holder.reminderText.setText(month_events.get(position).eventDisplay1);
        }
        if(!Objects.equals(month_events.get(position).eventDisplay2, "")){
            holder.reminerDots.setVisibility(View.VISIBLE);
        }

        Calendar beginTime=Calendar.getInstance();
        beginTime.set(CalendarActivity.yearForRecyclerView, CalendarActivity.monthForRecyclerView, position+1);
        beginTime.set(Calendar.MILLISECOND, 0);
        beginTime.setTimeZone(TimeZone.getDefault());
        // A date-time specified in milliseconds since the epoch.
        final long  begin= beginTime.getTimeInMillis();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, begin);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("DEBXXa", String.valueOf(month_events.size()));
        return month_events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day, tv_date, tv_desc,reminderText,reminerDots;
        CardView cardView,reminderCardView;

        ViewHolder(View itemView) {
            super(itemView);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_description);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            reminderCardView = (CardView) itemView.findViewById(R.id.reminder_cardview);
            reminderText = (TextView) itemView.findViewById(R.id.reminders_textview);
            reminerDots = (TextView) itemView.findViewById(R.id.reminders_dots);
        }
    }
}
