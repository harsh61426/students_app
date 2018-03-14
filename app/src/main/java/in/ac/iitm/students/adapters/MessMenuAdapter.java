package in.ac.iitm.students.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.MessMenu;

/**
 * Created by sam10795 on 14/2/18.
 */

public class MessMenuAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<MessMenu> menuArrayList;

    public MessMenuAdapter(Context context, ArrayList<MessMenu> menuArrayList)
    {
        super(context, R.layout.item_mess_menu, menuArrayList);
        this.context = context;
        this.menuArrayList = menuArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder menuholder;
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_mess_menu, parent, false);
            menuholder = new ViewHolder();
            menuholder.messday = convertView.findViewById(R.id.mess_day);
            menuholder.menutype = convertView.findViewById(R.id.menu_time);
            menuholder.rate = convertView.findViewById(R.id.rate);
            menuholder.r = convertView.findViewById(R.id.menu_rating);
            menuholder.messmenu = convertView.findViewById(R.id.mess_menu);
            convertView.setTag(menuholder);
        }
        else {
            menuholder = (ViewHolder)convertView.getTag();
        }

        MessMenu menu = menuArrayList.get(position);
        menuholder.messday.setText(menu.getDay());

        if(menu.getMenutype().equalsIgnoreCase("Breakfast"))
        {
            menuholder.menutype.setText(R.string.breakfast);
            menuholder.messday.setVisibility(View.VISIBLE);
        }
        else if(menu.getMenutype().equalsIgnoreCase("Lunch"))
        {
            menuholder.menutype.setText(R.string.lunch);
            menuholder.messday.setVisibility(View.GONE);
        }
        else
        {
            menuholder.menutype.setText(R.string.dinner);
            menuholder.messday.setVisibility(View.GONE);
        }

        menuholder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDialog();
            }
        });

        menuholder.r.setRating(menu.getRating());

        menuholder.messmenu.setText(menu.getMenu());

        return convertView;
    }

    static class ViewHolder
    {
        TextView messmenu;
        TextView rate;
        RatingBar r;
        TextView messday;
        TextView menutype;
    }

    private void viewDialog()
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_menu_rating);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button button = dialog.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Rating","R"+ratingBar.getRating());
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
