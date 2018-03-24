package in.ac.iitm.students.complaint_box.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.adapters.g_commentsAdapter;
import in.ac.iitm.students.complaint_box.adapters.h_CommentsAdapter;
import in.ac.iitm.students.complaint_box.objects.CommentObj;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.others.h_CmntDataParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class g_Comments extends AppCompatActivity {

    List<CommentObj> commentList = new ArrayList<>();
    private ListView listView;
    private g_commentsAdapter adapter;
    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/searchComments.php";
    //private String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/searchComments.php";
    private InputStream stream;
    private Complaint hComplaint;
    private TextView comment;
    private LinearLayout comment_layout;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        comment_layout = (LinearLayout) findViewById(R.id.ll_comment);

        listView = (ListView)findViewById(R.id.comment_list);

        final String add_url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/newComment.php";
        //final String add_url = "https://rockstarharshitha.000webhostapp.com/general_complaints/newComment.php";
        //final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        //final String NAME = Utils.getprefString(UtilStrings.NAME, this);

        Intent i = getIntent();
        hComplaint = (Complaint) i.getSerializableExtra("cardData");

        View header = getLayoutInflater().inflate(R.layout.g_item_complaint,null);

        TextView tv_name = (TextView) header.findViewById(R.id.tv_name);
        TextView tv_date =(TextView)header.findViewById(R.id.date);
        TextView tv_title = (TextView) header.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) header.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) header.findViewById(R.id.tv_description);
        TextView tv_upvote = (TextView) header.findViewById(R.id.tv_upvote);
        tv_upvote.setVisibility(View.GONE);
        TextView tv_downvote = (TextView) header.findViewById(R.id.tv_downvote);
        tv_downvote.setVisibility(View.GONE);
        TextView tv_comment = (TextView) header.findViewById(R.id.tv_comment);
        tv_comment.setVisibility(View.GONE);
        Button bn_upvote = (Button) header.findViewById(R.id.bn_upvote);
        bn_upvote.setVisibility(View.GONE);
        Button bn_downvote = (Button) header.findViewById(R.id.bn_downvote);
        bn_downvote.setVisibility(View.GONE);
        Button bn_comment = (Button) header.findViewById(R.id.bn_comment);
        bn_comment.setVisibility(View.GONE);
        ImageView iv_profile = (ImageView) header.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout = (LinearLayout) header.findViewById(R.id.ll_title);

        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + hComplaint.getRollNo().toUpperCase() + ".JPG";
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(iv_profile);

        tv_name.setText(hComplaint.getName());
        if (hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.acaf_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.resaf_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.sgs_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.cocas_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.has_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.culsec_lit_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.culsec_arts_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.iar_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.speaker_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.sports_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.mitr_roll)) ||
                hComplaint.getRollNo().equalsIgnoreCase(getString(R.string.cfi_roll))) {

            tv_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondaryDark));
        }
        else
        {
            tv_name.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
        }

        tv_title.setText(hComplaint.getTitle());
        tv_tags.setText(hComplaint.getTag());
        tv_description.setText(hComplaint.getDescription());
        tv_upvote.setText("" + hComplaint.getUpvotes());
        tv_downvote.setText("" + hComplaint.getDownvotes());
        tv_comment.setText("" + hComplaint.getComments());
        tv_date.setText(hComplaint.getDate());
        final String mUUID = hComplaint.getUid();

        listView.addHeaderView(header);

        //mRecyclerView.setHasFixedSize(true);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("Comment response",response);

                h_CmntDataParser hCmntDataParser = new h_CmntDataParser(response, getApplicationContext());
                ArrayList<CommentObj> commentArray = null;
                try {
                    commentArray = hCmntDataParser.pleaseParseMyData();
                } catch (IOException e) {
                    e.printStackTrace();
                    makeSnackbar("Error loading comments");
                    //Toast.makeText(g_Comments.this, "IOException", Toast.LENGTH_SHORT).show();
                }

                //mRecyclerView.setLayoutManager(mLayoutManager);
                adapter = new g_commentsAdapter(getApplicationContext(),commentArray,g_Comments.this);//new h_CommentsAdapter(commentArray,getApplicationContext());
                listView.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                // error.networkResponse.statusCode
                // error.networkResponse.data

                //put error msg
                makeSnackbar("Error loading comments");
                //Toast.makeText(g_Comments.this, "not able to load comments", Toast.LENGTH_SHORT).show();
            }
        }) {
            //to POST params
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //get hostel from prefs
                //put some dummy for now
                params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, g_Comments.this));
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

        View footer = getLayoutInflater().inflate(R.layout.g_comment_add,null);
        Button post = (Button) footer.findViewById(R.id.post);
        final TextInputLayout til_com=(TextInputLayout)footer.findViewById(R.id.til_comment);
        final EditText comm = til_com.getEditText();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmntDescStr = comm.getText().toString();
                if (cmntDescStr.equals("")) makeSnackbar("Empty field");
                else {
                    //write code here to send the comment description to the database, increase the number of comments in database by 1
                    final String mUUID = hComplaint.getUid();


                    //Log.d("buiz", "hello");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, add_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //Log.d("buiz", "hello from heere");

                            stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                            JsonReader reader = null;
                            try {
                                reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                                reader.setLenient(true);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            try {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    while (reader.hasNext()) {
                                        String name = reader.nextName();
                                        //Log.e("name", name);
                                        if (name.equals("status")) {
                                            if (reader.nextString().equals("1")) {
                                                comm.setHint("Enter comment here");
                                                hideKeyboard(g_Comments.this);
                                                CommentObj cmtObj = new CommentObj();
                                                cmtObj.setName(Utils.getprefString(UtilStrings.NAME, g_Comments.this));
                                                cmtObj.setRollNo(Utils.getprefString(UtilStrings.ROLLNO, g_Comments.this));
                                                cmtObj.setRoomNo(Utils.getprefString(UtilStrings.HOSTEl, g_Comments.this));
                                                cmtObj.setCommentStr(cmntDescStr);
                                                cmtObj.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                                adapter.addComment(cmtObj);

                                                int cmnts = Integer.parseInt(comment.getText().toString()) + 1;
                                                comment.setText(cmnts + "");
                                            } else {
                                                makeSnackbar("Error commenting");
                                                hideKeyboard(g_Comments.this);
                                            }
                                        } else if (name.equals("error")) {
                                            reader.nextString();
                                            makeSnackbar("Error commenting");
                                            hideKeyboard(g_Comments.this);

                                        } else {
                                            reader.skipValue();
                                        }
                                    }
                                    reader.endObject();
                                }
                                reader.endArray();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error commenting");
                                hideKeyboard(g_Comments.this);
                            } finally {

                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    makeSnackbar("Error commenting");
                                    hideKeyboard(g_Comments.this);
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(g_Comments.this, error.toString(), Toast.LENGTH_SHORT).show();
                            makeSnackbar("Error commenting");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, g_Comments.this);
                            String room = Utils.getprefString(UtilStrings.ROOM, g_Comments.this);
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                            params.put("HOSTEL", hostel_name);
                            params.put("NAME", Utils.getprefString(UtilStrings.NAME, g_Comments.this));
                            params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, g_Comments.this));
                            params.put("COMMENT", cmntDescStr);
                            params.put("UUID", mUUID);
                            params.put("DATE_TIME", date);
                            return params;
                        }
                    };
                    MySingleton.getInstance(g_Comments.this).addToRequestQueue(stringRequest);
                }
            }
        });

        listView.addFooterView(footer);


    }

    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(comment_layout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, GeneralComplaintsActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed(){

        //Intent intent = new Intent(this, GeneralComplaintsActivity.class);
        //startActivity(intent);
        super.onBackPressed();
    }

}
