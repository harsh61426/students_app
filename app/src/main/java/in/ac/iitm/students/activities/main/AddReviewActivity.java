package in.ac.iitm.students.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by dell on 03-03-2018.
 */

public class AddReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        //Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();


        final TextView tvSubtitle = (TextView) findViewById(R.id.tv_course_title);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_number);
        final TextInputLayout tilGrading = (TextInputLayout) findViewById(R.id.til_grading);
        final TextInputLayout tilXp = (TextInputLayout) findViewById(R.id.til_xp);
        final EditText etGrading = (EditText) findViewById(R.id.et_grading);
        final EditText etXp = (EditText) findViewById(R.id.et_xp);
        final RatingBar rb=(RatingBar) findViewById(R.id.rb_ar);
        Button button = (Button) findViewById(R.id.b_submit);

        //Set title and course code
        String title = intent.getStringExtra(Acads.EXTRA_MESSAGE1);
        tvSubtitle.setText(title);

        title = intent.getStringExtra(Acads.EXTRA_MESSAGE2);
        tvTitle.setText(title);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get input values
                String grading = etGrading.getText().toString().trim();
                String xp = etXp.getText().toString().trim();

                //Check for the validity of input values
                Boolean isValid = true;

                //Show errors
                if (grading.length() == 0) {
                    tilGrading.setError("Field cannot be empty");
                    isValid = false;
                } else {
                    if (tilGrading.isErrorEnabled()) {
                        tilGrading.setError(null);
                    }
                }
                if (xp.length() == 0) {
                    tilXp.setError("Field cannot be empty");
                    isValid = false;
                } else {
                    if (tilXp.isErrorEnabled()) {
                        tilXp.setError(null);
                    }
                }
                //If everything is proper then submit
                if (isValid) {

                    String url = "students.iitm.ac.in/course_feedback_api/create/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            InputStream stream;
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
                                            Intent intent = new Intent(getBaseContext(), Acads.class);
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


                           /* try {
                                Log.i("tagconvertstr", "["+response+"]");
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status.equals("1")) {
                                    //getActivity().finish();
                                    makeSnackbar("Complaint registered");
                                    Intent intent = new Intent(getContext(), HostelComplaintsActivity.class);
                                    startActivity(intent);
                                } else if (status.equals("0")) {
                                    makeSnackbar("Error registering complaint");
                                    //Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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

                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                            //params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, getActivity()));
                            //params.put("NAME", Utils.getprefString(UtilStrings.NAME, getActivity()));
                            params.put("roll", Utils.getprefString(UtilStrings.ROLLNO, getApplicationContext()));
                            //params.put("ROOM_NO", Utils.getprefString(UtilStrings.ROOM, getActivity()));

                            //params.put("TITLE", title);
                            //params.put("PROXIMITY", "");
                            //params.put("DESCRIPTION", description);
                            params.put("number", tvSubtitle.getText().toString());
                            params.put("prof", etGrading.getText().toString());
                            params.put("xp", etXp.getText().toString());
                            params.put("rating", String.valueOf(rb.getRating()));
//                            params.put("TAGS", tags);
//                            params.put("DATETIME", date);
//                            params.put("COMMENTS", "0");
//                            params.put("IMAGEURL", finalImageUrl);
//                            params.put("CUSTOM", "1");
                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplication()).addToRequestQueue(stringRequest);


                    Intent intent = new Intent(view.getContext(), Acads.class);
                    intent.putExtra("BOOLEAN_FROM_ADD_REVIEW_ACTIVITY", true);
                    startActivity(intent);
                }
                }
            }
        );}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_about: {
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.cl_review), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
