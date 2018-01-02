package in.ac.iitm.students.complaint_box.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.adapters.h_CommentsAdapter;
import in.ac.iitm.students.complaint_box.fragments.g_LatestThreadFragment;
import in.ac.iitm.students.complaint_box.objects.CommentObj;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.others.h_CmntDataParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class g_Comments extends AppCompatActivity {

    List<CommentObj> commentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/searchComments.php";
    //private String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/searchComments.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_comments);
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        final String add_url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/newComment.php";
        //final String add_url = "https://rockstarharshitha.000webhostapp.com/general_complaints/newComment.php";
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        final String NAME = Utils.getprefString(UtilStrings.NAME, this);

        Intent i = getIntent();
        final Complaint hComplaint = (Complaint) i.getSerializableExtra("cardData");

        TextView name = (TextView) findViewById(R.id.comment_tv_name);
        TextView hostel = (TextView) findViewById(R.id.comment_tv_hostel);
        TextView resolved = (TextView) findViewById(R.id.comment_tv_is_resolved);
        TextView date =(TextView)findViewById(R.id.comment_date);
        TextView title = (TextView) findViewById(R.id.comment_tv_title);
        TextView description = (TextView) findViewById(R.id.comment_tv_description);
        final TextView upvote = (TextView) findViewById(R.id.comment_tv_upvote);
        final TextView downvote = (TextView) findViewById(R.id.comment_tv_downvote);
        TextView comment = (TextView) findViewById(R.id.comment_tv_comment);
        final EditText CmntDesc = (EditText) findViewById(R.id.editText);
        Button save = (Button) findViewById(R.id.bn_save);

        name.setText(hComplaint.getName());
        hostel.setText(Utils.getprefString(UtilStrings.HOSTEl, g_Comments.this));
        resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
        date.setText(hComplaint.getDate());
        title.setText(hComplaint.getTitle());
        description.setText(hComplaint.getDescription());
        upvote.setText("" + hComplaint.getUpvotes());
        downvote.setText("" + hComplaint.getDownvotes());
        comment.setText("" + hComplaint.getComments());
        final String mUUID = hComplaint.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_comments);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e("Comment response",response);

                h_CmntDataParser hCmntDataParser = new h_CmntDataParser(response, getApplicationContext());
                ArrayList<CommentObj> commentArray = null;
                try {
                    commentArray = hCmntDataParser.pleaseParseMyData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(g_Comments.this, "IOException", Toast.LENGTH_SHORT).show();
                }

                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new h_CommentsAdapter(commentArray,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // error.networkResponse.statusCode
                // error.networkResponse.data

                //put error msg
                Toast.makeText(g_Comments.this, "not able to load comments", Toast.LENGTH_SHORT).show();
            }
        }) {
            //to POST params
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //get hostel from prefs
                //put some dummy for now
                params.put("HOSTEL", "narmada");
                params.put("UUID", mUUID);
                return params;
            }

        };
        //volley singleton - ensures single request queue in an app
        MySingleton.getInstance(this).addToRequestQueue(request);


        //lite
        int MY_SOCKET_TIMEOUT_MS = 5000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmntDescStr = CmntDesc.getText().toString();
                //write code here to send the comment description to the database, increase the number of comments in database by 1
                final String mUUID = hComplaint.getUid();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, add_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(g_Comments.this, "sending comment...", Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("resp",response);
                            JSONObject jsObject = new JSONObject(response);

                            if (jsObject.has("error")) {
                                Toast.makeText(g_Comments.this, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                            } else if (jsObject.has("status")) {
                                String status = jsObject.getString("status");
                                Log.e("status",status);
                                if (status == "1") {
                                    Intent intent = new Intent(g_Comments.this,g_LatestThreadFragment.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(g_Comments.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(g_Comments.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, g_Comments.this);
                        String room = Utils.getprefString(UtilStrings.ROOM, g_Comments.this);
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        params.put("HOSTEL", hostel_name);
                        params.put("NAME", "Omkar Patil");
                        params.put("ROLL_NO", "me15b123");
                        params.put("ROOM_NO", room);
                        params.put("COMMENT", cmntDescStr);
                        params.put("UUID", mUUID);
                        params.put("DATE_TIME", date);
                        return params;
                    }
                };
                MySingleton.getInstance(g_Comments.this).addToRequestQueue(stringRequest);
            }
        });


    }

}
