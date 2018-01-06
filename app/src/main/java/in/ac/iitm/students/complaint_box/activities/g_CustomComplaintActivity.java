package in.ac.iitm.students.complaint_box.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class g_CustomComplaintActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private InputStream stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_custom_complaint);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/addComplaint.php";
        //final String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/addComplaint.php";
        final String hostel_url = "https://students.iitm.ac.in/studentsapp/studentlist/get_hostel.php";


        Button saveCustomCmplnt = (Button) findViewById(R.id.button_save);
        final EditText tv_title = (EditText) findViewById(R.id.editText_complaint_title);
        final EditText tv_description = (EditText) findViewById(R.id.editText_complaint_description);
        final EditText tv_tags = (EditText) findViewById(R.id.editText_tags);

        saveCustomCmplnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String title = tv_title.getText().toString();
            final String description = tv_description.getText().toString();
            final String tags = tv_tags.getText().toString();
            final String mUUID = UUID.randomUUID().toString();

                if (title.equals("") || description.equals("")) makeSnackbar("Empty field");
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                            JsonReader reader = null;
                            try {
                                reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                                reader.setLenient(true);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            try {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String name = reader.nextName();
                                    Log.e("name", name);
                                    if (name.equals("status")) {
                                        String status = reader.nextString();
                                        if (status.equals("1")) {
                                            makeSnackbar("Complaint registered");
                                            Intent intent = new Intent(g_CustomComplaintActivity.this, GeneralComplaintsActivity.class);
                                            startActivity(intent);
                                        } else if (status.equals("0")) {
                                            makeSnackbar("Error registering complaint");
                                        }
                                    } else if (name.equals("error")) {
                                        reader.nextString();
                                        makeSnackbar("Error registering complaint");
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error registering complaint");
                            } finally {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    makeSnackbar("Error registering complaint");
                                }
                            }

                            /*try {
                                Log.e("response", response);
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                Log.e("status", status);
                                if (status.equals("1")) {
                                    // finish();
                                    makeSnackbar("Complaint registered");
                                    Intent intent = new Intent(g_CustomComplaintActivity.this, GeneralComplaintsActivity.class);
                                    startActivity(intent);
                                } else if (status.equals("0")) {
                                    makeSnackbar("Error registering complaint");
                                    //Toast.makeText(g_CustomComplaintActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                makeSnackbar("Error registering complaint");
                            }*/


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            makeSnackbar("Error registering complaint");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, g_CustomComplaintActivity.this);
                            String room = Utils.getprefString(UtilStrings.ROOM, g_CustomComplaintActivity.this);
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                            params.put("HOSTEL", hostel_name);
                            params.put("NAME", Utils.getprefString(UtilStrings.NAME, g_CustomComplaintActivity.this));
                            params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, g_CustomComplaintActivity.this));
                            params.put("TITLE", title);
                            params.put("DESCRIPTION", description);
                            params.put("UPVOTES", "0");
                            params.put("DOWNVOTES", "0");
                            params.put("UUID", mUUID);
                            params.put("TAGS", tags);
                            params.put("DATETIME", date);
                            params.put("COMMENTS", "0");
                            return params;
                        }
                    };
                    MySingleton.getInstance(g_CustomComplaintActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
