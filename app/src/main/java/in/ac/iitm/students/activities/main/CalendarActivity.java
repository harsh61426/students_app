package in.ac.iitm.students.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.adapters.MonthFmAdapter;
import in.ac.iitm.students.adapters.RecyclerAdapter;
import in.ac.iitm.students.fragments.monthFragment;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

import static in.ac.iitm.students.activities.main.HomeActivity.fetchingCalendarData;
import static in.ac.iitm.students.fragments.monthFragment.date_list;
import static in.ac.iitm.students.fragments.monthFragment.day_list;
import static in.ac.iitm.students.fragments.monthFragment.desc_list;
import static in.ac.iitm.students.fragments.monthFragment.holiday_list;
import static in.ac.iitm.students.fragments.monthFragment.rv;

/**
 * Created by admin on 14-12-2016.
 */

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int monthForRecyclerView = Calendar.getInstance().get(Calendar.MONTH), yearForRecyclerView = 2017; // this data is used for displaying dayviews when cards are clicked, so be careful before changing these.
    public static int currentlyDisplayedMonth = 0; // this variable shows the value -6 (ex: for july is 6 instead it shows 0)
    //RecyclerView recyclerView;
    // RecyclerView.Adapter recyclerAdapter;
    //RecyclerView.LayoutManager layoutManager;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Checking the permission for writing calendar
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar snackbar = Snackbar
                        .make(drawer, "Granting this permission will allow the app to integrate official insti calendar with your personal calendar.", Snackbar.LENGTH_LONG);
                snackbar.show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        HomeActivity.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        HomeActivity.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        // Set the content of the activity to use the activity_main.xml layout file
        currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentlyDisplayedMonth -= 6;
        //monthForRecyclerView = currentMonth;
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MonthFmAdapter adapter = new MonthFmAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentlyDisplayedMonth);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int current = position % 6;
                monthForRecyclerView = current + 6;
                currentlyDisplayedMonth = current;
                monthFragment.resetLists();
                monthFragment.adapter.notifyDataSetChanged();
                monthFragment.setMonthName(CalendarActivity.currentlyDisplayedMonth);
                monthFragment.adapter = new RecyclerAdapter(day_list, date_list, desc_list, holiday_list, CalendarActivity.this);
                rv.setAdapter(monthFragment.adapter);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //  recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerAdapter= new RecyclerAdapter();
        //recyclerView.setAdapter(recyclerAdapter);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_calendar)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView rollNumber = (TextView) header.findViewById(R.id.tv_roll_number);

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

        username.setText(name);
        rollNumber.setText(roll_no);
        ImageView imageView = (ImageView) header.findViewById(R.id.user_pic);
        String urlPic = "https://photos.iitm.ac.in//byroll.php?roll=" + roll_no;
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case HomeActivity.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    HomeActivity.fetchingCalendarData();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Intent intent = new Intent(CalendarActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        return;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(CalendarActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(CalendarActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(CalendarActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        boolean flag = false;
        final Context context = CalendarActivity.this;

        if (id == R.id.nav_home) {
            intent = new Intent(context, HomeActivity.class);
            flag = true;
        } else if (id == R.id.nav_organisations) {
            intent = new Intent(context, OrganizationActivity.class);
            flag = true;
        } else if (id == R.id.nav_search) {
            intent = new Intent(context, StudentSearchActivity.class);
            flag = true;
        } else if (id == R.id.nav_map) {
            intent = new Intent(context, MapActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_box) {
            intent = new Intent(context, ComplaintBoxActivity.class);
            flag = true;
        } else if (id == R.id.nav_calendar) {
            //intent = new Intent(context, CalendarActivity.class);
            //flag = true;
        } else if (id == R.id.nav_timetable) {
            intent = new Intent(context, TimetableActivity.class);
            flag = true;
        } else if (id == R.id.nav_contacts) {
            intent = new Intent(context, ImpContactsActivity.class);
            flag = true;
        } else if (id == R.id.nav_subscriptions) {
            intent = new Intent(context, SubscriptionActivity.class);
            flag = true;

        } else if (id == R.id.nav_about) {
            intent = new Intent(context, AboutUsActivity.class);
            flag = true;

        } else if (id == R.id.nav_log_out) {
            drawer.closeDrawer(GravityCompat.START);
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            LogOutAlertClass lg = new LogOutAlertClass();
                            lg.isSure(CalendarActivity.this);
                        }
                    }
                    , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
            );
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);

        //Wait till the nav drawer is closed and then start new activity (for smooth animations)
        Handler mHandler = new Handler();
        final boolean finalFlag = flag;
        final Intent finalIntent = intent;
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (finalFlag) {
                            context.startActivity(finalIntent);
                        }
                    }
                }
                , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
        );
        return true;
    }
}