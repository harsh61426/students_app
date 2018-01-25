package in.ac.iitm.students.complaint_box.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.BunksAdapter;
import in.ac.iitm.students.complaint_box.objects.CommentObj;

/**
 * Created by sam10795 on 25/1/18.
 */

public class g_commentsAdapter extends ArrayAdapter {
    
    private ArrayList<CommentObj> mDataset;
    private Context context;
    
    public g_commentsAdapter(Context context, ArrayList<CommentObj> commentObjs)
    {
        super(context, R.layout.h_item_comment, commentObjs);
        this.context = context;
        this.mDataset = commentObjs;
    }

    public void addComment(CommentObj cmnt) {

        if (mDataset.get(0).getName().equals("Institute MobOps")) {
            mDataset.add(0, cmnt);
            mDataset.remove(1);
            notifyDataSetChanged();
        } else {
            mDataset.add(0, cmnt);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.h_item_comment,parent,false);
            holder = new CommentHolder();
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            holder.tv_description = (TextView)convertView.findViewById(R.id.tv_description);
            holder.dp = (CircleImageView) convertView.findViewById(R.id.imgProfilePicture); 
            convertView.setTag(holder);
        }
        else {
            holder = (CommentHolder) convertView.getTag();
        }
        
        holder.tv_name.setText(mDataset.get(position).getName());

        DateFormat df = new SimpleDateFormat("dd MMM yy");
        DateFormat pf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            holder.tv_date.setText(df.format(pf.parse(mDataset.get(position).getDate())));
        }
        catch (ParseException pe)
        {
            Log.e("DateParse","Error parsing "+mDataset.get(position).getDate());
            holder.tv_date.setText(mDataset.get(position).getDate());
            Log.e("date",mDataset.get(position).getDate());
        }
        
        holder.tv_description.setText(mDataset.get(position).getCommentStr());

        if (!mDataset.get(position).getRollNo().equals("X")) {
            String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + mDataset.get(position).getRollNo().toUpperCase() + ".JPG";
            Picasso.with(context)
                    .load(urlPic)
                    .placeholder(R.color.cardview_shadow_end_color)
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.dp);
        }


        if (mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.acaf_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.resaf_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.sgs_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.cocas_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.has_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.culsec_lit_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.culsec_arts_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.iar_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.speaker_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.sports_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.mitr_roll)) ||
                mDataset.get(position).getRollNo().equalsIgnoreCase(context.getString(R.string.cfi_roll))) {

            holder.tv_name.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorSecondaryDark));
        }
        else
        {
            holder.tv_name.setTextColor(ContextCompat.getColor(context.getApplicationContext(),R.color.colorPrimary));
        }
        
        return convertView;
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    static class CommentHolder
    {
        TextView tv_name, tv_date, tv_description;
        CircleImageView dp;
    }
    
}
