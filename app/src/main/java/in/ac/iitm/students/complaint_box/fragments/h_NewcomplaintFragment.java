package in.ac.iitm.students.complaint_box.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
    private String mUUID;
    private InputStream stream;

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
        View view = inflater.inflate(R.layout.h_fragment_new_complaint, container, false);

        final String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/addComplaint.php";
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
        description1.add("Others");


        final ArrayAdapter<String> dataAdapter_description1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description1);
        dataAdapter_description1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        description2.add("Power supply not proper");
        description2.add("Heating or cooling not working");
        description2.add("Monkey drank from the dispenser");
        description2.add("Algae is growing");
        description2.add("Others");


        final ArrayAdapter<String> dataAdapter_description2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description2);
        dataAdapter_description2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description3.add("Power supply not proper");
        description3.add("Taps not properly working");
        description3.add("Showers not properly working");
        description3.add("Towel Hangers not present");
        description3.add("Washroom doors not closing properly");
        description3.add("Flush tanks not working");
        description3.add("Pipes leaking");
        description3.add("Others");

        final ArrayAdapter<String> dataAdapter_description3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description3);
        dataAdapter_description3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description4.add("Electrical work");
        description4.add("Civil work");
        description4.add("Furniture broken");
        description4.add("Internet problem (LAN port repair)");
        description4.add("Others");


        final ArrayAdapter<String> dataAdapter_description4 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, description4);
        dataAdapter_description4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        description5.add("Electrical work");
        description5.add("Civil work");
        description5.add("Furniture broken");
        description5.add("Internet problem");
        description5.add("Wing not cleaned regularly");
        description5.add("Does not have a dustbin");
        description5.add("Cloth wires not proper");
        description5.add("Others");


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
                mUUID = UUID.randomUUID().toString();

                if (title.equals("") || description.equals("")) makeSnackbar("Empty field");
                else {


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("panc", "response: " + response);

                            stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                            JsonReader reader = null;
                            try {
                                reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                                reader.setLenient(true);

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            try {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String name = reader.nextName();
                                    Log.e("name", name);
                                    if (name.equals("status")) {
                                        String status = reader.nextString();
                                        if (status.equals("1")) {
                                            Log.d("panc", "succ");
                                            Intent intent = new Intent(getContext(), HostelComplaintsActivity.class);
                                            startActivity(intent);

                                            Snackbar snackbar = Snackbar
                                                    .make(getActivity().findViewById(R.id.cl_new_comp), "Complaint registered successfully", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        } else if (status.equals("0")) {
                                            Log.d("panc", "fail");
                                            Snackbar snackbar = Snackbar
                                                    .make(getActivity().findViewById(R.id.cl_new_comp), "Error registering Complaint", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    } else if (name.equals("error")) {
                                        reader.nextString();
                                        makeSnackbar("Error registering complaint");
                                    } else {
                                        reader.skipValue();
                                    }
                                }
                                reader.endObject();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error registering complaint");
                            } finally {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    makeSnackbar("Error registering complaint");
                                }
                            }

                            /*try {
                                JSONObject jsObject = new JSONObject(response);
                                String status = jsObject.getString("status");
                                if (status.equals("1")) {
                                    Log.d("panc", "succ");
                                    Intent intent = new Intent(getContext(), HostelComplaintsActivity.class);
                                    startActivity(intent);

                                    Snackbar snackbar = Snackbar
                                            .make(getActivity().findViewById(R.id.cl_new_comp), "Complaint registered successfully", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                } else if (status.equals("0")) {
                                    //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    Log.d("panc", "fail");
                                    Snackbar snackbar = Snackbar
                                            .make(getActivity().findViewById(R.id.cl_new_comp), "Error registering Complaint", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Snackbar snackbar = Snackbar
                                        .make(getActivity().findViewById(R.id.cl_new_comp), "Error registering Complaint", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }*/


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Snackbar snackbar = Snackbar
                                    .make(getActivity().findViewById(R.id.cl_new_comp), "Error registering Complaint", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            String moreRooms = Utils.getprefString(UtilStrings.ROOM, getActivity()) + ",";

                            params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, getActivity()));
                            params.put("NAME", Utils.getprefString(UtilStrings.NAME, getActivity()));
                            params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, getActivity()));
                            params.put("ROOM_NO", Utils.getprefString(UtilStrings.ROOM, getActivity()));
                            params.put("TITLE", title);
                            params.put("PROXIMITY", proximity);
                            params.put("DESCRIPTION", description);
                            params.put("UPVOTES", "0");
                            params.put("DOWNVOTES", "0");
                            params.put("RESOLVED", "0");
                            params.put("UUID", mUUID);
                            params.put("TAGS", title);
                            params.put("DATETIME", date);
                            params.put("COMMENTS", "0");
                            params.put("MORE_ROOMS", moreRooms);
                            params.put("CUSTOM", "0");

                            return params;
                        }
                    };
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

                }

            }
        });
        return view;
    }

    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.rl_new_cmplnt), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}