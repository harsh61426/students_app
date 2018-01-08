package in.ac.iitm.students.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;


/**
 * Created by DELL on 10/5/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profilePicImage;
    EditText et_email, et_phone;
    TextView tv_name, tv_roll, tv_hostel, tv_room, tv_phone, tv_email, tv_mess;

    public static String reverse(String input) {
        char[] in = input.toCharArray();
        int begin = 0;
        int end = in.length - 1;
        char temp;
        while (end > begin) {
            temp = in[begin];
            in[begin] = in[end];
            in[end] = temp;
            end--;
            begin++;
        }
        return new String(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.title_activity_profile);

        //activity = "ProfileActivity";
        tv_name = (TextView) findViewById(R.id.p_name);
        tv_roll = (TextView) findViewById(R.id.p_rollno);
        tv_hostel = (TextView) findViewById(R.id.p_hostel);
        tv_room = (TextView) findViewById(R.id.p_room);
        tv_phone = (TextView) findViewById(R.id.p_mobile);
        tv_email = (TextView) findViewById(R.id.p_email);
        tv_mess = (TextView) findViewById(R.id.p_mess);

        et_email = (EditText) findViewById(R.id.p_edit_email);
        et_phone = (EditText) findViewById(R.id.p_edit_mobile);

        profilePicImage = (CircleImageView) findViewById(R.id.p_propic);

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);
        String hostel = Utils.getprefString(UtilStrings.HOSTEl, this);
        String room = Utils.getprefString(UtilStrings.ROOM, this);
        String mobile = Utils.getprefString(UtilStrings.MOBILE,this);
        String email = Utils.getprefString(UtilStrings.MAIL,this);
        String mess = Utils.getprefString(UtilStrings.MESS, this);

        tv_name.setText(name);
        tv_roll.setText(roll_no.toUpperCase());
        tv_hostel.setText("Hostel: "+hostel.toUpperCase());
        tv_room.setText("Room: "+room);
        tv_phone.setText("Contact No: "+mobile);
        tv_mess.setText("Mess: "+mess);
        getMess(roll_no.toUpperCase(),mess);

        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.drawable.dummypropic)
                .error(R.drawable.dummypropic)
                .fit()
                .centerInside()
                .into(profilePicImage);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            tv_email.setText("Email ID: "+acct.getEmail());
        }else {
            tv_email.setText("Email ID: "+email);
        }

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_phone.setVisibility(View.GONE);
                et_phone.setVisibility(View.VISIBLE);
                et_phone.requestFocus();
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_email.setVisibility(View.GONE);
                et_email.setVisibility(View.VISIBLE);
                et_email.requestFocus();
            }
        });

        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("Event",Integer.toString(actionId));
                if(actionId== EditorInfo.IME_ACTION_DONE) {
                    String mail = et_email.getText().toString();
                    Log.i("Mail",mail);
                    if (!isEmailValid(mail)) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Email is invalid.")
                                .setPositiveButton("Ok", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return true;
                    }
                    else
                    {
                        Utils.saveprefString(UtilStrings.MAIL,mail,ProfileActivity.this);
                        tv_email.setText("Email ID: "+mail);
                        et_email.setVisibility(View.GONE);
                        tv_email.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });

        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_NEXT) {

                    String phone = et_phone.getText().toString();

                    if (!isPhonenoValid(phone)) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Phone no. is invalid.")
                                .setPositiveButton("Ok", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return true;
                    }
                    else
                    {
                        Utils.saveprefString(UtilStrings.MOBILE,phone,ProfileActivity.this);
                        tv_phone.setText("Contact No: "+phone);
                        et_phone.setVisibility(View.GONE);
                        tv_phone.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                et_phone.clearFocus();
                return false;
            }
        });


        if(!Utils.getprefString(UtilStrings.MAIL,this).isEmpty()) {
            et_email.setVisibility(View.GONE);
            tv_email.setVisibility(View.VISIBLE);
        }
        else
        {
            et_email.setVisibility(View.VISIBLE);
            tv_email.setVisibility(View.GONE);
        }
        if(!Utils.getprefString(UtilStrings.MOBILE,this).isEmpty()) {
            et_phone.setVisibility(View.GONE);
            tv_phone.setVisibility(View.VISIBLE);
        }
        else
        {
            et_phone.setVisibility(View.VISIBLE);
            tv_phone.setVisibility(View.GONE);
        }

    }


    /**
     * method is used for checking valid email id format.
     * @param email
     * @return boolean true for valid false for invalid
     */
    private boolean isEmailValid(String email) {
        if( email.length()==0)     // email field can be vacant.
            return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMess(final String roll, final String mess)
    {

        String url_mess = "https://students.iitm.ac.in/studentsapp/mess_reg/extract.php";
        StringRequest request = new StringRequest(Request.Method.POST, url_mess, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv_mess.setText("Mess: "+response);
                if(!mess.equalsIgnoreCase(response))
                {
                    Utils.saveprefString(UtilStrings.MESS,response,ProfileActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error","Unable to fetch");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rollno",roll);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private boolean isPhonenoValid(String s) {
        return s.length() == 10 || s.length() == 0;
    }




}
