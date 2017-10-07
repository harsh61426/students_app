package in.ac.iitm.students.fragments;

/**
 * Created by Ajay on 06/10/17.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.objects.ProfileAccomDetails;
import in.ac.iitm.students.others.ProfileAccomDetailArray;

public class ProfileAccomAddFragment extends Fragment {

    public static EditText accomOrgan,accomPos,accomFromyear,accomToyear;

    public static RadioButton radioButtonOrganisation,radioButtonProject;
    public static String radioStatus="Organisation";
    String firstEditText="";
    String secondeditText="";

    static String flagForDate="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_activity_accom_edit,container,false);

        ProfileActivity.activity = "ProfileAccomAddFragment";

        accomOrgan = (EditText) v.findViewById(R.id.profile_accom_organ_edittext);
        accomPos = (EditText) v.findViewById(R.id.profile_accom_pos_edittext);
        accomFromyear = (EditText) v.findViewById(R.id.profile_accom_fromyear_edittext);
        accomToyear = (EditText) v.findViewById(R.id.profile_accom_toyear_edittext);

        // Selecting between organisation and project.
        radioButtonOrganisation = (RadioButton) v.findViewById(R.id.profile_radio_button_organisation);
        radioButtonProject = (RadioButton) v.findViewById(R.id.profile_radio_Button_Project);
        radioButtonOrganisation.setChecked(true);


        accomToyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "ToDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(ProfileActivity.fragmentManager,"datepicker");
            }
        });

        accomFromyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "FromDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(ProfileActivity.fragmentManager,"datepicker");
            }
        });

        accomOrgan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(radioStatus.equals("Organisation")){
                    if(accomOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Organisation name is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomOrgan.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project title is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomOrgan.requestFocus();
                        return true;
                    }
                }
                accomPos.requestFocus();
                return true;
            }
        });

        accomPos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(radioStatus.equals("Organisation")){
                    if(accomPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Position is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPos.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project description is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPos.requestFocus();
                        return true;
                    }
                }

                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                accomPos.clearFocus();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.profile_accom_save_action,menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.profile_accom_save_action_button){

            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            if(radioStatus.equals("Organisation")){
                firstEditText="Organisation";
                secondeditText="Position";
            }
            else {
                firstEditText="Project Title";
                secondeditText="Project description";
            }
            //Checking validation
            if((accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage(firstEditText+" and "+secondeditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }
            else if (!(accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage(secondeditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }
            else if ((accomOrgan.getText().toString().length()==0) && !(accomPos.getText().toString().length()==0)){
                builder.setMessage(firstEditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }

            // Saving the data.
            //grabbing the details entered in the fields into cardview(RecyclerView's Adapter data).
            ProfileAccomDetails current=new ProfileAccomDetails();
            current.accomOrgan= ProfileAccomAddFragment.accomOrgan.getText().toString();
            current.accomPos  = ProfileAccomAddFragment.accomPos.getText().toString();
            current.accomFromyear = ProfileAccomAddFragment.accomFromyear.getText().toString();
            current.accomToyear = ProfileAccomAddFragment.accomToyear.getText().toString();
            current.radioStatus = ProfileAccomAddFragment.radioStatus;
            ProfileAccomDetailArray.makeAccomCard(current);


            Toast.makeText(getActivity(),"Saved.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),ProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }


}
