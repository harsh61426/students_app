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
        //   int current = position % 6; // 6 is used instead of 12 beacuse only 6 months are displayed in the viewpager
        //   CalendarActivity.currentlyDisplayedMonth = current;
//        MonthFragment.adapter.notifyDataSetChanged();
        MonthFragment monthFragment = new MonthFragment();
        monthFragment.setEventList(cal_events.get(position));
        return monthFragment;
    }

    @Override
    public int getCount() {
        return cal_events.size();
    }

    public void setCal_events(ArrayList<ArrayList<Calendar_Event>> cal_events) {
        this.cal_events = cal_events;
    }
}
