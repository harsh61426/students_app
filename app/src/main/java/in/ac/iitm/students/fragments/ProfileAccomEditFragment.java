package in.ac.iitm.students.fragments;

/**
 * Created by Ajay on 06/10/17.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.adapters.ProfileAccomAdapter;

public class ProfileAccomEditFragment extends Fragment {
    static EditText accomPencilOrgan,accomPencilPos,accomPencilFromyear,accomPencilToyear;
    RadioGroup radioGroup;
    public static String flagForDate="",radioStatus="" ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_activity_accom_edit,container,false);

        ProfileActivity.activity = "ProfileAccomEditFragment";

        accomPencilOrgan = (EditText) v.findViewById(R.id.profile_accom_organ_edittext);
        accomPencilPos= (EditText) v.findViewById(R.id.profile_accom_pos_edittext);
        accomPencilFromyear= (EditText) v.findViewById(R.id.profile_accom_fromyear_edittext);
        accomPencilToyear = (EditText) v.findViewById(R.id.profile_accom_toyear_edittext);

        accomPencilOrgan.setText(ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomOrgan);
        accomPencilPos.setText(ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomPos);
        accomPencilFromyear.setText(ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomFromyear);
        accomPencilToyear.setText(ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomToyear);
        radioStatus = ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).radioStatus;
        if(radioStatus.equals("Organisation")){
            accomPencilOrgan.setHint("Organisation");
            accomPencilPos.setHint("Position in Organisation");
        }else{
            accomPencilOrgan.setHint("Project Title");
            accomPencilPos.setHint("Project description");
        }

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroupAccom);
        radioGroup.setVisibility(View.GONE);

        accomPencilToyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "ToDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(ProfileActivity.fragmentManager,"datepicker");
            }
        });

        accomPencilFromyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "FromDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(ProfileActivity.fragmentManager,"datepicker");
            }
        });

        accomPencilOrgan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(radioStatus.equals("Organisation")){
                    if(accomPencilOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Organisation name is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilOrgan.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPencilOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project title is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilOrgan.requestFocus();
                        return true;
                    }
                }
                accomPencilPos.requestFocus();
                return true;
            }
        });

        accomPencilPos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(radioStatus.equals("Organisation")){
                    if(accomPencilPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Position is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilPos.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPencilPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project description is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilPos.requestFocus();
                        return true;
                    }
                }

                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                accomPencilPos.clearFocus();
                return false;
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.profile_accom_save_action_button){
            //Checking for validation.
            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

            //Checking validation
            if((accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name and position.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (!(accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Position name is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if ((accomPencilOrgan.getText().toString().length()==0) && !(accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }


            // Saving the data.
            builder.setMessage("Do you want to save changes?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomOrgan    = accomPencilOrgan   .getText().toString();
                            ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomPos      = accomPencilPos     .getText().toString();
                            ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomFromyear = accomPencilFromyear.getText().toString();
                            ProfileAccomAdapter.adapterData.get(ProfileAccomAdapter.postionValue).accomToyear   = accomPencilToyear  .getText().toString();
                            startActivity(new Intent(getActivity(),ProfileActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel",null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }
}
