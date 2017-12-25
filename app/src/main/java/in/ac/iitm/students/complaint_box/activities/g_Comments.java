package in.ac.iitm.students.complaint_box.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.adapters.h_CommentsAdapter;
import in.ac.iitm.students.complaint_box.objects.h_CommentObj;
import in.ac.iitm.students.complaint_box.objects.h_Complaint;
import in.ac.iitm.students.complaint_box.others.h_CmntDataParser;
import in.ac.iitm.students.others.MySingleton;

public class g_Comments extends AppCompatActivity {

    List<h_CommentObj> commentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/general_complaints/searchComment.php";
    private String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/searchComments.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g__comments);
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Intent i = getIntent();
        final h_Complaint hComplaint = (h_Complaint) i.getSerializableExtra("cardData");

        TextView name = (TextView) findViewById(R.id.comment_tv_name);
        TextView hostel = (TextView) findViewById(R.id.comment_tv_hostel);
        TextView resolved = (TextView) findViewById(R.id.comment_tv_is_resolved);
        TextView title = (TextView) findViewById(R.id.comment_tv_title);
        TextView description = (TextView) findViewById(R.id.comment_tv_description);
        final TextView upvote = (TextView) findViewById(R.id.comment_tv_upvote);
        final TextView downvote = (TextView) findViewById(R.id.comment_tv_downvote);
        TextView comment = (TextView) findViewById(R.id.comment_tv_comment);
        FloatingActionButton fab_comment = (FloatingActionButton) findViewById(R.id.comment_fab);

        name.setText(hComplaint.getName());
        hostel.setText(sharedPref.getString("hostel", "narmada"));
        resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
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
                Log.e("Comment response",response);

                h_CmntDataParser hCmntDataParser = new h_CmntDataParser(response, getApplicationContext());
                ArrayList<h_CommentObj> commentArray = null;
                try {
                    commentArray = hCmntDataParser.pleaseParseMyData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(g_Comments.this, "IOException", Toast.LENGTH_SHORT).show();
                }

                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new h_CommentsAdapter(commentArray);
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

        if (!hComplaint.isResolved()) {
            fab_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(g_Comments.this, g_AddYourComment.class);
                    intent.putExtra("cardData", hComplaint);
                    startActivity(intent);
                }
            });
        } else {
            fab_comment.setVisibility(View.GONE);
        }


        //lite
        int MY_SOCKET_TIMEOUT_MS = 5000;
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
