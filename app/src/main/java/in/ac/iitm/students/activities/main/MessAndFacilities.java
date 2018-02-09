package in.ac.iitm.students.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import in.ac.iitm.students.R;

public class MessAndFacilities extends AppCompatActivity {

    /*TODO: Mess Menu should show the menu for the next meal of that day
    * Add in feature that lets users rate the menu and view its rating
    * Make it a scrollable list so that users can see menus of rest of the week*/

    /*TODO: Mess complaints should let users post complaints for CMGFS
     * Add option to choose which facility the complaint is for
     * Show contact details of relevant secretary/vendor for the complaint
      */

    /*TODO: Mess contacts shows the contact details of HAS and all the coordinators */

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_mess_menu:
                    mTextMessage.setText(R.string.mess_menu);
                    return true;
                case R.id.nav_complaint_mess:
                    mTextMessage.setText(R.string.mess_complaints);
                    return true;
                case R.id.nav_cmgfs_contact:
                    mTextMessage.setText(R.string.mess_contact);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_and_facilities);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
