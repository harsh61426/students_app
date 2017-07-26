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

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.adapters.RecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class monthFragment extends Fragment {

    public static String[][] day, date, desc, holiday;

    public static RecyclerAdapter adapter;
    public static RecyclerView rv;
    public static TextView monthName;
    public static ArrayList<String> day_list, date_list, desc_list, holiday_list;


    static {
        day = new String[6][];
        /*day[0] = new String[31];
        day[1] = new String[28];
        day[2] = new String[31];
        day[3] = new String[30];
        day[4] = new String[31];
        day[5] = new String[30];*/
        day[0] = new String[31];
        day[1] = new String[31];
        day[2] = new String[30];
        day[3] = new String[31];
        day[4] = new String[30];
        day[5] = new String[31];

        date = new String[6][];
        /*date[0] = new String[31];
        date[1] = new String[28];
        date[2] = new String[31];
        date[3] = new String[30];
        date[4] = new String[31];
        date[5] = new String[30];*/
        date[0] = new String[31];
        date[1] = new String[31];
        date[2] = new String[30];
        date[3] = new String[31];
        date[4] = new String[30];
        date[5] = new String[31];

        desc = new String[6][];
        /*desc[0] = new String[31];
        desc[1] = new String[28];
        desc[2] = new String[31];
        desc[3] = new String[30];
        desc[4] = new String[31];
        desc[5] = new String[30];*/
        desc[0] = new String[31];
        desc[1] = new String[31];
        desc[2] = new String[30];
        desc[3] = new String[31];
        desc[4] = new String[30];
        desc[5] = new String[31];

        holiday = new String[6][];
        /*date[0] = new String[31];
        date[1] = new String[28];
        date[2] = new String[31];
        date[3] = new String[30];
        date[4] = new String[31];
        date[5] = new String[30];*/
        holiday[0] = new String[31];
        holiday[1] = new String[31];
        holiday[2] = new String[30];
        holiday[3] = new String[31];
        holiday[4] = new String[30];
        holiday[5] = new String[31];
    }

    public monthFragment() {
        // Required empty public constructor
    }

    public static void setMonthName(int i) {

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

    public static void resetLists() {
        day_list = new ArrayList(Arrays.asList(day[CalendarActivity.currentlyDisplayedMonth]));
        date_list = new ArrayList(Arrays.asList(date[CalendarActivity.currentlyDisplayedMonth]));
        desc_list = new ArrayList(Arrays.asList(desc[CalendarActivity.currentlyDisplayedMonth]));
        holiday_list = new ArrayList(Arrays.asList(desc[CalendarActivity.currentlyDisplayedMonth]));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_april, container, false);
        monthName = (TextView) rootView.findViewById(R.id.tv_april);

        setMonthName(CalendarActivity.currentlyDisplayedMonth);


        /*day = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"};
        date = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        desc = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a", "b", "c", "d"};
*/
        day_list = new ArrayList(Arrays.asList(day[CalendarActivity.currentlyDisplayedMonth]));
        date_list = new ArrayList(Arrays.asList(date[CalendarActivity.currentlyDisplayedMonth]));
        desc_list = new ArrayList(Arrays.asList(desc[CalendarActivity.currentlyDisplayedMonth]));
        holiday_list = new ArrayList(Arrays.asList(desc[CalendarActivity.currentlyDisplayedMonth]));


        rv = (RecyclerView) rootView.findViewById(R.id.rv_april);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapter(day_list, date_list, desc_list, holiday_list, getActivity());
        rv.setAdapter(adapter);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
