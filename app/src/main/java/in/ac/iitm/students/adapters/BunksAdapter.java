package in.ac.iitm.students.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by SAM10795 on 19-06-2017.
 */

public class BunksAdapter extends ArrayAdapter {

    private ArrayList<Bunks> bunks;
    private Context context;
    private GridView gridView;
    public BunksAdapter(Context context, ArrayList<Bunks> bunks, GridView g)
    {
        super(context, R.layout.item_bunk, bunks);
        this.context = context;
        this.bunks = bunks;
        this.gridView = g;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_bunk,parent,false);
            holder = new ViewHolder();
            holder.slot = (TextView)convertView.findViewById(R.id.slot);
            holder.course = (TextView)convertView.findViewById(R.id.courseid);
            holder.bunkcount = (TextView)convertView.findViewById(R.id.left);
            holder.plus = (TextView) convertView.findViewById(R.id.plus);
            holder.minus = (TextView) convertView.findViewById(R.id.minus);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.slot.setText(Character.toString(bunks.get(position).getSlot())+" slot");
        holder.course.setText(bunks.get(position).getCourse_id());
        holder.bunkcount.setText(bunks.get(position).getBunk_done()+"/"+bunks.get(position).getBunk_tot());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context)==bunks.get(position).getBunk_tot()){
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.saveprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context)+1,context);
                bunks.get(position).setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context));
                gridView.setAdapter(new BunksAdapter(context, bunks,gridView));
                //((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
                //Utils.saveprefInt(,Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()),getActivity());
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context)==0){
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.saveprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context)-1,context);
                bunks.get(position).setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+position+UtilStrings.BUNKS_DONE,context));
                gridView.setAdapter(new BunksAdapter(context, bunks,gridView));

                //((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
                //Utils.saveprefInt(,Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()),getActivity());
            }
        });
        if(bunks.get(position).getBunk_done()*2>=bunks.get(position).getBunk_tot())
        {
            holder.bunkcount.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.slot.setTextColor(ContextCompat.getColor(context,R.color.red));

        }
        return convertView;

    }

    static class ViewHolder
    {
        TextView slot;
        TextView course;
        TextView bunkcount;
        TextView plus;
        TextView minus;
    }
}
