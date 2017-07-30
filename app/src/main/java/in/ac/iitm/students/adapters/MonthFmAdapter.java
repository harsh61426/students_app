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
    public static ArrayList<Calendar_Event> eventArrayList = new ArrayList<>();

    public MonthFmAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //   int current = position % 6; // 6 is used instead of 12 beacuse only 6 months are displayed in the viewpager
        //   CalendarActivity.currentlyDisplayedMonth = current;
//        MonthFragment.adapter.notifyDataSetChanged();
        MonthFragment monthFragment = new MonthFragment();
        monthFragment.setEventList(eventArrayList.get(position));
        return monthFragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
