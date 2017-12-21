package in.ac.iitm.students.fragments;

/**
 * Created by Sarath on 06/10/17.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends android.app.DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Calendar myCalendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light, this, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day","id","android")).setVisibility(View.GONE);

        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Do something with the date chosen by the user
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        UpdateLabel();
    }

    private void UpdateLabel() {

        String myFormat = "MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        switch (ProfileAccomAddFragment.flagForDate) {
            case "FromDate" :  ProfileAccomAddFragment.accomFromyear.setText(sdf.format(myCalendar.getTime())); ProfileAccomAddFragment.flagForDate="";break;
            case  "ToDate":
                ProfileAccomAddFragment.accomToyear.setText(sdf.format(myCalendar.getTime())); ProfileAccomAddFragment.flagForDate="";break;
            default:  ;
        }

        switch (ProfileAccomEditFragment.flagForDate) {
            case "FromDate" :  ProfileAccomEditFragment.accomPencilFromyear.setText(sdf.format(myCalendar.getTime())); ProfileAccomEditFragment.flagForDate="";break;
            case  "ToDate":
                ProfileAccomEditFragment.accomPencilToyear.setText(sdf.format(myCalendar.getTime())); ProfileAccomEditFragment.flagForDate="";break;
            default:;
        }

    }
}