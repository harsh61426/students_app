package in.ac.iitm.students.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.AboutUsActivity;
import in.ac.iitm.students.adapters.ReviewsAdapter;

/**
 * Created by dell on 03-03-2018.
 */

public class ReadReviewActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        final Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(intent.getStringExtra("number") + " reviews") ;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter();


        String[] names = intent.getStringArrayExtra("names");
        String[] reviewGrading = intent.getStringArrayExtra("grading");
        String[] reviewXp = intent.getStringArrayExtra("xp");
        int anInt = intent.getIntExtra("count", 4);

        reviewsAdapter.setNames(names);
        reviewsAdapter.setReviewGrading(reviewGrading);
        reviewsAdapter.setReviewXp(reviewXp);
        reviewsAdapter.setCount(anInt);
        recyclerView.setAdapter(reviewsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent1 = new Intent(context, AddReviewActivity.class);
                intent1.putExtra(Acads.EXTRA_MESSAGE1, intent.getStringExtra("furtherIntent"));
                intent1.putExtra(Acads.EXTRA_MESSAGE2, intent.getStringExtra("number"));
                context.startActivity(intent1);
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
