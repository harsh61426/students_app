package in.ac.iitm.students.organisations.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.organisations.activities.main.PostActivity;
import in.ac.iitm.students.organisations.adapters.PostApapter;
import in.ac.iitm.students.organisations.object_items.Posts;

/**
 * Created by rohithram on 13/7/17.
 */

public class Fbfragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public PostActivity pactivity;
    public ArrayList<Posts> postList;
    public PostApapter adapter;
    ViewPager viewPager;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;




    public Fbfragment() {
        // Required empty public constructor
    }

    public void setResponse(ArrayList<Posts> postList, ViewPager viewPager) {
        this.postList = postList;
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fb_fragment
                , container, false);

        pactivity = (PostActivity) getActivity();

        View v1 = view.findViewById(R.id.rl_fb);
        recyclerView = (RecyclerView)v1.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = (SwipeRefreshLayout) v1.findViewById(R.id.swipe_fb1);
        swipeRefreshLayout.setOnRefreshListener(Fbfragment.this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        adapter = new PostApapter(getActivity(),postList,pactivity.key,pactivity.Pagename,pactivity.logo_url,pactivity.fragmentManager,pactivity.fragment,pactivity.layout_MainMenu,pactivity.pd,pactivity.reactions_popup,pactivity.layout,pactivity.multipopup,pactivity.layout1);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter =  new PostApapter(getActivity(),postList,pactivity.key,pactivity.Pagename,pactivity.logo_url,pactivity.fragmentManager,pactivity.fragment,pactivity.layout_MainMenu,pactivity.pd,pactivity.reactions_popup,pactivity.layout,pactivity.multipopup,pactivity.layout1);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
