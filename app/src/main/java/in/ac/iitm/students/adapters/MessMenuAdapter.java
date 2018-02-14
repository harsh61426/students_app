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

import in.ac.iitm.students.R;

/**
 * Created by sam10795 on 14/2/18.
 */

public class MessMenuAdapter extends ArrayAdapter {

    private Context context;

    public MessMenuAdapter(Context context)
    {
        super(context, R.layout.item_mess_menu);
        this.context = context;
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
            menuholder.messday = convertView.findViewById(R.id.mess_day);
            menuholder.messmenu = convertView.findViewById(R.id.mess_menu);
            menuholder.rate = convertView.findViewById(R.id.rate);
            menuholder.ratingBar = convertView.findViewById(R.id.menu_rating);
        }
        return super.getView(position, convertView, parent);
    }

    static class ViewHolder
    {
        TextView messday, messmenu, rate;
        RatingBar ratingBar;
    }
}
