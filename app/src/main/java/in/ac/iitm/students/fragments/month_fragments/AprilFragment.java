package in.ac.iitm.students.fragments.month_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.RecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AprilFragment extends Fragment {

    public static String[][] day, date, desc;
    public static int currentlyDisplayedMonth; // this variable shows the value -6 (ex: for july is 6 instead it shows 0)
    public static RecyclerAdapter adapter;

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
    }

    public AprilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_april, container, false);


        /*day = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"};
        date = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        desc = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a", "b", "c", "d"};
*/
        List<String> day_list = new ArrayList(Arrays.asList(day[currentlyDisplayedMonth]));
        List<String> date_list = new ArrayList(Arrays.asList(date[currentlyDisplayedMonth]));
        List<String> desc_list = new ArrayList(Arrays.asList(desc[currentlyDisplayedMonth]));

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_april);
        rv.setHasFixedSize(true);
        adapter = new RecyclerAdapter(day_list, date_list, desc_list, getActivity());
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
