package in.ac.iitm.students.complaint_box.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.complaint_box.activities.h_Comments;
import in.ac.iitm.students.complaint_box.objects.h_Complaint;
import in.ac.iitm.students.others.MySingleton;

/**
 * Created by lenovo on 23/12/17.
 */

public class g_ComplaintAdapter extends RecyclerView.Adapter<g_ComplaintAdapter.ViewHolder> {
    private ArrayList<h_Complaint> mDataset;
    private int mstatus;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;
    private boolean latest = false;
    private TextView tv_upvote;
    private TextView tv_downvote;
    private Button bn_upvote;
    private Button bn_downvote;
    private Button bn_resolve;


    public g_ComplaintAdapter(ArrayList<h_Complaint> myDataset, Activity a, Context c, Boolean latest) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.latest = latest;

    }

    @Override
    public g_ComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v;
        if (latest) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hostel_complaints_latest_complaint_card, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hostel_complaints_my_complaint_card, parent, false);
        }

        g_ComplaintAdapter.ViewHolder vh = new g_ComplaintAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(g_ComplaintAdapter.ViewHolder holder, final int position) {
//        holder.mTextView.setText(mDataset[position]);
        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView tv_resolved = (TextView) holder.view.findViewById(R.id.tv_is_resolved);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) holder.view.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        if (latest) tv_upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        if (latest) tv_downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView tv_comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        if (latest) bn_upvote = (Button) holder.view.findViewById(R.id.bn_upvote);
        if (latest) bn_downvote = (Button) holder.view.findViewById(R.id.bn_downvote);
        Button bn_comment = (Button) holder.view.findViewById(R.id.bn_comment);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout = (LinearLayout) holder.view.findViewById(R.id.ll_comment);
        final ImageButton bn_more_rooms = (ImageButton)holder.view.findViewById(R.id.more_rooms);
        if(!latest) bn_resolve = (Button)holder.view.findViewById(R.id.bn_resolve);


        final h_Complaint hComplaint = mDataset.get(position);

        tv_name.setText(hComplaint.getName());
        //TODO change narmada to IITM
        tv_hostel.setText(sharedPref.getString("hostel", "Narmada"));
        tv_resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
        tv_title.setText(hComplaint.getTitle());
        tv_description.setText(hComplaint.getDescription());
        if (latest) tv_upvote.setText("" + hComplaint.getUpvotes());
        if (latest) tv_downvote.setText("" + hComplaint.getDownvotes());
        tv_comment.setText("" + hComplaint.getComments());
        if (hComplaint.getTag() != null && hComplaint.getTag().equals("")) tv_tags.setVisibility(View.INVISIBLE);
        else tv_tags.setText(hComplaint.getTag());

        //todo use glide and get profile picture
        if (hComplaint.getName() != null && hComplaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            tv_hostel.setText(hComplaint.getHostel());
        }

        final String mUUID = hComplaint.getUid();

        if (hComplaint.isResolved()) {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.resolved_colour));

            if (latest) bn_upvote.setClickable(false);
            if (latest) bn_downvote.setClickable(false);
            bn_comment.setClickable(false);

        } else {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unresolved_colour));

            if (latest) bn_upvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/general_complaints/vote.php";
                    String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/vote.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsObject = new JSONObject(response);

                                if (jsObject.has("error")) {
                                    Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                } else if (jsObject.has("status")) {
                                    String status = jsObject.getString("status");
                                    if (status == "1") {
                                        increaseUpvotes();
                                        notifyItemChanged(position);
                                    } else {
                                        Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        //to POST params
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            //get hostel from prefs
                            //put some dummy for now
                            params.put("HOSTEL", "narmada");
                            params.put("UUID", mUUID);
                            params.put("VOTE", "1");
                            params.put("ROLL_NO", "ae11d001");
                            return params;
                        }
                    };
                    MySingleton.getInstance(activity).addToRequestQueue(request);
                }

                private void increaseUpvotes() {
                    int upvote_no = hComplaint.getUpvotes();
                    hComplaint.setUpvotes(upvote_no + 1);
                }
            });

            if (latest) bn_downvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/general_complaints/vote.php";
                    String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/vote.php";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsObject = new JSONObject(response);
                                if (jsObject.has("error")) {
                                    Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                } else if (jsObject.has("status")) {
                                    String status = jsObject.getString("status");
                                    if (status == "1") {
                                        increaseDownvotes();
                                        notifyItemChanged(position);
                                    } else {
                                        Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        //to POST params
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            //get hostel from prefs
                            //put some dummy for now
                            params.put("HOSTEL", "narmada");
                            params.put("UUID", mUUID);
                            params.put("VOTE", "0");
                            params.put("ROLL_NO", "ae11d001");
                            return params;
                        }

                    };
                    MySingleton.getInstance(activity).addToRequestQueue(request);
                }

                private void increaseDownvotes() {
                    int downvote_no = hComplaint.getDownvotes();
                    hComplaint.setDownvotes(downvote_no + 1);
                }
            });
        }

        bn_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, h_Comments.class);
                intent.putExtra("cardData", hComplaint);
                activity.startActivity(intent);
            }
        });

        bn_more_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, bn_more_rooms);

                MenuInflater inflater = popup.getMenuInflater();


                String[] roomNumber = hComplaint.getMoreRooms().split(",");

                for (String s:roomNumber) {
                    //adding items to menu
                    popup.getMenu().add(Menu.NONE,Menu.NONE,Menu.NONE,s);

                }
                //inflating popup menu from xml resource
                inflater.inflate(R.menu.more_rooms_popup, popup.getMenu());
                popup.show();
            }
        });

        if(!latest) bn_resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if custom hComplaint
                String url = "https://rockstarharshitha.000webhostapp.com/general_complaints/resolve.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsObject = new JSONObject(response);
                            if (jsObject.has("error")) {
                                Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                            } else if (jsObject.has("status")) {
                                String status = jsObject.getString("status");
                                if (status == "1") {
                                    notifyItemChanged(position);
                                } else {
                                    Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //to POST params
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        //get hostel from prefs
                        //put some dummy for now
                        params.put("HOSTEL", "narmada");
                        params.put("UUID", mUUID);
                        return params;
                    }

                };
                MySingleton.getInstance(activity).addToRequestQueue(request);

            }
        });

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
