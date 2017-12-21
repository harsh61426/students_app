package in.ac.iitm.students.activities.main;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.fragments.CourseTimetableFragment;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by dell on 07-08-2017.
 */

public class EditCourseDialogActivity extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText courseid;
    public EditText slot;
    public Button updatebutton,removebutton;
    public CheckBox c1,c2,c3,c4;
    public TextView days;
    public char ch;
    public int x,y;
    int pos_final;
    int pos_init;
    boolean flag;
    String s;
    int number;
    char b;
    int prime[][]={{2, 3, 5, 7, 11, 13, 17, 19},{23, 29, 31, 37, 41, 43, 47, 53},{59, 61, 67, 71, 73, 79, 83, 89},{97, 101, 103, 107, 109, 113, 127, 131},{137, 139, 149, 151, 157, 163, 167, 173}};
    //public ImageButton link,location, calender, dismiss_button;

    public EditCourseDialogActivity(Activity a,char ch,int x,int y) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.ch=ch;
        this.x=x;
        this.y=y;
        pos_init=-1;
        pos_final=-1;
        flag=false;
        number=0;
        b=' ';
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("dialog","created");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_add_course);
        days=(TextView)findViewById(R.id.textView3);
        days.setVisibility(View.INVISIBLE);
        (courseid)=(EditText)findViewById(R.id.course_id);
        LinearLayout LL=(LinearLayout)findViewById(R.id.days);
        LL.setVisibility(View.VISIBLE);
        c1 = (CheckBox) findViewById(R.id.day1);
        //c1.setText("A");
        c2 = (CheckBox) findViewById(R.id.day2);
        c3 = (CheckBox) findViewById(R.id.day3);
        c4 = (CheckBox) findViewById(R.id.day4);
        c1.setText("This action should affect all occurences of the chosen slot in the current timetable");
        slot=(EditText) findViewById(R.id.slot);
        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.INVISIBLE);
        c3.setVisibility(View.INVISIBLE);
        c4.setVisibility(View.INVISIBLE);
        updatebutton = (Button) findViewById(R.id.add);
        updatebutton.setText("UPDATE");
        removebutton = (Button) findViewById(R.id.remove);
        removebutton.setVisibility(View.VISIBLE);

        s=(courseid.getText()).toString();

        number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getOwnerActivity());


        for(int i=0;i<number;i++){
            if((Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getOwnerActivity())).charAt(0)==(ch)) {
                pos_init = i;
                break;
            }


        }
        for(int i=0;i<number;i++){
            if((Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getOwnerActivity())).charAt(0)==(b)) {

                flag = true;
                pos_final = i;
                break;
            }
        }

    }



    @Override
    public void onClick(View v) {
        slot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if(s.length()!=0) {
                    b = s.charAt(0);
                    b = Character.toUpperCase(b);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Log.d("dialog","created");



        switch (v.getId()) {

            case R.id.add:
                if(c1.isChecked()){


                }
                else{
                    //Utils.saveprefString(UtilStrings.COURSE_NUM+pos_final+UtilStrings.COURSE_ID,s,getOwnerActivity());
                    if(flag) {

                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_final + UtilStrings.COURSE_DAYS, getOwnerActivity()) * prime[x][y], getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, Utils.getprefInt(UtilStrings.COURSE_NUM + pos_init + UtilStrings.COURSE_DAYS, getOwnerActivity()) / prime[x][y], getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getOwnerActivity())-2,getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_final+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_final+UtilStrings.BUNKS_TOTAL,getOwnerActivity())+2,getOwnerActivity());
                        //notifyAll();
                    }
                    else{
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,getOwnerActivity())/prime[x][y],getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSES_COUNT,number+1,getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+number+UtilStrings.COURSE_DAYS,prime[x][y],getOwnerActivity());
                        Utils.saveprefString(UtilStrings.COURSE_NUM+number+UtilStrings.COURSE_ID,s,getOwnerActivity());
                        Utils.saveprefString(UtilStrings.COURSE_NUM+number+UtilStrings.COURSE_SLOT,Character.toString(b),getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getOwnerActivity())-2,getOwnerActivity());
                        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_final+UtilStrings.BUNKS_TOTAL,2,getOwnerActivity());



                    }
                }


                break;
            case R.id.remove:

                if(c1.isChecked()){
                    Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,1,getOwnerActivity());
                    Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,0,getOwnerActivity());
                }
                else{
                    Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.COURSE_DAYS,getOwnerActivity())/prime[x][y],getOwnerActivity());
                    Utils.saveprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,Utils.getprefInt(UtilStrings.COURSE_NUM+pos_init+UtilStrings.BUNKS_TOTAL,getOwnerActivity())-2,getOwnerActivity());
                }


                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}