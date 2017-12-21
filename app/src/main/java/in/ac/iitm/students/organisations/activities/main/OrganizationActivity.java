package in.ac.iitm.students.organisations.activities.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.MessAndFacilitiesActivity;
import in.ac.iitm.students.organisations.activities.DeveloperKey;
import in.ac.iitm.students.organisations.adapters.OrganisationAdapter;
import in.ac.iitm.students.organisations.object_items.OrganisationObject;
import in.ac.iitm.students.others.LogOutAlertClass;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class OrganizationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static AccessToken key;
    final String yt_url = "https://www.googleapis.com/youtube/v3/channels?part=id&forUsername=";
    final String yt_url2 = "&key=";
    final ArrayList<String> yt_username = new ArrayList<>();
    public ImageView iv_org_logo;
    public RecyclerView rv_org_list;
    public OrganisationAdapter adapter;
    TextView tv_org_name;
    List<OrganisationObject> orgsList;
    String[] PagesList;
    OrganisationObject org = null;
    ProgressDialog pd;
    String describe;
    String url_1 = "https://graph.facebook.com/v2.10/";
    String url_2 = "?fields=picture.type(large),name,";
    private DrawerLayout drawer;
    private Menu menu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle onRetainNonConfigurationChanges) {
        super.onCreate(onRetainNonConfigurationChanges);
        setContentView(R.layout.activity_organisations);
        FacebookSdk.sdkInitialize(this.getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Organisations in IITM");
        }
        //nav drawer code

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_organisations)).setChecked(true);
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

        //nav drawer code ends

        String apptoken = getString(R.string.Apptoken);
        String appid = getString(R.string.facebook_app_id);

        rv_org_list = (RecyclerView) findViewById(R.id.rv_org_list);
        rv_org_list.setItemAnimator(new DefaultItemAnimator());
        rv_org_list.setLayoutManager(new LinearLayoutManager(this));

        key = new AccessToken(apptoken, appid, getString(R.string.userid), null, null, AccessTokenSource.FACEBOOK_APPLICATION_NATIVE, null, null);

        tv_org_name = (TextView) findViewById(R.id.tv_post_des);
        iv_org_logo = (ImageView) findViewById(R.id.iv_content);

        orgsList = new ArrayList<OrganisationObject>();

        PagesList = getResources().getStringArray(R.array.Listofpagenames);

        pd = new ProgressDialog(OrganizationActivity.this);
        pd.setMessage("Loading Organisations");
        pd.show();

        String[] indexofyoutube = getResources().getStringArray(R.array.indexOfYoutubeOrg);


        for (int j = 0; j < indexofyoutube.length; j++) {
            yt_username.add(indexofyoutube[j].substring(indexofyoutube[j].lastIndexOf("|") + 1));
        }


        adapter = new OrganisationAdapter(OrganizationActivity.this, orgsList);
        rv_org_list.setAdapter(adapter);

        for(final int[] i = {0}; i[0] < PagesList.length; i[0]++) {
            final int finalI = i[0];
            if (PagesList[i[0]].matches("[0-9]+") && PagesList[i[0]].length() > 2) {
                describe = "description";
            } else {
                describe = "about";
            }
            final String finalDescribe = describe;

            JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                    (Request.Method.GET,url_1+PagesList[i[0]]+url_2+describe+"&access_token="+apptoken, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            org = new OrganisationObject();

                            try {
                                final JSONObject jsonresponse = response;
                                String pic_url = jsonresponse.getJSONObject("picture").getJSONObject("data").getString("url");
                                String name = jsonresponse.getString("name");
                                String about;
                                if (finalDescribe.equalsIgnoreCase("description")) {
                                    about = jsonresponse.getString("description");
                                } else {
                                    about = jsonresponse.getString("about");
                                }
                                String id = jsonresponse.getString("id");
                                org.pageid = id;
                                org.logo_url = pic_url;
                                org.org_name = name;
                                org.org_about = about;
                                org.index = finalI;

                                pd.dismiss();
                                switch (finalI) {
                                    case 0:
                                        channelIDrequest(yt_username.get(0), org);
                                        break;
                                    case 3:
                                        channelIDrequest(yt_username.get(1), org);
                                        break;
                                    case 1:
                                        channelIDrequest(yt_username.get(2), org);
                                        break;
                                    case 2:
                                        channelIDrequest(yt_username.get(3), org);
                                        break;
                                    case 5:
                                        channelIDrequest(yt_username.get(4), org);
                                        break;
                                    case 4:
                                        channelIDrequest(yt_username.get(5), org);
                                        break;
                                    default:
                                        org.isYoutube = false;
                                        org.channelID = null;
                                        Collections.sort(orgsList, new Comparator<OrganisationObject>() {
                                            @Override
                                            public int compare(OrganisationObject o1, OrganisationObject o2) {
                                                return o1.index-o2.index;
                                            }
                                        });
                                        orgsList.add(org);
                                        adapter.notifyDataSetChanged();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

        // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(OrganizationActivity.this).addToRequestQueue(jsObjRequest1);

        }
    }

    private void channelIDrequest(final String username, final OrganisationObject org) {
        final JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                (Request.Method.GET, yt_url + username + yt_url2 + DeveloperKey.DEVELOPER_KEY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonresponse = new JSONObject(response.toString());
                            JSONArray itemsjson = jsonresponse.getJSONArray("items");
                            JSONObject jsonitem = itemsjson.getJSONObject(0);
                            if (username.equalsIgnoreCase("mediaclubiitm")) {
                                org.channelID = getString(R.string.mediaclubchannelid);
                            } else {
                                org.channelID = jsonitem.getString("id");
                            }
                            org.isYoutube = true;
                            Collections.sort(orgsList, new Comparator<OrganisationObject>() {
                                @Override
                                public int compare(OrganisationObject o1, OrganisationObject o2) {
                                    return o1.index-o2.index;
                                }
                            });
                            orgsList.add(org);
                            ///adapter.notifyItemInserted(finalI3);
                            //Log.i("DDSXX"+String.valueOf(i),orgsList.get(i).org_name);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //orgsList.add(org);
                           // adapter.notifyItemInserted(finalI3);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        MySingleton.getInstance(OrganizationActivity
                .this).addToRequestQueue(jsObjRequest1);

    }




    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer!=null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Intent intent = new Intent(OrganizationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
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
            Intent intent = new Intent(OrganizationActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            LogOutAlertClass lg = new LogOutAlertClass();
            lg.isSure(OrganizationActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        final Context context = OrganizationActivity.this;

        if (id == R.id.nav_home) {
            intent = new Intent(context, HomeActivity.class);
            flag = true;

        } else if (id == R.id.nav_organisations) {
            //intent = new Intent(context, OrganizationActivity.class);
            //flag = true;
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
            navigationView.getMenu().getItem(getResources().getInteger(R.integer.nav_index_organisations)).setChecked(true);


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
                            lg.isSure(OrganizationActivity.this);
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
}
