package in.ac.iitm.students.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created by harshitha on 8/6/17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    //ArrayList<String> day_dataset, date_dataset, desc_dataset, holiday_dataset;
    Context context;
    ArrayList<Calendar_Event> month_events = new ArrayList<>();

    /*
    public DayAdapter(ArrayList<String> list1, ArrayList<String> list2, ArrayList<String> list3, ArrayList<String> list4, Context context) {
        day_dataset = list1;
        date_dataset = list2;
        desc_dataset = list3;
        holiday_dataset = list4;
        this.context = context;
    }
    */

    public DayAdapter(ArrayList<Calendar_Event> month_events) {
        this.month_events = month_events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_day.setText(month_events.get(position).getDay());
        holder.tv_date.setText(month_events.get(position).getDate());
        holder.tv_desc.setText(month_events.get(position).getDetails());
        if (month_events.get(position).isHoliday())
            holder.tv_holiday.setText("(Holiday)");


        if (month_events.get(position).getDay().equals("Sunday") || month_events.get(position).getDay().equals("Saturday"))
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ba68c8"));//violet color
        else
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(CalendarActivity.yearForRecyclerView, CalendarActivity.monthForRecyclerView, position + 1);
                // A date-time specified in milliseconds since the epoch.
                long hr = beginTime.getTimeInMillis();

                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, hr);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                context.startActivity(intent);
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return month_events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day, tv_date, tv_desc, tv_holiday;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_description);
            tv_holiday = (TextView) itemView.findViewById(R.id.tv_hoilday);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
