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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
import in.ac.iitm.students.complaint_box.activities.h_Comments;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by harisanker on 22/6/17.
 */

public class h_ComplaintAdapter extends RecyclerView.Adapter<h_ComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;
    private boolean latest = false;
    private Button bn_resolve;
    private CoordinatorLayout coordinatorLayout;
   // private Complaint hComplaint;
    private InputStream stream;


    public h_ComplaintAdapter(ArrayList<Complaint> myDataset, Activity a, Context c, Boolean latest, CoordinatorLayout coordinatorLayout) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.latest = latest;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public h_ComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v;
        if (latest) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.h_latest_complaint_card, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.h_my_complaint_card, parent, false);
        }

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mTextView.setText(mDataset[position]);
        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView tv_resolved = (TextView) holder.view.findViewById(R.id.tv_is_resolved);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) holder.view.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        TextView tv_upvote = (TextView) holder.view.findViewById(R.id.tv_upvote);
        TextView tv_downvote = (TextView) holder.view.findViewById(R.id.tv_downvote);
        TextView tv_comment = (TextView) holder.view.findViewById(R.id.tv_comment);
        Button bn_upvote = (Button) holder.view.findViewById(R.id.bn_upvote);
        Button bn_downvote = (Button) holder.view.findViewById(R.id.bn_downvote);
        final Button bn_comment = (Button) holder.view.findViewById(R.id.bn_comment);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        LinearLayout linearLayout = (LinearLayout) holder.view.findViewById(R.id.ll_comment);
        final ImageButton bn_more_rooms = (ImageButton)holder.view.findViewById(R.id.more_rooms);
        if(!latest) bn_resolve = (Button)holder.view.findViewById(R.id.bn_resolve);

       final Complaint hComplaint = mDataset.get(position);
        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + hComplaint.getRollNo().toUpperCase() + ".JPG";
        Picasso.with(context)
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(iv_profile);


        tv_name.setText(hComplaint.getName());
        tv_hostel.setText(hComplaint.getHostel());
        tv_resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
        tv_title.setText(hComplaint.getTitle());
        tv_description.setText(hComplaint.getDescription());
        tv_upvote.setText("" + hComplaint.getUpvotes());
        tv_downvote.setText("" + hComplaint.getDownvotes());
        tv_comment.setText("" + hComplaint.getComments());


        final String mUUID = hComplaint.getUid();

        if (hComplaint.isResolved()) {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.resolved_colour));

            bn_upvote.setClickable(false);
            bn_upvote.setAlpha(0.5f);
            bn_downvote.setClickable(false);
            bn_downvote.setAlpha(0.5f);
            if (!latest) bn_resolve.setVisibility(View.GONE);

        } else {
            linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unresolved_colour));

            bn_upvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/vote.php";
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
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Error up-voting the complaint", Snackbar.LENGTH_LONG);
                            snackbar.show();
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

            });

            bn_downvote.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/vote.php";
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

            });
        }

        bn_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (hComplaint.isResolved() && hComplaint.getComments() == 0) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No Comments", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(context, h_Comments.class);
                    intent.putExtra("cardData", hComplaint);
                    activity.startActivity(intent);
                }

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
                String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/resolve.php";
                //String url = "https://rockstarharshitha.000webhostapp.com/hostel_complaints/resolve.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsObject = new JSONObject(response);
                            if (jsObject.has("error")) {

                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Error resolving the complaint", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                //Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();

                            } else if (jsObject.has("status")) {
                                String status = jsObject.getString("status");
                                if (status == "1") {
                                    notifyItemChanged(holder.getAdapterPosition());

                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Complaint is resolved", Snackbar.LENGTH_LONG)
                                            .setAction("UNDO", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Code this", Snackbar.LENGTH_SHORT);
                                                    snackbar1.show();
                                                }
                                            });

                                    snackbar.show();

                                } else {

                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Error resolving the complaint", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    //Toast.makeText(activity, jsObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                        params.put("HOSTEL", Utils.getprefString(UtilStrings.HOSTEl, context));
                        params.put("UUID", mUUID);
                        return params;
                    }

                };
                MySingleton.getInstance(activity).addToRequestQueue(request);

            }
        });

        if (hComplaint.getCustom()) {
            if (hComplaint.getTag().equals("")) tv_tags.setVisibility(View.GONE);
            else tv_tags.setText(hComplaint.getTag());
            bn_more_rooms.setVisibility(View.GONE);

        } else {
            tv_tags.setVisibility(View.GONE);
        }

        if (hComplaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            tv_hostel.setText(hComplaint.getHostel());
            bn_comment.setClickable(false);
            bn_upvote.setClickable(false);
            bn_downvote.setClickable(false);
            bn_more_rooms.setClickable(false);
            if (!latest) bn_resolve.setClickable(false);

            bn_upvote.setAlpha(1);
            bn_downvote.setAlpha(1);
            //Toast.makeText(activity, "doodle", Toast.LENGTH_SHORT).show();
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

