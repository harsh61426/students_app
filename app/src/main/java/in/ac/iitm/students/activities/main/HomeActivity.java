package in.ac.iitm.students.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.fragments.ForceUpdateDialogFragment;
import in.ac.iitm.students.fragments.OptionalUpdateDialogFragment;
import in.ac.iitm.students.fragments.monthFragment;
import in.ac.iitm.students.objects.Calendar_Event;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

import static in.ac.iitm.students.activities.SubscriptionActivity.MY_PREFS_NAME;
import static in.ac.iitm.students.activities.main.HomeActivity.sendJsonRequest;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static int optionalUpdateDialogCount = 0;
    private static Context mContext;
    static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 99;
    static String urlForCalendarData = "https://students.iitm.ac.in/studentsapp/calendar/calendar_php.php";
    String url = "https://students.iitm.ac.in/studentsapp/general/subs.php";
    private Toolbar toolbar;
    private ProgressBar pbar;
    private Snackbar snackbar;
    private FragmentManager fm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawer;
    //for calendar
    static long CalID;
    private String[] months = {"january", "february", "march", "april", "may", "june", "july", "august", "september",
            "october", "november", "december"};
    private String calversion_url = "https://students.iitm.ac.in/studentsapp/calendar/cal_ver.php"; //url of api file
    private String cal_ver = "1";

    public static Context getContext() {
        return mContext;
    }

    public static void showAlert(Activity activity, String title, String message) {

        Drawable dialog_icon;
        dialog_icon = ContextCompat.getDrawable(activity, R.drawable.app_logo);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(dialog_icon);
        builder.setTitle(title);
        builder.setMessage(message)
                .setNeutralButton(R.string.dismiss_home_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlert(Activity activity, String title, String message, final String link) {

        Drawable dialog_icon;
        dialog_icon = ContextCompat.getDrawable(activity, R.drawable.app_logo);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(dialog_icon);
        builder.setTitle(title);
        builder.setMessage(message)
                .setNegativeButton(R.string.dismiss_home_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.go_to_link, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        openWebPage(link);
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private static void openWebPage(String url) {
        Toast.makeText(mContext, "Getting data...", Toast.LENGTH_SHORT).show();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "Error getting data, try again later...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            //Toast.makeText(this,version, Toast.LENGTH_SHORT).show();
            checkVersionMatch(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = getBaseContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updatePreferences();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshhome);
        swipeRefreshLayout.setOnRefreshListener(HomeActivity.this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        pbar = (ProgressBar) findViewById(R.id.pb_home);

        snackbar = Snackbar
                .make(drawer, R.string.error_connection, Snackbar.LENGTH_LONG);
        getData();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });





        // Here, thisActivity is the current activity
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
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            fetchingCalendarData();
        }

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("tada", firebaseToken.toString());
        sendRegistrationToServer(firebaseToken, name, roll_no);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_home)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView userrollNumber = (TextView) header.findViewById(R.id.tv_roll_number);

        username.setText(name);
        userrollNumber.setText(roll_no);
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
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    fetchingCalendarData();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        return;
    }

    //calendar code
    //***************************
    //***************************

    String getVersion() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, calversion_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsArray = new JSONArray(response);
                            JSONObject jsObject = jsArray.getJSONObject(0);
                            cal_ver = jsObject.getString("version");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                //Snackbar snackbar = Snackbar.make("Internet Connection Failed.", Snackbar.LENGTH_SHORT);
                //snackbar.show();

            }
        }) {
        };
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        return cal_ver;
    }

    void deleteallevents() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getContentResolver().delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + "= *", null);
    }

    public static void readMonthObject(JsonReader reader) throws IOException {

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("january")) {
                readMonthArray(reader, 0);

            } else if (name.equals("february")) {
                readMonthArray(reader, 1);

            } else if (name.equals("march")) {
                readMonthArray(reader, 2);

            } else if (name.equals("april")) {
                readMonthArray(reader, 3);

            } else if (name.equals("may")) {
                readMonthArray(reader, 4);

            } else if (name.equals("june")) {
                readMonthArray(reader, 5);

            } else if (name.equals("july")) {
                readMonthArray(reader, 6);

            } else if (name.equals("august")) {
                readMonthArray(reader, 7);

            } else if (name.equals("september")) {
                readMonthArray(reader, 8);

            } else if (name.equals("october")) {
                readMonthArray(reader, 9);

            } else if (name.equals("november")) {
                readMonthArray(reader, 10);

            } else if (name.equals("december")) {
                readMonthArray(reader, 11);

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
    }

    public static void readDayObject(JsonReader reader, int month, int i) throws IOException {

        Calendar_Event event = new Calendar_Event();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("date")) {
                event.setDate(Integer.parseInt(reader.nextString()));
                monthFragment.date[month - 6][i] = String.valueOf(event.getDate());

            } else if (name.equals("day")) {
                event.setDay(reader.nextString());
                monthFragment.day[month - 6][i] = event.getDay();

            } else if (name.equals("details")) {
                event.setDetails(reader.nextString());
                monthFragment.desc[month - 6][i] = event.getDetails();

            } else if (name.equals("holiday")) {
                event.setHoliday(reader.nextString().equals("TRUE"));
                if (event.isHoliday()) {
                    monthFragment.holiday[month - 6][i] = new String("TRUE");
                } else {
                    monthFragment.holiday[month - 6][i] = new String("FALSE");
                }

            } else if (name.equals("remind")) {
                event.setRemind(reader.nextString().equals("TRUE"));

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        event.setMonth(month);
        if (event.getDetails().length() > 0 && !exists(event)  ) {
            insertEvents(CalID, event);
        }
    }

    public static void readMonthArray(JsonReader reader, int month) throws IOException {

        reader.beginArray();
        int i = 0;
        while (reader.hasNext()) {
            readDayObject(reader, month, i);
            i++;
        }
        reader.endArray();
    }

    static void sendJsonRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForCalendarData, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                        readMonthObject(reader);
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
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    static void fetchingCalendarData(){
        String acc = "students.iitm";
        String disp = "IITM Calendar";
        String inter = "IITM Calendar";

        CalID = Utils.getprefLong("CalID", getContext());
        if (CalID == -1) {
            CalID = insertCalendar(acc, inter, disp);
            Log.i("CalID", CalID + "");
            Utils.saveprefLong("CalID", CalID, getContext());
        }
        //Utils.saveprefLong("Cal_Ver", -1, this);
        //if (!getVersion().equalsIgnoreCase(Utils.getprefString("Cal_Ver", this))) {
        //  deleteallevents();
        sendJsonRequest();

        //Utils.saveprefString("Cal_Ver", getVersion(), this);
        //}
    }

    static void insertEvents(long calId, Calendar_Event event) {

        Calendar cal = new GregorianCalendar(2017, event.getMonth() - 1, event.getDate());    //Jan is 0
        cal.setTimeZone(TimeZone.getTimeZone("IST"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long start = cal.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.TITLE, event.getDetails());
        values.put(CalendarContract.Events.EVENT_LOCATION, "Chennai");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "India");
        values.put(CalendarContract.Events.DESCRIPTION, event.isHoliday() ? "Holiday" : "");
// reasonable defaults exist:
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri =
                    getContext().getContentResolver().
                            insert(CalendarContract.Events.CONTENT_URI, values);
            long eventId = new Long(uri.getLastPathSegment());
            Log.i("EventID", eventId + "");
        }
    }

    static boolean exists(Calendar_Event event) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            String[] proj =
                    new String[]{CalendarContract.Events.TITLE};
            Calendar cal = new GregorianCalendar(2017, event.getMonth(), event.getDate());
            cal.setTimeZone(TimeZone.getTimeZone("IST"));
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long start = cal.getTimeInMillis();

            Cursor cursor = getContext().getContentResolver().query(
                    CalendarContract.Events.CONTENT_URI,
                    proj,
                    CalendarContract.Events.TITLE + " = ? AND " + CalendarContract.Events.DTSTART + " = ?",
                    new String[]{event.getDetails(), Long.toString(start)},
                    null);
            return cursor.moveToFirst();
        }
        return false;
    }

    static long getCalendarId(String acc) {
        Cursor cur = null;
        ContentResolver cr = getContext().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ?";
        String[] selectionArgs = new String[]{acc};
// Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            String[] EVENT_PROJECTION = new String[]{
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                    CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
            };

            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            if (cur.moveToFirst()) {
                return cur.getLong(0);
            }
            Log.i("CurCount", cur.getCount() + "");
        }
        return -1;
    }

    static long insertCalendar(String acc, String inter, String disp) {

        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, acc);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, inter);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, disp);
        //cv.put(CalendarContract.Calendars.CALENDAR_COLOR, yourColor);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, acc)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri result = getContext().getContentResolver().insert(calUri, cv);
        Log.i("Result", result.toString());
        return getCalendarId(acc);
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }


    //*****************************
    //*****************************
    //calendar code ends

    public void refreshList() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }

    private void getData() {

        final Context context = HomeActivity.this;

        pbar.setVisibility(View.VISIBLE);
        String url = getString(R.string.url_home);

        // Request a string response from the provided URL.
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    response = jsonArray.toString();
                    Log.d("response", "home " + response);
                    Utils.saveprefString(UtilStrings.homeData, response, getBaseContext());
                    goToAdapter(response);

                } catch (JSONException e) {

                    TextView tvError = (TextView) findViewById(R.id.tv_error_home);
                    tvError.setText(R.string.error_parsing);
                    pbar.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String dataBuffer = Utils.getprefString(UtilStrings.homeData, HomeActivity.this);
                    if (!dataBuffer.equals("")) {
                        String response = dataBuffer;
                        pbar.setVisibility(View.GONE);
                        snackbar.show();
                        goToAdapter(response);
                    } else {
                        error.printStackTrace();
                        pbar.setVisibility(View.GONE);
                        snackbar.show();

                        TextView tvError = (TextView) findViewById(R.id.tv_error_home);
                        tvError.setVisibility(View.VISIBLE);
                    }
                } catch (EmptyStackException e) {
                    error.printStackTrace();
                    pbar.setVisibility(View.GONE);
                    snackbar.show();

                    TextView tvError = (TextView) findViewById(R.id.tv_error_home);
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    private void goToAdapter(String response) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.content_home);
        HomeAdapter adapter = new HomeAdapter(response, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        pbar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void checkVersionMatch(final String version) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")//https://students.iitm.ac.in/studentsapp/general/version_check.php

                .authority("students.iitm.ac.in")
                .appendPath("studentsapp")
                .appendPath("general")
                .appendPath("version_check.php");
        final String url = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsArray = new JSONArray(response);
                            JSONObject jsObject = jsArray.getJSONObject(0);
                            String ver = jsObject.getString("version");
                            fm = getSupportFragmentManager();
                            if (Integer.parseInt(ver) == 1) {
                                DialogFragment optionalUpdateDialogFragment = new OptionalUpdateDialogFragment();
                                optionalUpdateDialogFragment.setCancelable(false);
                                if (optionalUpdateDialogCount == 0) {
                                    optionalUpdateDialogFragment.show(fm, "OptionalUpdateDialogFragment");
                                    optionalUpdateDialogCount++;
                                }


                            } else if (Integer.parseInt(ver) == 2) {

                                DialogFragment forceUpdateDialogFragment = new ForceUpdateDialogFragment();
                                forceUpdateDialogFragment.setCancelable(false);
                                forceUpdateDialogFragment.show(fm, "ForceUpdateDialogFragment");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                //Snackbar snackbar = Snackbar.make("Internet Connection Failed.", Snackbar.LENGTH_SHORT);
                //snackbar.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("version", version);
                return params;
            }
        };
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void sendRegistrationToServer(final String refreshedToken, final String name, final String roll_no) {
        // Instantiate the RequestQueue.
        //Toast.makeText(this,refreshedToken, Toast.LENGTH_SHORT).show();
        final String url = getString(R.string.url_register_fcm);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(HomeActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", refreshedToken);
                params.put("rollno", roll_no);
                params.put("name", name);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        int MY_SOCKET_TIMEOUT_MS = 10000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(HomeActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updatePreferences() {
        final ArrayList<HashMap<String, String>> database_topics = new ArrayList<>();

        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


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

                                hashMap.put("topic", jsObject.getString("topic"));
                                hashMap.put("value", jsObject.getString("value"));

                                database_topics.add(hashMap);
                            }

                            for (HashMap<String, String> hashMap : database_topics) {

                                if (Integer.parseInt(hashMap.get("value")) == 0) {

                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(hashMap.get("topic"));
                                    editor.remove(hashMap.get("topic"));
                                    editor.apply();
                                } else if (Integer.parseInt(hashMap.get("value")) == 1 && !prefs.contains(hashMap.get("topic"))) {

                                    FirebaseMessaging.getInstance().subscribeToTopic(hashMap.get("topic"));
                                    editor.putBoolean(hashMap.get("topic"), true);
                                    editor.apply();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Handle error
                        //Toast.makeText(context, "Couldn't connect to internet.", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        boolean flag = false;
        final Context context = HomeActivity.this;

        if (id == R.id.nav_home) {
            //intent = new Intent(context, HomeActivity.class);
            //flag = true;
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
            intent = new Intent(context, CalendarActivity.class);
            flag = true;
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
                            lg.isSure(HomeActivity.this);
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

    public interface ItemTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ItemTouchHelperAdapter {





        Context context;
        String response;

        private ArrayList<String> titles = new ArrayList<>();
        private ArrayList<String> details = new ArrayList<>();
        private ArrayList<String> image_urls = new ArrayList<>();
        private ArrayList<String> cats = new ArrayList<>();
        private ArrayList<String> createdat = new ArrayList<>();
        private ArrayList<String> subscribed = new ArrayList<>();
        private ArrayList<String> links = new ArrayList<>();
        private ImageLoader imageLoader;





        public HomeAdapter(String response, Context _context) {

            this.response = response;
            context = _context;

            imageLoader = MySingleton.getInstance(context).getImageLoader();
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Map<String, ?> allEntries = prefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getValue().toString().equals("true")) {
                    subscribed.add(entry.getKey());
                }
            }

            setUpData();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_feed, parent, false);


            return new ViewHolder(view);
        }

        private void setUpData() {

            try {

                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    String cat = jsonObject.getString("topic");

                    if (subscribed.size() != 0) {
                        if (subscribed.contains(cat)) {
                            titles.add(jsonObject.getString("title"));
                            details.add(jsonObject.getString("description"));
                            image_urls.add(jsonObject.getString("url"));
                            cats.add(cat);
                            links.add(jsonObject.getString("link"));
                            createdat.add(jsonObject.getString("created_at"));
                        }

                    } else {
                        links.add(jsonObject.getString("link"));
                        titles.add(jsonObject.getString("title"));
                        details.add(jsonObject.getString("description"));
                        cats.add(cat);
                        createdat.add(jsonObject.getString("created_at"));
                        image_urls.add(jsonObject.getString("url"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final String title = titles.get(position);
            final String detail = details.get(position);
            final String image_url = image_urls.get(position);
            final String link = links.get(position);
            final String topic = cats.get(position);
            Log.d("pani", title + " : " + link);

            holder.tvTitle.setText(title);
            holder.tvDetails.setText(detail);
            holder.ivHomeFeed.setImageUrl(image_url, imageLoader);
            holder.ivHomeFeed.setDefaultImageResId(R.mipmap.ic_launcher);
            holder.ivHomeFeed.setErrorImageResId(R.mipmap.ic_launcher);

            if (link.equals("nada")) {
                holder.iv_link.setVisibility(View.INVISIBLE);

                holder.rlHomeFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (topic) {
                            case "eml": {
                                //Intent intent = new Intent(context, EMLActivity.class);
                                //context.startActivity(intent);
                                break;
                            }
                            case "t5e": {
                                //Intent intent = new Intent(context, T5EActivity.class);
                                //context.startActivity(intent);
                                break;
                            }
                            default: {
                                try {
                                    HomeActivity.showAlert(HomeActivity.this, title, detail);
                                } catch (Exception e) {
                                    Log.e("tada", "home fragment", e);
                                }
                                break;
                            }
                        }

                    }
                });
            } else {

                Log.d("pika", title + " : link image visible");
                holder.iv_link.setVisibility(View.VISIBLE);

                holder.rlHomeFeed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            HomeActivity.showAlert(HomeActivity.this, title, detail, link);
                        } catch (Exception e) {
                            Log.e("tada", "home fragment", e);
                        }

                    }
                });
            }


        }


        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(titles, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(titles, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;

        }

        @Override
        public void onItemDismiss(int position) {

            titles.remove(position);
            notifyItemRemoved(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public int getItemCount() {
            return titles.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle, tvDetails;
            RelativeLayout rlHomeFeed;
            NetworkImageView ivHomeFeed;
            CardView cvhome;
            ImageView iv_link;

            ViewHolder(View itemView) {
                super(itemView);

                tvTitle = (TextView) itemView.findViewById(R.id.text_title_home_feed);
                tvDetails = (TextView) itemView.findViewById(R.id.tv_home_details);
                rlHomeFeed = (RelativeLayout) itemView.findViewById(R.id.rl_home_feed);
                ivHomeFeed = (NetworkImageView) itemView.findViewById(R.id.iv_home_feed_icon);
                cvhome = (CardView) itemView.findViewById(R.id.cl_home_feed);
                iv_link = (ImageView) itemView.findViewById(R.id.link_image_view);

            }


        }

    }

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

    }

}
