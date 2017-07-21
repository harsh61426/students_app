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
        int current = position % 12;
        AprilFragment.currentMonth = current;
        return new AprilFragment();


    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
