package in.ac.iitm.students.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

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
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_mess_menu,parent,false);
            menuholder = new ViewHolder();

            MessMenu menu = menuArrayList.get(position);

            menuholder.messday = convertView.findViewById(R.id.mess_day);

            menuholder.messmenu_b = convertView.findViewById(R.id.mess_menu_b);
            menuholder.messmenu_b.setText(menu.getBreakfast());
            menuholder.rate_b = convertView.findViewById(R.id.rate_b);
            menuholder.b = convertView.findViewById(R.id.menu_rating_b);
            menuholder.b.setRating(menu.getRate_breakfast());

            menuholder.messmenu_l = convertView.findViewById(R.id.mess_menu_l);
            menuholder.messmenu_l.setText(menu.getLunch());
            menuholder.rate_l = convertView.findViewById(R.id.rate_l);
            menuholder.l = convertView.findViewById(R.id.menu_rating_l);
            menuholder.l.setRating(menu.getRate_lunch());

            menuholder.messmenu_d = convertView.findViewById(R.id.mess_menu_d);
            menuholder.messmenu_d.setText(menu.getDinner());
            menuholder.rate_d = convertView.findViewById(R.id.rate_d);
            menuholder.d = convertView.findViewById(R.id.menu_rating_d);
            menuholder.d.setRating(menu.getRate_dinner());
        }
        return super.getView(position, convertView, parent);
    }

    static class ViewHolder
    {
        TextView messmenu_b,messmenu_l,messmenu_d;
        TextView rate_b, rate_l, rate_d;
        RatingBar b,l,d;
        TextView messday;
    }
}
