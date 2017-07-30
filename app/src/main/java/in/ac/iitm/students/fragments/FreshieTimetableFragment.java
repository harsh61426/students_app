package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.adapters.CourseAdapter;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.objects.RowCol;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class FreshieTimetableFragment extends Fragment {


    ViewFlipper flipper;
    Dialog dialog;
    ArrayList<Bunks> bunks;
    CourseAdapter courseAdapter;
    HashMap<Character,String> coursemap;
    int ids[][] = {{R.id.m1,R.id.m2,R.id.m3,R.id.m4,R.id.m5,R.id.m6,R.id.m7,R.id.m8,R.id.m9},
            {R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9},
            {R.id.w1,R.id.w2,R.id.w3,R.id.w4,R.id.w5,R.id.w6,R.id.w7,R.id.w8,R.id.w9},
            {R.id.h1,R.id.h2,R.id.h3,R.id.h4,R.id.h5,R.id.h6,R.id.h7,R.id.h8,R.id.h9},
            {R.id.f1,R.id.f2,R.id.f3,R.id.f4,R.id.f5,R.id.f6,R.id.f7,R.id.f8,R.id.f9}};
    int texids[] = {R.id.mex,R.id.tex,R.id.wex,R.id.hex,R.id.fex,R.id.mex_m,R.id.tex_m,R.id.wex_m,R.id.hex_m,R.id.fex_m};
    TextView tvs[][] = new TextView[5][9];
    char slots[][] = new char[5][9];
    boolean bunk[][] = new boolean[5][9];
    View view;

    //TODO: Remake

    public FreshieTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_course_timetable_freshie, container, false);

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

        bunks = new ArrayList<>();

        getbunks();

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

        for(int i=0;i<5;i++)
        {
            for(int j=0;j<9;j++)
            {
                slots[i][j] = 'X';
                bunk[i][j] = Utils.getprefBool("state"+9*i+j,getActivity());
                tvs[i][j] = (TextView)view.findViewById(ids[i][j]);
                if(bunk[i][j])
                {
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
            }
        }

        //TODO: After this

        getcoursemap();
        for (Bunks c : bunks) {
            mapslots(c.getSlot(), c.getDays());
        }

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if(week>Utils.getprefInt("LastLogTT",getActivity()))
        {
            clearbunks();
        }
        Utils.saveprefInt("LastLogTT",week,getActivity());



        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++)
            {
                final int x = i;
                final int y = j;
                tvs[i][j].setText(Character.toString(slots[i][j])+'\n'+coursemap.get(slots[i][j]));
                if(slots[i][j]=='X')
                {
                    tvs[i][j].setVisibility(View.INVISIBLE);
                }
                tvs[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bunk[x][y])
                        {
                            v.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                            bunk[x][y] = false;
                            Utils.saveprefBool("state"+8*x+y,false,getActivity());
                            updatebunks(x,y,false);
                        }
                        else
                        {
                            v.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                            bunk[x][y] = true;
                            Utils.saveprefBool("state"+8*x+y,true,getActivity());
                            updatebunks(x,y,true);
                        }
                    }
                });
            }
        }

        return view;
    }

    private void clearbunks()   //clear bunks at end of each week
    {
        for(int x=0;x<5;x++)
        {
            for(int y=0;y<8;y++)
            {
                Utils.saveprefBool("state"+8*x+y,false,getActivity());
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
        ((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
    }

    private void getcoursemap()
    {
        for(Bunks c:bunks)
        {
            coursemap.put(c.getSlot(),c.getCourse_id());
        }
    }

    private void getbunks()
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
            int count = Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_COUNT,getActivity());
            ArrayList<RowCol> rowcols = new ArrayList();
            for(int j=0;j<count;j++)
            {
                rowcols.add(new RowCol(
                        Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_NUM+j+UtilStrings.ROW,getActivity()),
                        Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_NUM+j+UtilStrings.COLUMN,getActivity())
                ));
            }
            course.setRowCols(rowcols);
            bunks.add(course);
        }
    }

    private void mapslots(char c, int days)
    {
        switch(c)
        {
            case 'A': {
                slots[0][0] = (days%2 == 0) ? 'A' : 'X';
                slots[1][4] = (days%3 == 0) ? 'A' : 'X';
                slots[3][3] = (days%5 == 0) ? 'A' : 'X';
                slots[4][2] = (days%7 == 0) ? 'A' : 'X';
                break;
            }
            case 'B': {
                slots[0][1] = (days%2 == 0) ? 'B' : 'X';
                slots[1][0] = (days%3 == 0) ? 'B' : 'X';
                slots[2][4] = (days%5 == 0) ? 'B' : 'X';
                slots[4][3] = (days%7 == 0) ? 'B' : 'X';
                break;
            }
            case 'C': {
                slots[0][2] = (days%2 == 0) ? 'C' : 'X';
                slots[1][1] = (days%3 == 0) ? 'C' : 'X';
                slots[2][0] = (days%5 == 0) ? 'C' : 'X';
                slots[4][4] = (days%7 == 0) ? 'C' : 'X';
                break;
            }
            case 'D': {
                slots[0][3] = (days%2 == 0) ? 'D' : 'X';
                slots[1][2] = (days%3 == 0) ? 'D' : 'X';
                slots[2][1] = (days%5 == 0) ? 'D' : 'X';
                slots[3][4] = (days%7 == 0) ? 'D' : 'X';
                break;
            }
            case 'E': {
                slots[1][3] = (days%2 == 0) ? 'E' : 'X';
                slots[2][2] = (days%3 == 0) ? 'E' : 'X';
                slots[3][0] = (days%5 == 0) ? 'E' : 'X';
                slots[4][7] = (days%7 == 0) ? 'E' : 'X';
                break;
            }
            case 'F': {
                slots[1][7] = (days%2 == 0) ? 'F' : 'X';
                slots[2][3] = (days%3 == 0) ? 'F' : 'X';
                slots[3][1] = (days%5 == 0) ? 'F' : 'X';
                slots[4][0] = (days%7 == 0) ? 'F' : 'X';
                break;
            }
            case 'G': {
                slots[0][4] = (days%2 == 0) ? 'G' : 'X';
                slots[2][7] = (days%3 == 0) ? 'G' : 'X';
                slots[3][2] = (days%5 == 0) ? 'G' : 'X';
                slots[4][1] = (days%7 == 0) ? 'G' : 'X';
                break;
            }
            case 'H': {
                slots[0][5] = (days%2==0)?'H':'X';
                slots[1][6] = (days%3==0)?'H':'X';
                slots[3][7] = (days%5==0)?'H':'X';
                break;
            }
            case 'J': {
                slots[0][7] = (days%2==0)?'J':'X';
                slots[2][5] = (days%3==0)?'J':'X';
                slots[3][6] = (days%5==0)?'J':'X';
                break;
            }
            case 'K': {
                slots[2][6] = (days%2==0)?'K':'X';
                slots[4][5] = (days%3==0)?'K':'X';
                break;
            }
            case 'L': {
                slots[3][5] = (days%2==0)?'L':'X';
                slots[4][6] = (days%3==0)?'L':'X';
                break;
            }
            case 'M': {
                slots[0][6] = (days%2==0)?'M':'X';
                slots[1][5] = (days%3==0)?'M':'X';
                break;
            }
            case 'P': {
                slots[0][5] = 'P';
                ids[0][5] = R.id.mex;
                tvs[0][5] = (TextView)view.findViewById(ids[0][5]);
                tvs[0][5].setVisibility(View.VISIBLE);
                if(bunk[0][5])
                {
                    tvs[0][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[0][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'Q': {
                slots[1][5] = 'Q';
                ids[1][5] = R.id.tex;
                tvs[1][5] = (TextView)view.findViewById(ids[1][5]);
                tvs[1][5].setVisibility(View.VISIBLE);
                if(bunk[1][5])
                {
                    tvs[1][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[1][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'R': {
                slots[2][5] = 'R';
                ids[2][5] = R.id.wex;
                tvs[2][5] = (TextView)view.findViewById(ids[2][5]);
                tvs[2][5].setVisibility(View.VISIBLE);
                if(bunk[2][5])
                {
                    tvs[2][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[2][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'S': {
                slots[3][5] = 'S';
                ids[3][5] = R.id.hex;
                tvs[3][5] = (TextView)view.findViewById(ids[3][5]);
                tvs[3][5].setVisibility(View.VISIBLE);
                if(bunk[3][5])
                {
                    tvs[3][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[3][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'T': {
                slots[4][5] = 'T';
                ids[4][5] = R.id.fex;
                tvs[4][5] = (TextView)view.findViewById(ids[4][5]);
                tvs[4][5].setVisibility(View.VISIBLE);
                if(bunk[4][5])
                {
                    tvs[4][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[4][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            default:
                break;
        }
    }

    private void addcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,bunks.size(),getActivity());
        int i=0;
        for(Bunks c: bunks)
        {
            ArrayList<RowCol> rowCols = c.getRowCols();
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,c.getDays(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,c.getCourse_id(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,Character.toString(c.getSlot()),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,2*rowCols.size(),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,0,getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_COUNT,rowCols.size(),getActivity());
            int j=0;
            for(RowCol rowCol: rowCols)
            {
                Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_NUM+j+UtilStrings.ROW,rowCol.getRow(),getActivity());
                Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.ROWCOLS_NUM+j+UtilStrings.COLUMN,rowCol.getCol(),getActivity());
            }
            i++;
        }
    }

    private void removeallcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,0,getActivity());
    }

    private void initdialog()
    {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_course_freshie);
        final EditText courseid = (EditText)dialog.findViewById(R.id.edit_cid);
        final EditText slots = (EditText)dialog.findViewById(R.id.edit_slot);
        Button add = (Button)dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String cid = courseid.getText().toString();
                if(cid.length()!=6)
                {
                    flag = false;
                    courseid.setError("Course ID must be 6 characters");
                }
                if(!verify(slots.getText().toString()))
                {
                    flag = false;
                    slots.setError("Incorrect format. Please refer to instructions for correct format");
                }
                if(flag)
                {
                    Bunks bunks = new Bunks();
                    bunks.setCourse_id(cid);
                    ArrayList<RowCol> rowCols= new ArrayList<RowCol>();
                    StringTokenizer colontokens = new StringTokenizer(slots.getText().toString(),";");
                    while(colontokens.hasMoreTokens())
                    {
                        String token = colontokens.nextToken();
                        RowCol rowCol = new RowCol(token.charAt(0)-'0',token.charAt(2)-'0');
                        rowCols.add(rowCol);
                    }
                    bunks.setRowCols(rowCols);
                }
            }
        });

    }

    boolean verify(String slots)
    {
        if(slots.isEmpty())
        {
            return false;
        }
        StringTokenizer colontokens = new StringTokenizer(slots,";");
        while(colontokens.hasMoreTokens())
        {
            String token = colontokens.nextToken();
            if(token.length()==3&&Character.isDigit(token.charAt(0))&&Character.isDigit(token.charAt(2)))
            {
                continue;
            }
            else
            {
                return false;
            }
        }
        return true;
    }


}
