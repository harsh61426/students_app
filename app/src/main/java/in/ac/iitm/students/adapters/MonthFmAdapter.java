package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.month_fragments.AprilFragment;

/**
 * Created by harshitha on 8/6/17.
 */

public class MonthFmAdapter extends FragmentPagerAdapter {


    public MonthFmAdapter(FragmentManager fm) {

        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        int current = position % 6; // 6 is used instead of 12 beacuse only 6 months are displayed in the viewpager
        AprilFragment.currentlyDisplayedMonth = current;
        AprilFragment.adapter.notifyDataSetChanged();
        return new AprilFragment();


    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
