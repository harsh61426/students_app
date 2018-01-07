package in.ac.iitm.students.activities.main;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import in.ac.iitm.students.Manifest;
import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.LoginActivity;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.MessAndFacilitiesActivity;
import in.ac.iitm.students.fragments.ForceUpdateDialogFragment;
import in.ac.iitm.students.fragments.OptionalUpdateDialogFragment;
import in.ac.iitm.students.objects.HomeNotifObject;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.InstiCalendar;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.facebook.FacebookSdk.getApplicationContext;
import static in.ac.iitm.students.activities.SubscriptionActivity.MY_PREFS_NAME;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 99;
    static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 23;
    private static int optionalUpdateDialogCount = 0;
    public PopupWindow multipopup;
    public CardView containerLayout;
    public RelativeLayout containerLayout2;
    public View layout1;
    String url = "https://students.iitm.ac.in/studentsapp/general/subs.php";
    HomeAdapter adapter;
    RecyclerView recyclerView;
    String SwipePrefsName = "Ids_of_swiped_notifs";
    SharedPreferences swipedprefs,favprefs;
    String favPrefs = "Ids_of_fav_notifs";
    private Context mContext;
    private Toolbar toolbar;
    private ProgressBar pbar;
    private Snackbar snackbar;
    private FragmentManager fm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawer;
    private ArrayList<String> subscribed = new ArrayList<>();
    private ArrayList<String> favorite = new ArrayList<>();
    private ArrayList<HomeNotifObject> notifObjectList = new ArrayList<>();
    private Menu menu;
    private NavigationView navigationView;
    private String url_roll = "https://students.iitm.ac.in/studentsapp/studentlist/search_by_roll.php";
    private String rollNO;


//    public static void showAlert(Activity activity, String title, String message) {
//
//        Drawable dialog_icon;
//        dialog_icon = ContextCompat.getDrawable(activity, R.drawable.app_logo);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setIcon(dialog_icon);
//        builder.setTitle(title);
//        builder.setMessage(message)
//                .setNeutralButton(R.string.dismiss_home_dialog, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public static void showAlert(final Activity activity, String title, String message, final String link) {
//
//        Drawable dialog_icon;
//        dialog_icon = ContextCompat.getDrawable(activity, R.drawable.app_logo);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setIcon(dialog_icon);
//        builder.setTitle(title);
//        builder.setMessage(message)
//                .setNegativeButton(R.string.dismiss_home_dialog, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton(R.string.go_to_link, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        openWebPage(link, activity);
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    private static void openWebPage(String url, Activity activity) {
//        Toast.makeText(activity, "Getting data...", Toast.LENGTH_SHORT).show();
//        Uri webpage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (intent.resolveActivity(activity.getPackageManager()) != null) {
//            activity.startActivity(intent);
//        } else {
//            Toast.makeText(activity, "Error getting data, try again later...", Toast.LENGTH_SHORT).show();
//        }
//    }


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

        Intent i = getIntent();
        if(i.getStringExtra("signin")!=null){
            rollNO = i.getStringExtra("signin");
        }else{
            rollNO = null;
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshhome);
        swipeRefreshLayout.setOnRefreshListener(HomeActivity.this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        pbar = (ProgressBar) findViewById(R.id.pb_home);

        snackbar = Snackbar.make(drawer, R.string.error_connection, Snackbar.LENGTH_LONG);
        getData();

        containerLayout2 = (RelativeLayout) findViewById(R.id.rl_multipopup);
        multipopup = new PopupWindow(HomeActivity.this);
        multipopup.setContentView(containerLayout);
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout1 = inflater.inflate(R.layout.multimagepopup, (ViewGroup) findViewById(R.id.rl_multipopup));

//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
////            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
////                    Manifest.permission.WRITE_CALENDAR)) {
////
////                // Show an explanation to the user *asynchronously* -- don't block
////                // this thread waiting for the user's response! After the user
////                // sees the explanation, try again to request the permission.
////
////                Snackbar snackbar = Snackbar
////                        .make(drawer, "Granting this permission will allow the app to integrate official insti calendar with your personal calendar.", Snackbar.LENGTH_INDEFINITE);
////                snackbar.show();
////
////                ActivityCompat.requestPermissions(this,
////                        new String[]{Manifest.permission.WRITE_CALENDAR},
////                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
////
////            } else {
////
////                // No explanation needed, we can request the permission.
////
////                ActivityCompat.requestPermissions(this,
////                        new String[]{Manifest.permission.WRITE_CALENDAR},
////                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
////
////                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
////                // app-defined int constant. The callback method gets the
////                // result of the request.
////            }
////        } else {
////                if(Utils.getprefInt("CalStat",this)==1)
////                    new InstiCalendar(HomeActivity.this).fetchCalData(0);
////        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getGivenName();
            String personEmail = acct.getEmail();
//            Uri personPhoto = acct.getPhotoUrl();
            Utils.saveprefString(UtilStrings.NAME, personName, getBaseContext());
            Utils.saveprefInt(UtilStrings.REVEAL_PHOTO, Integer.parseInt("1"), getBaseContext());
            Utils.saveprefInt(UtilStrings.REVEAL_PLACE, Integer.parseInt("1"), getBaseContext());
            Utils.saveprefString(UtilStrings.ROLLNO, personEmail.split("@")[0], getBaseContext());
            Utils.saveprefBool(UtilStrings.LOGEDIN, true, this);
            getMyDetails();
        }

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);

        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(firebaseToken, name, roll_no);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_home)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView userrollNumber = (TextView) header.findViewById(R.id.tv_roll_number);

        username.setText(name);
        userrollNumber.setText(roll_no.toUpperCase());
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new InstiCalendar(this).getAllEvents(this);
                } else {
                    Toast.makeText(this,"Unable to sync calendar: Permission denied",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        return;
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }

    private void getMyDetails() {

        pbar.setVisibility(View.VISIBLE);

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")//https://students.iitm.ac.in/studentsapp/map/get_location.php?
                .authority("students.iitm.ac.in")
                .appendPath("Android")
                .appendPath("includes")
                .appendPath("search_by_roll.php");

        StringRequest stud_detail_via_roll_req = new StringRequest(Request.Method.POST, url_roll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    String studName = "Name appears here",
                            studRoll = "Roll number appears here",
                            hostel = "Hostel",
                            roomNo = "room number",
                            email = "Email here",
                            phone = "Phone no. here",
                            about = "About student";

//                    pDialog.dismiss();
                    JSONArray baseArray = new JSONArray(response);
                    for (int i = 0; i < baseArray.length(); i++) {
                        JSONObject baseObject = baseArray.getJSONObject(i);
                        studName = baseObject.getString("fullname");
                        hostel = baseObject.getString("hostel");
                        roomNo = baseObject.getString("room");
                        phone = baseObject.getString("phone_no");
                        email = baseObject.getString("email");
                    }

                    Utils.saveprefString(UtilStrings.NAME,studName, getBaseContext());
                    Utils.saveprefString(UtilStrings.HOSTEl, hostel, getBaseContext());
                    Utils.saveprefString(UtilStrings.ROOM, roomNo, getBaseContext());
                    Utils.saveprefInt(UtilStrings.REVEAL_PHOTO, Integer.parseInt("1"), getBaseContext());
                    Utils.saveprefInt(UtilStrings.REVEAL_PLACE, Integer.parseInt("1"), getBaseContext());

                    if(phone.equalsIgnoreCase("null")){
                        Utils.saveprefString(UtilStrings.MOBILE,"Enter mobile number", getBaseContext());
                    }else{
                        Utils.saveprefString(UtilStrings.MOBILE, phone, getBaseContext());
                    }

                    if(email.equalsIgnoreCase("null")){
                        Utils.saveprefString(UtilStrings.MAIL,"Enter email", getBaseContext());
                    }else{
                        Utils.saveprefString(UtilStrings.MAIL,email, getBaseContext());
                    }

                    pbar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet. Please check your connection!!";
                } else if (error instanceof ServerError) {
                    message = "Server down. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Authentication error!!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("roll" , Utils.getprefString(UtilStrings.ROLLNO,getApplicationContext()));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stud_detail_via_roll_req);
    }

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

        recyclerView = (RecyclerView) findViewById(R.id.content_home);
        adapter = new HomeAdapter(response, this);
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

        Boolean checkMenuItem = true;
        MenuItem item1 = menu.findItem(R.id.nav_complaint_mess);
        MenuItem item2 = menu.findItem(R.id.nav_complaint_hostel);
        MenuItem item3 = menu.findItem(R.id.nav_complaint_general);

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
            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_home)).setChecked(true);


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
        }
        else if(id==R.id.nav_sync_calendar) {
            sync_calendar();
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
                            lg.isSure(HomeActivity.this);
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

    private void sync_calendar()
    {
        //new Thread
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(android.Manifest.permission.WRITE_CALENDAR)==PackageManager.PERMISSION_GRANTED)
            {
                new InstiCalendar(this).getAllEvents(this);
            }
            else
            {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR},MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        }
        else
        {
            new InstiCalendar(this).getAllEvents(this);
        }
    }

    public interface ItemTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ItemTouchHelperAdapter {

        final HomeActivity obj = new HomeActivity();
        Context context;
        PopupWindow multipopup;
        View layout1;


        public HomeAdapter(String response, Context _context) {

            context = _context;

            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Map<String, ?> allEntries = prefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getValue().toString().equals("true")) {
                    subscribed.add(entry.getKey());
                }
            }

            swipedprefs = HomeActivity.this.getSharedPreferences(SwipePrefsName, MODE_PRIVATE);

            favprefs = HomeActivity.this.getSharedPreferences(favPrefs,MODE_PRIVATE);
            Map<String, ?> allEntries1 = favprefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                if (entry.getValue().toString().equals("true")) {
                    favorite.add(entry.getKey());
                }
            }
            //SharedPreferences.Editor editor = swipedprefs.edit();
            //editor.clear();
            //editor.apply();

            try {
                setUpData(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_feed, parent, false);
            return new ViewHolder(view);
        }

        private void readNotifArray(JsonReader reader) throws IOException {

            reader.beginArray();
            while (reader.hasNext()) {
                HomeNotifObject obj = readNotif(reader);
                //Log.d("taad",obj.Topic);
                if (subscribed.size() != 0) {
                    if (subscribed.contains(obj.Topic)) {

                        notifObjectList.add(obj);
                        if (obj.title.equals(swipedprefs.getString(obj.title, ""))) {
                            notifObjectList.remove(obj);
                        }
                        if(obj.title.equals(favprefs.getString(obj.title,""))){
                            obj.isfav = true;
                        }

                    }
                } else notifObjectList.add(obj);
            }
            //Log.d("taad",notifObjectList.toString());
            reader.endArray();
        }

        private HomeNotifObject readNotif(JsonReader reader) throws IOException {

            HomeNotifObject notifObject = new HomeNotifObject();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("topic")) {
                    notifObject.Topic = reader.nextString();
                } else if (name.equals("title")) {
                    notifObject.title = reader.nextString();
                } else if (name.equals("description")) {
                    notifObject.detail = reader.nextString();
                } else if (name.equals("created_at")) {
                    notifObject.createdat = reader.nextString();
                } else if (name.equals("link") && reader.peek() != JsonToken.NULL) {
                    notifObject.link = reader.nextString();
                } else if (name.equals("location") && reader.peek() != JsonToken.NULL) {
                    notifObject.location = reader.nextString();
                } else if (name.equals("image_urls") && reader.peek() != JsonToken.NULL) {
                    //readImageUrlArray(reader);
                    reader.beginArray();
                    while (reader.hasNext()) {
                        notifObject.image_urls.add(reader.nextString());
                    }
                    reader.endArray();
                    // todo add image functionality @rohithram
                } else if (name.equals("date") && reader.peek() != JsonToken.NULL) {
                    notifObject.date = reader.nextString();
                } else if (name.equals("time") && reader.peek() != JsonToken.NULL) {
                    notifObject.time = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return notifObject;
        }

        private void setUpData(String response) throws IOException {

            InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
            JsonReader reader = null;
            try {
                reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                reader.setLenient(true);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                readNotifArray(reader);
            } finally {

                reader.close();

            }

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final String title = notifObjectList.get(holder.getAdapterPosition()).title;
            final String detail = notifObjectList.get(holder.getAdapterPosition()).detail;
            final String link = notifObjectList.get(holder.getAdapterPosition()).link;
            final String topic = notifObjectList.get(holder.getAdapterPosition()).Topic;
            final String space = "";
            final ArrayList<String> image_urls = notifObjectList.get(holder.getAdapterPosition()).image_urls;

            holder.tvTitle.setText(title);
            holder.tvDetails.setText(detail);
            holder.tvorg.setText(topic);

            SpannableString content = new SpannableString(link);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.tv_link.setText(content);

            if(notifObjectList.get(holder.getAdapterPosition()).date!=null && notifObjectList.get(holder.getAdapterPosition()).time.equalsIgnoreCase(space)){
                holder.tv_date.setText(notifObjectList.get(holder.getAdapterPosition()).date);
                holder.bt_date.setVisibility(View.VISIBLE);
                holder.tv_date.setVisibility(View.VISIBLE);
            }

            if(notifObjectList.get(holder.getAdapterPosition()).time!=null && notifObjectList.get(holder.getAdapterPosition()).time.equalsIgnoreCase(space)){
                holder.tv_time.setText(notifObjectList.get(holder.getAdapterPosition()).time);
                holder.bt_time.setVisibility(View.VISIBLE);
                holder.tv_time.setVisibility(View.VISIBLE);
            }

            if(notifObjectList.get(holder.getAdapterPosition()).location!=null && notifObjectList.get(holder.getAdapterPosition()).location.equalsIgnoreCase(space)){
                holder.tv_location.setText(notifObjectList.get(holder.getAdapterPosition()).location);
                holder.bt_loc.setVisibility(View.VISIBLE);
                holder.tv_location.setVisibility(View.VISIBLE);
            }


            holder.tv_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(link!=null && !link.isEmpty()){
                        Uri uri = null;
                        if (!link.startsWith("http://") && !link.startsWith("https://")) {
                                uri = Uri.parse("http://" + url);
                            }

                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent)

                }
            });

            holder.cvhome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.bt_date.getLayoutParams();

                    if (holder.tvDetails.getVisibility()==View.GONE && holder.rv_gridimages2.getVisibility()==View.GONE && holder.rv_gridimages.getVisibility()==View.GONE) {
                        // it's collapsed - expand it
                        holder.tvDetails.setVisibility(View.VISIBLE);
                        holder.ibt_show.setImageResource(R.drawable.ic_expand_less_black_24dp);
//                        holder.bt_not_going.setVisibility(View.VISIBLE);
//                        holder.bt_going.setVisibility(View.VISIBLE);
                        holder.tv_link.setVisibility(View.VISIBLE);
                        holder.ibt_link.setVisibility(View.VISIBLE);

//                            lp.addRule(RelativeLayout.BELOW, holder.tvDetails.getId());
//
//                        if(notifObjectList.get(holder.getAdapterPosition()).time!=null && notifObjectList.get(holder.getAdapterPosition()).time.equalsIgnoreCase(space)){
//                            holder.tv_time.setText(notifObjectList.get(holder.getAdapterPosition()).time);
//                            holder.bt_time.setVisibility(View.VISIBLE);
//                            holder.tv_time.setVisibility(View.VISIBLE);
//                            lp.addRule(RelativeLayout.BELOW, holder.tv_date.getId());
//
//                        }
//
//                        if(notifObjectList.get(holder.getAdapterPosition()).location!=null && notifObjectList.get(holder.getAdapterPosition()).location.equalsIgnoreCase(space)){
//                            holder.tv_location.setText(notifObjectList.get(holder.getAdapterPosition()).location);
//                            holder.bt_loc.setVisibility(View.VISIBLE);
//                            holder.tv_location.setVisibility(View.VISIBLE);
//                            lp.addRule(RelativeLayout.BELOW, holder.tv_date.getId());
//
//                        }

                        if(image_urls!=null){
                            if(image_urls.size()!=0) {

                            if(image_urls.size()==1){
                                Glide.with(context).
                                        load(image_urls.get(0))
                                        .placeholder(R.color.Imageback)
                                        .crossFade(500)
                                        .into(holder.iv_content);

                            }


                            if(image_urls!=null && image_urls.size() >= 2){

                                holder.iv_content.setVisibility(View.INVISIBLE);
                                holder.iv_content.getLayoutParams().height = 0;
                                holder.iv_content.getLayoutParams().width = 0;

                                if(image_urls.size()>=3){

                                    Glide.with(context).
                                            load(image_urls.get(0))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .centerCrop()
                                            .into(holder.iv_imag11);
                                    Glide.with(context).
                                            load(image_urls.get(1))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .into(holder.iv_image12);

                                    Glide.with(context).
                                            load(image_urls.get(2))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .into(holder.iv_image13);

                                    holder.rv_gridimages.setVisibility(View.VISIBLE);

                                }

                                if(image_urls.size()==2) {

                                    Glide.with(context).
                                            load(image_urls.get(0))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .centerCrop()
                                            .into(holder.iv_image21);
                                    Glide.with(context).
                                            load(image_urls.get(1))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .centerCrop()
                                            .into(holder.iv_image22);

                                    holder.rv_gridimages2.setVisibility(View.VISIBLE);


                                }

                                if(image_urls.size()==3){
                                    holder.tv_nofimages.setVisibility(View.INVISIBLE);

                                }
                                else if(image_urls.size()==2){
                                    holder.tv_nofimages.setVisibility(View.INVISIBLE);
                                    holder.iv_image12.getLayoutParams().height = 280;

                                }
                                else if(image_urls.size() > 3) {

                                    holder.tv_nofimages.setText(String.valueOf(image_urls.size() - 3) + "+");
                                }

                            }

                            holder.rv_gridimages2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    try{

                                        final int[] p = {0};
                                        multipopup = new PopupWindow(layout1,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,true);

                                        final ImageView iv_popupimage;
                                        ImageButton ibt_close,ibt_fwd,ibt_back;

                                        iv_popupimage = (ImageView)layout1.findViewById(R.id.iv_popupimage);

                                        ibt_close = (ImageButton)layout1.findViewById(R.id.ibt_close);
                                        ibt_fwd = (ImageButton)layout1.findViewById(R.id.ibt_forward);
                                        ibt_back = (ImageButton)layout1.findViewById(R.id.ibt_backward);

                                        Glide.with(context).
                                                load(image_urls.get(p[0]))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .into(iv_popupimage);

                                        multipopup.setTouchable(true);
                                        multipopup.setFocusable(true);
                                        multipopup.setBackgroundDrawable(new ColorDrawable(
                                                android.graphics.Color.TRANSPARENT));
                                        multipopup.setOutsideTouchable(false);



                                        new Handler().postDelayed(new Runnable(){
                                            public void run() {
                                                multipopup.showAtLocation(v, Gravity.CENTER,0,0);
//                                                obj.dim();
                                            }

                                        }, 200L);


                                        ibt_back.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if(p[0]==0){
                                                    Toast.makeText(context, "Move Forward!", Toast.LENGTH_SHORT).show();

                                                }
                                                if(p[0]!=0){
                                                    p[0] = p[0]-1;
                                                    setImage(iv_popupimage,image_urls.get(p[0]));
                                                }

                                            }
                                        });


                                        ibt_fwd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(p[0]==image_urls.size()-1){
                                                    Toast.makeText(context, "That's All Buddy!", Toast.LENGTH_SHORT).show();

                                                }
                                                if(p[0]!=image_urls.size()-1){
                                                    p[0] = p[0] +1;
                                                    setImage(iv_popupimage,image_urls.get(p[0]));}

                                            }
                                        });

                                        ibt_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                multipopup.dismiss();
//                                                obj.normal();

                                            }
                                        });

                                        multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                            @Override
                                            public void onDismiss() {
//                                                obj.normal();
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                            holder.rv_gridimages.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(final View v) {

                                    try{

                                        final int[] p = {0};
                                        multipopup = new PopupWindow(layout1,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,true);

                                        final ImageView iv_popupimage;
                                        ImageButton ibt_close,ibt_fwd,ibt_back;

                                        iv_popupimage = (ImageView)layout1.findViewById(R.id.iv_popupimage);

                                        ibt_fwd = (ImageButton)layout1.findViewById(R.id.ibt_forward);
                                        ibt_close = (ImageButton)layout1.findViewById(R.id.ibt_close);
                                        ibt_back = (ImageButton)layout1.findViewById(R.id.ibt_backward);

                                        Glide.with(context).
                                                load(image_urls.get(p[0]))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .into(iv_popupimage);

                                        multipopup.setTouchable(true);
                                        multipopup.setFocusable(true);
                                        multipopup.setElevation(32);
                                        multipopup.setBackgroundDrawable(new ColorDrawable(
                                                android.graphics.Color.TRANSPARENT));
                                        multipopup.setOutsideTouchable(false);

                                        new Handler().postDelayed(new Runnable(){
                                            public void run() {
                                                multipopup.showAtLocation(v,Gravity.CENTER,0,0);
//                                                obj.dim();
                                            }

                                        }, 200L);

                                        ibt_back.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if(p[0]==0){
                                                    Toast.makeText(context, "Move Forward!", Toast.LENGTH_SHORT).show();

                                                }
                                                if(p[0]!=0){
                                                    p[0] = p[0]-1;
                                                    setImage(iv_popupimage,image_urls.get(p[0]));
                                                }

                                            }
                                        });


                                        ibt_fwd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if(p[0]==image_urls.size()-1 ){
                                                    Toast.makeText(context, "That's All Buddy", Toast.LENGTH_SHORT).show();
                                                }
                                                if(p[0]!=image_urls.size()-1){
                                                    p[0] = p[0] +1;
                                                    if(p[0]==image_urls.size()-2){

                                                    }
                                                    setImage(iv_popupimage,image_urls.get(p[0]));
                                                }

                                            }
                                        });

                                        ibt_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                multipopup.dismiss();
//                                                obj.normal();

                                            }
                                        });

                                        multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                            @Override
                                            public void onDismiss() {
//                                                obj.normal();
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }

//                        holder.v_bottom.setLayoutParams(lp);
                    }

                    }else {
                        // it's expanded - collapse it
                        holder.tvDetails.setVisibility(View.GONE);
                        holder.tv_link.setVisibility(View.GONE);
                        holder.bt_going.setVisibility(View.GONE);
                        holder.bt_not_going.setVisibility(View.GONE);
                        holder.ibt_link.setVisibility(View.GONE);
                        holder.ibt_show.setImageResource(R.drawable.ic_expand_more_black_24dp);
//                        lp.removeRule(RelativeLayout.BELOW);
//                        lp.addRule(RelativeLayout.BELOW,holder.tvTitle.getId());

//                        holder.bt_date.setVisibility(View.GONE);
//                        holder.tv_date.setVisibility(View.GONE);
//                        holder.bt_time.setVisibility(View.GONE);
//                        holder.tv_time.setVisibility(View.GONE);
//                        holder.bt_loc.setVisibility(View.GONE);
//                        holder.tv_location.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) holder.v_bottom.getLayoutParams();
                        lp1.addRule(RelativeLayout.BELOW, holder.bt_going.getId());
                        holder.v_bottom.setLayoutParams(lp1);
                        holder.v_bottom.getLayoutParams().height = WRAP_CONTENT;

                    }

                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.tvDetails, "maxLines",holder.tvDetails.getMaxLines());
                    animation.setDuration(200).start();
                }
            });

            holder.ibt_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.bt_date.getLayoutParams();

                    if (holder.tvDetails.getVisibility()==View.GONE && holder.rv_gridimages2.getVisibility()==View.GONE && holder.rv_gridimages.getVisibility()==View.GONE) {
                        // it's collapsed - expand it
                        holder.tvDetails.setVisibility(View.VISIBLE);
                        holder.ibt_show.setImageResource(R.drawable.ic_expand_less_black_24dp);
//                        holder.bt_not_going.setVisibility(View.VISIBLE);
//                        holder.bt_going.setVisibility(View.VISIBLE);
                        holder.tv_link.setVisibility(View.VISIBLE);
                        holder.ibt_link.setVisibility(View.VISIBLE);

//                            lp.addRule(RelativeLayout.BELOW, holder.tvDetails.getId());
//
//                        if(notifObjectList.get(holder.getAdapterPosition()).time!=null && notifObjectList.get(holder.getAdapterPosition()).time.equalsIgnoreCase(space)){
//                            holder.tv_time.setText(notifObjectList.get(holder.getAdapterPosition()).time);
//                            holder.bt_time.setVisibility(View.VISIBLE);
//                            holder.tv_time.setVisibility(View.VISIBLE);
//                            lp.addRule(RelativeLayout.BELOW, holder.tv_date.getId());
//
//                        }
//
//                        if(notifObjectList.get(holder.getAdapterPosition()).location!=null && notifObjectList.get(holder.getAdapterPosition()).location.equalsIgnoreCase(space)){
//                            holder.tv_location.setText(notifObjectList.get(holder.getAdapterPosition()).location);
//                            holder.bt_loc.setVisibility(View.VISIBLE);
//                            holder.tv_location.setVisibility(View.VISIBLE);
//                            lp.addRule(RelativeLayout.BELOW, holder.tv_date.getId());
//
//                        }

                        if(image_urls!=null){
                            if(image_urls.size()!=0) {

                                if(image_urls.size()==1){
                                    Glide.with(context).
                                            load(image_urls.get(0))
                                            .placeholder(R.color.Imageback)
                                            .crossFade(500)
                                            .into(holder.iv_content);

                                }


                                if(image_urls!=null && image_urls.size() >= 2){

                                    holder.iv_content.setVisibility(View.INVISIBLE);
                                    holder.iv_content.getLayoutParams().height = 0;
                                    holder.iv_content.getLayoutParams().width = 0;

                                    if(image_urls.size()>=3){

                                        Glide.with(context).
                                                load(image_urls.get(0))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .centerCrop()
                                                .into(holder.iv_imag11);
                                        Glide.with(context).
                                                load(image_urls.get(1))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .into(holder.iv_image12);

                                        Glide.with(context).
                                                load(image_urls.get(2))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .into(holder.iv_image13);

                                        holder.rv_gridimages.setVisibility(View.VISIBLE);

                                    }

                                    if(image_urls.size()==2) {

                                        Glide.with(context).
                                                load(image_urls.get(0))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .centerCrop()
                                                .into(holder.iv_image21);
                                        Glide.with(context).
                                                load(image_urls.get(1))
                                                .placeholder(R.color.Imageback)
                                                .crossFade(500)
                                                .centerCrop()
                                                .into(holder.iv_image22);

                                        holder.rv_gridimages2.setVisibility(View.VISIBLE);


                                    }

                                    if(image_urls.size()==3){
                                        holder.tv_nofimages.setVisibility(View.INVISIBLE);

                                    }
                                    else if(image_urls.size()==2){
                                        holder.tv_nofimages.setVisibility(View.INVISIBLE);
                                        holder.iv_image12.getLayoutParams().height = 280;

                                    }
                                    else if(image_urls.size() > 3) {

                                        holder.tv_nofimages.setText(String.valueOf(image_urls.size() - 3) + "+");
                                    }

                                }

                                holder.rv_gridimages2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        try{

                                            final int[] p = {0};
                                            multipopup = new PopupWindow(layout1,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,true);

                                            final ImageView iv_popupimage;
                                            ImageButton ibt_close,ibt_fwd,ibt_back;

                                            iv_popupimage = (ImageView)layout1.findViewById(R.id.iv_popupimage);

                                            ibt_close = (ImageButton)layout1.findViewById(R.id.ibt_close);
                                            ibt_fwd = (ImageButton)layout1.findViewById(R.id.ibt_forward);
                                            ibt_back = (ImageButton)layout1.findViewById(R.id.ibt_backward);

                                            Glide.with(context).
                                                    load(image_urls.get(p[0]))
                                                    .placeholder(R.color.Imageback)
                                                    .crossFade(500)
                                                    .into(iv_popupimage);

                                            multipopup.setTouchable(true);
                                            multipopup.setFocusable(true);
                                            multipopup.setBackgroundDrawable(new ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT));
                                            multipopup.setOutsideTouchable(false);



                                            new Handler().postDelayed(new Runnable(){
                                                public void run() {
                                                    multipopup.showAtLocation(v, Gravity.CENTER,0,0);
//                                                obj.dim();
                                                }

                                            }, 200L);


                                            ibt_back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if(p[0]==0){
                                                        Toast.makeText(context, "Move Forward!", Toast.LENGTH_SHORT).show();

                                                    }
                                                    if(p[0]!=0){
                                                        p[0] = p[0]-1;
                                                        setImage(iv_popupimage,image_urls.get(p[0]));
                                                    }

                                                }
                                            });


                                            ibt_fwd.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(p[0]==image_urls.size()-1){
                                                        Toast.makeText(context, "That's All Buddy!", Toast.LENGTH_SHORT).show();

                                                    }
                                                    if(p[0]!=image_urls.size()-1){
                                                        p[0] = p[0] +1;
                                                        setImage(iv_popupimage,image_urls.get(p[0]));}

                                                }
                                            });

                                            ibt_close.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    multipopup.dismiss();
//                                                obj.normal();

                                                }
                                            });

                                            multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                                @Override
                                                public void onDismiss() {
//                                                obj.normal();
                                                }
                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                                holder.rv_gridimages.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onClick(final View v) {

                                        try{

                                            final int[] p = {0};
                                            multipopup = new PopupWindow(layout1,RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT,true);

                                            final ImageView iv_popupimage;
                                            ImageButton ibt_close,ibt_fwd,ibt_back;

                                            iv_popupimage = (ImageView)layout1.findViewById(R.id.iv_popupimage);

                                            ibt_fwd = (ImageButton)layout1.findViewById(R.id.ibt_forward);
                                            ibt_close = (ImageButton)layout1.findViewById(R.id.ibt_close);
                                            ibt_back = (ImageButton)layout1.findViewById(R.id.ibt_backward);

                                            Glide.with(context).
                                                    load(image_urls.get(p[0]))
                                                    .placeholder(R.color.Imageback)
                                                    .crossFade(500)
                                                    .into(iv_popupimage);

                                            multipopup.setTouchable(true);
                                            multipopup.setFocusable(true);
                                            multipopup.setElevation(32);
                                            multipopup.setBackgroundDrawable(new ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT));
                                            multipopup.setOutsideTouchable(false);

                                            new Handler().postDelayed(new Runnable(){
                                                public void run() {
                                                    multipopup.showAtLocation(v,Gravity.CENTER,0,0);
//                                                obj.dim();
                                                }

                                            }, 200L);

                                            ibt_back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if(p[0]==0){
                                                        Toast.makeText(context, "Move Forward!", Toast.LENGTH_SHORT).show();

                                                    }
                                                    if(p[0]!=0){
                                                        p[0] = p[0]-1;
                                                        setImage(iv_popupimage,image_urls.get(p[0]));
                                                    }

                                                }
                                            });


                                            ibt_fwd.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if(p[0]==image_urls.size()-1 ){
                                                        Toast.makeText(context, "That's All Buddy", Toast.LENGTH_SHORT).show();
                                                    }
                                                    if(p[0]!=image_urls.size()-1){
                                                        p[0] = p[0] +1;
                                                        if(p[0]==image_urls.size()-2){

                                                        }
                                                        setImage(iv_popupimage,image_urls.get(p[0]));
                                                    }

                                                }
                                            });

                                            ibt_close.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    multipopup.dismiss();
//                                                obj.normal();

                                                }
                                            });

                                            multipopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                                @Override
                                                public void onDismiss() {
//                                                obj.normal();
                                                }
                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });
                            }

//                        holder.v_bottom.setLayoutParams(lp);
                        }

                    }else {
                        // it's expanded - collapse it
                        holder.tvDetails.setVisibility(View.GONE);
                        holder.tv_link.setVisibility(View.GONE);
                        holder.bt_going.setVisibility(View.GONE);
                        holder.bt_not_going.setVisibility(View.GONE);
                        holder.ibt_link.setVisibility(View.GONE);
                        holder.ibt_show.setImageResource(R.drawable.ic_expand_more_black_24dp);
//                        lp.removeRule(RelativeLayout.BELOW);
//                        lp.addRule(RelativeLayout.BELOW,holder.tvTitle.getId());

//                        holder.bt_date.setVisibility(View.GONE);
//                        holder.tv_date.setVisibility(View.GONE);
//                        holder.bt_time.setVisibility(View.GONE);
//                        holder.tv_time.setVisibility(View.GONE);
//                        holder.bt_loc.setVisibility(View.GONE);
//                        holder.tv_location.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) holder.v_bottom.getLayoutParams();
                        lp1.addRule(RelativeLayout.BELOW, holder.bt_going.getId());
                        holder.v_bottom.setLayoutParams(lp1);
                        holder.v_bottom.getLayoutParams().height = WRAP_CONTENT;

                    }

                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.tvDetails, "maxLines",holder.tvDetails.getMaxLines());
                    animation.setDuration(200).start();
                }
            });


            holder.ibt_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                    String shareBody = "Here is the share content body";
                    String shareBody = "#Students App\n"+holder.tvDetails.getText().toString();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, holder.tvTitle.getText());
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

            if(notifObjectList.get(holder.getAdapterPosition()).isfav){
                holder.ibt_fav.setVisibility(View.VISIBLE);
            }

            holder.ibt_save.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                   holder.ibt_fav.setVisibility(View.VISIBLE);
                    final SharedPreferences[] sharedprefs = {HomeActivity.this.getSharedPreferences(favPrefs, MODE_PRIVATE)};
                    final SharedPreferences.Editor editor = sharedprefs[0].edit();
                    editor.putString(notifObjectList.get(holder.getAdapterPosition()).title,notifObjectList.get(holder.getAdapterPosition()).title
                    );
                    editor.apply();
                }
            });

//                holder.rlHomeFeed.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        CustomDialog cdd = new CustomDialog(HomeActivity.this, notifObjectList.get(holder.getAdapterPosition()));
//                        cdd.show();
//
//                    }
//                });


        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(notifObjectList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(notifObjectList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;

        }

        @Override
        public void onItemDismiss(int position) {

            notifObjectList.remove(position);
            notifyItemRemoved(position);

        }

        private Void  setImage(ImageView image,String url){
        Glide.with(context).
                load(url)
                .placeholder(R.color.Imageback)
                .fitCenter()
                .crossFade(500)
                .into(image);
        return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

//        public void setEmpty(View view){
//            view.getLayoutParams().height = 0;
//            view.getLayoutParams().width =0;
//            view.setVisibility(View.INVISIBLE);
//        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

       @Override
        public int getItemCount() {
           return notifObjectList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle, tvDetails ,tvorg ,tv_time, tv_date, tv_location,tv_nofimages,tv_link;
            RelativeLayout rlHomeFeed;
            CardView cvhome;
            ImageButton ibt_show ,bt_loc,bt_date, bt_time;
            ImageButton ibt_share,ibt_save,ibt_link;
            ImageView ibt_fav,iv_content;
            FrameLayout fl_images;
            View v_bottom;
            ImageView iv_imag11,iv_image12,iv_image13,iv_image21,iv_image22;
            LinearLayout rv_gridimages;
            LinearLayout rv_gridimages2;
            Button bt_going,bt_not_going;

            ViewHolder(View itemView) {
                super(itemView);

                tvorg = (TextView)itemView.findViewById(R.id.tv_org);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
                tv_location = (TextView) itemView.findViewById(R.id.tv_loc);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                tv_link = (TextView)itemView.findViewById(R.id.tv_link);
                ibt_show = (ImageButton) itemView.findViewById(R.id.bt_show);
                ibt_share = (ImageButton) itemView.findViewById(R.id.bt_share);
                ibt_save= (ImageButton) itemView.findViewById(R.id.bt_save);
                ibt_fav= (ImageView) itemView.findViewById(R.id.bt_fav);
                ibt_link = (ImageButton) itemView.findViewById(R.id.ibt_link);
                iv_content = (ImageView) itemView.findViewById(R.id.iv_content);
                fl_images = (FrameLayout)itemView.findViewById(R.id.fl_images);
                bt_going = (Button)itemView.findViewById(R.id.bt_going);
                bt_not_going = (Button) itemView.findViewById(R.id.bt_not_going);

                rv_gridimages = (LinearLayout)itemView.findViewById(R.id.rv_gridimages3);
                rv_gridimages2 = (LinearLayout)itemView.findViewById(R.id.rv_gridimages2);
                tv_nofimages = (TextView)itemView.findViewById(R.id.tv_nofimages);
                iv_imag11 = (ImageView)itemView.findViewById(R.id.iv_image11);
                iv_image12 = (ImageView)itemView.findViewById(R.id.iv_image12);
                iv_image13 = (ImageView)itemView.findViewById(R.id.iv_image13);
                iv_image21 = (ImageView)itemView.findViewById(R.id.iv_image21);
                iv_image22 = (ImageView)itemView.findViewById(R.id.iv_image22);

                rlHomeFeed = (RelativeLayout) itemView.findViewById(R.id.rl_home_feed);
                cvhome = (CardView) itemView.findViewById(R.id.cv_home_feed);
                bt_loc = (ImageButton)itemView.findViewById(R.id.bt_loc);
                bt_time = (ImageButton) itemView.findViewById(R.id.bt_time);
                bt_date = (ImageButton)itemView.findViewById(R.id.bt_event);
                v_bottom = itemView.findViewById(R.id.v_bottom);

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

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            final int adapterPosition = viewHolder.getAdapterPosition();

            final SharedPreferences[] sharedprefs = {HomeActivity.this.getSharedPreferences(SwipePrefsName, MODE_PRIVATE)};
            final SharedPreferences.Editor editor = sharedprefs[0].edit();
            editor.putString(notifObjectList.get(adapterPosition).title, notifObjectList.get(adapterPosition).title);
            editor.apply();
            HomeNotifObject notifobj = notifObjectList.remove(adapterPosition);

            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            final HomeNotifObject finalNotifobj = notifobj;
            final HomeNotifObject finalNotifobj1 = notifobj;
            Snackbar snackbar = Snackbar    //assuming that a Snackbar with "UNDO" button is what you want.
                    .make(viewHolder.itemView, "Notification Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                        @Override
                        public void onClick(View view) {

                            notifObjectList.add(adapterPosition, finalNotifobj); //deleted element readded to ArrayList
                            adapter.notifyItemInserted(adapterPosition);
                            sharedprefs[0] = HomeActivity.this.getSharedPreferences(SwipePrefsName, MODE_PRIVATE);
                            editor.remove(finalNotifobj1.title);
                            editor.apply();
                            recyclerView.scrollToPosition(adapterPosition);
                            //this happens when "Undo" is clicked.

                            // viewHolder.itemView.scroll(viewHolder.getAdapterPosition());
                        }
                    });
            snackbar.show();
        }

    }


}




