package in.ac.iitm.students.organisations.activities.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.activities.SubscriptionActivity;
import in.ac.iitm.students.activities.main.CalendarActivity;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.activities.main.ImpContactsActivity;
import in.ac.iitm.students.activities.main.MapActivity;
import in.ac.iitm.students.activities.main.StudentSearchActivity;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.complaint_box.activities.main.ComplaintBoxActivity;
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
    public ImageView iv_org_logo;
    public RecyclerView rv_org_list;
    public OrganisationAdapter adapter;
    TextView tv_org_name;
    List<OrganisationObject> orgsList;
    String[] PagesList;
    OrganisationObject org = null;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle onRetainNonConfigurationChanges) {
        super.onCreate(onRetainNonConfigurationChanges);
        setContentView(R.layout.activity_organisations);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Organisations in IITM");

        //nav drawer code

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        final ProgressDialog pd = new ProgressDialog(OrganizationActivity.this);
        pd.setMessage("Loading Organisations");
        pd.show();

        String[] indexofyoutube = getResources().getStringArray(R.array.indexOfYoutubeOrg);


        final ArrayList<String> yt_username = new ArrayList<>();

        for (int j = 0; j < indexofyoutube.length; j++) {
            yt_username.add(indexofyoutube[j].substring(indexofyoutube[j].lastIndexOf("|") + 1));
        }

        String describe;

        for (int i = 0; i < PagesList.length; i++) {
            final int finalI = i;
            final int finalI1 = i;
            if(PagesList[i].matches("[0-9]+") && PagesList[i].length()>2){
                describe = "description";
                Log.i("XXSWFW",describe);
            }
            else{
                describe = "about";
            }

            final String finalDescribe = describe;
            final GraphRequest request = new GraphRequest(
                    key,
                    PagesList[i] + "?fields=picture.type(large),name,"+describe,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                final JSONObject jsonresponse = new JSONObject(String.valueOf(response.getJSONObject()));
                                String pic_url = jsonresponse.getJSONObject("picture").getJSONObject("data").getString("url");
                                String name = jsonresponse.getString("name");
                                String about;
                                if(finalDescribe.equalsIgnoreCase("description")){
                                     about = jsonresponse.getString("description");
                                }
                                else{
                                about = jsonresponse.getString("about");
                                }

                                String id = jsonresponse.getString("id");
                                org = new OrganisationObject(pic_url, name, id, about);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                pd.dismiss();
                                switch (finalI) {
                                    case 0:
                                        channelIDrequest(yt_username.get(0), org);
                                        break;
                                    case 4:
                                        channelIDrequest(yt_username.get(1), org);
                                        break;
                                    case 5:
                                        channelIDrequest(yt_username.get(2), org);
                                        break;
                                    case 10:
                                        channelIDrequest(yt_username.get(3), org);
                                        break;
                                    case 11:
                                        channelIDrequest(yt_username.get(4), org);
                                        break;
                                    case 15:
                                        channelIDrequest(yt_username.get(5), org);
                                        break;
                                    default:
                                        org.isYoutube = false;
                                        org.channelID = null;
                                        orgsList.add(org);
                                        adapter = new OrganisationAdapter(OrganizationActivity.this, orgsList);
                                        rv_org_list.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
            request.executeAsync();
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
                            orgsList.add(org);
                            adapter = new OrganisationAdapter(OrganizationActivity.this, orgsList);
                            rv_org_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            orgsList.add(org);
                            adapter = new OrganisationAdapter(OrganizationActivity.this, orgsList);
                            rv_org_list.setAdapter(adapter);
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(OrganizationActivity.this, HomeActivity.class);
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
                            lg.isSure(OrganizationActivity.this);
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
