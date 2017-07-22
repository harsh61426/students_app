package in.ac.iitm.students.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by SAM10795 on 14-06-2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context context;
    public static ArrayList<Course> courses;
    Dialog dialog;

    public CourseAdapter(Context context, ArrayList<Course> courses)
    {
        this.context = context;
        this.courses = courses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout container;
        TextView slot;
        TextView courseid;
        //Checkboxes: D1 = 2, D2 = 3, D3 = 5, D4 = 7
        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;

        ViewHolder(View view)
        {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.container);
            c1 = (TextView) view.findViewById(R.id.day1);
            c2 = (TextView) view.findViewById(R.id.day2);
            c2.setVisibility(View.INVISIBLE);
            c3 = (TextView) view.findViewById(R.id.day3);
            c3.setVisibility(View.INVISIBLE);
            c4 = (TextView) view.findViewById(R.id.day4);
            c4.setVisibility(View.INVISIBLE);
            slot = (TextView) view.findViewById(R.id.slot);
            courseid = (TextView) view.findViewById(R.id.course_id);
        }

    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_course,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseAdapter.ViewHolder holder, final int position) {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdialog(position);
                dialog.show();
            }
        });
        final boolean freshie = Utils.isFreshie(context);
        holder.c1.setText(gettext(courses.get(position).getSlot(),1,freshie));
        holder.c2.setText(gettext(courses.get(position).getSlot(),2,freshie));
        holder.c3.setText(gettext(courses.get(position).getSlot(),3,freshie));
        holder.c4.setText(gettext(courses.get(position).getSlot(),4,freshie));
        if(courses.get(position).getDays()%2!=0&&courses.get(position).getDays()!=1)
        {
            holder.c1.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.c1.setVisibility(View.VISIBLE);
        }

        if(!gettext(courses.get(position).getSlot(),2,freshie).isEmpty()&&courses.get(position).getDays()%3==0)
        {
            holder.c2.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c2.setVisibility(View.INVISIBLE);
        }
        if(!gettext(courses.get(position).getSlot(),3,freshie).isEmpty()&&courses.get(position).getDays()%5==0)
        {
            holder.c3.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c3.setVisibility(View.INVISIBLE);
        }
        if(!gettext(courses.get(position).getSlot(),4,freshie).isEmpty()&&courses.get(position).getDays()%7==0)
        {
            holder.c4.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c4.setVisibility(View.INVISIBLE);
        }
        holder.slot.setText("Slot: "+courses.get(position).getSlot());
        holder.courseid.setText("Course ID: "+courses.get(position).getCourse_id());
    }

    private void initdialog(final int position)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_course);
        final LinearLayout shift = (LinearLayout) dialog.findViewById(R.id.shift);
        final boolean freshie = Utils.isFreshie(context);
        if(!freshie)
        {
            shift.setVisibility(View.GONE);
        }
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
        c1.setChecked(course.getDays()==1||course.getDays()%2==0);
        c2.setChecked(course.getDays()%3==0);
        c3.setChecked(course.getDays()%5==0);
        c4.setChecked(course.getDays()%7==0);
        final CheckBox morning = (CheckBox) dialog.findViewById(R.id.morning);
        morning.setChecked(Character.isUpperCase(course.getSlot()));
        final CheckBox afternoon = (CheckBox) dialog.findViewById(R.id.afternoon);
        afternoon.setChecked(!morning.isChecked());
        morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afternoon.setChecked(!isChecked);
            }
        });
        afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                morning.setChecked(!isChecked);
            }
        });
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
                    courses.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                }
            }
        });
        char sl = course.getSlot();
        boolean after = !Character.isUpperCase(course.getSlot());
        switch(sl)
        {
            case 'A': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("T");
                        c2.setText("W");
                        c3.setText("F");
                    }
                    else
                    {
                        c1.setText("M");
                        c2.setText("Th");
                        c3.setText("F");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("M");
                    c2.setText("T");
                    c3.setText("Th");
                    c4.setText("F");
                }
                break;
            }
            case 'B': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("W");
                        c2.setText("Th");
                        c3.setText("F");
                    }
                    else
                    {
                        c1.setText("M");
                        c2.setText("T");
                        c3.setText("F");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("M");
                    c2.setText("T");
                    c3.setText("W");
                    c4.setText("F");
                }
                break;
            }
            case 'C': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("M");
                        c2.setText("W");
                        c3.setText("Th");
                    }
                    else
                    {
                        c1.setText("M");
                        c2.setText("T");
                        c3.setText("W");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("M");
                    c2.setText("T");
                    c3.setText("W");
                    c4.setText("F");
                }
                break;
            }
            case 'D': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("M");
                        c2.setText("W");
                        c3.setText("Th");
                    }
                    else
                    {
                        c1.setText("M");
                        c2.setText("T");
                        c3.setText("W");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("M");
                    c2.setText("T");
                    c3.setText("W");
                    c4.setText("Th");
                }
                break;
            }
            case 'E': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("M");
                        c2.setText("T");
                        c3.setText("Th");
                    }
                    else
                    {
                        c1.setText("T");
                        c2.setText("W");
                        c3.setText("Th");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("T");
                    c2.setText("W");
                    c3.setText("Th");
                    c4.setText("F");
                }
                break;
            }
            case 'F': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("M");
                        c2.setText("T");
                        c3.setText("F");
                    }
                    else
                    {
                        c1.setText("W");
                        c2.setText("Th");
                        c3.setText("F");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("T");
                    c2.setText("W");
                    c3.setText("Th");
                    c4.setText("F");
                }
                break;
            }
            case 'G': {
                if(freshie) {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.INVISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                    if(after)
                    {
                        c1.setText("T");
                        c2.setText("F");
                    }
                    else
                    {
                        c1.setText("Th");
                        c2.setText("F");
                    }
                }
                else {
                    days.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    c3.setVisibility(View.VISIBLE);
                    c4.setVisibility(View.VISIBLE);
                    c1.setText("M");
                    c2.setText("W");
                    c3.setText("Th");
                    c4.setText("F");
                }
                break;
            }
            case 'H': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("M");
                c2.setText("W");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'J': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("T");
                c2.setText("Th");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'K': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("T");
                c2.setText("W");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'L': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("M");
                c2.setText("Th");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'M': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("Th");
                c2.setText("F");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'N': {
                if(freshie)
                {
                    slot.setError("Invalid slot");
                }
                days.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c1.setText("M");
                c2.setText("F");
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'P': {
                if(freshie) {
                    Toast.makeText(context,"Note that afternoon shift P slot takes place in the morning",Toast.LENGTH_SHORT).show();
                }
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'Q': {
                if(freshie) {
                    Toast.makeText(context,"Note that afternoon shift Q slot takes place in the morning",Toast.LENGTH_SHORT).show();
                }
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'R': {
                if(freshie) {
                    Toast.makeText(context,"Note that afternoon shift R slot takes place in the morning",Toast.LENGTH_SHORT).show();
                }
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'S': {
                if(freshie) {
                    Toast.makeText(context,"Note that afternoon shift S slot takes place in the morning",Toast.LENGTH_SHORT).show();
                }
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            case 'T': {
                if(freshie) {
                    Toast.makeText(context,"Note that afternoon shift T slot takes place in the morning",Toast.LENGTH_SHORT).show();
                }
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                break;
            }
            default: {
                slot.setError("Invalid slot");
                days.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
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
                    boolean after = afternoon.isChecked();
                    switch(sl)
                    {
                        case 'A': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("T");
                                    c2.setText("W");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'B': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("W");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'C': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("W");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'D': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("W");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("Th");
                            }
                            break;
                        }
                        case 'E': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("T");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("T");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'F': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("W");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("T");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'G': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.INVISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("T");
                                    c2.setText("F");
                                }
                                else
                                {
                                    c1.setText("Th");
                                    c2.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'H': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("Th");
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'J': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("W");
                            c3.setText("Th");
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'K': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("W");
                            c2.setText("F");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'L': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("Th");
                            c2.setText("F");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'M': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'P': {
                            if(freshie) {
                                Toast.makeText(context,"Note that afternoon shift P slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'Q': {
                            if(freshie) {
                                Toast.makeText(context,"Note that afternoon shift Q slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'R': {
                            if(freshie) {
                                Toast.makeText(context,"Note that afternoon shift R slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'S': {
                            if(freshie) {
                                Toast.makeText(context,"Note that afternoon shift S slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'T': {
                            if(freshie) {
                                Toast.makeText(context,"Note that afternoon shift T slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        default: {
                            slot.setError("Invalid slot");
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else
                {
                    slot.setError("Invalid slot");
                    days.setVisibility(View.INVISIBLE);
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c3.setVisibility(View.INVISIBLE);
                    c4.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*2);
                }
                else
                {
                    course.setDays(course.getDays()/2);
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*3);
                }
                else
                {
                    course.setDays(course.getDays()/3);
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*5);
                }
                else
                {
                    course.setDays(course.getDays()/5);
                }
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*7);
                }
                else
                {
                    course.setDays(course.getDays()/7);
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
                    course.setSlot(afternoon.isChecked()?Character.toLowerCase(slt):Character.toUpperCase(slt));
                    courses.set(position,course);
                    notifyItemChanged(position);
                    dialog.dismiss();
                }
            }
        });
    }

    boolean clash (char slot)
    {
        if(Utils.isFreshie(context))
        {
            return false;
        }
        boolean flag = false;
        for(Course c:courses)
        {
            flag = flag||((c.getSlot()=='P'&(slot=='H'||slot=='M')));
            flag = flag||((c.getSlot()=='Q'&(slot=='M'||slot=='H')));
            flag = flag||((c.getSlot()=='R'&(slot=='J'||slot=='K')));
            flag = flag||((c.getSlot()=='S'&(slot=='L'||slot=='J')));
            flag = flag||((c.getSlot()=='T'&(slot=='K'||slot=='L')));
        }
        return flag;
    }


    private String gettext(char c, int pos, boolean freshie)
    {
        switch (c) {
            case 'A': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return freshie?"Th":"T";
                    case 3: return freshie?"F":"Th";
                    case 4: return freshie?"":"F";
                    default: return "";
                }
            }
            case 'B': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return freshie?"F":"W";
                    case 4: return freshie?"":"F";
                    default: return ""; 
                }
                
            }
            case 'C': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return "W"; 
                    case 4: return freshie?"":"F";
                    default: return ""; 
                }
                
            }
            case 'D': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return "W";
                    case 4: return freshie?"":"Th";
                    default: return ""; 
                }
                
            }
            case 'E': {
                switch (pos)
                {
                    case 1: return "T"; 
                    case 2: return "W"; 
                    case 3: return "Th"; 
                    case 4: return freshie?"":"F";
                    default: return ""; 
                }
                
            }
            case 'F': {
                switch (pos)
                {
                    case 1: return freshie?"W":"T";
                    case 2: return freshie?"Th":"W";
                    case 3: return freshie?"F":"Th";
                    case 4: return freshie?"":"F";
                    default: return ""; 
                }
                
            }
            case 'G': {
                switch (pos)
                {
                    case 1: return freshie?"Th":"M";
                    case 2: return freshie?"F":"W";
                    case 3: return "Th";
                    case 4: return "F";
                    default: return "";
                }
                
            }
            case 'a': {
                switch (pos)
                {
                    case 1: return "T";
                    case 2: return "W";
                    case 3: return "F";
                    default: return "";
                }

            }
            case 'b': {
                switch (pos)
                {
                    case 1: return "W";
                    case 2: return "Th";
                    case 3: return "F";
                    default: return "";
                }

            }
            case 'c': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "W";
                    case 3: return "Th";
                    default: return "";
                }

            }
            case 'd': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "W";
                    case 3: return "Th";
                    default: return "";
                }

            }
            case 'e': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T";
                    case 3: return "Th";
                    default: return "";
                }

            }
            case 'f': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T";
                    case 3: return "F";
                    default: return "";
                }

            }
            case 'g': {
                switch (pos)
                {
                    case 1: return "T";
                    case 2: return "F";
                    default: return "";
                }

            }
            case 'H': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T";
                    case 3: return "Th";
                    default: return ""; 
                }
                
            }
            case 'J': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "W";
                    case 3: return "Th";
                    default: return ""; 
                }
                
            }
            case 'K': {
                switch (pos)
                {
                    case 1: return "W";
                    case 2: return "F";
                    default: return ""; 
                }
                
            }
            case 'L': {
                switch (pos)
                {
                    case 1: return "Th";
                    case 2: return "F";
                    default: return ""; 
                }
                
            }
            case 'M': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T";
                    default: return ""; 
                }
                
            }
            case 'P': {
                switch (pos)
                {
                    case 1: return "M"; 
                    default: return ""; 
                }
                
            }
            case 'Q': {
                switch (pos)
                {
                    case 1: return "T"; 
                    default: return ""; 
                }
                
            }
            case 'R': {
                switch (pos)
                {
                    case 1: return "W"; 
                    default: return ""; 
                }
                
            }
            case 'S': {
                switch (pos)
                {
                    case 1: return "Th"; 
                    default: return ""; 
                }
                
            }
            case 'T': {
                switch (pos)
                {
                    case 1: return "F"; 
                    default: return ""; 
                }
                
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
