package in.ac.iitm.students.complaint_box.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

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
import in.ac.iitm.students.complaint_box.adapters.g_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.others.g_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;


public class g_LatestThreadFragment extends Fragment implements Updateable,SwipeRefreshLayout.OnRefreshListener{

    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String hostel_name;

    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/getAllComplaints.php";

    public g_LatestThreadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.g_fragment_latest_thread, container, false);

        hostel_name = Utils.getprefString(UtilStrings.HOSTEl, getActivity());
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_latest_thread);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        getAllComplaints();

        return view;
    }

    @Override
    public void update(String string) {
        String searchResponse =string;
        //if (getArguments() != null) {
        if(searchResponse!=null){
            //String searchResponse = getArguments().getString("tagSearch");
            Log.d("searchResponse",searchResponse);
            g_JSONComplaintParser gJsonComplaintParser = new g_JSONComplaintParser(searchResponse, getActivity());
            ArrayList<Complaint> hComplaintArray = null;
            try {
                hComplaintArray = gJsonComplaintParser.pleasePleaseParseMyData();
                /*add
                hwm=hJsonComplaintParser.getH_wm();
                hwd=hJsonComplaintParser.getH_wd();
                hw=hJsonComplaintParser.getH_w();
                hpr=hJsonComplaintParser.getH_pr();
                hpw=hJsonComplaintParser.getH_pw();
                */
                //Log.e("ComplaintArray",hComplaintArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
            }

            //TODO always null
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    public void getAllComplaints() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Complaints....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("latest tag", response);
                g_JSONComplaintParser gJsonComplaintParser = new g_JSONComplaintParser(response, getActivity());
                ArrayList<Complaint> gComplaintArray = null;
                try {
                    //fix
                    gComplaintArray = gJsonComplaintParser.pleasePleaseParseMyData();
                    Log.d("bleek", "yaasss");
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(R.id.main_content), "Error fetching the complaints", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();

                    gComplaintArray = new ArrayList<>();
                    gComplaintArray.add(Complaint.getErrorComplaintObject());

                    progressDialog.dismiss();
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new g_ComplaintAdapter(gComplaintArray, getActivity(), getContext(), false, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                }

                progressDialog.dismiss();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new g_ComplaintAdapter(gComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);
                // mAdapter.notifyDataSetChanged();


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

                progressDialog.dismiss();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_HOSTEL, hostel_name);
                return params;
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
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