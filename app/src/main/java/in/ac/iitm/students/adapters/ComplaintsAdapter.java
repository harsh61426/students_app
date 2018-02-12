package in.ac.iitm.students.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.g_Comments;
import in.ac.iitm.students.objects.Complaints;
import in.ac.iitm.students.objects.Student;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by sam10795 on 11/2/18.
 */

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder>{

    public static int DATA_CHANGED = 0;
    private ArrayList<Complaints> mDataset;
    private int mstatus;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;
    private boolean latest = false;
    private Button bn_resolve;
    private CoordinatorLayout coordinatorLayout;
    private InputStream stream;
    private Student stu;

    public ComplaintsAdapter(ArrayList<Complaints> myDataset, Activity a, Context c, Boolean latest, CoordinatorLayout coordinatorLayout) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.latest = latest;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public ComplaintsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.g_item_complaint, parent, false);

        stu=new Student();
        ComplaintsAdapter.ViewHolder vh = new ComplaintsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ComplaintsAdapter.ViewHolder holder, int position) {

        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_date =(TextView)holder.view.findViewById(R.id.date);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) holder.view.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        TextView tv_upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        TextView tv_downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView tv_comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        //TextView tv_trending = (TextView) holder.view.findViewById(R.id.tv_trending);
        Button bn_upvote = (Button) holder.view.findViewById(R.id.bn_upvote);
        Button bn_downvote = (Button) holder.view.findViewById(R.id.bn_downvote);
        Button bn_comment = (Button) holder.view.findViewById(R.id.bn_comment);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout = (LinearLayout) holder.view.findViewById(R.id.ll_title);
        //RelativeLayout relativeLayout=(RelativeLayout)holder.view.findViewById(R.id.rl_name);

        final Complaints complaint = mDataset.get(position);
        if (!complaint.getRollNo().equals("X")) {
            String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + complaint.getRollNo().toUpperCase() + ".JPG";
            Picasso.with(context)
                    .load(urlPic)
                    .placeholder(R.color.cardview_shadow_end_color)
                    .error(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(iv_profile);
        }


        tv_name.setText(complaint.getName());

        DateFormat df = new SimpleDateFormat("dd MMM yy");
        DateFormat pf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tv_date.setText(df.format(pf.parse(complaint.getDate())));
        }
        catch (ParseException pe)
        {
            Log.e("DateParse","Error parsing "+complaint.getDate());
            tv_date.setText(complaint.getDate());
            Log.e("date",complaint.getDate());
        }
        tv_title.setText(complaint.getTitle());
        tv_description.setText(complaint.getDescription());
        tv_upvote.setText(Integer.toString(complaint.getUpvotes()));
        tv_downvote.setText(Integer.toString(complaint.getDownvotes()));
        tv_comment.setText(Integer.toString(complaint.getComments()));
        tv_tags.setText(complaint.getTag());

        if (complaint.getName() != null && complaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
        }

        final String mUUID = complaint.getUid();

        if (complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.acaf_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.resaf_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.sgs_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.cocas_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.has_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.culsec_lit_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.culsec_arts_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.iar_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.speaker_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.sports_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.mitr_roll)) ||
                complaint.getRollNo().equalsIgnoreCase(context.getString(R.string.cfi_roll))) {

            tv_name.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorSecondaryDark));
        }
        else
        {
            tv_name.setTextColor(ContextCompat.getColor(context.getApplicationContext(),R.color.colorPrimary));
        }
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stu.setHostel(complaint.getHostel());
                stu.setName(complaint.getName());
                stu.setRollno(complaint.getRollNo().toUpperCase());
                stu.setGender('m');
                viewDetails(stu);
            }
        });
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
                                        if (Utils.getprefString(UtilStrings.ROLLNO, context).equalsIgnoreCase(complaint.getRollNo())) {
                                            DATA_CHANGED = 1;
                                        }

                                    } else if (status.equals("3")) {
                                        makeSnackbar("Already up-voted");
                                    } else if (status.equals("2")) {
                                        Log.d("lollz", "rev");
                                        int upvotes = mDataset.get(pos).getUpvotes();
                                        int downvotes = mDataset.get(pos).getDownvotes();
                                        mDataset.get(pos).setUpvotes(upvotes + 1);
                                        mDataset.get(pos).setDownvotes(downvotes - 1);
                                        notifyItemChanged(pos);
                                        if (Utils.getprefString(UtilStrings.ROLLNO, context).equalsIgnoreCase(complaint.getRollNo())) {
                                            DATA_CHANGED = 1;
                                        }

                                    } else {
                                        makeSnackbar("Error up-voting the complaint");
                                    }
                                } else if (name.equals("error")) {
                                    if (!reader.nextString().equals("Same vote")) {
                                        makeSnackbar("Error up-voting the complaint");

                                    }
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
                                        if (Utils.getprefString(UtilStrings.ROLLNO, context).equalsIgnoreCase(complaint.getRollNo())) {
                                            DATA_CHANGED = 1;
                                        }

                                    } else if (status.equals("2")) {
                                        int upvotes = mDataset.get(pos).getUpvotes();
                                        int downvotes = mDataset.get(pos).getDownvotes();
                                        mDataset.get(pos).setUpvotes(upvotes - 1);
                                        mDataset.get(pos).setDownvotes(downvotes + 1);
                                        notifyItemChanged(pos);
                                        if (Utils.getprefString(UtilStrings.ROLLNO, context).equalsIgnoreCase(complaint.getRollNo())) {
                                            DATA_CHANGED = 1;
                                        }

                                    } else if (status.equals("3")) {
                                        makeSnackbar("Already down-voted");
                                    } else {
                                        makeSnackbar("Error down-voting the complaint");
                                    }
                                } else if (name.equals("error")) {
                                    if (!reader.nextString().equals("Same vote")) {
                                        makeSnackbar("Error down-voting the complaint");

                                    }
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
                intent.putExtra("cardData", complaint);
                activity.startActivity(intent);
            }
        });

        if (complaint.getName().equals("Institute MobOps")) {
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

    private void viewDetails(Student student)
    {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Student details");
        dialog.setContentView(R.layout.dialog_details);
        TextView rollno = (TextView)dialog.findViewById(R.id.d_rollno);
        rollno.setText(student.getRollno());
        TextView name = (TextView)dialog.findViewById(R.id.d_name);
        name.setText(student.getName());
        TextView room = (TextView)dialog.findViewById(R.id.d_room);
        room.setText(student.getHostel());
        CircleImageView photo = (CircleImageView)dialog.findViewById(R.id.d_photo);Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority("photos.iitm.ac.in")
                .appendPath("byroll.php")
                .appendQueryParameter("roll", student.getRollno());

        String url = builder.build().toString();

        Picasso.with(context).load(url).
                placeholder(R.drawable.dummypropic).
                error(R.drawable.dummypropic).
                fit().centerCrop().
                into(photo);

        dialog.show();
    }
    
}
