package in.ac.iitm.students.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.adapters.EWAdapter;
import in.ac.iitm.students.objects.ew_member;

public class EWFragment extends Fragment {

    private Context context;
    private ImageLoader imageLoader;
    private ArrayList<ew_member> members;

    public EWFragment() {
    }

    public static EWFragment newInstance(String param1, String param2) {
        EWFragment fragment = new EWFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ew, container, false);
        GridView gridView = (GridView)view.findViewById(R.id.ew_grid);

        context = getActivity();

        members = new ArrayList<ew_member>();

        members.add(new ew_member(context.getResources().getString(R.string.sgs_name),
                context.getResources().getString(R.string.sgs_roll),
                context.getResources().getString(R.string.sgs_email),
                context.getResources().getString(R.string.sgs_phone),
                "Students General Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.resaf_name),
                context.getResources().getString(R.string.resaf_roll),
                context.getResources().getString(R.string.resaf_email),
                context.getResources().getString(R.string.resaf_phone),
                "Research Affairs Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.acaf_name),
                context.getResources().getString(R.string.acaf_roll),
                context.getResources().getString(R.string.acaf_email),
                context.getResources().getString(R.string.acaf_phone),
                "Academic Affairs Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.cocas_name),
                context.getResources().getString(R.string.cocas_roll),
                context.getResources().getString(R.string.cocas_email),
                context.getResources().getString(R.string.cocas_phone),
                "Co-Curricular Affairs Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.has_name),
                context.getResources().getString(R.string.has_roll),
                context.getResources().getString(R.string.has_email),
                context.getResources().getString(R.string.has_phone),
                "Hostel Affairs Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.culsec_arts_name),
                context.getResources().getString(R.string.culsec_arts_roll),
                context.getResources().getString(R.string.culsec_arts_email),
                context.getResources().getString(R.string.culsec_arts_phone),
                "CulSec - Arts"));

        members.add(new ew_member(context.getResources().getString(R.string.culsec_lit_name),
                context.getResources().getString(R.string.culsec_lit_roll),
                context.getResources().getString(R.string.culsec_lit_email),
                context.getResources().getString(R.string.culsec_lit_phone),
                "CulSec - Lit"));

        members.add(new ew_member(context.getResources().getString(R.string.iar_name),
                context.getResources().getString(R.string.iar_roll),
                context.getResources().getString(R.string.iar_email),
                context.getResources().getString(R.string.iar_phone),
                "I&AR Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.sports_name),
                context.getResources().getString(R.string.sports_roll),
                context.getResources().getString(R.string.sports_email),
                context.getResources().getString(R.string.sports_phone),
                "Sports Secretary"));

        members.add(new ew_member(context.getResources().getString(R.string.speaker_name),
                context.getResources().getString(R.string.speaker_roll),
                context.getResources().getString(R.string.speaker_email),
                context.getResources().getString(R.string.speaker_phone),
                "SAC Speaker"));

        members.add(new ew_member(context.getResources().getString(R.string.mitr_name),
                context.getResources().getString(R.string.mitr_roll),
                context.getResources().getString(R.string.mitr_email),
                context.getResources().getString(R.string.mitr_phone),
                "MITr Head"));

        members.add(new ew_member(context.getResources().getString(R.string.cfi_name),
                context.getResources().getString(R.string.cfi_roll),
                context.getResources().getString(R.string.cfi_email),
                context.getResources().getString(R.string.cfi_phone),
                "CFI, Students' Head"));

        gridView.setAdapter(new EWAdapter(context,members));

        return view;
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
}
