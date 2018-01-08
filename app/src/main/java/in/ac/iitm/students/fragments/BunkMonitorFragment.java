package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.adapters.BunksAdapter;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class BunkMonitorFragment extends Fragment {

    GridView gridView;
    BunksAdapter bunksAdapter;
    ArrayList<Bunks> bunks;
    Dialog dialog;
    //TODO: Add reminders for slots

    public BunkMonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bunk_monitor, container, false);

        bunks = new ArrayList<>();

        getcourses();

        bunksAdapter = new BunksAdapter(getActivity(), bunks);
        gridView = (GridView)view.findViewById(R.id.bunk_list);

        gridView.setAdapter(bunksAdapter);
        gridView.setNumColumns((getActivity().getResources().getConfiguration().orientation
                ==Configuration.ORIENTATION_PORTRAIT)?2:3);  //3 if landscape

        final int size = gridView.getChildCount();
        final int firstVisiblePosition = gridView.getFirstVisiblePosition();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                initdialog(position);
                //dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

            }
        });
        return view;
    }
    private void initdialog(int pos){
        dialog = new Dialog(getActivity());
        final int position=pos;
        dialog.setContentView(R.layout.dialog_add_course);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button plus = (Button) dialog.findViewById(R.id.add);
        Button minus = (Button) dialog.findViewById(R.id.remove);
        minus.setVisibility(View.VISIBLE);
        //LinearLayout parent_layout=(LinearLayout) dialog.findViewById(R.id.dialog_layout);
        //LinearLayout button_layout=(LinearLayout) dialog.findViewById(R.id.button_layout);
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)(button_layout.getLayoutParams());
        //params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom

        //button_layout.setLayoutParams(params);

        //LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //buttonLayoutParams.setMargins(1, 1, 1, 1);

        plus.setText("+1");
        minus.setText("-1");

        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        slot.setVisibility(View.GONE);
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        courseid.setVisibility(View.GONE);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,getActivity())+1,getActivity());
                bunks.get(position).setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,getActivity()));
                //((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
                //Utils.saveprefInt(,Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()),getActivity());
                bunksAdapter = new BunksAdapter(getActivity(), bunks);
                gridView.setAdapter(bunksAdapter);
                dialog.dismiss();

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,getActivity())==0){
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.saveprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,getActivity())-1,getActivity());
                bunks.get(position).setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,getActivity()));
                //((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
                //Utils.saveprefInt(,Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()),getActivity());
                bunksAdapter = new BunksAdapter(getActivity(), bunks);
                gridView.setAdapter(bunksAdapter);
                dialog.dismiss();
            }
        });
    }


    public void getcourses()
    {
        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Bunks course = new Bunks();
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            course.setBunk_tot(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,getActivity()));
            course.setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()));
            course.setFlag1(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_FLAG,getActivity()));
            if(course.getBunk_tot()>0)bunks.add(course);
        }
    }


}
