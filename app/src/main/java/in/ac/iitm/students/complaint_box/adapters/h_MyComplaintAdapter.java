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
import in.ac.iitm.students.complaint_box.activities.main.HostelComplaintsActivity;
import in.ac.iitm.students.complaint_box.objects.Complaint;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by harisanker on 22/6/17.
 */

public class h_MyComplaintAdapter extends RecyclerView.Adapter<h_MyComplaintAdapter.ViewHolder> {
    private ArrayList<Complaint> mDataset;
    private Activity activity;
    private Context context;
    private SharedPreferences sharedPref;
    private Button bn_resolve;
    private CoordinatorLayout coordinatorLayout;
    private InputStream stream;


    public h_MyComplaintAdapter(ArrayList<Complaint> myDataset, Activity a, Context c, CoordinatorLayout coordinatorLayout) {
        mDataset = myDataset;
        activity = a;
        context = c;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    public h_MyComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.h_my_complaint_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        TextView tv_name = (TextView) holder.view.findViewById(R.id.tv_name);
        TextView tv_hostel = (TextView) holder.view.findViewById(R.id.tv_hostel);
        TextView tv_resolved = (TextView) holder.view.findViewById(R.id.tv_is_resolved);
        TextView tv_title = (TextView) holder.view.findViewById(R.id.tv_title);
        TextView tv_tags = (TextView) holder.view.findViewById(R.id.tv_tags);
        TextView tv_description = (TextView) holder.view.findViewById(R.id.tv_description);
        ImageView iv_profile = (ImageView) holder.view.findViewById(R.id.imgProfilePicture);
        bn_resolve = (Button) holder.view.findViewById(R.id.bn_resolve);

        final Complaint hComplaint = mDataset.get(position);
        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + hComplaint.getRollNo().toUpperCase() + ".JPG";
        Picasso.with(context)
                .load(urlPic)
                .placeholder(R.color.cardview_shadow_end_color)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(iv_profile);


        tv_name.setText(hComplaint.getName());
        tv_hostel.setText(hComplaint.getHostel());
        tv_resolved.setText(hComplaint.isResolved() ? "Resolved" : "Unresolved");
        tv_title.setText(hComplaint.getTitle());
        tv_description.setText(hComplaint.getDescription());

        if (hComplaint.getCustom()) {
            tv_tags.setText(hComplaint.getTag());
        } else {
            String str = "Proximity: " + hComplaint.getProximity();
            tv_tags.setText(str);
        }

        final String mUUID = hComplaint.getUid();


        bn_resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://students.iitm.ac.in/studentsapp/complaints_portal/hostel_complaints/resolve.php";
                //String url = "https://rockstarharshitha.000webhostapp.com/hostel_complaints/resolve.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                        notifyItemChanged(holder.getAdapterPosition());
                                        makeSnackbar("Complaint is resolved");
                                        Intent intent = new Intent(activity, HostelComplaintsActivity.class);
                                        activity.startActivity(intent);

                                    } else if (status.equals("0")) {
                                        makeSnackbar("Error resolving the complaint");
                                    }
                                } else if (name.equals("error")) {
                                    reader.nextString();
                                    makeSnackbar("Error resolving the complaint");

                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                            makeSnackbar("Error resolving the complaint");
                        } finally {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                makeSnackbar("Error resolving the complaint");
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeSnackbar("Error resolving the complaint");
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

        if (hComplaint.isResolved()) {
            bn_resolve.setText("");
            bn_resolve.setBackgroundColor(ContextCompat.getColor(context, R.color.resolved_colour));
            bn_resolve.setClickable(false);
        } else {
            bn_resolve.setText("Resolve");
            bn_resolve.setBackgroundColor(ContextCompat.getColor(context, R.color.unresolved_colour));
            bn_resolve.setClickable(true);
        }

        if (hComplaint.getName().equals("Institute MobOps")) {
            iv_profile.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            tv_hostel.setText(hComplaint.getHostel());
            bn_resolve.setClickable(false);

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

