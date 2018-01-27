package in.ac.iitm.students.adapters;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.others.UtilStrings;


public class FreshieCourseAdapter extends RecyclerView.Adapter<FreshieCourseAdapter.ViewHolder> {

    public static ArrayList<Course> courses;
    Dialog dialog;
    //int prime[][]={{2, 3, 5, 7, 11, 13, 17, 19},{23, 29, 31, 37, 41, 43, 47, 53},{59, 61, 67, 71, 73, 79, 83, 89},{97, 101, 103, 107, 109, 113, 127, 131},{137, 139, 149, 151, 157, 163, 167, 173}};
    int prime[][]={{2, 3, 5, 7, 11, 13, 17, 19,23,29},{ 31, 37, 41, 43, 47, 53,59,61,67,71},{ 73, 79, 83, 89,97,101,103,107,109,113},{ 127, 131,137,139,149,151,157,163,167,173},{179,181,191,193,197,199,211,223,227,229}};
    private Context context;
    public FreshieCourseAdapter(Context context, ArrayList<Course> courses)
    {
        this.context = context;
        FreshieCourseAdapter.courses = courses;
    }

    @Override
    public FreshieCourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_course,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FreshieCourseAdapter.ViewHolder holder, final int position) {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdialog(position);
                dialog.show();
            }
        });

        holder.c1.setText(gettext(courses.get(position).getSlot(),1));
        holder.c2.setText(gettext(courses.get(position).getSlot(),2));
        holder.c3.setText(gettext(courses.get(position).getSlot(),3));
        holder.c4.setText(gettext(courses.get(position).getSlot(),4));
        holder.c5.setText(gettext(courses.get(position).getSlot(),5));
        holder.c6.setText(gettext(courses.get(position).getSlot(),6));
        holder.c7.setText(gettext(courses.get(position).getSlot(),7));

        switch(courses.get(position).getSlot()){
            case 'A':
                if(courses.get(position).getDays()%prime[0][0]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);

                if(courses.get(position).getDays()%prime[1][5]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][3]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][8]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[4][2]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                break;
            case 'B':
                if(courses.get(position).getDays()%prime[0][1]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[0][6]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][0]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][5]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[4][3]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                break;
            case 'C':
                if(courses.get(position).getDays()%prime[0][2]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[0][7]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][1]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][6]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][0]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[4][5]==0)
                    holder.c6.setVisibility(View.VISIBLE);
                else
                    holder.c6.setVisibility(View.GONE);
                break;
            case 'D':
                if(courses.get(position).getDays()%prime[0][3]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[0][8]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][2]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][7]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][1]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][6]==0)
                    holder.c6.setVisibility(View.VISIBLE);
                else
                    holder.c6.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][5]==0)
                    holder.c7.setVisibility(View.VISIBLE);
                else
                    holder.c7.setVisibility(View.GONE);
                break;
            case 'E':
                if(courses.get(position).getDays()%prime[0][5]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][3]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[1][8]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][2]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][7]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][0]==0)
                    holder.c6.setVisibility(View.VISIBLE);
                else
                    holder.c6.setVisibility(View.GONE);
                break;
            case 'F':
                if(courses.get(position).getDays()%prime[2][3]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[2][8]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][1]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][6]==0)
                    holder.c4.setVisibility(View.VISIBLE);
                else
                    holder.c4.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[4][0]==0)
                    holder.c5.setVisibility(View.VISIBLE);
                else
                    holder.c5.setVisibility(View.GONE);
                break;
            case 'G':
                if(courses.get(position).getDays()%prime[3][2]==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[3][7]==0)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                if(courses.get(position).getDays()%prime[4][1]==0)
                    holder.c3.setVisibility(View.VISIBLE);
                else
                    holder.c3.setVisibility(View.GONE);
                break;
            case 'P':
                if(courses.get(position).getFlag1()==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getFlag1()==1)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                break;
            case 'Q':
                if(courses.get(position).getFlag1()==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getFlag1()==1)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                break;
            case 'R':
                if(courses.get(position).getFlag1()==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getFlag1()==1)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                break;
            case 'S':
                if(courses.get(position).getFlag1()==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getFlag1()==1)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                break;
            case 'T':
                if(courses.get(position).getFlag1()==0)
                    holder.c1.setVisibility(View.VISIBLE);
                else
                    holder.c1.setVisibility(View.GONE);
                if(courses.get(position).getFlag1()==1)
                    holder.c2.setVisibility(View.VISIBLE);
                else
                    holder.c2.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        if((holder.c1.getVisibility()==View.INVISIBLE||holder.c1.getVisibility()==View.GONE)&&(holder.c2.getVisibility()==View.INVISIBLE||holder.c2.getVisibility()==View.GONE)&&(holder.c3.getVisibility()==View.INVISIBLE||holder.c3.getVisibility()==View.GONE)&&(holder.c4.getVisibility()==View.INVISIBLE||holder.c4.getVisibility()==View.GONE)&&(holder.c5.getVisibility()==View.INVISIBLE||holder.c5.getVisibility()==View.GONE)&&(holder.c6.getVisibility()==View.INVISIBLE||holder.c6.getVisibility()==View.GONE)&&(holder.c7.getVisibility()==View.INVISIBLE||holder.c7.getVisibility()==View.GONE))
            holder.days.setText("");
        holder.slot.setText("Slot: "+courses.get(position).getSlot());
        holder.courseid.setText("Course ID: "+courses.get(position).getCourse_id());
    }

    private void initdialog(final int position)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_course_freshie);
        final Course course;
        try {
            course = courses.get(position);
        }
        catch(IndexOutOfBoundsException e)
        {
            notifyDataSetChanged();
            return;
        }
        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        slot.setText(Character.toString(course.getSlot()));
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        courseid.setText(course.getCourse_id());
        final LinearLayout days = (LinearLayout) dialog.findViewById(R.id.days);
        Button add = (Button) dialog.findViewById(R.id.add);
        add.setText("UPDATE");
        final CheckBox c1 = (CheckBox) dialog.findViewById(R.id.day1);
        final CheckBox c2 = (CheckBox) dialog.findViewById(R.id.day2);
        final CheckBox c3 = (CheckBox) dialog.findViewById(R.id.day3);
        final CheckBox c4 = (CheckBox) dialog.findViewById(R.id.day4);
        final CheckBox c5 = (CheckBox) dialog.findViewById(R.id.day5);
        final CheckBox c6 = (CheckBox) dialog.findViewById(R.id.day6);
        final CheckBox c7 = (CheckBox) dialog.findViewById(R.id.day7);
        switch(course.getSlot()){
            case 'A':
                if(course.getDays()%prime[0][0]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[1][5]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[3][3]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[3][8]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[4][2]==0)
                    c5.setChecked(true);
                break;
            case 'B':
                if(course.getDays()%prime[0][1]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[0][6]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[1][0]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[2][5]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[4][3]==0)
                    c5.setChecked(true);
                break;
            case 'C':
                if(course.getDays()%prime[0][2]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[0][7]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[1][1]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[1][6]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[2][0]==0)
                    c5.setChecked(true);
                if(course.getDays()%prime[4][5]==0)
                    c6.setChecked(true);
                break;
            case 'D':
                if(course.getDays()%prime[0][3]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[0][8]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[1][2]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[1][7]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[2][1]==0)
                    c5.setChecked(true);
                if(course.getDays()%prime[2][6]==0)
                    c6.setChecked(true);
                if(course.getDays()%prime[3][5]==0)
                    c7.setChecked(true);
                break;
            case 'E':
                if(course.getDays()%prime[0][5]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[1][3]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[1][8]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[2][2]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[2][7]==0)
                    c5.setChecked(true);
                if(course.getDays()%prime[3][0]==0)
                    c6.setChecked(true);
                break;
            case 'F':
                if(course.getDays()%prime[2][3]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[2][8]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[3][1]==0)
                    c3.setChecked(true);
                if(course.getDays()%prime[3][6]==0)
                    c4.setChecked(true);
                if(course.getDays()%prime[4][0]==0)
                    c4.setChecked(true);
                break;
            case 'G':
                if(course.getDays()%prime[3][2]==0)
                    c1.setChecked(true);
                if(course.getDays()%prime[3][7]==0)
                    c2.setChecked(true);
                if(course.getDays()%prime[4][1]==0)
                    c3.setChecked(true);
                break;
            case 'P':
                if(course.getFlag1()==0)
                    c1.setChecked(true);
                if(course.getFlag1()==1)
                    c2.setChecked(true);
                break;
            case 'Q':
                if(course.getFlag1()==0)
                    c1.setChecked(true);
                if(course.getFlag1()==1)
                    c2.setChecked(true);
                break;
            case 'R':
                if(course.getFlag1()==0)
                    c1.setChecked(true);
                if(course.getFlag1()==1)
                    c2.setChecked(true);
                break;
            case 'S':
                if(course.getFlag1()==0)
                    c1.setChecked(true);
                if(course.getFlag1()==1)
                    c2.setChecked(true);
                break;
            case 'T':
                if(course.getFlag1()==0)
                    c1.setChecked(true);
                if(course.getFlag1()==1)
                    c2.setChecked(true);
                break;
            default:
                break;
        }

        Button remove = (Button) dialog.findViewById(R.id.remove);
        remove.setVisibility(View.VISIBLE);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courses.isEmpty())
                {
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
                else {
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.COURSE_DAYS),0).edit().clear().apply();
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.COURSE_ID),0).edit().clear().apply();
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.COURSE_SLOT),0).edit().clear().apply();
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_TOTAL),0).edit().clear().apply();
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE),0).edit().clear().apply();
                    context.getSharedPreferences((UtilStrings.COURSE_NUM+position+UtilStrings.COURSE_FLAG),0).edit().clear().apply();
                    courses.remove(position);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        char sl = course.getSlot();
        switch(sl)
        {
            case 'A': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
                c5.setVisibility(View.VISIBLE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
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
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
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
                c7.setVisibility(View.GONE);
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
                c7.setVisibility(View.GONE);
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
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
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
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("Th FN");
                c2.setText("Th AN");
                c3.setText("F");
                break;
            }

            case 'P': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("M FN");
                c2.setText("M AN");
                break;
            }
            case 'Q': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("T FN");
                c2.setText("T AN");
                break;
            }
            case 'R': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("W FN");
                c2.setText("W AN");
                break;
            }
            case 'S': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("Th FN");
                c2.setText("Th AN");
                break;
            }
            case 'T': {
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c1.setText("F FN");
                c2.setText("F AN");
                break;
            }
            default: {
                slot.setError("Invalid slot");
                days.setVisibility(View.GONE);
                c1.setVisibility(View.GONE);
                c2.setVisibility(View.GONE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);

            }
        }

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
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
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
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
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
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            c3.setText("Tue FN");
                            c4.setText("Tue AN");
                            c5.setText("F");
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
                            c7.setVisibility(View.GONE);
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
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
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
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("Th FN");
                            c2.setText("Th AN");
                            c3.setText("F");
                            break;
                        }

                        case 'P': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("M FN");
                            c2.setText("M AN");
                            break;
                        }
                        case 'Q': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("T FN");
                            c2.setText("T AN");
                            break;
                        }
                        case 'R': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("W FN");
                            c2.setText("W AN");
                            break;
                        }
                        case 'S': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("Th FN");
                            c2.setText("Th AN");
                            break;
                        }
                        case 'T': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);
                            c1.setText("F FN");
                            c2.setText("F AN");
                            break;
                        }
                        default: {
                            slot.setError("Invalid slot");
                            days.setVisibility(View.GONE);
                            c1.setVisibility(View.GONE);
                            c2.setVisibility(View.GONE);
                            c3.setVisibility(View.GONE);
                            c4.setVisibility(View.GONE);
                            c5.setVisibility(View.GONE);
                            c6.setVisibility(View.GONE);
                            c7.setVisibility(View.GONE);

                        }
                    }
                }
                else
                {
                    slot.setError("Invalid slot");
                    days.setVisibility(View.GONE);
                    c1.setVisibility(View.GONE);
                    c2.setVisibility(View.GONE);
                    c3.setVisibility(View.GONE);
                    c4.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //correct below part
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()*prime[0][0]);
                            break;
                        case 'B':
                            course.setDays(course.getDays()*prime[0][1]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()*prime[0][2]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[0][3]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[0][5]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()*prime[2][3]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()*prime[3][2]);
                            break;
                        case 'P':
                            course.setFlag1(0);
                            break;
                        case 'Q':
                            course.setFlag1(0);
                            break;
                        case 'R':
                            course.setFlag1(0);
                            break;
                        case 'S':
                            course.setFlag1(0);
                            break;
                        case 'T':
                            course.setFlag1(0);
                            break;


                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*2);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()/prime[0][0]);
                            break;
                        case 'B':
                            course.setDays(course.getDays()/prime[0][1]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()/prime[0][2]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[0][3]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[0][5]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()/prime[2][3]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()/prime[3][2]);
                            break;
                        case 'P':
                            course.setFlag1(1);
                            break;
                        case 'Q':
                            course.setFlag1(1);
                            break;
                        case 'R':
                            course.setFlag1(1);
                            break;
                        case 'S':
                            course.setFlag1(1);
                            break;
                        case 'T':
                            course.setFlag1(1);
                            break;
                        default:
                            break;
                    }

                    //course.setDays(course.getDays()/2);
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()*prime[1][5]);
                            break;
                        case 'B':

                            course.setDays(course.getDays()*prime[0][6]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()*prime[0][7]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[0][8]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[1][3]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()*prime[2][8]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()*prime[3][7]);
                            break;
                        case 'P':
                            course.setFlag1(1);
                            break;
                        case 'Q':
                            course.setFlag1(1);
                            break;
                        case 'R':
                            course.setFlag1(1);
                            break;
                        case 'S':
                            course.setFlag1(1);
                            break;
                        case 'T':
                            course.setFlag1(1);
                            break;

                        default:
                            break;
                    }

                    //course.setDays(course.getDays()*3);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()/prime[1][5]);
                            break;
                        case 'B':

                            course.setDays(course.getDays()/prime[0][6]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()/prime[0][7]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[0][8]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[1][3]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()/prime[2][8]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()/prime[3][7]);
                            break;
                        case 'P':
                            course.setFlag1(0);
                            break;
                        case 'Q':
                            course.setFlag1(0);
                            break;
                        case 'R':
                            course.setFlag1(0);
                            break;
                        case 'S':
                            course.setFlag1(0);
                            break;
                        case 'T':
                            course.setFlag1(0);
                            break;

                        default:
                            break;
                    }
                    //course.setDays(course.getDays()/3);
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()*prime[3][3]);
                            break;
                        case 'B':

                            course.setDays(course.getDays()*prime[1][0]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()*prime[1][1]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[1][2]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[1][8]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()*prime[3][1]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()*prime[4][1]);
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*5);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()/prime[3][3]);
                            break;
                        case 'B':

                            course.setDays(course.getDays()/prime[1][0]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()/prime[1][1]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[1][2]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[1][8]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()/prime[3][1]);
                            break;
                        case 'G':
                            course.setDays(course.getDays()/prime[4][1]);
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()/5);
                }
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()*prime[3][8]);
                            break;
                        case 'B':
                            course.setDays(course.getDays()*prime[2][5]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()*prime[1][6]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[1][7]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[2][2]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()*prime[3][6]);
                            break;
                        case 'G':
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*7);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()/prime[3][8]);
                            break;
                        case 'B':
                            course.setDays(course.getDays()/prime[2][5]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()/prime[1][6]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[1][7]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[2][2]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()/prime[3][6]);
                            break;
                        case 'G':
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()/7);
                }
            }
        });
        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()*prime[4][2]);
                            break;
                        case 'B':

                            course.setDays(course.getDays()*prime[4][3]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()*prime[2][0]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[2][1]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[2][7]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()*prime[4][0]);
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*2);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'A':
                            course.setDays(course.getDays()/prime[4][2]);
                            break;
                        case 'B':
                            course.setDays(course.getDays()/prime[4][3]);
                            break;
                        case 'C':
                            course.setDays(course.getDays()/prime[2][0]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[2][1]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[2][7]);
                            break;
                        case 'F':
                            course.setDays(course.getDays()/prime[4][0]);
                            break;
                        default:
                            break;
                    }

                    //course.setDays(course.getDays()/2);
                }
            }
        });
        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'C':
                            course.setDays(course.getDays()*prime[4][5]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()*prime[2][6]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()*prime[3][0]);
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*2);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'C':
                            course.setDays(course.getDays()/prime[4][5]);
                            break;
                        case 'D':
                            course.setDays(course.getDays()/prime[2][6]);
                            break;
                        case 'E':
                            course.setDays(course.getDays()/prime[3][0]);
                            break;
                        default:
                            break;
                    }

                    //course.setDays(course.getDays()/2);
                }
            }
        });
        c7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switch(slot.getText().charAt(0)){
                        case 'D':
                            course.setDays(course.getDays()*prime[3][5]);
                            break;
                        default:
                            break;
                    }
                    //course.setDays(course.getDays()*2);
                }
                else
                {
                    switch(slot.getText().charAt(0)){
                        case 'D':
                            course.setDays(course.getDays()/prime[3][5]);
                            break;
                        default:
                            break;
                    }

                    //course.setDays(course.getDays()/2);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coursed = courseid.getText().toString().toUpperCase();
                char slt = Character.toUpperCase(slot.getText().charAt(0));
                boolean flag = true;
                if(coursed.length()!=6)
                {
                    courseid.setError("Course ID is incorrect");
                    flag = false;
                }
                else if(!(Character.isLetter(coursed.charAt(0))&&Character.isLetter(coursed.charAt(1))
                        &&Character.isDigit(coursed.charAt(2))&&Character.isDigit(coursed.charAt(3))
                        &&Character.isDigit(coursed.charAt(4))&&Character.isDigit(coursed.charAt(5))))
                {
                    courseid.setError("Incorrect ID format");
                    flag = false;
                }
                if(!(slt-'A'>=0&&slt-'A'<22&&slt!='I'))
                {
                    slot.setError("Invalid slot");
                    flag = false;
                }
                if(clash(slt))
                {
                    slot.setError("There is a slot clash");
                    flag = false;
                }
                if(flag) {
                    course.setCourse_id(coursed);
                    course.setSlot(Character.toUpperCase(slt));
                    courses.set(position,course);
                    notifyItemChanged(position);
                    dialog.dismiss();
                }
            }
        });
    }

    private boolean clash (char slot)
    {
        boolean flag = false;
        for(Course c:courses)
        {

            //flag = flag||(c.getSlot()==slot);

            /*flag = flag||((c.getSlot()=='P')&&(slot=='H'||slot=='M'));
            flag = flag||((c.getSlot()=='Q')&&(slot=='M'||slot=='H'));
            flag = flag||((c.getSlot()=='R')&&(slot=='J'||slot=='K'));
            flag= flag||((c.getSlot()=='S')&&(slot=='L'||slot=='J'));
            flag = flag||((c.getSlot()=='T')&&(slot=='K'||slot=='L'));*/

            if(c.getSlot()=='P'&&c.getFlag1()==0){
                for(Course d:courses){
                    if((d.getSlot()=='B'&&d.getDays()%prime[0][1]==0)||(d.getSlot()=='C'&&d.getDays()%prime[0][2]==0)||(d.getSlot()=='D'&&d.getDays()%prime[0][3]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='P'&&c.getFlag1()==1){
                for(Course d:courses){
                    if((d.getSlot()=='B'&&d.getDays()%prime[0][6]==0)||(d.getSlot()=='C'&&d.getDays()%prime[0][7]==0)||(d.getSlot()=='D'&&d.getDays()%prime[0][8]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='Q'&&c.getFlag1()==0){
                for(Course d:courses){
                    if((d.getSlot()=='C'&&d.getDays()%prime[1][1]==0)||(d.getSlot()=='D'&&d.getDays()%prime[1][2]==0)||(d.getSlot()=='E'&&d.getDays()%prime[1][3]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='Q'&&c.getFlag1()==1){
                for(Course d:courses){
                    if((d.getSlot()=='C'&&d.getDays()%prime[1][6]==0)||(d.getSlot()=='D'&&d.getDays()%prime[1][7]==0)||(d.getSlot()=='E'&&d.getDays()%prime[1][8]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='R'&&c.getFlag1()==0){
                for(Course d:courses){
                    if((d.getSlot()=='D'&&d.getDays()%prime[2][1]==0)||(d.getSlot()=='E'&&d.getDays()%prime[2][2]==0)||(d.getSlot()=='F'&&d.getDays()%prime[2][3]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='R'&&c.getFlag1()==1){
                for(Course d:courses){
                    if((d.getSlot()=='D'&&d.getDays()%prime[2][6]==0)||(d.getSlot()=='E'&&d.getDays()%prime[2][7]==0)||(d.getSlot()=='F'&&d.getDays()%prime[2][8]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='S'&&c.getFlag1()==0){
                for(Course d:courses){
                    if((d.getSlot()=='F'&&d.getDays()%prime[3][1]==0)||(d.getSlot()=='G'&&d.getDays()%prime[3][2]==0)||(d.getSlot()=='A'&&d.getDays()%prime[3][3]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='S'&&c.getFlag1()==1){
                for(Course d:courses){
                    if((d.getSlot()=='F'&&d.getDays()%prime[3][6]==0)||(d.getSlot()=='G'&&d.getDays()%prime[3][7]==0)||(d.getSlot()=='A'&&d.getDays()%prime[3][8]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='T'&&c.getFlag1()==0){
                for(Course d:courses){
                    if((d.getSlot()=='G'&&d.getDays()%prime[4][1]==0)||(d.getSlot()=='A'&&d.getDays()%prime[4][2]==0)||(d.getSlot()=='B'&&d.getDays()%prime[4][3]==0))
                        flag=true;
                }
            }
            if(c.getSlot()=='T'&&c.getFlag1()==1){
                for(Course d:courses){
                    if((d.getSlot()=='G'&&d.getDays()%prime[4][6]==0)||(d.getSlot()=='A'&&d.getDays()%prime[4][7]==0)||(d.getSlot()=='B'&&d.getDays()%prime[4][8]==0))
                        flag=true;
                }
            }

        }
        return flag;
    }

    private String gettext(char c, int pos)
    {
        switch (c) {
            case 'A': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T";
                    case 3: return "Th FN";
                    case 4: return "Th AN";
                    case 5: return "F";
                    default: return "";
                }
            }
            case 'B': {
                switch (pos)
                {
                    case 1: return "M FN";
                    case 2: return "M AN";
                    case 3: return "T";
                    case 4: return "W";
                    case 5: return "F";
                    default: return "";
                }

            }
            case 'C': {
                switch (pos)
                {
                    case 1: return "M FN";
                    case 2: return "M AN";
                    case 3: return "T FN";
                    case 4: return "T AN";
                    case 5: return "W";
                    case 6: return "F";
                    default: return "";
                }

            }
            case 'D': {
                switch (pos)
                {
                    case 1: return "M FN";
                    case 2: return "M AN";
                    case 3: return "T FN";
                    case 4: return "T AN";
                    case 5: return "W FN";
                    case 6: return "W AN";
                    case 7: return "Th";
                    default: return "";
                }

            }
            case 'E': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T FN";
                    case 3: return "T AN";
                    case 4: return "W FN";
                    case 5: return "W AN";
                    case 6: return "Th";
                    default: return "";
                }

            }
            case 'F': {
                switch (pos)
                {
                    case 1: return "W FN";
                    case 2: return "W AN";
                    case 3: return "Th FN";
                    case 4: return "Th AN";
                    case 5: return "F";
                    default: return "";
                }

            }
            case 'G': {
                switch (pos)
                {
                    case 1: return "Th FN";
                    case 2: return "Th AN";
                    case 3: return "F";
                    default: return "";
                }

            }

            case 'P': {
                switch (pos)
                {
                    case 1: return "M FN";
                    case 2: return "M AN";
                    default: return "";
                }

            }
            case 'Q': {
                switch (pos)
                {
                    case 1: return "T FN";
                    case 2: return "T AN";
                    default: return "";
                }

            }
            case 'R': {
                switch (pos)
                {
                    case 1: return "W FN";
                    case 2: return "W AN";

                    default: return "";
                }

            }
            case 'S': {
                switch (pos)
                {
                    case 1: return "Th FN";
                    case 2: return "Th AN";
                    default: return "";
                }

            }
            case 'T': {
                switch (pos)
                {
                    case 1: return "F FN";
                    case 2: return "F AN";
                    default: return "";
                }

            }
            default: {
                return "";
            }
        }
    }

    private String getday(int pos){
        switch(pos){
            case 0:
                return "M";

            case 1:
                return "T";

            case 2:
                return "W";

            case 3:
                return "Th";

            case 4:
                return "F";

            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView slot;
        TextView days;
        TextView courseid;
        //Checkboxes: D1 = 2, D2 = 3, D3 = 5, D4 = 7
        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;
        TextView c5;
        TextView c6;
        TextView c7;


        ViewHolder(View view) {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.container);
            days = (TextView) view.findViewById(R.id.textView3);
            c1 = (TextView) view.findViewById(R.id.day1);
            c2 = (TextView) view.findViewById(R.id.day2);
            c2.setVisibility(View.GONE);
            c3 = (TextView) view.findViewById(R.id.day3);
            c3.setVisibility(View.GONE);
            c4 = (TextView) view.findViewById(R.id.day4);
            c4.setVisibility(View.GONE);
            c5 = (TextView) view.findViewById(R.id.day5);
            c5.setVisibility(View.GONE);
            c6 = (TextView) view.findViewById(R.id.day6);
            c6.setVisibility(View.GONE);
            c7 = (TextView) view.findViewById(R.id.day7);
            c7.setVisibility(View.GONE);
            slot = (TextView) view.findViewById(R.id.slot);
            courseid = (TextView) view.findViewById(R.id.course_id);
        }


    }
}
