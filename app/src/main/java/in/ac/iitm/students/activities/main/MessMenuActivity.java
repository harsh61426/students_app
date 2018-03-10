package in.ac.iitm.students.activities.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.fragments.EWFragment;
import in.ac.iitm.students.fragments.ImpContactsFragment;
import in.ac.iitm.students.fragments.MessMenuFragment;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class MessMenuActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MessMenuPagerAdapter adapter;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_menu);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        fragments.add(MessMenuFragment.newInstance("SI1"));
        fragments.add(MessMenuFragment.newInstance("SI1"));
        titles.add("South Indian 1");
        titles.add("South Indian 2");
        adapter = new MessMenuPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.pager_mess_menu);
        viewPager.setAdapter(adapter);
    }

    public class MessMenuPagerAdapter extends FragmentPagerAdapter {

        public MessMenuPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
