package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import in.ac.iitm.students.fragments.MonthFragment;
import in.ac.iitm.students.objects.Calendar_Event;

/**
 * Created    harshitha on 8/6/17.
 */

public class MonthFmAdapter extends FragmentPagerAdapter {

    private ArrayList<ArrayList<Calendar_Event>> cal_events;

    public MonthFmAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        int current = position % 6; // 6 is used instead of 12 beacuse only 6 months are displayed in the viewpager
        //   CalendarActivity.currentlyDisplayedMonth = current;
//        MonthFragment.adapter.notifyDataSetChanged();
        MonthFragment monthFragment = new MonthFragment();
        monthFragment.setEventList(cal_events.get(current),current);
        return monthFragment;
    }

    @Override
    public int getCount() {
        //return cal_events.size();
        return 6;
    }

    public void setCal_events(ArrayList<ArrayList<Calendar_Event>> cal_events) {
        this.cal_events = cal_events;
    }

    private String setMonthName(int i) {
        switch (i)
        {
            case 0:
                return "January,2018";
            case 1:
                return "February,2018";
            case 2:
                return "March,2018";
            case 3:
                return "April,2018";
            case 4:
                return "May,2018";
            case 5:
                return "June,2018";
            default:
                return "Unknown";
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return setMonthName(position);
    }
}
