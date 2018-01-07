package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.adapters.FreshieCourseAdapter;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class FreshieCourseTimetableFragment extends Fragment {

    // adapter for the recyclerView in the edit courses page
    FreshieCourseAdapter courseAdapter;
    ArrayList<Course> courses;
    Dialog dialog;
    boolean check[]={false, false,false,false,false,false,false};
    //int prime[][]={{2, 3, 5, 7, 11, 13, 17, 19},{23, 29, 31, 37, 41, 43, 47, 53},{59, 61, 67, 71, 73, 79, 83, 89},{97, 101, 103, 107, 109, 113, 127, 131},{137, 139, 149, 151, 157, 163, 167, 173}};
    int prime[][]={{2, 3, 5, 7, 11, 13, 17, 19,23,29},{ 31, 37, 41, 43, 47, 53,59,61,67,71},{ 73, 79, 83, 89,97,101,103,107,109,113},{ 127, 131,137,139,149,151,157,163,167,173},{179,181,191,193,197,199,211,223,227,229}};
    // ids representing each cell in the grid in timetable.
    // The initial character is for the day and the integer is for the slot sequence
    int ids[][] = {{R.id.m1,R.id.m2,R.id.m3,R.id.m4,R.id.m5,R.id.m6,R.id.m7,R.id.m8,R.id.m9,R.id.m10},
            {R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10},
            {R.id.w1,R.id.w2,R.id.w3,R.id.w4,R.id.w5,R.id.w6,R.id.w7,R.id.w8,R.id.w9,R.id.w10},
            {R.id.h1,R.id.h2,R.id.h3,R.id.h4,R.id.h5,R.id.h6,R.id.h7,R.id.h8,R.id.h9,R.id.h10},
            {R.id.f1,R.id.f2,R.id.f3,R.id.f4,R.id.f5,R.id.f6,R.id.f7,R.id.f8,R.id.f9,R.id.f10}};
    int texids[] = {R.id.mex1,R.id.tex1,R.id.wex1,R.id.hex1,R.id.fex1,R.id.mex2,R.id.tex2,R.id.wex2,R.id.hex2,R.id.fex2};
    // The timetable is basically a grid of textViews
    TextView tvs[][] = new TextView[5][10];

    // grid for corresponding slots
    char slots[][] = new char[5][10];
    // grid for corresponding bunks
    boolean bunk[][] = new boolean[5][10];
    HashMap<Character,String> coursemap;
    ArrayList<Bunks> bunks;
    View view;
    // viewFlipper to flip between the timetable and the edit courses page
    ViewFlipper flipper;
    boolean flag[][]=new boolean[5][10];

    public FreshieCourseTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_course_timetable_freshie, container, false);


        // declaring the top row, ie the row with day names
        for(int i = 0;i<10;i++)
        {
            TextView textView = (TextView)view.findViewById(texids[i]);
            textView.setVisibility(View.INVISIBLE);
        }

        if(!Utils.getprefBool("OldLaunch_TT",getActivity()))
        {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_instructions_timetable);
            dialog.show();
            Utils.saveprefBool("OldLaunch_TT",true,getActivity());
        }

        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        flipper.setDisplayedChild(Utils.getprefInt("TT_Screen", getActivity()));

        // courses is an arrayList of Course object
        courses = new ArrayList<>();

        // gets all the courses from preferences.
        // Initialises Course objects, which contains all the course related information, except the bunks tally.
        getcourses();

        // edit course page code starts here
        courseAdapter = new FreshieCourseAdapter(getActivity(), courses);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(courseAdapter);

        Button add = (Button) view.findViewById(R.id.add);
        Button done = (Button) view.findViewById(R.id.done);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdialog();
                dialog.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcourses();
                Utils.saveprefInt("TT_Screen",1,getActivity());
                getActivity().recreate();
            }
        });

        // ends

        // bunks is an arrayList of Bunk object
        bunks = new ArrayList<>();
        // hashMap <Character, String>
        coursemap = new HashMap<>();

        // setting background color for the cells in the grid according to whether it is bunked or not.
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<10;j++)
            {
                slots[i][j] = 'X';
                bunk[i][j] = Utils.getprefBool("state"+10*i+j,getActivity());
                tvs[i][j] = (TextView)view.findViewById(ids[i][j]);
                if(bunk[i][j])
                {
                    //tvs[i][j].setBackgroundResource(R.drawable.back);
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));

                    //tvs[i][j].setBackgroundResource(R.drawable.cellborder);
                }
                else
                {
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    //tvs[i][j].setBackgroundResource(R.drawable.back);
                    //tvs[i][j].setBackgroundResource(R.drawable.cellborder);
                }
            }
        }

        // Bunks class extends the course class which implies that to completely initialize a Bunks object you have to
        // initialize all the variables in the Course class along with the Bunks class.
        // getBunks() initializes the bunks arrayList from prefs
        getbunks();
        // hashMap for slot (char) and course id (string)
        getcoursemap();
        for (Bunks c : bunks) {
            mapslots(c.getSlot(), c.getDays(),c.getFlag1());
            //.d("map","Each time this comes, a slot is mapped");
        }

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        // This gets the current week and compares it with the value stored in prefs.
        // If it does not match then it removes the red colour from slots and updates the bunks tally.
        if(week>Utils.getprefInt("LastLogTT",getActivity()))
        {
            clearbunks();
            // Todo test
        }
        Utils.saveprefInt("LastLogTT",week,getActivity());


        for(int i=0;i<5;i++)
        {
            for(int j=0;j<10;j++)
            {
                final int x = i;
                final int y = j;
                // initializing the textViews
                tvs[i][j].setText(Character.toString(slots[i][j])+'\n'+coursemap.get(slots[i][j]));
                // The cells in the grid to which slots are not assigned are 'X' by default
                if(slots[i][j]=='X')
                {
                    // there are 3 colors in the timetable- red indicating bunked,
                    // white indicating the presence of a slot in that hour and transparent implying free hour.
                    //tvs[i][j].setVisibility(View.INVISIBLE);
                    //tvs[i][j].setBackgroundResource(R.drawable.back);
                    tvs[i][j].setAlpha(0.0f);


                }
                //tvs[i][j].setLongClickable(true);

                // setting listeners for the textViews (in the grid)
                tvs[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //fix bug of lab slots
                        if(slots[x][y]!='X') {

                            if (bunk[x][y]) {
                                tvs[x][y].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                                bunk[x][y] = false;
                                //tvs[x][y].setAlpha(0.0f);
                                Utils.saveprefBool("state" + 10 * x + y, false, getActivity());
                                updatebunks(x, y, false);
                            } else {

                                tvs[x][y].setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightRed));
                                bunk[x][y] = true;
                                //tvs[x][y].setAlpha(0.0f);
                                Utils.saveprefBool("state" + 10 * x + y, true, getActivity());
                                updatebunks(x, y, true);
                            }
                        }
                        return true;

                    }

                });


                tvs[i][j].setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        //fix this for lab slots

                        TextView edit = (TextView) (v);
                        for (int m = 0; m < 5; m++) {
                            for (int n = 0; n < 10; n++) {
                                if (flag[m][n])
                                    if (m != x || n != y) {
                                        flag[m][n] = false;
                                        tvs[m][n].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        tvs[m][n].setAlpha(1.0f);
                                        if(tvs[m][n].getText().charAt(0)=='X')
                                            tvs[m][n].setAlpha(0.0f);
                                    }
                            }
                        }

                        if(!flag[x][y]) {
                            edit.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.pencil, 0);
                            v.setAlpha(0.5f);
                            flag[x][y] = true;

                            return;
                        }
                        if(flag[x][y]){
                            if(slots[x][y]>='P'&&slots[x][y]<='T'){
                                Snackbar snackbar = Snackbar
                                        .make(view, "Please edit this slot from Edit Courses menu on top right corner", Snackbar.LENGTH_LONG);

                                snackbar.show();
                                flag[x][y] = false;
                                tvs[x][y].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                tvs[x][y].setAlpha(1.0f);
                            }
                            else {

                                initdialog_edit(tvs[x][y].getText().toString().charAt(0), tvs[x][y].getText().toString().substring(2), x, y);
                                dialog.show();

                                flag[x][y] = false;

                                tvs[x][y].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                tvs[x][y].setAlpha(1.0f);
                                if (tvs[x][y].getText().charAt(0) == 'X')
                                    tvs[x][y].setAlpha(0.0f);
                                //^ for making the empty views transparent
                            }


                        }
                    }
                });
            }
        }
        return view;
    }

    
    private void getcourses()
    {
        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Course course = new Course();
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            course.setFlag1((Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_FLAG,getActivity())));
            courses.add(course);
        }
    }

    private void clearbunks()   //clear bunks at end of each week
    {
        for(int x=0;x<5;x++)
        {
            for(int y=0;y<10;y++)
            {
                Utils.saveprefBool("state"+10*x+y,false,getActivity());
            }
        }
    }

    private void updatebunks(int x, int y, boolean add)
    {
        int pos = -1;
        for(int i=0;i<bunks.size();i++)
        {
            if(bunks.get(i).getSlot()==slots[x][y])
            {
                pos = i;
                break;
            }
        }
        Bunks b = bunks.get(pos);
        int bunks_d = add?(b.getBunk_done()+1):(b.getBunk_done()-1);
        b.setBunk_done(bunks_d>=0?bunks_d:0);

        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos+UtilStrings.BUNKS_DONE,(bunks_d>=0?bunks_d:0),getActivity());
        bunks.set(pos,b);
        ((TimetableActivity) getActivity()).freshiereturnadapter().notifyDataSetChanged();
    }

    private void getcoursemap()
    {
        for(Bunks c:bunks)
        {
            coursemap.put(c.getSlot(),c.getCourse_id());
        }
    }

    //use this for an example of getting preferences
    private void getbunks()
    {

        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Bunks course = new Bunks();
            // Bunk class extends the Course class
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            course.setFlag1(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_FLAG,getActivity()));
            course.setBunk_tot(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,getActivity()));
            course.setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()));
            bunks.add(course);
        }
    }

    private void mapslots(char c, int days,int f)
    {
        switch(c)
        {
            case 'A': {


                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'A';
                        if(slots[i][j]=='A'&&days%prime[i][j]!=0)
                            slots[i][j]='X';

                    }
                /*slots[0][0] = (days%2 == 0) ? 'A' : 'X';
                slots[1][4] = (days%3 == 0) ? 'A' : 'X';
                slots[3][3] = (days%5 == 0) ? 'A' : 'X';
                slots[4][2] = (days%7 == 0) ? 'A' : 'X';*/
                break;
            }
            case 'B': {

                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'B';
                        if (slots[i][j] == 'B' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }
                /*slots[0][1] = (days%2 == 0) ? 'B' : 'X';
                slots[1][0] = (days%3 == 0) ? 'B' : 'X';
                slots[2][4] = (days%5 == 0) ? 'B' : 'X';
                slots[4][3] = (days%7 == 0) ? 'B' : 'X';*/
                break;
            }
            case 'C': {
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'C';
                        if (slots[i][j] == 'C' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }

                /*slots[0][2] = (days%2 == 0) ? 'C' : 'X';
                slots[1][1] = (days%3 == 0) ? 'C' : 'X';
                slots[2][0] = (days%5 == 0) ? 'C' : 'X';
                slots[4][4] = (days%7 == 0) ? 'C' : 'X';*/
                break;
            }
            case 'D': {
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'D';
                        if (slots[i][j] == 'D' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }
                /*slots[0][3] = (days%2 == 0) ? 'D' : 'X';
                slots[1][2] = (days%3 == 0) ? 'D' : 'X';
                slots[2][1] = (days%5 == 0) ? 'D' : 'X';
                slots[3][4] = (days%7 == 0) ? 'D' : 'X';*/
                break;
            }
            case 'E': {
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'E';
                        if (slots[i][j] == 'E' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }

                /*slots[1][3] = (days%2 == 0) ? 'E' : 'X';
                slots[2][2] = (days%3 == 0) ? 'E' : 'X';
                slots[3][0] = (days%5 == 0) ? 'E' : 'X';
                slots[4][7] = (days%7 == 0) ? 'E' : 'X';*/
                break;
            }
            case 'F': {
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'F';
                        if (slots[i][j] == 'F' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }
                /*slots[1][7] = (days%2 == 0) ? 'F' : 'X';
                slots[2][3] = (days%3 == 0) ? 'F' : 'X';
                slots[3][1] = (days%5 == 0) ? 'F' : 'X';
                slots[4][0] = (days%7 == 0) ? 'F' : 'X';*/
                break;
            }
            case 'G': {
                for(int i=0;i<5;i++)
                    for(int j=0;j<10;j++) {
                        if (days % prime[i][j] == 0)
                            slots[i][j] = 'G';
                        if (slots[i][j] == 'G' && days % prime[i][j] != 0)
                            slots[i][j] = 'X';
                    }
                /*slots[0][4] = (days%2 == 0) ? 'G' : 'X';
                slots[2][7] = (days%3 == 0) ? 'G' : 'X';
                slots[3][2] = (days%5 == 0) ? 'G' : 'X';
                slots[4][1] = (days%7 == 0) ? 'G' : 'X';*/
                break;
            }

            case 'P': {
                /*slots[0][6] = 'P';
                ids[0][6] = R.id.mex;
                tvs[0][6] = (TextView)view.findViewById(ids[0][5]);
                tvs[0][6].setVisibility(View.VISIBLE);
                if(bunk[0][6])
                {
                    tvs[0][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[0][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;*/
                if(f==0){
                    slots[0][1] = 'P';
                    ids[0][1] = R.id.mex1;
                    tvs[0][1].setText("");
                    if (tvs[0][1] != view.findViewById(ids[0][1])) tvs[0][1].setAlpha(0.0f);
                    tvs[0][1] = (TextView)view.findViewById(ids[0][1]);
                    tvs[0][1].setVisibility(View.VISIBLE);
                    if(!bunk[0][1])
                        tvs[0][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[0][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));

                }
                if(f==1){
                    slots[0][6] = 'P';
                    ids[0][6] = R.id.mex2;
                    if (tvs[0][6] != view.findViewById(ids[0][6])) tvs[0][6].setAlpha(0.0f);
                    tvs[0][6] = (TextView)view.findViewById(ids[0][6]);
                    tvs[0][6].setVisibility(View.VISIBLE);
                    if(!bunk[0][6])
                        tvs[0][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[0][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }

                break;
            }
            case 'Q': {
                if(f==0){
                    slots[1][1] = 'Q';
                    ids[1][1] = R.id.tex1;
                    if (tvs[1][1] != view.findViewById(ids[1][1])) tvs[1][1].setAlpha(0.0f);
                    tvs[1][1] = (TextView)view.findViewById(ids[1][1]);
                    tvs[1][1].setVisibility(View.VISIBLE);
                    if(!bunk[1][1])
                        tvs[1][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[1][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                if(f==1){
                    slots[1][6] = 'Q';
                    ids[1][6] = R.id.mex2;
                    if (tvs[1][6] != view.findViewById(ids[1][6])) tvs[1][6].setAlpha(0.0f);
                    tvs[1][6] = (TextView)view.findViewById(ids[1][6]);
                    tvs[1][6].setVisibility(View.VISIBLE);
                    if(!bunk[1][6])
                        tvs[1][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[1][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }break;
            }
            case 'R': {
                if(f==0){
                    slots[2][1] = 'R';
                    ids[2][1] = R.id.wex1;
                    if (tvs[2][1] != view.findViewById(ids[2][1])) tvs[2][1].setAlpha(0.0f);
                    tvs[2][1] = (TextView)view.findViewById(ids[0][1]);
                    tvs[2][1].setVisibility(View.VISIBLE);
                    if(!bunk[2][1])
                        tvs[2][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[2][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                if(f==1){
                    slots[2][6] = 'R';
                    ids[2][6] = R.id.wex2;
                    if (tvs[2][6] != view.findViewById(ids[2][6])) tvs[2][6].setAlpha(0.0f);
                    tvs[2][6] = (TextView)view.findViewById(ids[2][6]);
                    tvs[2][6].setVisibility(View.VISIBLE);
                    if(!bunk[2][6])
                        tvs[2][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[2][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }break;
            }
            case 'S': {
                if(f==0){
                    slots[3][1] = 'S';
                    ids[3][1] = R.id.hex1;
                    if (tvs[3][1] != view.findViewById(ids[3][1])) tvs[3][1].setAlpha(0.0f);
                    tvs[3][1] = (TextView)view.findViewById(ids[3][1]);
                    tvs[3][1].setVisibility(View.VISIBLE);
                    if(!bunk[3][1])
                        tvs[3][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[3][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                if(f==1){
                    slots[3][6] = 'S';
                    ids[3][6] = R.id.hex2;
                    if (tvs[3][6] != view.findViewById(ids[3][6])) tvs[3][6].setAlpha(0.0f);
                    tvs[3][6] = (TextView)view.findViewById(ids[3][6]);
                    tvs[3][6].setVisibility(View.VISIBLE);
                    if(!bunk[3][6])
                        tvs[3][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[3][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                break;
            }
            case 'T': {
                if(f==0){
                    slots[4][1] = 'T';
                    ids[4][1] = R.id.fex1;
                    if (tvs[4][1] != view.findViewById(ids[4][1])) tvs[4][1].setAlpha(0.0f);
                    tvs[4][1] = (TextView)view.findViewById(ids[4][1]);
                    tvs[4][1].setVisibility(View.VISIBLE);
                    if(!bunk[4][1])
                        tvs[4][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[4][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                if(f==1){
                    slots[4][6] = 'T';
                    ids[4][6] = R.id.fex2;
                    if (tvs[4][6] != view.findViewById(ids[4][6])) tvs[4][6].setAlpha(0.0f);
                    tvs[4][6] = (TextView)view.findViewById(ids[4][6]);
                    tvs[4][6].setVisibility(View.VISIBLE);
                    if(!bunk[4][6])
                        tvs[4][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                    else
                        tvs[4][6].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }break;
            }

            default:
                break;
        }
    }

    private void addcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,courses.size(),getActivity());
        int i=0;
        for(Course c: courses)
        {
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,c.getDays(),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_FLAG,c.getFlag1(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,c.getCourse_id(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,Character.toString(c.getSlot()),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,c.getSlot()>='P'&&c.getSlot()<='T'?2:getbunks(c.getDays()),getActivity());

            i++;
        }
    }

    //fix this
    private int getbunks(int number)
    {
        int bunks = 0;
        for(int i=0;i<5;i++)
            for(int j=0;j<10;j++)
                if(number%prime[i][j]==0)
                    bunks++;
        /*bunks+=((number%2==0)?1:0);
        bunks+=((number%3==0)?1:0);
        bunks+=((number%5==0)?1:0);
        bunks+=((number%7==0)?1:0);

        return 2*(bunks>0?bunks:1);*/
        return 2*bunks;
    }

    private void removeallcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,0,getActivity());
    }


    private void initdialog()
    {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_course_freshie);
        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        final LinearLayout days = (LinearLayout) dialog.findViewById(R.id.days);
        Button add = (Button) dialog.findViewById(R.id.add);
        final Course course = new Course();
        course.setDays(1);
        course.setSlot(' ');
        course.setCourse_id("");
        final CheckBox c1 = (CheckBox) dialog.findViewById(R.id.day1);
        final CheckBox c2 = (CheckBox) dialog.findViewById(R.id.day2);
        final CheckBox c3 = (CheckBox) dialog.findViewById(R.id.day3);
        final CheckBox c4 = (CheckBox) dialog.findViewById(R.id.day4);
        final CheckBox c5 = (CheckBox) dialog.findViewById(R.id.day5);
        final CheckBox c6 = (CheckBox) dialog.findViewById(R.id.day6);
        final CheckBox c7 = (CheckBox) dialog.findViewById(R.id.day7);
        slot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    char sl = s.charAt(0);
                    sl = Character.toUpperCase(sl);
                    switch(sl)
                    {
                        case 'A': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("Th FN");
                            c4.setText("Th AN");
                            c5.setText("F");
                            break;
                        }
                        case 'B': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            c3.setText("Tue");
                            c4.setText("W");
                            c5.setText("F");
                            break;
                        }
                        case 'C': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.VISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            c3.setText("Tue FN");
                            c4.setText("Tue AN");
                            c5.setText("W");
                            c6.setText("F");
                            break;
                        }
                        case 'D': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.VISIBLE);
                            c7.setVisibility(View.VISIBLE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            c3.setText("T FN");
                            c4.setText("T AN");
                            c5.setText("W FN");
                            c6.setText("W AN");
                            c7.setText("Th");
                            break;
                        }
                        case 'E': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.VISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("M");
                            c2.setText("T FN");
                            c3.setText("T AN");
                            c4.setText("W FN");
                            c5.setText("W AN");
                            c6.setText("Th");
                            break;
                        }
                        case 'F': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("W FN");
                            c2.setText("W AN");
                            c3.setText("Th FN");
                            c4.setText("Th AN");
                            c5.setText("F");
                            break;
                        }
                        case 'G': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("Th FN");
                            c2.setText("Th AN");
                            c3.setText("F");
                            break;
                        }

                        case 'P': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            break;
                        }
                        case 'Q': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("T FN");
                            c2.setText("T AN");
                            break;
                        }
                        case 'R': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("W FN");
                            c2.setText("W AN");
                            break;
                        }
                        case 'S': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("Th FN");
                            c2.setText("Th AN");
                            break;
                        }
                        case 'T': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                            c1.setText("F FN");
                            c2.setText("F AN");
                            break;
                        }
                        default: {
                            slot.setError("Invalid slot");
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            c5.setVisibility(View.INVISIBLE);
                            c6.setVisibility(View.INVISIBLE);
                            c7.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*2);
//course.setDays(course.getDays()/2);
                check[0] = isChecked;
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*3);
//course.setDays(course.getDays()/3);
                check[1] = isChecked;
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*5);
//course.setDays(course.getDays()/5);
                check[2] = isChecked;
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*7);
//course.setDays(course.getDays()/7);
                check[3] = isChecked;
            }
        });
        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*7);
//course.setDays(course.getDays()/7);
                check[4] = isChecked;
            }
        });
        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*7);
//course.setDays(course.getDays()/7);
                check[5] = isChecked;
            }
        });
        c7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //course.setDays(course.getDays()*7);
//course.setDays(course.getDays()/7);
                check[6] = isChecked;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slot.getText().toString().isEmpty())
                {
                    slot.setError("Please enter the slot");
                    return;
                }
                if(courseid.getText().toString().isEmpty())
                {
                    courseid.setError("Please enter the Course ID");
                    return;
                }
                else {
                    String coursed = courseid.getText().toString().toUpperCase();
                    char slt = Character.toUpperCase(slot.getText().charAt(0));
                    boolean flag = true;
                    if (coursed.length() != 6) {
                        courseid.setError("Course ID is incorrect");
                        flag = false;
                    } else if (!(Character.isLetter(coursed.charAt(0)) && Character.isLetter(coursed.charAt(1))
                            && Character.isDigit(coursed.charAt(2)) && Character.isDigit(coursed.charAt(3))
                            && Character.isDigit(coursed.charAt(4)) && Character.isDigit(coursed.charAt(5)))) {
                        courseid.setError("Incorrect ID format");
                        flag = false;
                    }
                    if (!(slt - 'A' >= 0 && slt - 'A' < 22 && slt != 'I')) {
                        slot.setError("Invalid slot");
                        flag = false;
                    }
                    if (clash(slt)) {
                        slot.setError("There is a slot clash");
                        flag = false;
                    }
                    for(int i=0;i<check.length;i++){
                        if(check[i]) {
                            switch(slt){
                                case 'A':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[0][0]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[1][5]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[3][3]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[3][8]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[4][2]);
                                            break;
                                    }
                                    break;
                                case 'B':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[0][1]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[0][6]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[1][0]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[2][5]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[4][3]);
                                            break;
                                    }
                                    break;
                                case 'C':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[0][2]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[0][7]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[1][1]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[1][6]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[2][0]);
                                            break;
                                        case 5:
                                            course.setDays(course.getDays()*prime[4][5]);
                                            break;
                                    }
                                    break;
                                case 'D':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[0][3]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[0][8]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[1][2]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[1][7]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[2][1]);
                                            break;
                                        case 5:
                                            course.setDays(course.getDays()*prime[2][6]);
                                            break;
                                        case 6:
                                            course.setDays(course.getDays()*prime[3][5]);
                                            break;
                                    }
                                    break;
                                case 'E':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[0][5]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[1][3]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[1][8]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[2][2]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[2][7]);
                                            break;
                                        case 5:
                                            course.setDays(course.getDays()*prime[3][0]);
                                            break;
                                    }
                                    break;
                                case 'F':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[2][3]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[2][8]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[3][1]);
                                            break;
                                        case 3:
                                            course.setDays(course.getDays()*prime[3][6]);
                                            break;
                                        case 4:
                                            course.setDays(course.getDays()*prime[4][0]);
                                            break;
                                    }
                                    break;
                                case 'G':
                                    switch(i){
                                        case 0:
                                            course.setDays(course.getDays()*prime[3][2]);
                                            break;
                                        case 1:
                                            course.setDays(course.getDays()*prime[3][7]);
                                            break;
                                        case 2:
                                            course.setDays(course.getDays()*prime[4][1]);
                                            break;
                                    }
                                    break;
                                case 'P':
                                    switch(i){
                                        case 0:
                                            //course.setDays(course.getDays()*prime[0][1]);
                                            /*slots[0][1] = 'P';
                                            ids[0][1] = R.id.mex1;
                                            tvs[0][1] = (TextView)view.findViewById(ids[0][1]);
                                            tvs[0][1].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(0);

                                            break;
                                        case 1:
                                            //course.setDays(course.getDays()*prime[0][6]);
                                            /*slots[0][6] = 'P';
                                            ids[0][6] = R.id.mex2;
                                            tvs[0][6] = (TextView)view.findViewById(ids[0][6]);
                                            tvs[0][6].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(1);

                                            break;
                                    }
                                    break;
                                case 'Q':
                                    switch(i){
                                        case 0:
                                            /*slots[1][1] = 'Q';
                                            ids[1][1] = R.id.tex1;
                                            tvs[1][1] = (TextView)view.findViewById(ids[1][1]);
                                            tvs[1][1].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(0);
                                            break;
                                        case 1:
                                            /*slots[1][6] = 'Q';
                                            ids[1][6] = R.id.tex2;
                                            tvs[1][6] = (TextView)view.findViewById(ids[1][6]);
                                            tvs[1][6].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(1);
                                            break;
                                    }
                                    break;

                                case 'R':
                                    switch(i){
                                        case 0:
                                            /*slots[2][1] = 'R';
                                            ids[2][1] = R.id.wex1;
                                            tvs[2][1] = (TextView)view.findViewById(ids[2][1]);
                                            tvs[2][1].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(0);
                                            break;
                                        case 1:
                                            /*slots[2][6] = 'R';
                                            ids[2][6] = R.id.wex2;
                                            tvs[2][6] = (TextView)view.findViewById(ids[2][6]);
                                            tvs[2][6].setVisibility(View.VISIBLE);
                                            break;*/

                                            course.setFlag1(1);
                                            break;
                                    }
                                    break;

                                case 'S':
                                    switch(i){
                                        case 0:
                                            /*slots[3][1] = 'S';
                                            ids[3][1] = R.id.hex1;
                                            tvs[3][1] = (TextView)view.findViewById(ids[3][1]);
                                            tvs[3][1].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(0);
                                            break;
                                        case 1:
                                            /*slots[3][6] = 'S';
                                            ids[3][6] = R.id.hex2;
                                            tvs[3][6] = (TextView)view.findViewById(ids[3][6]);
                                            tvs[3][6].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(1);
                                            break;
                                    }
                                    break;

                                case 'T':
                                    switch(i){
                                        case 0:
                                            /*slots[4][1] = 'T';
                                            ids[4][1] = R.id.fex1;
                                            tvs[4][1] = (TextView)view.findViewById(ids[4][1]);
                                            tvs[4][1].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(0);
                                            break;
                                        case 1:
                                            /*slots[4][6] = 'T';
                                            ids[4][6] = R.id.fex2;
                                            tvs[4][6] = (TextView)view.findViewById(ids[4][6]);
                                            tvs[4][6].setVisibility(View.VISIBLE);
                                            break;*/
                                            course.setFlag1(1);
                                            break;
                                    }
                                    break;


                            }

                        }
                    }
                    if (flag) {
                        course.setCourse_id(coursed);
                        course.setSlot(Character.toUpperCase(slt));
                        courses.add(course);
                        courseAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }


                }
            }
        });
    }
    private void initdialog_edit(final char ch,final String course_id,final int x,final int y)
    {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_course_freshie);
        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        slot.setText(Character.toString(ch));
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        courseid.setText(course_id);
        final LinearLayout days = (LinearLayout) dialog.findViewById(R.id.days);
        days.setVisibility(View.INVISIBLE);
        Button add = (Button) dialog.findViewById(R.id.add);
        Button remove = (Button) dialog.findViewById(R.id.remove);
        remove.setVisibility(View.VISIBLE);
        add.setText("UPDATE");
        final int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(slot.getText().toString().isEmpty())
                {
                    slot.setError("Please enter the slot");
                    return;
                }
                if(courseid.getText().toString().isEmpty())
                {
                    courseid.setError("Please enter the Course ID");
                    return;
                }
                if(clash(courseid.getText().toString().substring(0,2).toUpperCase()+courseid.getText().toString().substring(2),slot.getText().toString()))
                {
                    courseid.setError(slot.getText().toString()+" slot already has a different course. If you want to change the course of this slot, use Edit Courses from top right corner menu.");
                    return;
                }
                if(courseid.getText().toString().length()!=6){
                    courseid.setError("Please enter a valid Course ID");
                    return;

                }
                else{
                    char b=Character.toUpperCase(slot.getText().charAt(0));
                    String s=courseid.getText().toString();
                    s=s.substring(0,2).toUpperCase()+s.substring(2);
                    boolean flag=false;
                    int pos_final=-1;
                    int pos_init=-1;
                    for(int i=0;i<number;i++){
                        if((Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity())).equals(s)) {

                            flag = true;
                            pos_final = i;
                            break;
                        }
                    }
                    for(int i=0;i<number;i++){
                        if((Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity())).charAt(0)==(ch)) {
                            pos_init = i;
                            break;
                        }
                    }
                    if(flag){
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_final + UtilStrings.COURSE_DAYS, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_final + UtilStrings.COURSE_DAYS, getActivity()) * prime[x][y], getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, getActivity()) / prime[x][y], getActivity());
                        //COURSE_FLAG is needed?
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_final + UtilStrings.COURSE_FLAG, y<=4?0:1, getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_FLAG, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_FLAG, getActivity()), getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getActivity())-2,getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_final+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_final+UtilStrings.BUNKS_TOTAL,getActivity())+2,getActivity());

                    }
                    else{
                        Utils.saveprefInt(UtilStrings.COURSES_COUNT,number+1,getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + number + UtilStrings.COURSE_DAYS, prime[x][y], getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, getActivity()) / prime[x][y], getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + number + UtilStrings.COURSE_FLAG, y<=4?0:1, getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_FLAG, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_FLAG, getActivity()) , getActivity());
                        //COURSE_FLAG is needed?
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getActivity())-2,getActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+number+UtilStrings.BUNKS_TOTAL,2,getActivity());
                        //edit below
                        Utils.saveprefString(UtilStrings.COURSE_NUM+number+UtilStrings.COURSE_ID,s,getActivity());
                        Utils.saveprefString(UtilStrings.COURSE_NUM+number+UtilStrings.COURSE_SLOT,Character.toString(b),getActivity());



                    }
                }
                courseAdapter.notifyDataSetChanged();

                getbunks();
                getcoursemap();
                for (Bunks c : bunks) {
                    mapslots(c.getSlot(), c.getDays(),c.getFlag1());
                    //.d("map","Each time this comes, a slot is mapped");
                }
                for(int i=0;i<5;i++) {
                    for (int j = 0; j < 10; j++) {
                        // initializing the textViews
                        tvs[i][j].setText(Character.toString(slots[i][j]) + '\n' + coursemap.get(slots[i][j]));
                        // The cells in the grid to which slots are not assigned are 'X' by default
                        if (slots[i][j] == 'X') {
                            // there are 3 colors in the timetable- red indicating bunked,
                            // white indicating the presence of a slot in that hour and transparent implying free hour.
                            tvs[i][j].setVisibility(View.INVISIBLE);
                        }
                    }
                }
                //recreate();
                ((TimetableActivity) getActivity()).freshiereturnadapter().notifyDataSetChanged();

                dialog.dismiss();

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos_init=-1;
                for(int i=0;i<number;i++){
                    if((Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity())).charAt(0)==(ch)) {
                        pos_init = i;
                        break;
                    }
                }
                Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,getActivity())/prime[x][y],getActivity());
                Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_FLAG,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_FLAG,getActivity()),getActivity());
                Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getActivity())-2,getActivity());
                Utils.saveprefBool("state" + 10 * x + y, false, getActivity());
                getbunks();
                getcoursemap();
                for (Bunks c : bunks) {
                    mapslots(c.getSlot(), c.getDays(),c.getFlag1());
                    //.d("map","Each time this comes, a slot is mapped");
                }
                for(int i=0;i<5;i++) {
                    for (int j = 0; j < 10; j++) {
                        // initializing the textViews
                        tvs[i][j].setText(Character.toString(slots[i][j]) + '\n' + coursemap.get(slots[i][j]));
                        // The cells in the grid to which slots are not assigned are 'X' by default
                        if (slots[i][j] == 'X') {
                            // there are 3 colors in the timetable- red indicating bunked,
                            // white indicating the presence of a slot in that hour and transparent implying free hour.
                            tvs[i][j].setVisibility(View.INVISIBLE);
                        }
                    }
                }
                dialog.dismiss();
                courseAdapter.notifyDataSetChanged();
                ((TimetableActivity) getActivity()).freshiereturnadapter().notifyDataSetChanged();

            }
        });
    }

    boolean clash (char slot)
    {
        if(Utils.isFreshie(getActivity()))
        {
            return false;
        }
        boolean flag = false;
        for(Course c:courses)
        {
            flag = flag||(c.getSlot()==slot);
            flag = flag||((c.getSlot()=='P'));
            flag = flag||((c.getSlot()=='Q'));
            flag = flag||((c.getSlot()=='R'));
            flag = flag||((c.getSlot()=='S'));
            flag = flag||((c.getSlot()=='T'));
        }
        return flag;
    }
    boolean clash (String id,String slot)
    {
        if(Utils.isFreshie(getActivity()))
        {
            return false;
        }
        boolean flag = false;
        for(Course c:courses)
        {
            flag = flag||(!c.getCourse_id().equals(id)&&Character.toString(c.getSlot()).equals(slot));
            /*flag = flag||((c.getSlot()=='P')&&(slot=='H'||slot=='M'));
            flag = flag||((c.getSlot()=='Q')&&(slot=='M'||slot=='H'));
            flag = flag||((c.getSlot()=='R')&&(slot=='J'||slot=='K'));
            flag = flag||((c.getSlot()=='S')&&(slot=='L'||slot=='J'));
            flag = flag||((c.getSlot()=='T')&&(slot=='K'||slot=='L'));*/
        }
        return flag;
    }



}
