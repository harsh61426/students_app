package in.ac.iitm.students.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.adapters.DayAdapter;
import in.ac.iitm.students.objects.Calendar_Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {

    // todo dont make them static, find a work-around
    public  DayAdapter adapter;
    public  RecyclerView rv;
    public  TextView monthName=null;
    int monthNo=0;
    int currentMonth,currentDate;
    private ArrayList<Calendar_Event> month_events;


    public MonthFragment() {
        // Required empty public constructor
    }

    private void setMonthName(int i) {
        if (i == 0)
            monthName.setText("July,2017");
        else if (i == 1)
            monthName.setText("August,2017");
        else if (i == 2)
            monthName.setText("September,2017");
        else if (i == 3)
            monthName.setText("October,2017");
        else if (i == 4)
            monthName.setText("November,2017");
        else
            monthName.setText("December,2017");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_month, container, false);

        monthName = (TextView) rootView.findViewById(R.id.tv_month_name);
        setMonthName(monthNo);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_month);
        //rv.setHasFixedSize(true);
        adapter = new DayAdapter(month_events, getActivity());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentDate = Calendar.getInstance().get(Calendar.DATE);
        if(currentMonth>=6)
            currentMonth-=6;
        if(monthNo==currentMonth)
            rv.smoothScrollToPosition(currentDate);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void setEventList(ArrayList<Calendar_Event> month_events,int i) {

        this.month_events = month_events;
        monthNo = i;
    }
}
