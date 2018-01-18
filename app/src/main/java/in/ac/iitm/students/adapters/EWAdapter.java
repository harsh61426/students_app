package in.ac.iitm.students.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.ew_member;

/**
 * Created by sam10795 on 11/1/18.
 */

public class EWAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<ew_member> members;

    public EWAdapter(Context context, ArrayList<ew_member> members)
    {
        super(context, R.layout.item_ew_member);
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ew_member,parent,false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.ew_name);
            holder.tv_title = (TextView) convertView.findViewById(R.id.ew_title);
            holder.tv_call = (TextView) convertView.findViewById(R.id.ew_call);
            holder.tv_mail = (TextView) convertView.findViewById(R.id.ew_mail);
            holder.im_dp = (ImageView) convertView.findViewById(R.id.ew_img);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ew_member member = members.get(position);
        holder.tv_name.setText(member.getName());
        holder.tv_title.setText(member.getTitle());
        holder.tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + member.getContact()));
                context.startActivity(intent);
            }
        });
        holder.tv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email[] = new String[1];
                email[0] = member.getEmail();
                shareToGMail(email);
            }
        });
        setProfileImage(holder.im_dp,member.getRollno());
        return convertView;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    private void setProfileImage(ImageView imageView, String roll_no) {

        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
        Picasso.with(context)
                .load(urlPic)
                .placeholder(R.color.cardview_light_background)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(imageView);
    }

    public void shareToGMail(String[] email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.setType("text/plain");
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        context.startActivity(emailIntent);
    }

    static class ViewHolder
    {
        TextView tv_title, tv_name;
        TextView tv_call, tv_mail;
        ImageView im_dp;
    }
}
