package in.ac.iitm.students.organisations.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.News;
import in.ac.iitm.students.organisations.activities.main.PostActivity;
import in.ac.iitm.students.organisations.adapters.NewsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Socsfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Socsfragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Socsfragment extends Fragment {

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

        /*mRecyclerView = (RecyclerView)view. findViewById(R.id.recyclerfifthsetate);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(getContext(),NewsList);
        mRecyclerView.setAdapter(adapter);*/

        Drawable dialog_icon;
        dialog_icon = ContextCompat.getDrawable(getActivity(), R.drawable.app_logo);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(dialog_icon);
        builder.setTitle(PostActivity.Pagename+" Coming Soon!");
        builder.setMessage(PostActivity.Pagedes)
                .setNeutralButton(R.string.dismiss_home_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
    }

}
