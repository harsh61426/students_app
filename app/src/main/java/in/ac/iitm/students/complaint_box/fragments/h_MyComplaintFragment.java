package in.ac.iitm.students.complaint_box.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.adapters.h_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.others.h_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;


public class h_MyComplaintFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String hostel = "narmada";
    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;

    ArrayList<Complaint> hComplaintList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/myComplaints.php";

    public h_MyComplaintFragment() {
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
        View view = inflater.inflate(R.layout.h_fragment_my_complaint, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_my_complaint);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        getMyComplaints();

        return view;
    }

    public void getMyComplaints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                //Log.d("tag", response);
                h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(response, getActivity());

                //ArrayList<Complaint> hComplaintArray = null;
                try {
                    hJsonComplaintParser.pleasePleaseParseMyData();
                    hComplaintList=hJsonComplaintParser.gethComplaintArray();
                    Log.d("polly", "dumma");
                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(R.id.main_content), "Error fetching the complaints", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    //hComplaintArray = new ArrayList<>();
                    hComplaintList.add(Complaint.getErrorComplaintObject());

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new h_ComplaintAdapter(hComplaintList,getActivity(), getContext(), false, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                }
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_ComplaintAdapter(hComplaintList,getActivity(), getContext(), false, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), "No internet connectivity", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar.show();

                ArrayList<Complaint> hComplaintArray = new ArrayList<>();
                hComplaintArray.add(Complaint.getErrorComplaintObject());

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_ComplaintAdapter(hComplaintList, getActivity(), getContext(), false, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String hostel_name = Utils.getprefString(UtilStrings.HOSTEl, getActivity());
                String rollno = Utils.getprefString(UtilStrings.ROLLNO, getActivity());

                params.put(KEY_HOSTEL, hostel_name);
                params.put("ROLL_NO", rollno);
                return params;
            }

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }*/
        };

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        //lite
        int MY_SOCKET_TIMEOUT_MS = 5000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onRefresh() {

        //code here to load new data and setRefreshing to false
        swipeLayout.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }
}
