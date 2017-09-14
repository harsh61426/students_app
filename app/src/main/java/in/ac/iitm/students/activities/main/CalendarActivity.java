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
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.adapters.MonthFmAdapter;
import in.ac.iitm.students.complaint_box.activities.main.MessAndFacilitiesActivity;
import in.ac.iitm.students.objects.Calendar_Event;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.InstiCalendar;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by admin on 14-12-2016.
 */

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int monthForRecyclerView = Calendar.getInstance().get(Calendar.MONTH), yearForRecyclerView = 2017; // this data is used for displaying dayviews when cards are clicked, so be careful before changing these.
    public static int currentlyDisplayedMonth;
    //RecyclerView recyclerView;
    //RecyclerView.Adapter recyclerAdapter;
    //RecyclerView.LayoutManager layoutManager;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private MenuItem calendarSync;

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
        else{
            new InstiCalendar(CalendarActivity.this).fetchCalData(1);
        }



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
        if(currentlyDisplayedMonth>=6)
            currentlyDisplayedMonth -= 6;
        //monthForRecyclerView = currentMonth;
        // Find the view pager that will allow the user to swipe between fragments

        String urlForCalendarData = "https://students.iitm.ac.in/studentsapp/calendar/calendar_php.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForCalendarData, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("kaka", response);
                InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));

                JsonReader reader = null;
                try {
                    reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                    reader.setLenient(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        ArrayList<ArrayList<Calendar_Event>> cal_events = InstiCalendar.readMonthObject(reader, CalendarActivity.this, 1);
                        // Create an adapter that knows which fragment should be shown on each page
                        MonthFmAdapter adapter = new MonthFmAdapter(getSupportFragmentManager());
                        adapter.setCal_events(cal_events);
                        // Set the adapter onto the view pager
                        viewPager.setAdapter(adapter);
                        viewPager.setCurrentItem(currentlyDisplayedMonth);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Snackbar snackbar = Snackbar
                        .make(drawer,"No internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });
        MySingleton.getInstance(CalendarActivity.this).addToRequestQueue(stringRequest);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int current = position % 6;
                monthForRecyclerView = current + 6;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //  recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerAdapter= new DayAdapter();
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
                    new InstiCalendar(CalendarActivity.this).fetchCalData(1);


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
        getMenuInflater().inflate(R.menu.calendar_menu, menu);

        if(new InstiCalendar(this).getCalendarId(CalendarActivity.this)==-1){
            Utils.saveprefInt("CalStat",0,this);
        }else{
            Utils.saveprefInt("CalStat",1,this);
        }

        MenuItem item=menu.getItem(0); // here itemIndex is int
        if(Utils.getprefInt("CalStat",this)==0)
            item.setTitle("Insert Calendar");
        else
            item.setTitle("Remove Calendar");

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
        } else if(id == R.id.calendar_sync){
            if(Utils.getprefInt("CalStat",this)==1){
                new InstiCalendar(CalendarActivity.this).deleteCalendarTest(CalendarActivity.this);
                item.setTitle("Insert Calendar");
            }else{
                long CalID=new InstiCalendar(CalendarActivity.this).insertCalendar(this);

                Log.i("CalID", CalID + "");
                Utils.saveprefLong("CalID", CalID, this);
                Toast.makeText(this, "Updating Calendar", Toast.LENGTH_SHORT).show();
                new InstiCalendar(CalendarActivity.this).deleteallevents();
                new InstiCalendar(CalendarActivity.this).sendJsonRequest(this, 0);
                Utils.saveprefString("Cal_Ver", new InstiCalendar(CalendarActivity.this).getVersion(), this);


                item.setTitle("Remove Calendar");
            }
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
            intent = new Intent(context, MessAndFacilitiesActivity.class);
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