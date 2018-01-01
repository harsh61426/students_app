package in.ac.iitm.students.complaint_box.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class g_CustomComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_custom_complaint);

        final String url = "https://students.iitm.ac.in/studentsapp/);complaints_portal/gen_complaints/addComplaint.php";
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



            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("response",response);
                        JSONObject jsObject = new JSONObject(response);
                        String status = jsObject.getString("status");
                        Log.e("status",status);
                        if (status.equals("1")) {
                            // finish();
                            Intent intent = new Intent(g_CustomComplaintActivity.this, GeneralComplaintsActivity.class);
                            startActivity(intent);
                        } else if (status.equals("0")) {
                            Toast.makeText(g_CustomComplaintActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, g_CustomComplaintActivity.this);
                    String room = Utils.getprefString(UtilStrings.ROOM, g_CustomComplaintActivity.this);
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String moreRooms = room + ",";

                    params.put("HOSTEL", hostel_name);
                    //TODO get name from prefs
                    params.put("NAME", "Omkar Patil");
                    //TODO get rollno from prefs
                    params.put("ROLL_NO", "me15b123");
                    params.put("ROOM_NO", room);
                    params.put("TITLE", title);
                    params.put("PROXIMITY", "");
                    params.put("DESCRIPTION", description);
                    params.put("UPVOTES", "0");
                    params.put("DOWNVOTES", "0");
                    params.put("RESOLVED", "0");
                    params.put("UUID", mUUID);
                    params.put("TAGS", tags);
                    params.put("DATETIME", date);
                    params.put("COMMENTS", "0");
                    params.put("MORE_ROOMS", moreRooms);
                    return params;
                }
            };
            MySingleton.getInstance(g_CustomComplaintActivity.this).addToRequestQueue(stringRequest);
        }
    });


}
}
