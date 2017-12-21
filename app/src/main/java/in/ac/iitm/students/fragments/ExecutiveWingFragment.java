package in.ac.iitm.students.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.ac.iitm.students.R;
import in.ac.iitm.students.others.MySingleton;

/**
 * Created by admin on 10-01-2017.
 */

public class ExecutiveWingFragment extends Fragment implements View.OnClickListener {

    private static ExecutiveWingFragment sExecutiveWingFragment = null;
    private Context context;
    private ImageLoader mImageLoader;

    public static ExecutiveWingFragment getInstance() {
        if (sExecutiveWingFragment == null) {
            sExecutiveWingFragment = new ExecutiveWingFragment();
        }
        return sExecutiveWingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mImageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ew_fragment_ew, container, false);

        ImageView mAcaf_ImageView;
        ImageView mResaf_ImageView;
        ImageView mSgsImageView;
        ImageView mCocas_ImageView;
        ImageView mHas_ImageView;
        ImageView mLit_ImageView;
        ImageView mArts_ImageView;
        ImageView mIar_ImageView;
        ImageView mSpeaker_ImageView;
        ImageView mSportsImageView;
        ImageView mMitr_ImageView;
        ImageView mCfi_ImageView;


        ImageButton mAcaf_MailButton;
        ImageButton mResaf_MailButton;
        ImageButton mSgsMailButton;
        ImageButton mCocas_MailButton;
        ImageButton mHas_MailButton;
        ImageButton mLit_MailButton;
        ImageButton mArts_MailButton;
        ImageButton mIar_MailButton;
        ImageButton mSpeaker_MailButton;
        ImageButton mSportsMailButton;
        ImageButton mMitr_MailButton;
        ImageButton mCfi_MailButton;

        ImageButton mAcaf_PhoneButton;
        ImageButton mResaf_PhoneButton;
        ImageButton mSgsPhoneButton;
        ImageButton mCocas_PhoneButton;
        ImageButton mHas_PhoneButton;
        ImageButton mLit_PhoneButton;
        ImageButton mArts_PhoneButton;
        ImageButton mIar_PhoneButton;
        ImageButton mSpeaker_PhoneButton;
        ImageButton mSportsPhoneButton;
        ImageButton mMitr_PhoneButton;
        ImageButton mCfi_PhoneButton;

        // Profile Images

        mSgsImageView = (ImageView) v.findViewById(R.id.sgs_image);
        mSgsImageView.setTag(context.getResources().getString(R.string.sgs_name));
        setProfileImage(mSgsImageView, context.getResources().getString(R.string.sgs_roll));

        mAcaf_ImageView = (ImageView) v.findViewById(R.id.acaf_image);
        mAcaf_ImageView.setTag(context.getResources().getString(R.string.acaf_name));
        setProfileImage(mAcaf_ImageView, context.getResources().getString(R.string.acaf_roll));

        mResaf_ImageView = (ImageView) v.findViewById(R.id.resaf_image);
        mResaf_ImageView.setTag(context.getResources().getString(R.string.resaf_name));
        setProfileImage(mResaf_ImageView, context.getResources().getString(R.string.resaf_roll));


        mCocas_ImageView = (ImageView) v.findViewById(R.id.cocas_image);
        mCocas_ImageView.setTag(context.getResources().getString(R.string.cocas_name));
        setProfileImage(mCocas_ImageView, context.getResources().getString(R.string.cocas_roll));

        mHas_ImageView = (ImageView) v.findViewById(R.id.has_image);
        mHas_ImageView.setTag(context.getResources().getString(R.string.has_name));
        setProfileImage(mHas_ImageView, context.getResources().getString(R.string.has_roll));


        mLit_ImageView = (ImageView) v.findViewById(R.id.culsec_lit_image);
        mLit_ImageView.setTag(context.getResources().getString(R.string.culsec_lit_name));
        setProfileImage(mLit_ImageView, context.getResources().getString(R.string.culsec_lit_roll));

        mArts_ImageView = (ImageView) v.findViewById(R.id.culsec_arts_image);
        mArts_ImageView.setTag(context.getResources().getString(R.string.culsec_arts_name));
        setProfileImage(mArts_ImageView, context.getResources().getString(R.string.culsec_arts_roll));

        mIar_ImageView = (ImageView) v.findViewById(R.id.iar_image);
        mIar_ImageView.setTag(context.getResources().getString(R.string.iar_name));
        setProfileImage(mIar_ImageView, context.getResources().getString(R.string.iar_roll));

        mSpeaker_ImageView = (ImageView) v.findViewById(R.id.speaker_image);
        mSpeaker_ImageView.setTag(context.getResources().getString(R.string.speaker_name));
        setProfileImage(mSpeaker_ImageView, context.getResources().getString(R.string.speaker_roll));

        mMitr_ImageView = (ImageView) v.findViewById(R.id.mitr_image);
        mMitr_ImageView.setTag(context.getResources().getString(R.string.mitr_name));
        setProfileImage(mMitr_ImageView, context.getResources().getString(R.string.mitr_roll));

        mCfi_ImageView = (ImageView) v.findViewById(R.id.cfi_image);
        mCfi_ImageView.setTag(context.getResources().getString(R.string.cfi_name));
        setProfileImage(mCfi_ImageView, context.getResources().getString(R.string.cfi_roll));


        mSportsImageView = (ImageView) v.findViewById(R.id.sports_image);
        mSportsImageView.setTag(context.getResources().getString(R.string.sports_name));
        setProfileImage(mSportsImageView, context.getResources().getString(R.string.sports_roll));


        // Profile Images Over

        // Mail


        mAcaf_MailButton = (ImageButton) v.findViewById(R.id.mail_acaf);
        mAcaf_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.acaf_email));
        mAcaf_MailButton.setOnClickListener(this);

        mResaf_MailButton = (ImageButton) v.findViewById(R.id.mail_resaf);
        mResaf_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.resaf_email));
        mResaf_MailButton.setOnClickListener(this);

        mSgsMailButton = (ImageButton) v.findViewById(R.id.mail_sgs);
        mSgsMailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.sgs_email));
        mSgsMailButton.setOnClickListener(this);

        mCocas_MailButton = (ImageButton) v.findViewById(R.id.mail_cocas);
        mCocas_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.cocas_email));
        mCocas_MailButton.setOnClickListener(this);

        mHas_MailButton = (ImageButton) v.findViewById(R.id.mail_has);
        mHas_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.has_email));
        mHas_MailButton.setOnClickListener(this);


        mLit_MailButton = (ImageButton) v.findViewById(R.id.mail_culsec_lit);
        mLit_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.culsec_lit_email));
        mLit_MailButton.setOnClickListener(this);

        mArts_MailButton = (ImageButton) v.findViewById(R.id.mail_culsec_arts);
        mArts_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.culsec_arts_email));
        mArts_MailButton.setOnClickListener(this);

        mIar_MailButton = (ImageButton) v.findViewById(R.id.mail_iar);
        mIar_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.iar_email));
        mIar_MailButton.setOnClickListener(this);

        mSpeaker_MailButton = (ImageButton) v.findViewById(R.id.mail_speaker);
        mSpeaker_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.speaker_email));
        mSpeaker_MailButton.setOnClickListener(this);

        mSportsMailButton = (ImageButton) v.findViewById(R.id.mail_sports);
        mSportsMailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.sports_email));
        mSportsMailButton.setOnClickListener(this);


        mMitr_MailButton = (ImageButton) v.findViewById(R.id.mail_mitr);
        mMitr_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.mitr_email));
        mMitr_MailButton.setOnClickListener(this);

        mCfi_MailButton = (ImageButton) v.findViewById(R.id.mail_cfi);
        mCfi_MailButton.setTag(R.string.tag_mail, context.getResources().getString(R.string.cfi_email));
        mCfi_MailButton.setOnClickListener(this);


        // Mail Over

        // Phone

        mAcaf_PhoneButton = (ImageButton) v.findViewById(R.id.phone_acaf);
        mAcaf_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.acaf_phone));
        mAcaf_PhoneButton.setOnClickListener(this);

        mResaf_PhoneButton = (ImageButton) v.findViewById(R.id.phone_resaf);
        mResaf_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.resaf_phone));
        mResaf_PhoneButton.setOnClickListener(this);

        mSgsPhoneButton = (ImageButton) v.findViewById(R.id.phone_sgs);
        mSgsPhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.sgs_phone));
        mSgsPhoneButton.setOnClickListener(this);

        mCocas_PhoneButton = (ImageButton) v.findViewById(R.id.phone_cocas);
        mCocas_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.cocas_phone));
        mCocas_PhoneButton.setOnClickListener(this);

        mHas_PhoneButton = (ImageButton) v.findViewById(R.id.phone_has);
        mHas_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.has_phone));
        mHas_PhoneButton.setOnClickListener(this);


        mLit_PhoneButton = (ImageButton) v.findViewById(R.id.phone_culsec_lit);
        mLit_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.culsec_lit_phone));
        mLit_PhoneButton.setOnClickListener(this);

        mArts_PhoneButton = (ImageButton) v.findViewById(R.id.phone_culsec_arts);
        mArts_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.culsec_arts_phone));
        mArts_PhoneButton.setOnClickListener(this);

        mIar_PhoneButton = (ImageButton) v.findViewById(R.id.phone_iar);
        mIar_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.iar_phone));
        mIar_PhoneButton.setOnClickListener(this);

        mSpeaker_PhoneButton = (ImageButton) v.findViewById(R.id.phone_speaker);
        mSpeaker_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.speaker_phone));
        mSpeaker_PhoneButton.setOnClickListener(this);

        mSportsPhoneButton = (ImageButton) v.findViewById(R.id.phone_sports);
        mSportsPhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.sports_phone));
        mSportsPhoneButton.setOnClickListener(this);

        mMitr_PhoneButton = (ImageButton) v.findViewById(R.id.phone_mitr);
        mMitr_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.mitr_phone));
        mMitr_PhoneButton.setOnClickListener(this);

        mCfi_PhoneButton = (ImageButton) v.findViewById(R.id.phone_cfi);
        mCfi_PhoneButton.setTag(R.string.tag_phone, context.getResources().getString(R.string.cfi_phone));
        mCfi_PhoneButton.setOnClickListener(this);

        return v;
    }

    private void setProfileImage(ImageView imageView, String roll_no) {

        String urlPic = "https://ccw.iitm.ac.in/sites/default/files/photos/" + roll_no.toUpperCase() + ".JPG";
        Picasso.with(getActivity())
                .load(urlPic)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fit()
                .into(imageView);

    }

    @Override
    public void onClick(View v) {

        if (v.getTag(R.string.tag_mail) != null) {
            String[] email = {v.getTag(R.string.tag_mail).toString()};
            shareToGMail(email);

        } else if (v.getTag(R.string.tag_phone) != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + v.getTag(R.string.tag_phone).toString()));
            startActivity(intent);
        }


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
