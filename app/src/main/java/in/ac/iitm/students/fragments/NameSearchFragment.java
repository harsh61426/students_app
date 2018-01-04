package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.StudentDetailsActivity;
import in.ac.iitm.students.adapters.StudentSearchAdapter;
import in.ac.iitm.students.objects.Student;
import in.ac.iitm.students.others.MySingleton;

import static com.facebook.FacebookSdk.getApplicationContext;


public class NameSearchFragment extends Fragment {

    ListView lvSuggestion;
    StudentSearchAdapter adapter;
    ArrayList<Student> listSuggestion = new ArrayList<>(25);
    EditText etSearch;
    ProgressBar progressSearch;
    Context context;
    FrameLayout frameLayout;
    TextView searchMessage;
    CircleImageView profilePic_;
    TextView name_;
    TextView rollno_;
    TextView hostel_;
    TextView room_;
    TextView email_;
    TextView phoneno_;
    TextView abtyourself_;
    ScrollView sc_;

    public NameSearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_name_search, container, false);
        context = view.getContext();
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout_name);
        progressSearch = (ProgressBar) view.findViewById(R.id.pb_search);
        etSearch = (EditText) view.findViewById(R.id.et_search_name);
        searchMessage = (TextView) view.findViewById(R.id.tv_search_result_msg);
        lvSuggestion = (ListView) view.findViewById(R.id.lv_suggestion);
        adapter = new StudentSearchAdapter(listSuggestion,getContext());
        lvSuggestion.setAdapter(adapter);
        lvSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = (Student) parent.getItemAtPosition(position);
                viewDetails(student);
                //goToDetails(name);
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


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showSuggestion(s.toString());
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final Editable selection = etSearch.getText();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    }
                    showSuggestion(selection.toString());
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void showSuggestion(String query) {

        listSuggestion.clear();
        MySingleton.getInstance(context).getRequestQueue().cancelAll("tag");
        if (query.length() <= 2) {
            progressSearch.setVisibility(View.GONE);
            searchMessage.setText(R.string.error_enter_more_characters);
            searchMessage.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            return;
        }
        searchMessage.setVisibility(View.INVISIBLE);
        progressSearch.setVisibility(View.VISIBLE);

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")//https://students.iitm.ac.in/studentsapp/studentlist/search_by_name.php
                .authority("students.iitm.ac.in")
                .appendPath("studentsapp")
                .appendPath("studentlist")
                .appendPath("search_by_name.php");

        String url = builder.build().toString();
//        Log.d("URL", url);
        // Request a string response from the provided URL.
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    int i;
                    Student student;
                    listSuggestion.clear();

                    for (i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Log.i("JSON",jsonObject.toString());
                        student = new Student();
                        student.setName(jsonObject.getString("fullname"));
                        student.setRollno(jsonObject.getString("username"));
                        student.setHostel(jsonObject.getString("hostel"));
                        student.setRoom(jsonObject.getString("room"));
                        student.setGender(jsonObject.getString("gender").charAt(0));

                        if (!listSuggestion.contains(student))
                            listSuggestion.add(student);//+", "+studRoll
                    }
                    adapter.notifyDataSetChanged();
                    searchMessage.setText("Search Results");
                    searchMessage.setVisibility(View.VISIBLE);
                    progressSearch.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Log.d("URL", response);
                    if (response.equals("No results") || response.equals("")) {
                        searchMessage.setText(R.string.error_no_result);
                        searchMessage.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(frameLayout, getString(R.string.error_parsing), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    listSuggestion.clear();
                    adapter.notifyDataSetChanged();
                    progressSearch.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                searchMessage.setText(R.string.error_connection);
                searchMessage.setVisibility(View.VISIBLE);
                listSuggestion.clear();
                adapter.notifyDataSetChanged();
                progressSearch.setVisibility(View.GONE);
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", etSearch.getText().toString());
                Log.i("name",etSearch.getText().toString());
                return params;
            }
        };

        jsonObjReq.setTag("tag");
        MySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    private void viewDetails(Student student)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Student details");
        dialog.setContentView(R.layout.dialog_details);
        TextView rollno = (TextView)dialog.findViewById(R.id.d_rollno);
        rollno.setText(student.getRollno());
        TextView name = (TextView)dialog.findViewById(R.id.d_name);
        name.setText(student.getName());
        TextView room = (TextView)dialog.findViewById(R.id.d_room);
        room.setText(student.getRoom()+", "+student.getHostel());
        CircleImageView photo = (CircleImageView)dialog.findViewById(R.id.d_photo);Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority("photos.iitm.ac.in")
                .appendPath("byroll.php")
                .appendQueryParameter("roll", student.getRollno());

        String url = builder.build().toString();

        Picasso.with(getContext()).load(url).
                placeholder(R.drawable.dummypropic).
                error(R.drawable.dummypropic).
                fit().centerCrop().
                into(photo);

        dialog.show();
    }

    private void goToDetails(String passed_name) {

        final String stud_name = passed_name;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Getting data...");
        pDialog.show();
        pDialog.setCancelable(false);


        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")//https://students.iitm.ac.in/studentsapp/map/get_location.php?
                .authority("192.168.1.7")
                .appendPath("Android")
                .appendPath("includes")
                .appendPath("search_by_name.php");


        String url = builder.build().toString();

        // Request a string response from the provided URL.
        StringRequest stud_detail_via_name_req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

                    /*Intent intent = new Intent(context, StudentDetailsActivity.class);
                    intent.putExtra("studName", studName);
                    intent.putExtra("studRoll", studRoll);
                    intent.putExtra("hostel", hostel);
                    intent.putExtra("roomNo", roomNo);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("reveal_photo", reveal_photo);
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
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", stud_name);
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stud_detail_via_name_req);
    }
}
