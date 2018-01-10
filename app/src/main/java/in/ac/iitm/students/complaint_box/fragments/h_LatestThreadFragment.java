package in.ac.iitm.students.complaint_box.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.adapters.g_ComplaintAdapter;
import in.ac.iitm.students.complaint_box.adapters.h_LatThCompAdapter;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.complaint_box.objects.h_PinRoom;
import in.ac.iitm.students.complaint_box.objects.h_PinWing;
import in.ac.iitm.students.complaint_box.objects.h_WashingMachine;
import in.ac.iitm.students.complaint_box.objects.h_Washroom;
import in.ac.iitm.students.complaint_box.objects.h_WaterDispenser;
import in.ac.iitm.students.complaint_box.others.g_JSONComplaintParser;
import in.ac.iitm.students.complaint_box.others.h_JSONComplaintParser;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

//import in.ac.iitm.students.complaint_box.objects.h_Complaint;


public class h_LatestThreadFragment extends Fragment implements Updateable,SwipeRefreshLayout.OnRefreshListener {

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
    int t1dids[]={R.id.t1description1,R.id.t1description2,R.id.t1description3,R.id.t1description4,R.id.t1description5,R.id.t1description6};
    int t2dids[]={R.id.t2d1,R.id.t2d2,R.id.t2d3,R.id.t2d4,R.id.t2d5};
    int t3dids[]={R.id.t3d1,R.id.t3d2,R.id.t3d3,R.id.t3d4,R.id.t3d5,R.id.t3d6,R.id.t3d7,R.id.t3d8};
    int t4dids[]={R.id.t4d1,R.id.t4d2,R.id.t4d3,R.id.t4d4,R.id.t4d5};
    int t5dids[]={R.id.t5d1,R.id.t5d2,R.id.t5d3,R.id.t5d4,R.id.t5d5,R.id.t5d6,R.id.t5d7,R.id.t5d8};

    int ll1ids[] = {R.id.ll1_1, R.id.ll1_2, R.id.ll1_3, R.id.ll1_4, R.id.ll1_5, R.id.ll1_6};
    int ll2ids[] = {R.id.ll2_1, R.id.ll2_2, R.id.ll2_3, R.id.ll2_4, R.id.ll2_5};
    int ll3ids[] = {R.id.ll3_1, R.id.ll3_2, R.id.ll3_3, R.id.ll3_4, R.id.ll3_5, R.id.ll3_6, R.id.ll3_7, R.id.ll3_8};
    int ll4ids[] = {R.id.ll4_1, R.id.ll4_2, R.id.ll4_3, R.id.ll4_4, R.id.ll4_5};
    int ll5ids[] = {R.id.ll5_1, R.id.ll5_2, R.id.ll5_3, R.id.ll5_4, R.id.ll5_5, R.id.ll5_6, R.id.ll5_7, R.id.ll5_8};

    TextView tvst1[][]=new TextView[6][2];
    TextView tvst2[][]=new TextView[5][2];
    TextView tvst3[][]=new TextView[8][2];
    TextView tvst4[][]=new TextView[5][2];
    TextView tvst5[][]=new TextView[8][2];
    TextView tvst1d[]=new TextView[6];
    TextView tvst2d[]=new TextView[5];
    TextView tvst3d[]=new TextView[8];
    TextView tvst4d[]=new TextView[5];
    TextView tvst5d[]=new TextView[8];

    LinearLayout ll1[] = new LinearLayout[6];
    LinearLayout ll2[] = new LinearLayout[5];
    LinearLayout ll3[] = new LinearLayout[8];
    LinearLayout ll4[] = new LinearLayout[5];
    LinearLayout ll5[] = new LinearLayout[8];

    CardView t1;
    CardView t2;
    CardView t3;
    CardView t4;
    CardView t5;
    private ProgressDialog progressDialog;
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
                             Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.h_fragment_latest_thread, container, false);
        hostel_name = Utils.getprefString(UtilStrings.HOSTEl, getActivity());
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_latest_thread);
        swipeLayout.setOnRefreshListener(this);
        for(int i=0;i<6;i++){
            for(int j=0;j<2;j++){
                tvst1[i][j]=(TextView)(view.findViewById(t1ids[i][j]));
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                tvst2[i][j]=(TextView)(view.findViewById(t2ids[i][j]));
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                tvst3[i][j]=(TextView)(view.findViewById(t3ids[i][j]));
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<2;j++){
                tvst4[i][j]=(TextView)(view.findViewById(t4ids[i][j]));
            }
        }

        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                tvst5[i][j]=(TextView)(view.findViewById(t5ids[i][j]));
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<2;j++){
                tvst5[i][j]=(TextView)(view.findViewById(t5ids[i][j]));
            }
        }
        for(int i=0;i<6;i++){
            tvst1d[i]=(TextView)(view.findViewById(t1dids[i]));
            ll1[i] = (LinearLayout) (view.findViewById(ll1ids[i]));
        }
        for(int i=0;i<5;i++){
            tvst2d[i]=(TextView)(view.findViewById(t2dids[i]));
            ll2[i] = (LinearLayout) (view.findViewById(ll2ids[i]));
        }
        for(int i=0;i<8;i++){
            tvst3d[i]=(TextView)(view.findViewById(t3dids[i]));
            ll3[i] = (LinearLayout) (view.findViewById(ll3ids[i]));
        }
        for(int i=0;i<5;i++){
            tvst4d[i]=(TextView)(view.findViewById(t4dids[i]));
            ll4[i] = (LinearLayout) (view.findViewById(ll4ids[i]));
        }
        for(int i=0;i<8;i++){
            tvst5d[i]=(TextView)(view.findViewById(t5dids[i]));
            ll5[i] = (LinearLayout) (view.findViewById(ll5ids[i]));
        }

        t1=(CardView)view.findViewById(R.id.WashingMachine);
        t2=(CardView)view.findViewById(R.id.WaterDispenser);
        t3=(CardView)view.findViewById(R.id.Washroom);
        t4=(CardView)view.findViewById(R.id.PinRoom);
        t5=(CardView)view.findViewById(R.id.PinWing);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.latest_thread_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        Button bn_comments1=(Button)view.findViewById(R.id.bn_comment1);
        Button bn_comments2=(Button)view.findViewById(R.id.bn_comment2);
        Button bn_comments3=(Button)view.findViewById(R.id.bn_comment3);
        Button bn_comments4=(Button)view.findViewById(R.id.bn_comment4);
        Button bn_comments5=(Button)view.findViewById(R.id.bn_comment5);

        bn_comments1.setVisibility(View.INVISIBLE);
        bn_comments2.setVisibility(View.INVISIBLE);
        bn_comments3.setVisibility(View.INVISIBLE);
        bn_comments4.setVisibility(View.INVISIBLE);
        bn_comments5.setVisibility(View.INVISIBLE);

        HostelComplaintsActivity hostelComplaintsActivity = (HostelComplaintsActivity)getActivity();

//        getAllComplaints();
        //Log.d("dork",hwm.getT1details()[0][0]);
///        String frag_content = hostelComplaintsActivity.mGeneralString;
//        if(frag_content!=null && frag_content.length()>0){
//            update(frag_content);
//        }else {
        getAllComplaints();

        return view;
    }

    public void update(String string) {

        if(string !=null && string.length()>0){
            Log.d("searchResponse", string);
            g_JSONComplaintParser gJsonComplaintParser = new g_JSONComplaintParser(string, getActivity());
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
                Log.d("lanag", response);
                h_JSONComplaintParser hJsonComplaintParser = new h_JSONComplaintParser(response, getActivity());
                //ArrayList<Complaint> hComplaintArray = null;
                try {
                    hComplaintList = hJsonComplaintParser.pleasePleaseParseMyData();
                    hwm=hJsonComplaintParser.getH_wm();
                    hwd=hJsonComplaintParser.getH_wd();
                    Log.d("gucci_gang", "onResponse: " + hwd.getT2details()[0][0]);
                    hw=hJsonComplaintParser.getH_w();
                    hpr=hJsonComplaintParser.getH_pr();
                    hpw=hJsonComplaintParser.getH_pw();
                    //Log.d("lanag", "onResponse: "+hComplaintList.get(0).getName());

                    for(int i=0;i<6;i++){
                        for(int j=0;j<2;j++){
                            if(!hwm.getT1details()[i][j].isEmpty()){
                                tvst1[i][j].setText(hwm.getT1details()[i][j]);
                            }
                        }
                        if (hwm.getT1details()[i][0].isEmpty() && hwm.getT1details()[i][1].isEmpty()) {
                            tvst1d[i].setVisibility(View.GONE);
                            ll1[i].setVisibility(View.GONE);
                        }
                    }
                    if(tvst1d[0].getVisibility()==View.GONE&&tvst1d[1].getVisibility()==View.GONE&&tvst1d[2].getVisibility()==View.GONE&&tvst1d[3].getVisibility()==View.GONE&&tvst1d[4].getVisibility()==View.GONE&&tvst1d[5].getVisibility()==View.GONE)
                        t1.setVisibility(View.GONE);

                    for(int i=0;i<5;i++){
                        for(int j=0;j<2;j++){
                            if(!hwd.getT2details()[i][j].isEmpty()){
                                tvst2[i][j].setText(hwd.getT2details()[i][j]);
                            }
                        }
                        if (hwd.getT2details()[i][0].isEmpty() && hwd.getT2details()[i][1].isEmpty()) {
                            tvst2d[i].setVisibility(View.GONE);
                            ll2[i].setVisibility(View.GONE);
                        }
                    }
                    if(tvst2d[0].getVisibility()==View.GONE&&tvst2d[1].getVisibility()==View.GONE&&tvst2d[2].getVisibility()==View.GONE&&tvst2d[3].getVisibility()==View.GONE&&tvst2d[4].getVisibility()==View.GONE)
                        t2.setVisibility(View.GONE);
                    for(int i=0;i<8;i++){
                        for(int j=0;j<2;j++){
                            if(!hw.getT3details()[i][j].isEmpty()){
                                tvst3[i][j].setText(hw.getT3details()[i][j]);
                            }
                        }
                        if (hw.getT3details()[i][0].isEmpty() && hw.getT3details()[i][1].isEmpty()) {
                            tvst3d[i].setVisibility(View.GONE);
                            ll3[i].setVisibility(View.GONE);
                        }
                    }
                    if(tvst3d[0].getVisibility()==View.GONE&&tvst3d[1].getVisibility()==View.GONE&&tvst3d[2].getVisibility()==View.GONE&&tvst3d[3].getVisibility()==View.GONE&&tvst3d[4].getVisibility()==View.GONE&&tvst3d[5].getVisibility()==View.GONE&&tvst3d[6].getVisibility()==View.GONE&&tvst3d[7].getVisibility()==View.GONE)
                        t3.setVisibility(View.GONE);
                    for(int i=0;i<5;i++){
                        for(int j=0;j<2;j++){
                            if(!hpr.getT4details()[i][j].isEmpty()){
                                tvst4[i][j].setText(hpr.getT4details()[i][j]);
                                Log.d("dork",hpr.getT4details()[0][0]);
                            }
                        }
                        if (hpr.getT4details()[i][0].isEmpty() && hpr.getT4details()[i][1].isEmpty()) {
                            ll4[i].setVisibility(View.GONE);
                            tvst4d[i].setVisibility(View.GONE);
                        }

                    }
                    if(tvst4d[0].getVisibility()==View.GONE&&tvst4d[1].getVisibility()==View.GONE&&tvst4d[2].getVisibility()==View.GONE&&tvst4d[3].getVisibility()==View.GONE&&tvst4d[4].getVisibility()==View.GONE)
                        t4.setVisibility(View.GONE);
                    for(int i=0;i<8;i++){
                        for(int j=0;j<2;j++){
                            if(!hpw.getT5details()[i][j].isEmpty()){
                                tvst5[i][j].setText(hpw.getT5details()[i][j]);
                            }
                        }
                        if (hpw.getT5details()[i][0].isEmpty() && hpw.getT5details()[i][1].isEmpty()) {
                            tvst5d[i].setVisibility(View.GONE);
                            ll5[i].setVisibility(View.GONE);
                        }
                    }
                    if(tvst5d[0].getVisibility()==View.GONE&&tvst5d[1].getVisibility()==View.GONE&&tvst5d[2].getVisibility()==View.GONE&&tvst5d[3].getVisibility()==View.GONE&&tvst5d[4].getVisibility()==View.GONE&&tvst5d[5].getVisibility()==View.GONE&&tvst5d[6].getVisibility()==View.GONE&&tvst5d[7].getVisibility()==View.GONE)
                        t5.setVisibility(View.GONE);
                    progressDialog.dismiss();





                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(R.id.main_content), "Error fetching the complaints", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();

                    //hComplaintArray = new ArrayList<>();
                    hComplaintList.add(Complaint.getHostelErrorComplaintObject());

                    progressDialog.dismiss();
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new h_LatThCompAdapter(hComplaintList, getActivity(), getContext(), (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                }

                if (!hComplaintList.isEmpty()) {
                    Log.d("pip", "onResponse: " + hComplaintList.get(0).getName());
                    progressDialog.dismiss();
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new h_LatThCompAdapter(hComplaintList, getActivity(), getContext(), (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setNestedScrollingEnabled(false);
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
                hComplaintArray.add(Complaint.getHostelErrorComplaintObject());

                progressDialog.dismiss();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new h_LatThCompAdapter(hComplaintArray, getActivity(), getContext(), (CoordinatorLayout) getActivity().findViewById(R.id.main_content));
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
