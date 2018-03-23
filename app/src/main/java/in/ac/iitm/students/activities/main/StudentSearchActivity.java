package in.ac.iitm.students.activities.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.adapters.StudentSearchAdapter;
import in.ac.iitm.students.objects.Student;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;

public class StudentSearchActivity extends AppCompatActivity {

    /*TODO: Add in fragment to search for courses
    * Get courses data from academic.iitm
    * Should allow search by course number/title
    * Should display course number, title, instructor name and room number as result*/

    /*TODO: Add in fragment for places search
     * Add in info of places which exist in insti i.e. departments, hostels, OAT, CLT, etc.
     * Display information about place(if any) as given in heritage trails app
     * Search fragment should show results on map(can reuse mapactivity)
     */

    /*TODO: Add in faculty search fragment
     * Should display facult name and room number along with photo
     * Information should be available on academic.iitm
     * If not, let one of the MobOps cores know, we'll get it for you
     */

    Toolbar toolbar;
    ListView lvSuggestion;
    StudentSearchAdapter adapter;
    ArrayList<Student> listSuggestion = new ArrayList<>(25);
    EditText etSearch;
    ProgressBar progressSearch;
    TextView searchMessage;
    FrameLayout frameLayout;
    //    private DrawerLayout drawer;
//    private Menu menu;
//    private NavigationView navigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent1;
            final Context context = StudentSearchActivity.this;

            switch (item.getItemId()) {
                case R.id.bot_nav_home:
                    intent1 = new Intent(context, HomeActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_organisations:
                    intent1 = new Intent(context, OrganizationActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_subscriptions:
                    intent1 = new Intent(context, SubscriptionActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_map:
                    intent1 = new Intent(context, MapActivity.class);
                    context.startActivity(intent1);
                    return true;
                case R.id.bot_nav_student_search:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        menu = navigationView.getMenu();
//        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_search)).setChecked(true);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        View header = navigationView.getHeaderView(0);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bot_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavBehaviour());
        navigation.setSelectedItemId(R.id.bot_nav_student_search);

//        TextView username = (TextView) header.findViewById(R.id.tv_username);
//        TextView rollNumber = (TextView) header.findViewById(R.id.tv_roll_number);
//
//        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
//        String name = Utils.getprefString(UtilStrings.NAME, this);
//
//        username.setText(name);
//        rollNumber.setText(roll_no);
//
//        ImageView imageView = (ImageView) header.findViewById(R.id.user_pic);
//        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
//        Picasso.with(this)
//                .load(urlPic)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .fit()
//                .centerCrop()
//                .into(imageView);

        initialize_search();
    }

    private void initialize_search()
    {
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout_name);
        progressSearch = (ProgressBar) findViewById(R.id.pb_search);
        etSearch = (EditText) findViewById(R.id.et_search_name);
        searchMessage = (TextView) findViewById(R.id.tv_search_result_msg);
        lvSuggestion = (ListView) findViewById(R.id.lv_suggestion);
        adapter = new StudentSearchAdapter(listSuggestion,this);
        lvSuggestion.setAdapter(adapter);
        lvSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = (Student) parent.getItemAtPosition(position);
                viewDetails(student);
                //goToDetails(name);
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showSuggestion(s.toString());
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final Editable selection = etSearch.getText();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    }
                    showSuggestion(selection.toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void showSuggestion(String query) {

        listSuggestion.clear();
        MySingleton.getInstance(this).getRequestQueue().cancelAll("tag");
        if (query.length() <= 2) {
            progressSearch.setVisibility(View.GONE);
            searchMessage.setText(R.string.error_enter_more_characters);
            searchMessage.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            return;
        }
        searchMessage.setVisibility(View.INVISIBLE);
        progressSearch.setVisibility(View.VISIBLE);

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")//https://students.iitm.ac.in/studentsapp/studentlist/search_by_name.php
                .authority("students.iitm.ac.in")
                .appendPath("studentsapp")
                .appendPath("studentlist")
                .appendPath("search_student.php");

        String url = builder.build().toString();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    int i;
                    Student student;
                    listSuggestion.clear();

                    for (i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //Log.i("JSON",jsonObject.toString());
                        student = new Student();
                        student.setName(jsonObject.getString("fullname"));
                        student.setRollno(jsonObject.getString("username"));
                        student.setHostel(jsonObject.getString("hostel"));
                        student.setRoom(jsonObject.getString("room"));
                        student.setGender(jsonObject.getString("gender").charAt(0));

                        if (!listSuggestion.contains(student))
                            listSuggestion.add(student);//+", "+studRoll
                    }
                    adapter.notifyDataSetChanged();
                    if(listSuggestion.size()!=1) {
                        searchMessage.setText("Search returned " + listSuggestion.size() + " results");
                    }
                    else
                    {
                        searchMessage.setText("Search returned 1 result");
                    }
                    searchMessage.setVisibility(View.VISIBLE);
                    progressSearch.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Log.d("URL", response);
                    if (response.equals("No results") || response.equals("")) {
                        searchMessage.setText(R.string.error_no_result);
                        searchMessage.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(frameLayout, getString(R.string.error_parsing), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    listSuggestion.clear();
                    adapter.notifyDataSetChanged();
                    progressSearch.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                searchMessage.setText(R.string.error_connection);
                searchMessage.setVisibility(View.VISIBLE);
                listSuggestion.clear();
                adapter.notifyDataSetChanged();
                progressSearch.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", etSearch.getText().toString());
                //Log.i("name",etSearch.getText().toString());
                return params;
            }
        };

        jsonObjReq.setTag("tag");
        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new NameSearchFragment(), "Name");
//        adapter.addFragment(new RollSearchFragment(), "Roll number");
//        viewPager.setAdapter(adapter);
//    }

    private void viewDetails(Student student)
    {
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Student details");
        dialog.setContentView(R.layout.dialog_details);
        TextView rollno = (TextView)dialog.findViewById(R.id.d_rollno);
        rollno.setText(student.getRollno());
        TextView name = (TextView)dialog.findViewById(R.id.d_name);
        name.setText(student.getName());
        TextView room = (TextView)dialog.findViewById(R.id.d_room);
        room.setText(student.getRoom()+", "+student.getHostel());
        CircleImageView photo = (CircleImageView)dialog.findViewById(R.id.d_photo);Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority("photos.iitm.ac.in")
                .appendPath("byroll.php")
                .appendQueryParameter("roll", student.getRollno());

        String url = builder.build().toString();

        Picasso.with(this).load(url).
                placeholder(R.drawable.dummypropic).
                error(R.drawable.dummypropic).
                fit().centerCrop().
                into(photo);

        dialog.show();
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            Intent intent = new Intent(StudentSearchActivity.this, HomeActivity.class);
//            startActivity(intent);
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentSearchActivity.this, HomeActivity.class);
        startActivity(intent);

    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle nav_cmgfs view item clicks here.
//
//        Boolean checkMenuItem = true;
//        MenuItem item1 = menu.findItem(R.id.nav_complaint_mess);
//        MenuItem item2 = menu.findItem(R.id.nav_complaint_hostel);
//        MenuItem item3 = menu.findItem(R.id.nav_complaint_general);
//
//        int id = item.getItemId();
//        Intent intent = new Intent();
//        boolean flag = false;
//        final Context context = StudentSearchActivity.this;
//
//        if (id == R.id.nav_home) {
//            intent = new Intent(context, HomeActivity.class);
//            flag = true;
//
//        } else if (id == R.id.nav_search) {
//            return true;
//        } else if (id == R.id.nav_mess_menu) {
//            intent = new Intent(context, MessMenuActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_complaint_box) {
//            if (!item2.isVisible()) {
//                item1.setVisible(false);
//                item2.setVisible(true);
//                item3.setVisible(true);
//                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_down_black_24dp));
//                checkMenuItem = false;
//            } else {
//                item1.setVisible(false);
//                item2.setVisible(false);
//                item3.setVisible(false);
//                checkMenuItem = false;
//                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_forum_black_24dp));
//            }
//            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_search)).setChecked(true);
//
//
//        } else if (id == R.id.nav_complaint_hostel) {
//            intent = new Intent(context, HostelComplaintsActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_complaint_general) {
//            intent = new Intent(context, GeneralComplaintsActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_complaint_mess) {
//            intent = new Intent(context, MessAndFacilitiesActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_calendar) {
//            intent = new Intent(context, CalendarActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_timetable) {
//            intent = new Intent(context, TimetableActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_contacts) {
//            intent = new Intent(context, ImpContactsActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_about) {
//            intent = new Intent(context, AboutUsActivity.class);
//            flag = true;
//        } else if (id == R.id.nav_profile) {
//            intent = new Intent(context, ProfileActivity.class);
//            flag = true;
//
//        } else if (id == R.id.nav_log_out) {
//            drawer.closeDrawer(GravityCompat.START);
//            Handler handler = new Handler();
//            handler.postDelayed(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            LogOutAlertClass lg = new LogOutAlertClass();
//                            lg.isSure(StudentSearchActivity.this);
//                        }
//                    }
//                    , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
//            );
//            return true;
//        }
//
//        if (checkMenuItem) {
//            item1.setVisible(false);
//            item2.setVisible(false);
//            item3.setVisible(false);
//
//            drawer.closeDrawer(GravityCompat.START);
//
//            //Wait till the nav drawer is closed and then start new activity (for smooth animations)
//            Handler mHandler = new Handler();
//            final boolean finalFlag = flag;
//            final Intent finalIntent = intent;
//            mHandler.postDelayed(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            if (finalFlag) {
//                                context.startActivity(finalIntent);
//                            }
//                        }
//                    }
//                    , getResources().getInteger(R.integer.close_nav_drawer_delay)  // it takes around 200 ms for drawer to close
//            );
//        }
//        return true;
//
//    }

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
            Intent intent = new Intent(StudentSearchActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(StudentSearchActivity.this);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
}
