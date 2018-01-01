package in.ac.iitm.students.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.StudentDetailsActivity;
import in.ac.iitm.students.others.MySingleton;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RollSearchFragment extends Fragment {
    Context context;
    EditText etRollNoSearch;
    FrameLayout frameLayout;
    CircleImageView profilePic_;
    TextView name_;
    TextView rollno_;
    TextView hostel_;
    TextView room_;
    TextView email_;
    TextView phoneno_;
    TextView abtyourself_;
    ScrollView sc_;

    public RollSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_roll_search, container, false);

        context = view.getContext();
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_roll);
        etRollNoSearch = (EditText) view.findViewById(R.id.et_search_no);
        etRollNoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                goToDetails();
                return false;
            }
        });

        profilePic_ = (CircleImageView) view.findViewById(R.id.profile_pic);
        name_ = (TextView) view.findViewById(R.id.name_overview);
        rollno_ = (TextView) view.findViewById(R.id.rollno_overview);
        hostel_ = (TextView) view.findViewById(R.id.hostel_overview);
        room_ = (TextView) view.findViewById(R.id.room_overview);
        email_ = (TextView) view.findViewById(R.id.email_info);
        phoneno_ = (TextView) view.findViewById(R.id.phone_info);
        abtyourself_ = (TextView) view.findViewById(R.id.aboutyourself);
        sc_=(ScrollView) view.findViewById(R.id.scroll_view);

        Button buttonShowDetails = (Button) view.findViewById(R.id.button_show_details);
        buttonShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetails();
            }
        });

        return view;
    }

    private void goToDetails() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etRollNoSearch.getWindowToken(), 0);

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Getting data...");
        pDialog.show();
        pDialog.setCancelable(false);

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")//https://students.iitm.ac.in/studentsapp/map/get_location.php?
                .authority("students.iitm.ac.in")
                .appendPath("Android")
                .appendPath("includes")
                .appendPath("search_by_roll.php");


        String url = "https://students.iitm.ac.in/studentsapp/studentlist/search_by_roll.php";


        StringRequest stud_detail_via_roll_req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    String studName = "Name appears here",
                            studRoll = "Roll number appears here",
                            hostel = "Hostel",
                            roomNo = "room number",
                            email = "Email here",
                            phone = "Phone no. here",
                            about = "About student";

                    int reveal_photo = 0;

                    pDialog.dismiss();
                    JSONArray baseArray = new JSONArray(response);
                    for (int i = 0; i < baseArray.length(); i++) {
                        JSONObject baseObject = baseArray.getJSONObject(i);
                        studName = baseObject.getString("fullname");
                        studRoll = baseObject.getString("username");
                        hostel = baseObject.getString("hostel");
                        roomNo = baseObject.getString("room");
                        email = baseObject.getString("email");
                        phone = baseObject.getString("phone_no");
                        reveal_photo = baseObject.getInt("reveal_photo");
                        about = baseObject.getString("about");
                    }

                    // TODO : @aebel remove intent and add fragment below the search widget dynamically

                    /*Intent intent = new Intent(context, StudentDetailsActivity.class);
                    intent.putExtra("studName", studName);
                    intent.putExtra("studRoll", studRoll);
                    intent.putExtra("hostel", hostel);
                    intent.putExtra("roomNo", roomNo);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("reveal_photo",reveal_photo);
                    intent.putExtra("about", about);
                    startActivity(intent);*/
                    sc_.setVisibility(View.VISIBLE);
                    name_.setText(studName);
                    rollno_.setText(studRoll);
                    room_.setText(roomNo);
                    hostel_.setText(hostel);
                    if(email.equalsIgnoreCase("null")){
                        email_.setText("Not available");
                    }else{
                        email_.setText(email);
                    }

                    if(phone.equalsIgnoreCase("null")){
                        phoneno_.setText("Not available");
                    }else{
                        phoneno_.setText(phone);
                    }

                    if(about.equalsIgnoreCase("null")){
                        abtyourself_.setText("Not available");
                    }else{
                        abtyourself_.setText(about);
                    }

                    int check=reveal_photo;
                    //int check = getIntent().getIntExtra("reveal_photo", 1);

                    if(check == 1) {
                        Uri.Builder builder = new Uri.Builder();

                        builder.scheme("https")
                                .authority("photos.iitm.ac.in")
                                .appendPath("byroll.php")
                                .appendQueryParameter("roll", rollno_.getText().toString());
                        String url = builder.build().toString();
                        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.dummypropic).error(R.drawable.dummypropic).fit().centerCrop().into(profilePic_);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
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
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("roll" , etRollNoSearch.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stud_detail_via_roll_req);
    }


}
