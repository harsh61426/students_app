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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.adapters.h_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.objects.h_Complaint;
import in.ac.iitm.students.complaint_box.objects.h_Description;
import in.ac.iitm.students.complaint_box.objects.h_DescriptionwithFreq;
import in.ac.iitm.students.complaint_box.others.h_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;


public class h_LatestThreadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String VALUE_HOSTEL = "narmada";
    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;
    ArrayList<h_Complaint> hComplaintList = new ArrayList<>();
    HashMap<String,ArrayList<h_Description>> hComplaintTitleHash = new HashMap<>();
    //ArrayList<h_Description> hComplaintDescription = new ArrayList<>();
    ArrayList<String> hComplaintTitle=new ArrayList<>();
    ArrayList<ArrayList<h_DescriptionwithFreq>> hComplaintDescription=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/getAllComplaints.php";
    private String url = "https://rockstarharshitha.000webhostapp.com/hostel_complaints/getAllComplaints.php";

    public h_LatestThreadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.h_fragment_latest_thread, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_latest_thread);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        getAllComplaints();
        return view;
    }


    public void getAllComplaints() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lanag", response);
                h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(response, getActivity());
                //ArrayList<h_Complaint> hComplaintArray=null;
                try {
                    hComplaintList = hJsonComplaintParser.pleasePleaseParseMyData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(R.id.main_content), "Error fetching the complaints", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();

                    hComplaintArray = new ArrayList<>();
                    hComplaintArray.add(h_Complaint.getErrorComplaintObject());

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_ComplaintAdapter(hComplaintList, getActivity(), getContext(), true);
                int j=hComplaintList.size()-1;
                //bringing false to top
                for(int i=0;i<j;i++){
                    if(hComplaintList.get(i).isType()){
                        while(!hComplaintList.get(j).isType())
                            j--;
                        Collections.swap(hComplaintList,i,j);
                        j--;
                    }
                }

                for(int i=0;i<hComplaintList.size();i++){
                    h_Description hd=new h_Description(hComplaintList.get(i).getDescription(),hComplaintList.get(i).getProximity());


                    if(!hComplaintTitleHash.containsKey(hComplaintList.get(i).getTitle())){
                        ArrayList<h_Description> ahd=new ArrayList<>();
                        ahd.add(hd);
                        hComplaintTitleHash.put(hComplaintList.get(i).getTitle(),ahd);
                    }
                    else{
                        hComplaintTitleHash.get(hComplaintList.get(i).getTitle()).add(hd);
                    }
                }

                hComplaintTitle = new ArrayList<String>(hComplaintTitleHash.keySet());

                for(String i:hComplaintTitle){

                    ArrayList<h_DescriptionwithFreq> hComplaintDescriptionwithFreq=new ArrayList<>();
                    for(int k=0;k<hComplaintTitleHash.get(i).size();k++){

                        int freq=1;
                        for(int m=k+1;m<hComplaintTitleHash.get(i).size();m++){
                            if(hComplaintTitleHash.get(i).get(k).getDescription().equals(hComplaintTitleHash.get(i).get(m).getDescription())){
                                if(hComplaintTitleHash.get(i).get(k).getProximity().equals(hComplaintTitleHash.get(i).get(m).getProximity())){
                                    freq++;
                                    hComplaintTitleHash.get(i).remove(m);
                                    m--;
                                }
                            }
                        }
                        h_DescriptionwithFreq hdf=new h_DescriptionwithFreq(hComplaintTitleHash.get(i).get(k).getDescription(),hComplaintTitleHash.get(i).get(k).getProximity(),freq);
                        hComplaintDescriptionwithFreq.add(hdf);
                    }
                    hComplaintDescription.add(hComplaintDescriptionwithFreq);
                }











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

                ArrayList<h_Complaint> hComplaintArray = new ArrayList<>();
                hComplaintArray.add(h_Complaint.getErrorComplaintObject());

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_ComplaintAdapter(hComplaintArray, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                mRecyclerView.setAdapter(mAdapter);

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
