package in.ac.iitm.students.complaint_box.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import br.com.mauker.materialsearchview.MaterialSearchView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.activities.main.ImpContactsActivity;
import in.ac.iitm.students.activities.main.MapActivity;
import in.ac.iitm.students.activities.main.StudentSearchActivity;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.complaint_box.activities.g_CustomComplaintActivity;
import in.ac.iitm.students.complaint_box.adapters.g_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.fragments.g_LatestThreadFragment;
import in.ac.iitm.students.complaint_box.fragments.g_MyComplaintFragment;
import in.ac.iitm.students.organisations.activities.main.OrganizationActivity;
import in.ac.iitm.students.organisations.adapters.OrgPagerAdapter;
import in.ac.iitm.students.organisations.fragments.Fbfragment;
import in.ac.iitm.students.organisations.fragments.YoutubeFragment;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class GeneralComplaintsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    public String mGeneralString;
    public static ViewPagerAdapter adapter;
    MaterialSearchView searchView;
    private MenuItem searchIcon;
    String[] suggestions;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu menu;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UUID> uuidArrayList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    public g_LatestThreadFragment g_latestThreadFragment;
    public g_MyComplaintFragment g_myComplaintFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_general_complaints);
        Log.d("crai", "acti");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.title_activity_complaint_general);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        searchView =(MaterialSearchView)findViewById(R.id.search_view);

        searchViewCode();
        //mRecyclerView = (RecyclerView) findViewById(R.id.latest_thread_recycler);
        //mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(this);


        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_general_complaints);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_complaint_box)).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView username = (TextView) header.findViewById(R.id.tv_username);
        TextView rollNumber = (TextView) header.findViewById(R.id.tv_roll_number);



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


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(this);
    }


    private void searchViewCode(){

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchQuery(s);
                return false;
            }
        });

        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);
                Log.d("SUGGXX",suggestion);
                searchView.setQuery(suggestion, true);
            }
        });
    }


    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void SearchQuery(String s){
        final String query = s;

        //to get suggestion
        String url ="https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/search.php";

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("zoop", "response: " + response);
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.has("error")) {
//                        makeSnackbar("Error Searching");
                        Log.d("zoop1", "error:" + jsonObject.getString("error"));
                    } else if (jsonObject.has("status")) {
                        String status = jsonObject.getString("status");

                        if (Objects.equals(status, "1")) {
                            JSONArray jsonArray =jsonObject.getJSONArray("searchResult");
                            Log.d("XXXX","entered: "+jsonArray);

                            JSONArray array =jsonObject.getJSONArray("suggestions");
                            suggestions = new String[array.length()];
                            //suggestions=getResources().getStringArray(R.array.query_suggestions);
                            //List<String> list = new ArrayList<String>();
                            //list = Arrays.asList(suggestions);
                            //ArrayList<String> arrayList = new ArrayList<String>(list);
                            for (int i=0;i <array.length(); i++) {
                                JSONObject tag = array.getJSONObject(i);
                                suggestions[i] = tag.getString("tags");
                                //arrayList.add(tag.getString("tags"));
                                Log.d("zoop", "sugg: " + suggestions[i]);
                            }
                            //suggestions = arrayList.toArray(new String[list.size()]);
                            if(suggestions.length>0)
                                searchView.addSuggestions(suggestions);
                            else
                                searchView.addSuggestion("Not Found");

                            if(jsonArray.length()>0)
                                adapter.update(jsonArray.toString());
                            else
                                makeSnackbar("Not found");


                            //searchView.showSuggestions();

                            /*if(jsonArray!=null) {
                                Log.e("array",jsonArray.toString());
                                Bundle bundle = new Bundle();
                                bundle.putString("tagSearch", jsonArray.toString());
                                g_LatestThreadFragment fragment = new g_LatestThreadFragment();
                                fragment.setArguments(bundle);
                            }

                            /*h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(jsonArray.toString(), GeneralComplaintsActivity.this);
                            ArrayList<Complaint> hComplaintArray = null;
                            try {
                                hComplaintArray = hJsonComplaintParser.pleasePleaseParseMyData();
                                Log.e("ComplaintArray",hComplaintArray.toString());

                                if(hComplaintArray!=null){
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mAdapter = new g_ComplaintAdapter(hComplaintArray, GeneralComplaintsActivity.this, getApplicationContext());
                                    mRecyclerView.setAdapter(mAdapter);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(GeneralComplaintsActivity.this, "IOException", Toast.LENGTH_SHORT).show();
                            }*/
                        } else {
                            makeSnackbar("Error Searching");
                            //Toast.makeText(GeneralComplaintsActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    /*

                    for (i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        suggestions[i] = (jsonObject.getString("tags"));
                    }*/

                } catch (JSONException e) {
                    makeSnackbar("Error Searching");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                makeSnackbar("Error Searching");
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("String", query);
                return params;
            }
        };

        MySingleton.getInstance(GeneralComplaintsActivity.this).addToRequestQueue(jsonObjReq);
    }

    private void setupViewPager(ViewPager viewPager) {

        g_latestThreadFragment = new g_LatestThreadFragment();
        g_myComplaintFragment = new g_MyComplaintFragment();

        adapter = new GeneralComplaintsActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(g_latestThreadFragment, "Latest Thread");
        adapter.addFragment(g_myComplaintFragment, "My complaints");
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onBackPressed() {

        if(searchView.isOpen()){
            searchView.closeSearch();
        }else{
            Intent intent = new Intent(GeneralComplaintsActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            fab.show();
            if(searchIcon!=null)
                searchIcon.setVisible(true);
            searchView.setVisibility(View.VISIBLE);
        }
        else {
            fab.hide();
            if(searchIcon!=null)
                searchIcon.setVisible(false);
            searchView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            if (g_ComplaintAdapter.DATA_CHANGED == 1) {
                adapter.notifyDataSetChanged();
                g_ComplaintAdapter.DATA_CHANGED = 0;
            }
            if(searchIcon!=null)
                searchIcon.setVisible(true);
            searchView.setVisibility(View.VISIBLE);
            fab.show();
        } else {
            if (g_ComplaintAdapter.DATA_CHANGED == 1) {
                Log.d("jane", "call in else");
                adapter.notifyDataSetChanged();
                g_ComplaintAdapter.DATA_CHANGED = 0;
            }
            if(searchIcon!=null)
                searchIcon.setVisible(false);
            fab.hide();
            searchView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GeneralComplaintsActivity.this, g_CustomComplaintActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main_menu, menu);
       getMenuInflater().inflate(R.menu.search_item,menu);
       searchIcon = menu.findItem(R.id.action_search);
       searchIcon.setVisible(true);

       //MenuItem item=menu.findItem(R.id.action_search);
       //searchView.setMenuItem(item);
        //SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //text has changed,apply filtering
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //perform the final search
                return false;
            }
        });*/

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
            Intent intent = new Intent(GeneralComplaintsActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(GeneralComplaintsActivity.this);
            return true;
        } else if (id == R.id.home) {
            onBackPressed();
            return true;
        }else if(id==R.id.action_search){
            searchView.openSearch();
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

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
        final Context context = GeneralComplaintsActivity.this;

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
            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_complaint_general)).setChecked(true);


        } else if (id == R.id.nav_complaint_hostel) {
            intent = new Intent(context, HostelComplaintsActivity.class);
            flag = true;
        } else if (id == R.id.nav_complaint_general) {
            //intent = new Intent(context, GeneralComplaintsActivity.class);
           // flag = true;
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
                            lg.isSure(GeneralComplaintsActivity.this);
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

    public class ViewPagerAdapter extends FragmentPagerAdapter {
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

        public void update(String string) {
            mGeneralString = string;
            //updated
            notifyDataSetChanged();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//
//            if (mFragmentList != null) {
//
//                if(mGeneralString!=null && mGeneralString.length()>0){
//                    g_LatestThreadFragment g_latestThreadFragment1 = (g_LatestThreadFragment) mFragmentList.get(position);
//                    Log.d("XGENRES",mGeneralString);
//                    g_latestThreadFragment1.update(mGeneralString);
//                    Log.i("ob1", "********instantiateItem position:" + position + " FragmentDataChanged");
//                }
//            } else {
//                //No fragment instance available for this index, create a new fragment by calling getItem() and show the data.
//                Log.i("ob2", "********instantiateItem position:" + position + " NewFragmentCreated");
//            }
//
//            return super.instantiateItem(container, position);
//        }

        /*
        @Override
        public int getItemPosition(Object object) {
            if (object instanceof Updateable) {
                //sent to FirstFragment and SecondFragment
                ((Updateable) object).update(mGeneralString);
            }
            return super.getItemPosition(object);
        }
        */

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

//        @Override
//        public void finishUpdate(ViewGroup container) {
//            try {
//                super.finishUpdate(container);
//            } catch (NullPointerException nullPointerException) {
//                System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
//            }
//        }
    }


}
