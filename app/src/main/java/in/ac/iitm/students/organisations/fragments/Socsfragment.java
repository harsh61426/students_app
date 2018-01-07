package in.ac.iitm.students.organisations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.organisations.activities.main.PostActivity;



public class Socsfragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public PostActivity pactivity;
    //public ArrayList<News> List;
    //public NewsAdapter adapter;
    ViewPager viewPager;
    RecyclerView mRecyclerView;


    public Socsfragment() {
        // Required empty public constructor
    }

   /* public void setResponse(ArrayList<News> NewsList, ViewPager viewPager) {
        this.viewPager = viewPager;
        //this.NewsList = NewsList;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_socsfragment, container, false);

        pactivity = (PostActivity) getActivity();


        TextView tv_head = (TextView)view.findViewById(R.id.tv_heading);
        TextView tv_message = (TextView)view.findViewById(R.id.tv_message);

        tv_head.setText(PostActivity.Pagename+" Coming Soon");
        tv_message.setText(PostActivity.Pagedes);
        tv_message.setMovementMethod(new ScrollingMovementMethod());

        /*mRecyclerView = (RecyclerView)view. findViewById(R.id.recyclerfifthsetate);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(getContext(),NewsList);
        mRecyclerView.setAdapter(adapter);*/



        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

    }

    @Override
    public void onRefresh() {

    }
    public void refreshList() {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, 3000);

    }
}
