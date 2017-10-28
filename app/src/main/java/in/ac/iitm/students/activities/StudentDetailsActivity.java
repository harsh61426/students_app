package in.ac.iitm.students.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
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

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.StudentSearchAccomAdapter;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.StudentSearchAccomArray;

public class StudentDetailsActivity extends AppCompatActivity {

    public StudentSearchAccomAdapter accomAdapter;
    RecyclerView recyclerView;
    CircleImageView profilePic;
    TextView name, rollno, hostel, email, phoneno, abtyourself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name_overview);
        rollno = (TextView) findViewById(R.id.rollno_overview);
        hostel = (TextView) findViewById(R.id.hostel_overview);
        email = (TextView) findViewById(R.id.email_info);
        phoneno = (TextView) findViewById(R.id.phone_info);
        abtyourself = (TextView) findViewById(R.id.aboutyourself);

        recyclerView = (RecyclerView) findViewById(R.id.accom_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        accomAdapter = new StudentSearchAccomAdapter(StudentDetailsActivity.this, StudentSearchAccomArray.getAccomData());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(accomAdapter);
    }


    //Setting up back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void sendJsonRequest() {


        String urlForData = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForData, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));

                JsonReader reader = null;
                try {
                    reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                    reader.setLenient(true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        reader.beginObject();
                        String Name = reader.nextName();
                        int reveal_photo = reader.nextInt();
                        int reveal_place = reader.nextInt();
                        String email = reader.nextString();
                        String Phone = reader.nextString();
                        String about = reader.nextString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                //Toast.makeText(context,"No Internet Access",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}