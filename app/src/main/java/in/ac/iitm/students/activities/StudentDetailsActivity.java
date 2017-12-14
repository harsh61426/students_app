package in.ac.iitm.students.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;

public class StudentDetailsActivity extends AppCompatActivity {

    CircleImageView profilePic;
    TextView name, rollno, hostel, email, phoneno, abtyourself, room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        profilePic = (CircleImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name_overview);
        rollno = (TextView) findViewById(R.id.rollno_overview);
        hostel = (TextView) findViewById(R.id.hostel_overview);
        room = (TextView) findViewById(R.id.room_overview);
        email = (TextView) findViewById(R.id.email_info);
        phoneno = (TextView) findViewById(R.id.phone_info);
        abtyourself = (TextView) findViewById(R.id.aboutyourself);

        name.setText(getIntent().getStringExtra("studName"));
        rollno.setText(getIntent().getStringExtra("studRoll"));
        room.setText(getIntent().getStringExtra("roomNo"));
        hostel.setText(getIntent().getStringExtra("hostel"));


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

//    void sendJsonRequest() {
//
//
//        String urlForData = "";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForData, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                InputStream stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
//
//                JsonReader reader = null;
//                try {
//                    reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
//                    reader.setLenient(true);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    try {
//                        reader.beginObject();
//                        String Name = reader.nextName();
//                        int reveal_photo = reader.nextInt();
//                        int reveal_place = reader.nextInt();
//                        String email = reader.nextString();
//                        String Phone = reader.nextString();
//                        String about = reader.nextString();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } finally {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VolleyError", error.toString());
//                //Toast.makeText(context,"No Internet Access",Toast.LENGTH_SHORT).show();
//            }
//        });
//        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
//
//    }


}