package in.ac.iitm.students.complaint_box.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;


public class h_NewcomplaintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public h_NewcomplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment h_NewcomplaintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static h_NewcomplaintFragment newInstance(String param1, String param2) {
        h_NewcomplaintFragment fragment = new h_NewcomplaintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hostel_complaints_fragment_new_complaint, container, false);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/addComplaint.php";
        final String url = "https://rockstarharshitha.000webhostapp.com/hostel_complaints/addComplaint.php";
        final EditText prox = (EditText) view.findViewById(R.id.editText_room_number);
        final String roll_no = Utils.getprefString(UtilStrings.ROLLNO, getActivity());
        final String name = Utils.getprefString(UtilStrings.NAME, getActivity());

        final Spinner spinner_complaint_title = (Spinner) view.findViewById(R.id.spinner_complaint_title);
        //spinner_complaint_title.setOnItemSelectedListener(this);
        List<String> title = new ArrayList<String>();
        title.add("Washing Machine");
        title.add("Water Dispenser");
        title.add("Washroom");
        title.add("Problems in your room");
        title.add("Problems in your wing");


        ArrayAdapter<String> dataAdapter_title = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, title);
        dataAdapter_title.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_complaint_title.setAdapter(dataAdapter_title);

        final Spinner spinner_complaint_description = (Spinner) view.findViewById(R.id.spinner_complaint_description);

        List<String> description1 = new ArrayList<String>();
        List<String> description2 = new ArrayList<String>();
        List<String> description3 = new ArrayList<String>();
        List<String> description4 = new ArrayList<String>();
        List<String> description5 = new ArrayList<String>();


        description1.add("Power supply not proper");
        description1.add("Check water error");
        description1.add("Water outlet problem");
        description1.add("Dryer not functional");
        description1.add("Not even starting wash cycle");
        description1.add("Don't know what's the problem");

        final ArrayAdapter<String> dataAdapter_description1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description1);
        dataAdapter_description1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        description2.add("Power supply not proper");
        description2.add("Heating or cooling not working");
        description2.add("Monkey drank from the dispenser");
        description2.add("Algae is growing");

        final ArrayAdapter<String> dataAdapter_description2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description2);
        dataAdapter_description2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description3.add("Power supply not proper");
        description3.add("Taps not properly working");
        description3.add("Showers not properly working");
        description3.add("Towel Hangers not present");
        description3.add("Washroom doors not closing properly");
        description3.add("Flush tanks not working");
        description3.add("Pipes leaking");

        final ArrayAdapter<String> dataAdapter_description3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description3);
        dataAdapter_description3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description4.add("Electrical work");
        description4.add("Civil work");
        description4.add("Furniture broken");
        description4.add("Internet problem (LAN port repair)");

        final ArrayAdapter<String> dataAdapter_description4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description4);
        dataAdapter_description4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description5.add("Electrical work");
        description5.add("Civil work");
        description5.add("Furniture broken");
        description5.add("Internet problem");
        description5.add("Wing not cleaned regularly");
        description5.add("Do not have a dustbin");
        description5.add("Cloth wires not proper");

        final ArrayAdapter<String> dataAdapter_description5 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description5);
        dataAdapter_description5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_complaint_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Spinner spinner = (Spinner) parent;

                //do this
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                switch (position) {
                    case 0:
                        spinner_complaint_description.setAdapter(dataAdapter_description1);
                        break;
                    case 1:
                        spinner_complaint_description.setAdapter(dataAdapter_description2);
                        break;
                    case 2:
                        spinner_complaint_description.setAdapter(dataAdapter_description3);
                        break;
                    case 3:
                        spinner_complaint_description.setAdapter(dataAdapter_description4);
                        break;
                    case 4:
                        spinner_complaint_description.setAdapter(dataAdapter_description5);
                        break;

                }


                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }


            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Button saveNewComplnt = (Button) view.findViewById(R.id.button_save);
        saveNewComplnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = spinner_complaint_title.getSelectedItem().toString();
                final String description = spinner_complaint_description.getSelectedItem().toString();
                final String proximity = prox.getText().toString();
                final String mUUID = UUID.randomUUID().toString();

                if(title != "" && description !="") {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status.equals("1")) {
                                    //finish();
                                    Intent intent = new Intent(getContext(), HostelComplaintsActivity.class);
                                    startActivity(intent);

                                } else if (status.equals("0")) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            //setType
                            Map<String, String> params = new HashMap<>();
                            String hostel_name = sharedPref.getString("hostel", "narmada");
                            String room = sharedPref.getString("roomno", "1004");
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            String moreRooms = room + ",";

                            params.put("HOSTEL", hostel_name);
                            //TODO get name from prefs
                            params.put("NAME", "Omkar Patil");
                            //TODO get rollno from prefs
                            params.put("ROLL_NO", "me15b123");
                            params.put("ROOM_NO", room);
                            params.put("TITLE", title);
                            params.put("PROXIMITY", proximity);
                            //Todo add proximity to card
                            params.put("DESCRIPTION", description);
                            params.put("UPVOTES", "0");
                            params.put("DOWNVOTES", "0");
                            params.put("RESOLVED", "0");
                            params.put("UUID", mUUID);
                            params.put("TAGS", title);
                            params.put("DATETIME", date);
                            params.put("COMMENTS", "0");
                            params.put("MORE_ROOMS", moreRooms);
                            //
                            params.put("TYPE","false");
                            //
                            return params;
                        }
                    };
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

                } else {
                    Toast.makeText(getContext(), "select appropraite title and description from the drop down menu", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}