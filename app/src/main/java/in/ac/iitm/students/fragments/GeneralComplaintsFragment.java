package in.ac.iitm.students.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import in.ac.iitm.students.complaint_box.activities.main.GeneralComplaintsActivity;
import in.ac.iitm.students.complaint_box.adapters.g_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.others.g_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;

/**
 * Created by sam10795 on 11/2/18.
 */

public class GeneralComplaintsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String hostel_name;
    public GeneralComplaintsActivity generalComplaintsActivity;

    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/getAllComplaints.php";


    public GeneralComplaintsFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void update(String string) {
        //if (getArguments() != null) {
        if(string !=null && string.length()>0){
            //String searchResponse = getArguments().getString("tagSearch");
            Log.d("searchResponse",string);
            g_JSONComplaintParser gJsonComplaintParser = new g_JSONComplaintParser(string, getActivity());
            ArrayList<Complaint> hComplaintArray = null;
            try {
                hComplaintArray = gJsonComplaintParser.pleasePleaseParseMyData();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(generalComplaintsActivity, "IOException", Toast.LENGTH_SHORT).show();
            }

            //TODO always null

            mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
//            GeneralComplaintsActivity.adapter.notifyDataSetChanged();
//            FragmentTransaction.commitNow();
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
                mAdapter = new g_ComplaintAdapter(gComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


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
                mAdapter = new g_ComplaintAdapter(hComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
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
