package in.ac.iitm.students.complaint_box.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class g_AddYourComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_add_your_comment);

        final String url = "https://students.iitm.ac.in/studentsapp/);complaints_portal/gen_complaints/newComment.php";
        //final String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/newComment.php";
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        final String NAME = Utils.getprefString(UtilStrings.NAME, this);

        Intent i = getIntent();
        final Complaint hComplaint = (Complaint) i.getSerializableExtra("cardData");

        TextView name = (TextView) findViewById(R.id.comment_tv_name);
        TextView hostel = (TextView) findViewById(R.id.comment_tv_hostel);
        TextView resolved = (TextView) findViewById(R.id.comment_tv_is_resolved);
        TextView title = (TextView) findViewById(R.id.comment_tv_title);
        TextView description = (TextView) findViewById(R.id.comment_tv_description);
        final TextView upvote = (TextView) findViewById(R.id.comment_tv_upvote);
        final TextView downvote = (TextView) findViewById(R.id.comment_tv_downvote);
        TextView comment = (TextView) findViewById(R.id.comment_tv_comment);
        final EditText CmntDesc = (EditText) findViewById(R.id.editText);
        Button save = (Button) findViewById(R.id.bn_save);

        name.setText(hComplaint.getName());
        //todo change narmad
        hostel.setText(Utils.getprefString(UtilStrings.HOSTEl, g_AddYourComment.this));
        resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
        title.setText(hComplaint.getTitle());
        description.setText(hComplaint.getDescription());
        upvote.setText("" + hComplaint.getUpvotes());
        downvote.setText("" + hComplaint.getDownvotes());
        comment.setText("" + hComplaint.getComments());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmntDescStr = CmntDesc.getText().toString();
                //write code here to send the comment description to the database, increase the number of comments in database by 1
                final String mUUID = hComplaint.getUid();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(g_AddYourComment.this, "sending comment...", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsObject = new JSONObject(response);

                            if (jsObject.has("error")) {
                                Toast.makeText(g_AddYourComment.this, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                            } else if (jsObject.has("status")) {
                                String status = jsObject.getString("status");
                                if (status == "1") {
                                    Intent intent = new Intent(g_AddYourComment.this, g_Comments.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(g_AddYourComment.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(g_AddYourComment.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, g_AddYourComment.this);
                        String room = Utils.getprefString(UtilStrings.ROOM, g_AddYourComment.this);
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
                MySingleton.getInstance(g_AddYourComment.this).addToRequestQueue(stringRequest);
            }
        });
    }
}
