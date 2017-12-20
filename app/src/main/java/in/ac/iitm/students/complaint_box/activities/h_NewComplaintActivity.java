package in.ac.iitm.students.complaint_box.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.fragments.h_CustomComplainFragment;
import in.ac.iitm.students.complaint_box.fragments.h_NewcomplaintFragment;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by dell on 21-06-2017.
 */
public class h_NewComplaintActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ArrayList<String> imageUrls;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hostel_complaints_activity_new_complaint);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final SharedPreferences sharedPref = h_NewComplaintActivity.this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/addComplaint.php";
        final String url = "https://rockstarharshitha.000webhostapp.com/hostel_complaints/addComplaint.php";
        final EditText prox = (EditText) findViewById(R.id.editText_room_number);
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        final String name = Utils.getprefString(UtilStrings.NAME, this);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(h_NewComplaintActivity.this);


    }

    private void setupViewPager(ViewPager viewPager) {
        h_NewComplaintActivity.ViewPagerAdapter adapter = new h_NewComplaintActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new h_NewcomplaintFragment(), "New Complaint");
        adapter.addFragment(new h_CustomComplainFragment(), "Custom Complaint");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0)
            fab.show();
        else
            fab.hide();
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0)
            fab.show();
        else
            fab.hide();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
