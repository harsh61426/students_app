package in.ac.iitm.students.complaint_box.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.g_Comments;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by lenovo on 23/12/17.
 */

public class g_ComplaintAdapter extends RecyclerView.Adapter<g_ComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;
    private int mstatus;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;
    private boolean latest = false;
    private Button bn_resolve;
    private CoordinatorLayout coordinatorLayout;
    private InputStream stream;


    public g_ComplaintAdapter(ArrayList<Complaint> myDataset, Activity a, Context c, Boolean latest, CoordinatorLayout coordinatorLayout) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.latest = latest;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public g_ComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v;

            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.g_complaint_card, parent, false);


        g_ComplaintAdapter.ViewHolder vh = new g_ComplaintAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mTextView.setText(mDataset[position]);
        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) holder.view.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        TextView tv_upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        TextView tv_downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView tv_comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        TextView tv_trending = (TextView) holder.view.findViewById(R.id.tv_trending);
        Button bn_upvote = (Button) holder.view.findViewById(R.id.bn_upvote);
        Button bn_downvote = (Button) holder.view.findViewById(R.id.bn_downvote);
        Button bn_comment = (Button) holder.view.findViewById(R.id.bn_comment);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout = (LinearLayout) holder.view.findViewById(R.id.ll_comment);
        RelativeLayout relativeLayout=(RelativeLayout)holder.view.findViewById(R.id.rl_name);

        final Complaint gComplaint = mDataset.get(position);
        if (!gComplaint.getRollNo().equals("X")) {
            String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + gComplaint.getRollNo().toUpperCase() + ".JPG";
            Picasso.with(context)
                    .load(urlPic)
                    .placeholder(R.color.cardview_shadow_end_color)
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(iv_profile);
        }


        tv_name.setText(gComplaint.getName());
        tv_hostel.setText(gComplaint.getHostel());
        tv_title.setText(gComplaint.getTitle());
        tv_description.setText(gComplaint.getDescription());
        tv_upvote.setText("" + gComplaint.getUpvotes());
        tv_downvote.setText("" + gComplaint.getDownvotes());
        tv_comment.setText("" + gComplaint.getComments());
        int p = position + 1;
        String trend = "#" + p;
        gComplaint.setTrending(trend);
        tv_trending.setText(trend);
        //if (gComplaint.getTag() != null && gComplaint.getTag().equals("")) tv_tags.setVisibility(View.INVISIBLE);
        //else
        tv_tags.setText(gComplaint.getTag());

        if (gComplaint.getName() != null && gComplaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            tv_hostel.setText(gComplaint.getHostel());
        }

        final String mUUID = gComplaint.getUid();

        if (gComplaint.getRollNo() == context.getString(R.string.acaf_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.resaf_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.sgs_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.cocas_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.has_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.culsec_lit_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.culsec_arts_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.iar_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.speaker_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.sports_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.mitr_roll) ||
                gComplaint.getRollNo() == context.getString(R.string.cfi_roll)) {

            relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red_background));
        }

        linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unresolved_colour));

        bn_upvote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/vote.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("chlk", "onResponse: " + response);
                        int pos = holder.getAdapterPosition();
                        Log.d("lollz", response);
                        stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                        JsonReader reader = null;
                        try {
                            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                            reader.setLenient(true);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        try {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String name = reader.nextName();
                                Log.e("name", name);
                                if (name.equals("status")) {
                                    String status = reader.nextString();
                                    if (status.equals("1")) {
                                        int upvotes = mDataset.get(pos).getUpvotes();
                                        mDataset.get(pos).setUpvotes(upvotes + 1);
                                        notifyItemChanged(pos);

                                    } else if (status.equals("3")) {
                                        makeSnackbar("Already up-voted");
                                    } else if (status.equals("2")) {
                                        Log.d("lollz", "rev");
                                        int upvotes = mDataset.get(pos).getUpvotes();
                                        int downvotes = mDataset.get(pos).getDownvotes();
                                        mDataset.get(pos).setUpvotes(upvotes + 1);
                                        mDataset.get(pos).setDownvotes(downvotes - 1);
                                        notifyItemChanged(pos);

                                    } else {
                                        makeSnackbar("Error up-voting the complaint");
                                    }
                                } else if (name.equals("error")) {
                                    reader.nextString();
                                    makeSnackbar("Error up-voting the complaint");
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                            makeSnackbar("Error up-voting the complaint");
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error up-voting the complaint");
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeSnackbar("Error up-voting the complaint");
                    }
                }) {
                    //to POST params
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        //get hostel from prefs
                        //put some dummy for now
                        params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, context));
                        params.put("UUID", mUUID);
                        params.put("VOTE", "1");
                        params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, context));
                        return params;
                    }
                };
                MySingleton.getInstance(activity).addToRequestQueue(request);
            }
        });

        bn_downvote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/gen_complaints/vote.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("chlk", "onResponse: " + response);
                        int pos = holder.getAdapterPosition();
                        Log.d("lollz", response);
                        stream = new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
                        JsonReader reader = null;
                        try {
                            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                            reader.setLenient(true);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        try {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String name = reader.nextName();
                                if (name.equals("status")) {
                                    String status = reader.nextString();
                                    if (status.equals("1")) {
                                        int downvotes = mDataset.get(pos).getDownvotes();
                                        mDataset.get(pos).setDownvotes(downvotes + 1);
                                        notifyItemChanged(pos);

                                    } else if (status.equals("2")) {
                                        int upvotes = mDataset.get(pos).getUpvotes();
                                        int downvotes = mDataset.get(pos).getDownvotes();
                                        mDataset.get(pos).setUpvotes(upvotes - 1);
                                        mDataset.get(pos).setDownvotes(downvotes + 1);
                                        notifyItemChanged(pos);

                                    } else if (status.equals("3")) {
                                        makeSnackbar("Already down-voted");
                                    } else {
                                        makeSnackbar("Error down-voting the complaint");
                                    }
                                } else if (name.equals("error")) {
                                    reader.nextString();
                                    makeSnackbar("Error down-voting the complaint");
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                            makeSnackbar("Error down-voting the complaint");
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error down-voting the complaint");
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeSnackbar("Error down-voting the complaint");
                    }
                }) {
                    //to POST params
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        //get hostel from prefs
                        //put some dummy for now
                        params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, context));
                        params.put("UUID", mUUID);
                        params.put("VOTE", "0");
                        params.put("ROLL_NO", Utils.getprefString(UtilStrings.ROLLNO, context));
                        return params;
                    }

                };
                MySingleton.getInstance(activity).addToRequestQueue(request);
            }
        });

        bn_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, g_Comments.class);
                intent.putExtra("cardData", gComplaint);
                activity.startActivity(intent);

            }
        });

        if (gComplaint.getName().equals("Institute MobOps")) {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.resolved_colour));

            bn_upvote.setClickable(false);
            bn_downvote.setClickable(false);
            bn_comment.setClickable(false);
        }

    }

    private void makeSnackbar(String msg) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
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
