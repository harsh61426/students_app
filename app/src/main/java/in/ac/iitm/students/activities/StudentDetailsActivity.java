package in.ac.iitm.students.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        Log.v("CheckMate", getIntent().getStringExtra("email"));
        if(getIntent().getStringExtra("email").equalsIgnoreCase("null")){
            email.setText("Not available");
        }else{
            email.setText(getIntent().getStringExtra("email"));
        }

        if(getIntent().getStringExtra("phone").equalsIgnoreCase("null")){
            phoneno.setText("Not available");
        }else{
            phoneno.setText(getIntent().getStringExtra("phone"));
        }

        if(getIntent().getStringExtra("about").equalsIgnoreCase("null")){
            abtyourself.setText("Not available");
        }else{
            abtyourself.setText(getIntent().getStringExtra("about"));
        }

        int check = getIntent().getIntExtra("reveal_photo", 1);

        if(check == 1) {
            Uri.Builder builder = new Uri.Builder();

            builder.scheme("https")
                    .authority("photos.iitm.ac.in")
                    .appendPath("byroll.php")
                    .appendQueryParameter("roll", rollno.getText().toString());
            String url = builder.build().toString();
            Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.dummypropic).error(R.drawable.dummypropic).fit().centerCrop().into(profilePic);
        }

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

}