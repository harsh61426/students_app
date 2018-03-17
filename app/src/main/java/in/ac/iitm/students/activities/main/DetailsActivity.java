package in.ac.iitm.students.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;

/**
 * Created by dell on 03-03-2018.
 */

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String stringCourseCode = intent.getStringExtra("NO");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("" + stringCourseCode);

        TextView name = (TextView) findViewById(R.id.tv_course_title);
        TextView prof = (TextView) findViewById(R.id.event_prof_details);
        TextView courseContent = (TextView) findViewById(R.id.event_content);

        //Select a position at random
        Random random = new Random();
        int randomPosition = random.nextInt(4);
        String[] contents = {"Random description of course - Sinusoidal steady state analysis, phasors, response to periodic inputs, power and energy.",
                "Random description of course - I-V relationship for ideal sources, R, C, L, M, controlled sources in time and Laplace/frequency domain, complex impedance and admittance.",
                "Random description of course - Determine transient and sinusoidal steady state operation of electric circuits.",
                "Random description of course - Apply Kirchhoff's laws to simple electric circuits and derive the basic circuit equations."
        };

        final String nameString = intent.getStringExtra("name");
        name.setText(nameString);
        prof.setText(intent.getStringExtra("prof"));
        courseContent.setText(contents[randomPosition]);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddReviewActivity.class);
                intent.putExtra(Acads.EXTRA_MESSAGE1, nameString);
                intent.putExtra(Acads.EXTRA_MESSAGE2, stringCourseCode);
                context.startActivity(intent);
            }
        });
    }


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
}
