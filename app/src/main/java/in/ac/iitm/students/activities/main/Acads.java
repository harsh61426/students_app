package in.ac.iitm.students.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import in.ac.iitm.students.R;

public class Acads extends AppCompatActivity {

    /* TODO: Seperate timetable from bunk monitor
     */

    /*TODO: Make widget to view timetable and bunk monitor */

    /*TODO: Make calendar save data offline and try to reduce its loading time */

    /*TODO: Add a drag and drop feature when editing courses for timetable*/


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.calendar:
                    mTextMessage.setText(R.string.calendar);
                    return true;
                case R.id.timetable:
                    mTextMessage.setText(R.string.timetable);
                    return true;
                case R.id.bunkmonitor:
                    mTextMessage.setText(R.string.bunk_monitor);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acads);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
