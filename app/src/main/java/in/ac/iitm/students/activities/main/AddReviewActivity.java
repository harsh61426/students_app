package in.ac.iitm.students.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;

/**
 * Created by dell on 03-03-2018.
 */

public class AddReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        //Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        TextView tvSubtitle = (TextView) findViewById(R.id.tv_course_title);
        TextView tvTitle = (TextView) findViewById(R.id.tv_number);
        final TextInputLayout tilGrading = (TextInputLayout) findViewById(R.id.til_grading);
        final TextInputLayout tilXp = (TextInputLayout) findViewById(R.id.til_xp);
        final EditText etGrading = (EditText) findViewById(R.id.et_grading);
        final EditText etXp = (EditText) findViewById(R.id.et_xp);
        Button button = (Button) findViewById(R.id.b_submit);

        //Set title and course code
        String title = intent.getStringExtra(Acads.EXTRA_MESSAGE1);
        tvSubtitle.setText(title);

        title = intent.getStringExtra(Acads.EXTRA_MESSAGE2);
        tvTitle.setText(title);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get input values
                String grading = etGrading.getText().toString().trim();
                String xp = etXp.getText().toString().trim();

                //Check for the validity of input values
                Boolean isValid = true;

                //Show errors
                if (grading.length() == 0) {
                    tilGrading.setError("Field cannot be empty");
                    isValid = false;
                } else {
                    if (tilGrading.isErrorEnabled()) {
                        tilGrading.setError(null);
                    }
                }
                if (xp.length() == 0) {
                    tilXp.setError("Field cannot be empty");
                    isValid = false;
                } else {
                    if (tilXp.isErrorEnabled()) {
                        tilXp.setError(null);
                    }
                }
                //If everything is proper then submit
                if (isValid) {

                    Intent intent = new Intent(view.getContext(),Acads.class);
                    intent.putExtra("BOOLEAN_FROM_ADD_REVIEW_ACTIVITY", true);
                    startActivity(intent);
                }
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
