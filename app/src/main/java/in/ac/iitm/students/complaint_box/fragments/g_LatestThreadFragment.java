package in.ac.iitm.students.complaint_box.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.adapters.g_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.adapters.h_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.objects.h_Complaint;
import in.ac.iitm.students.complaint_box.others.h_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;

import static com.facebook.FacebookSdk.getApplicationContext;


public class g_LatestThreadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private final String VALUE_HOSTEL = "narmada";
    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;
    List<h_Complaint> hComplaintList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/general_complaints/getAllComplaints.php";
    private String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/getAllComplaints.php";

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

        View view = inflater.inflate(R.layout.fragment_g__latest_thread, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_latest_thread);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        getAllComplaints();

        if (getArguments() != null) {
            String searchResponse = getArguments().getString("tagSearch");
            Log.e("searchResponse",searchResponse);
            h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(searchResponse, getActivity());
            ArrayList<h_Complaint> hComplaintArray = null;
            try {
                hComplaintArray = hJsonComplaintParser.pleasePleaseParseMyData();
                Log.e("ComplaintArray",hComplaintArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
            }

            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext());
            mRecyclerView.setAdapter(mAdapter);

        }

        return view;
    }

    public void getAllComplaints() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("latest tag", response);
                h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(response, getActivity());
                ArrayList<h_Complaint> hComplaintArray = null;
                try {
                    hComplaintArray = hJsonComplaintParser.pleasePleaseParseMyData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
                }

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext());
                mRecyclerView.setAdapter(mAdapter);
                // mAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No internet connectivity", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_HOSTEL, VALUE_HOSTEL);
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
