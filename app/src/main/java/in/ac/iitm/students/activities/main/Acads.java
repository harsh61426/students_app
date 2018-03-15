package in.ac.iitm.students.activities.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.MessAndFacilitiesActivity;
import in.ac.iitm.students.fragments.BunkMonitorFragment;
import in.ac.iitm.students.fragments.CourseFeedbackFragment;
import in.ac.iitm.students.fragments.CourseTimetableFragment;
import in.ac.iitm.students.fragments.FreshieCourseTimetableFragment;
import in.ac.iitm.students.fragments.GradCourseTimetableFragment;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class Acads extends AppCompatActivity {

    /* TODO: Seperate timetable from bunk monitor
     */

    /*TODO: Make widget to view timetable and bunk monitor */

    /*TODO: Make calendar save data offline and try to reduce its loading time */

    /*TODO: Add a drag and drop feature when editing courses for timetable*/


    public final static String EXTRA_MESSAGE1 = "TheExtraMessage1";
    public final static String EXTRA_MESSAGE2 = "TheExtraMessage2";
    private NavigationView navigationView;
    private TextView mTextMessage;
    private Menu menu;
    private DrawerLayout drawer;
    private android.support.v7.widget.Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.calendar:
                    //mTextMessage.setText(R.string.calendar);
                    //selectedFragment = CalenderFragment.newInstance();
                    selectedFragment = new CourseFeedbackFragment();
                    break;
                case R.id.timetable:
                    //mTextMessage.setText(R.string.timetable);
                    if(Utils.isGrad(getApplicationContext())){
                        selectedFragment = new GradCourseTimetableFragment();
                    }
                    else if(!Utils.isFreshie(getApplicationContext())){
                        selectedFragment = new CourseTimetableFragment();
                    }
                    else{
                        selectedFragment = new FreshieCourseTimetableFragment();
                    }
                    break;
                case R.id.bunkmonitor:
                    //mTextMessage.setText(R.string.bunk_monitor);
                    selectedFragment = new BunkMonitorFragment();
                    break;
                case R.id.coursefeedback:
                    //mTextMessage.setText(R.string.bunk_monitor);
                    selectedFragment = new CourseFeedbackFragment();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };


    private NavigationView.OnNavigationItemSelectedListener nOnNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Boolean checkMenuItem = true;
            MenuItem item1 = menu.findItem(R.id.nav_complaint_mess);
            MenuItem item2 = menu.findItem(R.id.nav_complaint_hostel);
            MenuItem item3 = menu.findItem(R.id.nav_complaint_general);

            int id = item.getItemId();
            Intent intent = new Intent();
            boolean flag = false;
            final Context context = Acads.this;

            if (id == R.id.nav_home) {
                intent = new Intent(context, HomeActivity.class);
                flag = true;

            } else if (id == R.id.nav_search) {
                intent = new Intent(context, StudentSearchActivity.class);
                flag = true;
            } else if (id == R.id.nav_map) {
                intent = new Intent(context, MapActivity.class);
                flag = true;
            } else if (id == R.id.nav_complaint_box) {
                if (!item2.isVisible()) {
                    item1.setVisible(false);
                    item2.setVisible(true);
                    item3.setVisible(true);
                    item.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.ic_keyboard_arrow_down_black_24dp));
                    checkMenuItem = false;
                } else {
                    item1.setVisible(false);
                    item2.setVisible(false);
                    item3.setVisible(false);
                    checkMenuItem = false;
                    item.setIcon(ContextCompat.getDrawable(getApplication(), R.drawable.ic_forum_black_24dp));
                }
                navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_timetable)).setChecked(true);


            } else if (id == R.id.nav_complaint_hostel) {
                intent = new Intent(context, HostelComplaintsActivity.class);
                flag = true;
            } else if (id == R.id.nav_complaint_general) {
                intent = new Intent(context, GeneralComplaintsActivity.class);
                flag = true;
            } else if (id == R.id.nav_complaint_mess) {
                intent = new Intent(context, MessAndFacilitiesActivity.class);
                flag = true;
            } else if (id == R.id.nav_calendar) {
                intent = new Intent(context, CalendarActivity.class);
                flag = true;
            } else if (id == R.id.nav_timetable) {
                //intent = new Intent(context, TimetableActivity.class);
                //flag = true;
            } else if (id == R.id.nav_contacts) {
                intent = new Intent(context, ImpContactsActivity.class);
                flag = true;
            } else if (id == R.id.nav_about) {
                intent = new Intent(context, AboutUsActivity.class);
                flag = true;
            } else if (id == R.id.nav_profile) {
                intent = new Intent(context, ProfileActivity.class);
                flag = true;

            } else if (id == R.id.nav_log_out) {
                drawer.closeDrawer(GravityCompat.START);
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                LogOutAlertClass lg = new LogOutAlertClass();
                                lg.isSure(Acads.this);
                            }
                        }
                        , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
                );
                return true;
            }

            if (checkMenuItem) {
                item1.setVisible(false);
                item2.setVisible(false);
                item3.setVisible(false);

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
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acads);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,new CourseFeedbackFragment());//change to calendar
        transaction.commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        */
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_gallery);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_timetable)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(nOnNavigationItemSelectedListener);

        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView rollNumber = (TextView) header.findViewById(R.id.tv_roll_number);

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

        username.setText(name);
        rollNumber.setText(roll_no);
        ImageView imageView = (ImageView) header.findViewById(R.id.user_pic);
        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Acads.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //change this
        getMenuInflater().inflate(R.menu.menu_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle nav_cmgfs view item clicks here.

        Boolean checkMenuItem = true;
        MenuItem item1 = menu.findItem(R.id.nav_complaint_mess);
        MenuItem item2 = menu.findItem(R.id.nav_complaint_hostel);
        MenuItem item3 = menu.findItem(R.id.nav_complaint_general);

        int id = item.getItemId();
        Intent intent = new Intent();
        boolean flag = false;
        final Context context = Acads.this;

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
            if (!item2.isVisible()) {
                item1.setVisible(false);
                item2.setVisible(true);
                item3.setVisible(true);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_down_black_24dp));
                checkMenuItem = false;
            } else {
                item1.setVisible(false);
                item2.setVisible(false);
                item3.setVisible(false);
                checkMenuItem = false;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_forum_black_24dp));
            }
            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_timetable)).setChecked(true);


        } else if (id == R.id.nav_complaint_hostel) {
            intent = new Intent(context, HostelComplaintsActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_general) {
            intent = new Intent(context, GeneralComplaintsActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_mess) {
            intent = new Intent(context, MessAndFacilitiesActivity.class);
            flag = true;
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(context, CalendarActivity.class);
            flag = true;
        } else if (id == R.id.nav_timetable) {
            //intent = new Intent(context, TimetableActivity.class);
            //flag = true;
        } else if (id == R.id.nav_contacts) {
            intent = new Intent(context, ImpContactsActivity.class);
            flag = true;
        } else if (id == R.id.nav_subscriptions) {
            intent = new Intent(context, SubscriptionActivity.class);
            flag = true;
        } else if (id == R.id.nav_about) {
            intent = new Intent(context, AboutUsActivity.class);
            flag = true;
        } else if (id == R.id.nav_profile) {
            intent = new Intent(context, ProfileActivity.class);
            flag = true;

        } else if (id == R.id.nav_log_out) {
            drawer.closeDrawer(GravityCompat.START);
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            LogOutAlertClass lg = new LogOutAlertClass();
                            lg.isSure(Acads.this);
                        }
                    }
                    , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
            );
            return true;
        }

        if (checkMenuItem) {
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);

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
        }
        return true;

    }*/

}
