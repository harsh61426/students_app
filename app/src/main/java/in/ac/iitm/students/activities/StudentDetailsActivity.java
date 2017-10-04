package in.ac.iitm.students.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.StudentSearchAccomAdapter;
import in.ac.iitm.students.others.StudentSearchAccomArray;

public class StudentDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public StudentSearchAccomAdapter accomAdapter;

    ImageButton profilePic;
    TextView name,rollno,hostel,email,phoneno,linkedin,abtyourself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        profilePic = (ImageButton) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name_overview);
        rollno = (TextView) findViewById(R.id.rollno_overview);
        hostel = (TextView) findViewById(R.id.hostel_overview);
        email = (TextView) findViewById(R.id.email_info);
        phoneno = (TextView) findViewById(R.id.phone_info);
        linkedin = (TextView) findViewById(R.id.linkedin_info);
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

}