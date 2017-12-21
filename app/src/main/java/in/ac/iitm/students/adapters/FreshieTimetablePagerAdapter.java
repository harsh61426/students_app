package in.ac.iitm.students.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.ac.iitm.students.fragments.BunkMonitorFragment;
import in.ac.iitm.students.fragments.FreshieCourseTimetableFragment;


public class FreshieTimetablePagerAdapter extends FragmentPagerAdapter{

    private static int NUM_ITEMS = 2;

    public FreshieTimetablePagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new FreshieCourseTimetableFragment();

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
