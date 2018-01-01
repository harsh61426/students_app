package in.ac.iitm.students.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.DayAdapter;
import in.ac.iitm.students.objects.Calendar_Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment{

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
            monthName.setText("January,2018");
        else if (i == 1)
            monthName.setText("February,2018");
        else if (i == 2)
            monthName.setText("March,2018");
        else if (i == 3)
            monthName.setText("April,2018");
        else if (i == 4)
            monthName.setText("May,2018");
        else
            monthName.setText("June,2018");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_month, container, false);

        monthName = (TextView) rootView.findViewById(R.id.tv_month_name);
        setMonthName(monthNo);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_month);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        //rv.setHasFixedSize(true);
        adapter = new DayAdapter(month_events, getActivity());
        rv.setAdapter(adapter);
//        getLoaderManager().initLoader(0, null, this);

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
