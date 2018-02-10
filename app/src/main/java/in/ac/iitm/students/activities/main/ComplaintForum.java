package in.ac.iitm.students.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import in.ac.iitm.students.R;

public class ComplaintForum extends AppCompatActivity {

    /*TODO: navigation_general should display general complaints
     * General complaints should show the complaints and should let users
      * view the comments in the same fragment(without switching over to a seperate activity)
       * Look up Expandable ListView for this*/

    /*TODO: navigation_hostel should redirect to hostel complaints
     *Hostel complaints should show all categories as dropdowns
      * i.e. There should be expandable lists for washing machine, room problems, etc.
       * These would show the count of complaints in that section
       * On clicking the list header, one should see the complaints in it(get back to me if it's unclear)
      */

    /*TODO: navigation_mess should redirect to CMGFS complaints
     * The users should be able to pick the facility they are complaining about
     * The contact details of the coordinator responsible for that facility should be visible
     */

    /*TODO: navigation_my_complaints should let users view and delete their complaints */

    /*TODO: navigation_add_complaint should let users add in new complaint
    * Add in a texview which says hostel complaints are in the CMGFS menu
    * Add in option to post a complaint anonymously
    * Give options to post in general forum or hostel and change the input options accordingly
    * E.g. If user selects hostel, show the room no., problem category, etc.
    * while for general complaints, give option to tag secretaries*/


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_general:
                    mTextMessage.setText(R.string.general);
                    return true;
                case R.id.navigation_hostel:
                    mTextMessage.setText(R.string.hostel);
                    return true;
                case R.id.navigation_my_complaints:
                    mTextMessage.setText(R.string.my_complaints);
                    return true;
                case R.id.navigation_mess:
                    mTextMessage.setText(R.string.mess_complaints);
                case R.id.navigation_add_complaint:
                    mTextMessage.setText(R.string.add_new);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_forum);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
