package in.ac.iitm.students.others;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.HomeNotifObject;

/**
 * Created by dell on 07-08-2017.
 */

public class CustomDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView title, content;
    public ImageButton link, location, calender, dismiss_button;
    private HomeNotifObject notifObject;

    public CustomDialog(Activity a, HomeNotifObject notifObject) {
        super(a);
        c = a;
        this.notifObject = notifObject;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notif_card);
        link = (ImageButton) findViewById(R.id.dialog_link);
        location = (ImageButton) findViewById(R.id.dialog_location);
        calender = (ImageButton) findViewById(R.id.dialog_calender);
        dismiss_button = (ImageButton) findViewById(R.id.dialog_dismiss);

        TextView tv_title = (TextView) findViewById(R.id.dialog_title);
        TextView tv_content = (TextView) findViewById(R.id.dialog_content);
        tv_title.setText(notifObject.title);
        tv_content.setText(notifObject.detail);

        link.setOnClickListener(this);
        location.setOnClickListener(this);
        calender.setOnClickListener(this);
        dismiss_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_link:

                break;
            case R.id.dialog_calender:

                break;
            case R.id.dialog_location:

                break;
            case R.id.dialog_dismiss:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}