package in.ac.iitm.students.complaint_box.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.objects.h_CommentObj;


/**
 * Created by harshitha on 11/7/17.
 */

public class h_CommentsAdapter extends RecyclerView.Adapter<h_CommentsAdapter.ViewHolder> {
    private ArrayList<h_CommentObj> mDataset;
    private Context context;

    public h_CommentsAdapter(ArrayList<h_CommentObj> dataset,Context c) {
        mDataset = dataset;
        context = c;
    }

    @Override
    public h_CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.h_comment_item, parent, false);

        h_CommentsAdapter.ViewHolder vh = new h_CommentsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(h_CommentsAdapter.ViewHolder holder, int position) {
        TextView name = (TextView) holder.view.findViewById(R.id.ci_name);
        TextView roomNo = (TextView) holder.view.findViewById(R.id.ci_roomNo);
        TextView date = (TextView) holder.view.findViewById(R.id.ci_date);
        TextView comStr = (TextView) holder.view.findViewById(R.id.ci_comStr);
        LinearLayout linearLayout=(LinearLayout)holder.view.findViewById(R.id.comment_item);

        final h_CommentObj hCommentObj = mDataset.get(position);

        name.setText(hCommentObj.getName());
        roomNo.setText("Room No: " + hCommentObj.getRoomNo());
        date.setText(hCommentObj.getDate());
        comStr.setText(hCommentObj.getCommentStr());

        if(hCommentObj.getRollNo()== context.getString(R.string.acaf_roll) ||
                hCommentObj.getRollNo()==context.getString(R.string.resaf_roll)||
                hCommentObj.getRollNo()== context.getString(R.string.sgs_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.cocas_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.has_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.culsec_lit_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.culsec_arts_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.iar_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.speaker_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.sports_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.mitr_roll) ||
                hCommentObj.getRollNo()== context.getString(R.string.cfi_roll) ) {

            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGreen));
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;


        }
    }
}

