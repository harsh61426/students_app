package in.ac.iitm.students.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.ProfileAccomAdapter;
import in.ac.iitm.students.fragments.ProfileAccomAddFragment;
import in.ac.iitm.students.objects.ProfileAccomDetails;
import in.ac.iitm.students.others.ProfileAccomDetailArray;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static in.ac.iitm.students.fragments.ProfileAccomAddFragment.accomPos;

/**
 * Created by DELL on 10/5/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private final static int SELECTED_PICTURE_FOR_GALLERY = 1;
    private final static int CAPTURED_PICTURE = 0;
    public static android.app.FragmentManager fragmentManager;
    public static ProfileAccomAdapter accomadapter;
    public static String activity = "";
    static String emailString = "", phonenoString = "", nickNameString = "", aboutThePersonString = "";
    // This is a reference to catch Profile picture imagebutton view.
    int reveal_place=1;
    int reveal_photo=1;
    int reveal_roll = 1;
    CircleImageView profilePicImage;
    String mCurrentPhotoPath;
    EditText email,phoneno,aboutThePerson;
    Switch roomNoSwitch, rollNoSwitch;
    RecyclerView accomRV;

    //private File imageFile;
    private TextView tv_name, tv_roll, tv_hostel, tv_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.title_activity_profile);

        activity = "ProfileActivity";
        tv_name = (TextView) findViewById(R.id.profile_name);
        tv_roll = (TextView) findViewById(R.id.profile_roll_no);
        tv_hostel = (TextView) findViewById(R.id.profile_hostel);
        tv_room = (TextView) findViewById(R.id.profile_room_no);

        email = (EditText) findViewById(R.id.profile_contactEmail);
        phoneno = (EditText) findViewById(R.id.profile_phoneno);
        aboutThePerson = (EditText) findViewById(R.id.profile_about_the_person);
        roomNoSwitch = (Switch) findViewById(R.id.profile_room_no_switch);
        rollNoSwitch = (Switch) findViewById(R.id.profile_rollno_switch);
        profilePicImage = (CircleImageView) findViewById(R.id.profile_propic);
        roomNoSwitch.setChecked(true);
        rollNoSwitch.setChecked(true);
        fragmentManager = getFragmentManager();


        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);
        String hostel = Utils.getprefString(UtilStrings.HOSTEl, this);
        String room = Utils.getprefString(UtilStrings.ROOM, this);

        tv_name.setText(name);
        tv_roll.setText(roll_no);
        tv_hostel.setText(hostel);
        tv_room.setText(room);

        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
        Picasso.with(this)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.dummypropic)
                .fit()
                .centerCrop()
                .into(profilePicImage);

        email.setText(Utils.getprefString("StudentEmail",this));
        phoneno.setText(Utils.getprefString("StudentPhoneNo",this));
        aboutThePerson.setText(Utils.getprefString("aboutYourself",this));
        if(Utils.getprefInt("reveal_place",this)==0){
            roomNoSwitch.setChecked(false);
        }else{
            roomNoSwitch.setChecked(true);
        }
        if (Utils.getprefInt("reveal_roll", this) == 0) {
            rollNoSwitch.setChecked(false);
        } else {
            rollNoSwitch.setChecked(true);
        }
        if(Utils.getprefInt("reveal_photo",this)==0){
            reveal_photo=0;
            profilePicImage.setImageResource(R.drawable.dummypropic);
        }else{
            reveal_photo=1;
            // set profilepicImage to original photo.

        }

        profilePicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(ProfileActivity.this,profilePicImage);
                popupMenu.getMenuInflater().inflate(R.menu.profile_image_dropdown_menu,popupMenu.getMenu());
                MenuItem item = popupMenu.getMenu().getItem(0);
                if(reveal_photo==1){
                    item.setTitle("Hide Image");
                }else{
                    item.setTitle("Show Image");
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.profile_remove_image_item){
                            item.setVisible(false);
                            if(reveal_photo==1){
                                item.setTitle("Show Image");
                            }else{
                                item.setTitle("Hide Image");
                            }
                            AlertDialog.Builder builder  = new AlertDialog.Builder(ProfileActivity.this);

                            if(reveal_photo==1) {
                                builder.setMessage("Do you want to hide your Profile Pic?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                profilePicImage.setImageResource(R.drawable.dummypropic);
                                                reveal_photo=0;
                                            }
                                        })
                                        .setNegativeButton("Cancel", null);

                            }else{
                                builder.setMessage("Do you want to show your Profile Pic?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // set the original pic here.
                                                //profilePicImage.setImageResource(R.drawable.dummypropic);
                                                reveal_photo=1;
                                            }
                                        })
                                        .setNegativeButton("Cancel", null);

                            }

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        /*if(item.getItemId()==R.id.profile_default_image_item){
                            item.setVisible(false);
                            setDefaultProfilePic();
                        }

                        else if(item.getItemId()==R.id.upload_image_item){
                            onUploadButtonClicked();
                        }

                       else if(item.getItemId()==R.id.capture_image_item){

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File

                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, CAPTURED_PICTURE);
                                }
                            }


                        }*/

                        return true;


                    }
                });

                popupMenu.show();
            }
        });




        accomRV=(RecyclerView) findViewById(R.id.profile_accom_rv);
        accomadapter=new ProfileAccomAdapter(this, ProfileAccomDetailArray.getAccomData());
        LinearLayoutManager layoutmanger=new LinearLayoutManager(this);
        accomRV.setLayoutManager(layoutmanger);
        accomRV.setAdapter(accomadapter);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!isEmailValid(email.getText().toString())){
                    final AlertDialog.Builder builder  = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage("Email is invalid.")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }

                return false;
            }
        });

        phoneno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!isPhonenoValid(phoneno.getText().toString())){
                    final AlertDialog.Builder builder  = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage("Phone no. is invalid.")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }
                InputMethodManager inputManager =
                        (InputMethodManager) ProfileActivity.this.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        ProfileActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                phoneno.clearFocus();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        aboutThePersonString = aboutThePerson.getText().toString();
        phonenoString = phoneno.getText().toString();
        emailString = email.getText().toString();
        if (roomNoSwitch.isChecked()) reveal_place = 1;
        if (rollNoSwitch.isChecked()) reveal_roll = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        email.setText(emailString);
        phoneno.setText(phonenoString);
        aboutThePerson.setText(aboutThePersonString);
        if(reveal_place==0){
            roomNoSwitch.setChecked(false);
            ((TextView) findViewById(R.id.profile_room_no)).setTextColor(Color.GRAY);
        }else{
            roomNoSwitch.setChecked(true);
            ((TextView) findViewById(R.id.profile_room_no)).setTextColor(Color.BLACK);
        }
        if (reveal_roll == 0) {
            rollNoSwitch.setChecked(false);
            ((TextView) findViewById(R.id.profile_roll_no)).setTextColor(Color.GRAY);
        } else {
            rollNoSwitch.setChecked(true);
            ((TextView) findViewById(R.id.profile_roll_no)).setTextColor(Color.BLACK);
        }
    }

    private void setDefaultProfilePic() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        if(item.getItemId() == R.id.profile_accom_save_action_button) {
            if (activity.equals("MainActivity")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //Checking validation
                if (!isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Email and phone no.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return super.onOptionsItemSelected(item);
                } else if (!isEmailValid(email.getText().toString()) && isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Email.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return super.onOptionsItemSelected(item);
                } else if (isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Phone no.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return super.onOptionsItemSelected(item);
                }
                // Saving the progress

                Utils.saveprefString("StudentEmail",email.getText().toString(),this);
                Utils.saveprefString("StudentPhoneNo",phoneno.getText().toString(),this);
                Utils.saveprefString("aboutYourself",aboutThePerson.getText().toString(),this);
                Utils.saveprefInt("reveal_place",reveal_place,this);
                Utils.saveprefInt("reveal_roll", reveal_roll, this);
                Utils.saveprefInt("reveal_photo",reveal_photo,this);

                //You can replace this when you have MySingleton class
                final RequestQueue queue = Volley.newRequestQueue(this);

                builder.setMessage("Do you want to save changes?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Upload the data into the server.


                                /**Changes made**/

                                //getPostParams method is declared dbelow
                                final String requestBody = getPostParams().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String message = null;
                                        if (error instanceof NetworkError) {
                                            message = "Cannot connect to Internet. Please check your connection!!";
                                        } else if (error instanceof ServerError) {
                                            message = "Server down. Please try again after some time!!";
                                        } else if (error instanceof AuthFailureError) {
                                            message = "Authentication error!!";
                                        } else if (error instanceof ParseError) {
                                            message = "Parsing error! Please try again after some time!!";
                                        } else if (error instanceof TimeoutError) {
                                            message = "Connection TimeOut! Please check your internet connection.";
                                        }
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    public String getBodyContentType() {
                                        return String.format("application/json; charset=utf-8");
                                    }

                                    @Override
                                    public byte[] getBody() throws AuthFailureError {
                                        try {
                                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                                    requestBody, "utf-8");
                                            return null;
                                        }
                                    }
                                };
                                queue.add(stringRequest);

                                //Changes end here

                                Toast.makeText(ProfileActivity.this, "Uploading", Toast.LENGTH_SHORT).show();

                                /**********************************/
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * method is used for checking valid email id format.
     *
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
    private boolean isPhonenoValid(String s) {

        return s.length() == 10 || s.length() == 0;
    }

    // This method is invoked when accom_plus_image is clicked.
    public void onAccomPlusClicked(View view) {

        // Create new fragment and transaction
        android.app.Fragment newFragment = new ProfileAccomAddFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.mainActivityRelativeLayout, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }


    // Required for Capture image ,this method creates a file for saving the captured image.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.profile_radio_button_organisation:
                if (checked)
                    ProfileAccomAddFragment.radioButtonProject.setChecked(false);
                ProfileAccomAddFragment.radioStatus="Organisation";
                ProfileAccomAddFragment.accomOrgan.setHint("Organisation");
                accomPos.setHint("Position in Organisation");
                break;
            case R.id.profile_radio_Button_Project:
                if (checked)
                    ProfileAccomAddFragment.radioButtonOrganisation.setChecked(false);
                ProfileAccomAddFragment.radioStatus="Project";
                ProfileAccomAddFragment.accomOrgan.setHint("Project Title");
                accomPos.setHint("Project description");
                break;
        }
    }

    public void onRoomNoChecked(View view){
        if (roomNoSwitch.isChecked()) {
            roomNoSwitch.setChecked(true);
            ((TextView) findViewById(R.id.profile_room_no)).setTextColor(Color.BLACK);
            reveal_place=1;

        }else{
            roomNoSwitch.setChecked(false);
            ((TextView) findViewById(R.id.profile_room_no)).setTextColor(Color.GRAY);
            reveal_place=0;
        }

        if (rollNoSwitch.isChecked()) {
            rollNoSwitch.setChecked(true);
            ((TextView) findViewById(R.id.profile_roll_no)).setTextColor(Color.BLACK);
            reveal_roll = 1;

        } else {
            rollNoSwitch.setChecked(false);
            ((TextView) findViewById(R.id.profile_roll_no)).setTextColor(Color.GRAY);
            reveal_roll = 0;
        }
    }

    //This is the method to get JsonObject from user input
    public JSONObject getPostParams(){
        int size = accomRV.getAdapter().getItemCount();
        JSONArray accomplishmentsArray = new JSONArray();
        for(int i = 0; i < size; i++){
            JSONObject accomplishmentsObj = new JSONObject();
            ProfileAccomDetails currDetails = ((ProfileAccomAdapter)accomRV.getAdapter()).getItem(i);
            try {
                accomplishmentsObj.put("title",currDetails.accomOrgan );
                accomplishmentsObj.put("desc", currDetails.accomPos);
                accomplishmentsObj.put("from", currDetails.accomFromyear);
                accomplishmentsObj.put("to", currDetails.accomToyear);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            accomplishmentsArray.put(accomplishmentsObj);
        }
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("Accomplishments", accomplishmentsArray);
            jsonPost.put("email",emailString );
            jsonPost.put("Phone", phonenoString);
            jsonPost.put("about", aboutThePersonString);
            jsonPost.put("reveal_photo", 1);
            jsonPost.put("reveal_place", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonPost;

    }



    /*
    // This method invokes when the upload imagebutton is clicked.

    public void onUploadButtonClicked(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECTED_PICTURE_FOR_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECTED_PICTURE_FOR_GALLERY){
                Uri image_uri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(image_uri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    profilePicImage.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to locate image",Toast.LENGTH_LONG).show();
                }
            }



            else if(requestCode == CAPTURED_PICTURE){
                Toast.makeText(ProfileActivity.this,"image saved.",Toast.LENGTH_LONG).show();
                // Get the dimensions of the View
                int targetW = profilePicImage.getWidth();
                int targetH = profilePicImage.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                profilePicImage.setImageBitmap(bitmap);

            }
        }
    }*/
    /*****************************************************************/




}
