package in.ac.iitm.students.activities;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.BottomNavBehaviour;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.activities.main.ImpContactsActivity;
import in.ac.iitm.students.activities.main.MapActivity;
import in.ac.iitm.students.activities.main.StudentSearchActivity;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.MessAndFacilitiesActivity;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by admin on 30-01-2017.
 */

public class SubscriptionActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,NavigationView.OnNavigationItemSelectedListener
{
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Context context;
    private HashMap<String, Boolean> subscriptionPref = new HashMap<>();

    private ArrayList<HashMap<String, String>> user_topics = new ArrayList<>();
    private int fromhome;
    private SharedPreferences.Editor editor;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        fromhome = intent.getIntExtra("fromhome", 0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
//        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_maps)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavBehaviour());
        navigation.setSelectedItemId(R.id.bot_nav_subscriptions);


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

        context = this;
        String url = "https://students.iitm.ac.in/studentsapp/general/subs.php";
        final ArrayList<HashMap<String, String>> database_topics = new ArrayList<>();

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        try {
                            JSONArray jsArray = new JSONArray(response);
                            for (int i = 0; i < jsArray.length(); i++) {
                                JSONObject jsObject = jsArray.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("name", jsObject.getString("name"));
                                hashMap.put("topic", jsObject.getString("topic"));
                                hashMap.put("value", jsObject.getString("value"));

                                database_topics.add(hashMap);
                            }

                            for (HashMap<String, String> hashMap : database_topics) {
                                if (Integer.parseInt(hashMap.get("value")) == 1) {

                                    subscriptionPref.put(hashMap.get("topic"), prefs.getBoolean(hashMap.get("topic"), true));
                                    user_topics.add(hashMap);
                                } else if (Integer.parseInt(hashMap.get("value")) == 0) {

                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(hashMap.get("topic"));
                                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.remove(hashMap.get("topic"));
                                    editor.apply();
                                }


                            }

                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_subscribe_list);
                            LinearLayout.LayoutParams rlParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            rlParams.setMargins(8, 0, 8, 48);

                            RelativeLayout.LayoutParams textView_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            textView_params.addRule(RelativeLayout.ALIGN_PARENT_START);

                            RelativeLayout.LayoutParams switchCompat_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            switchCompat_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                            for (int i = 0; i < user_topics.size(); i++) {
                                HashMap<String, String> hashMap = user_topics.get(i);

                                RelativeLayout rl = new RelativeLayout(context);
                                rl.setLayoutParams(rlParams);
                                rl.setPadding(8, 16, 8, 16);

                                TextView textView = new TextView(context);
                                textView.setText(hashMap.get("name"));

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setTextAppearance(R.style.TextAppearance_AppCompat_Caption);
                                    textView.setTextSize(14);
                                }

                                if (hashMap.get("topic").equals("general")) {
                                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                }
                                textView.setPadding(8, 0, 0, 0);
                                textView.setLayoutParams(textView_params);
                                rl.addView(textView);


                                SwitchCompat switchCompat = new SwitchCompat(context);
                                switchCompat.setOnCheckedChangeListener(SubscriptionActivity.this);
                                switchCompat.setTag(hashMap.get("topic"));
                                switchCompat.setChecked(subscriptionPref.get(hashMap.get("topic")));
                                switchCompat.setLayoutParams(switchCompat_params);
                                rl.addView(switchCompat);

                                linearLayout.addView(rl);

                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_subscribe_list);
                            LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            TextView tv = new TextView(SubscriptionActivity.this);
                            tv.setText("Error loading this page.");
                            tv.setLayoutParams(tv_params);
                            linearLayout.addView(tv);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_subscribe_list);
                        LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextView tv = new TextView(SubscriptionActivity.this);
                        tv.setText("No Internet Connection :(");
                        tv.setLayoutParams(tv_params);
                        linearLayout.addView(tv);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        // Handle error
                        //Toast.makeText(context, "Couldn't connect to internet.", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    @Override
    public void onPause() {
        super.onPause();

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        for (int i = 0; i < user_topics.size(); i++) {
            HashMap<String, String> hashMap = user_topics.get(i);
            editor.putBoolean(hashMap.get("topic"), subscriptionPref.get(hashMap.get("topic")));

            //Log.d("tada", hashMap.get("topic") + ":" + subscriptionPref.get(hashMap.get("topic")));
        }
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SubscriptionActivity.this, HomeActivity.class);
        startActivity(intent);
//        if (getIntent().hasExtra("knock_knock")) {
//            Intent intent = new Intent(SubscriptionActivity.this, HomeActivity.class);
//            startActivity(intent);
//        } else super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            if (buttonView.getTag().toString().equals("general")) {
                buttonView.setChecked(true);
                Snackbar.make(findViewById(R.id.coordinator_layout_subscribe), "Cannot Un-subscribe :|", Snackbar.LENGTH_LONG).show();
            } else {
                buttonView.setChecked(false);
                subscriptionPref.remove(buttonView.getTag().toString());
                subscriptionPref.put(buttonView.getTag().toString(), false);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(buttonView.getTag().toString());
            }

        } else {
            buttonView.setChecked(true);
            subscriptionPref.remove(buttonView.getTag().toString());
            subscriptionPref.put(buttonView.getTag().toString(), true);
            FirebaseMessaging.getInstance().subscribeToTopic(buttonView.getTag().toString());
        }

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Boolean checkMenuItem = true;
        MenuItem item1 = menu.findItem(R.id.nav_complaint_mess);
        MenuItem item2 = menu.findItem(R.id.nav_complaint_hostel);
        MenuItem item3 = menu.findItem(R.id.nav_complaint_general);

        int id = item.getItemId();
        Intent intent = new Intent();
        boolean flag = false;
        final Context context = SubscriptionActivity.this;

        if (id == R.id.nav_home) {
            intent = new Intent(context, HomeActivity.class);
            flag = true;

        }else if (id == R.id.nav_search) {
            intent = new Intent(context, StudentSearchActivity.class);
            flag = true;
        } else if (id == R.id.nav_map) {
            intent = new Intent(context, MapActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_box) {
            if (!item1.isVisible()) {
                item1.setVisible(true);
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
//            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_maps)).setChecked(true);


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
            intent = new Intent(context, TimetableActivity.class);
            flag = true;
        } else if (id == R.id.nav_contacts) {
            intent = new Intent(context, ImpContactsActivity.class);
            flag = true;
        }  else if (id == R.id.nav_about) {
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
                            lg.isSure(SubscriptionActivity.this);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent1;
            final Context context = SubscriptionActivity.this;

            switch (item.getItemId()) {
                case R.id.bot_nav_home:
                    intent1 = new Intent(context, HomeActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_organisations:
                    intent1 = new Intent(context,OrganizationActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_subscriptions:
                    return true;
                case R.id.bot_nav_map:
                    intent1 = new Intent(context, MapActivity.class);
                    context.startActivity(intent1);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }


}
