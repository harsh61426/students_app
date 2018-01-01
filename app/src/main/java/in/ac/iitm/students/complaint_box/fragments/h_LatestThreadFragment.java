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
import android.widget.Button;
import android.widget.TextView;

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
import in.ac.iitm.students.complaint_box.objects.h_PinRoom;
import in.ac.iitm.students.complaint_box.objects.h_PinWing;
import in.ac.iitm.students.complaint_box.objects.h_WashingMachine;
import in.ac.iitm.students.complaint_box.objects.h_Washroom;
import in.ac.iitm.students.complaint_box.objects.h_WaterDispenser;
import in.ac.iitm.students.complaint_box.others.h_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

//import in.ac.iitm.students.complaint_box.objects.h_Complaint;


public class h_LatestThreadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String KEY_HOSTEL = "HOSTEL";
    SwipeRefreshLayout swipeLayout;
    ArrayList<Complaint> hComplaintList = new ArrayList<>();
    h_WashingMachine hwm=new h_WashingMachine();
    h_WaterDispenser hwd=new h_WaterDispenser();
    h_Washroom hw=new h_Washroom();
    h_PinRoom hpr=new h_PinRoom();
    h_PinWing hpw=new h_PinWing();
    int t1ids[][]={{R.id.t1p1,R.id.t1f1},{R.id.t1p2,R.id.t1f2},{R.id.t1p3,R.id.t1f3},{R.id.t1p4,R.id.t1f4},{R.id.t1p5,R.id.t1f5},{R.id.t1p6,R.id.t1f6}};
    int t2ids[][]={{R.id.t2p1,R.id.t2f1},{R.id.t2p2,R.id.t2f2},{R.id.t2p3,R.id.t2f3},{R.id.t2p4,R.id.t2f4},{R.id.t2p5,R.id.t2f5}};
    int t3ids[][]={{R.id.t3p1,R.id.t3f1},{R.id.t3p2,R.id.t3f2},{R.id.t3p3,R.id.t3f3},{R.id.t3p4,R.id.t3f4},{R.id.t3p5,R.id.t3f5},{R.id.t3p6,R.id.t3f6},{R.id.t3p7,R.id.t3f7},{R.id.t3p8,R.id.t3f8}};
    int t4ids[][]={{R.id.t4p1,R.id.t4f1},{R.id.t4p2,R.id.t4f2},{R.id.t4p3,R.id.t4f3},{R.id.t4p4,R.id.t4f4},{R.id.t4p5,R.id.t4f5}};
    int t5ids[][]={{R.id.t5p1,R.id.t5f1},{R.id.t5p2,R.id.t5f2},{R.id.t5p3,R.id.t5f3},{R.id.t5p4,R.id.t5f4},{R.id.t5p5,R.id.t5f5},{R.id.t5p6,R.id.t5f6},{R.id.t5p7,R.id.t5f7},{R.id.t5p8,R.id.t5f8}};
    private String hostel_name;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/getAllComplaints.php";

    public h_LatestThreadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {

        View view = inflater.inflate(R.layout.h_fragment_latest_thread, container, false);
        hostel_name = Utils.getprefString(UtilStrings.HOSTEl, getActivity());
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_latest_thread);
        swipeLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        Button bn_comments1=(Button)view.findViewById(R.id.bn_comment1);
        Button bn_comments2=(Button)view.findViewById(R.id.bn_comment2);
        Button bn_comments3=(Button)view.findViewById(R.id.bn_comment3);
        Button bn_comments4=(Button)view.findViewById(R.id.bn_comment4);
        Button bn_comments5=(Button)view.findViewById(R.id.bn_comment5);
        getAllComplaints();
        for(int i=0;i<6;i++){
            for(int j=0;j<2;j++){
                if(hwm.getT1details()[i][j]!=null){
                    TextView tvs=(TextView)(view.findViewById(t1ids[i][j]));
                    tvs.setText(hwm.getT1details()[i][j]);
                }
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                if(hwd.getT2details()[i][j]!=null){
                    TextView tvs=(TextView)(view.findViewById(t2ids[i][j]));
                    tvs.setText(hwd.getT2details()[i][j]);
                }
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                if(hw.getT3details()[i][j]!=null){
                    TextView tvs=(TextView)(view.findViewById(t3ids[i][j]));
                    tvs.setText(hw.getT3details()[i][j]);
                }
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                if(hpr.getT4details()[i][j]!=null){
                    TextView tvs=(TextView)(view.findViewById(t4ids[i][j]));
                    tvs.setText(hpr.getT4details()[i][j]);
                }
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                if(hpw.getT5details()[i][j]!=null){
                    TextView tvs=(TextView)(view.findViewById(t5ids[i][j]));
                    tvs.setText(hpw.getT5details()[i][j]);
                }
            }
        }


        return view;
    }


    public void getAllComplaints() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lanag", response);
                h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(response, getActivity());
                //ArrayList<Complaint> hComplaintArray = null;
                try {
                    hComplaintList = hJsonComplaintParser.pleasePleaseParseMyData();
                    hwm=hJsonComplaintParser.getH_wm();
                    hwd=hJsonComplaintParser.getH_wd();
                    hw=hJsonComplaintParser.getH_w();
                    hpr=hJsonComplaintParser.getH_pr();
                    hpw=hJsonComplaintParser.getH_pw();
                    //Log.d("lanag", "onResponse: "+hComplaintList.get(0).getName());


                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(R.id.main_content), "Error fetching the complaints", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();

                    //hComplaintArray = new ArrayList<>();
                    hComplaintList.add(Complaint.getErrorComplaintObject());

                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new h_ComplaintAdapter(hComplaintList, getActivity(), getContext(), false, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                }

                if (!hComplaintList.isEmpty()) {
                    Log.d("pip", "onResponse: " + hComplaintList.get(0).getName());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new h_ComplaintAdapter(hComplaintList, getActivity(), getContext(), true, (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                }
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

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_ComplaintAdapter(hComplaintList, getActivity(), getContext(), true, (CoordinatorLayout)getActivity().findViewById(R.id.main_content));
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
