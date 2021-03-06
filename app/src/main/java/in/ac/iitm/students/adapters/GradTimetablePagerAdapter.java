package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.BunkMonitorFragment;
import in.ac.iitm.students.fragments.GradCourseTimetableFragment;

/**
 * Created by SAM10795 on 20-06-2017.
 */

public class GradTimetablePagerAdapter extends FragmentPagerAdapter{

    private static int NUM_ITEMS = 2;

    public GradTimetablePagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new GradCourseTimetableFragment();

            case 1: return new BunkMonitorFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0: return "TimeTable";
            //case 1: return "TimeTable";
            case 1: return "Bunk Monitor";
            default: return null;
        }
    }



    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
