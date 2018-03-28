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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.MessMenu;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.Utils;

/**
 * Created by sam10795 on 14/2/18.
 */

public class MessMenuAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<MessMenu> menuArrayList;
    private String messtype;
    int currentDay;

    public MessMenuAdapter(Context context, ArrayList<MessMenu> menuArrayList, String messtype)
    {
        super(context, R.layout.item_mess_menu, menuArrayList);
        this.context = context;
        this.menuArrayList = menuArrayList;
        this.messtype = messtype;
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

        final MessMenu menu = menuArrayList.get(position);
        menuholder.messday.setText(menu.getDay());

        //currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
       //menuArrayList.smoothScrollToPosition(currentDay);

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
                viewDialog(menu);
            }
        });

        if(Utils.getprefBool(menu.getMenu()+menu.getDay()+menu.getMenutype()+menu.getMenu(),context))
        {
            menuholder.rate.setVisibility(View.INVISIBLE);
        }
        else
        {
            menuholder.rate.setVisibility(View.VISIBLE);
        }

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

    private void viewDialog(final MessMenu menu)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_menu_rating);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button button = dialog.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Rating","R"+ratingBar.getRating());
                rateMenu(menu,ratingBar.getRating());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void rateMenu(final MessMenu menu, final float rating)
    {

        String url_mess = "https://students.iitm.ac.in/studentsapp/messmenu/ratemenu.php";
        StringRequest request = new StringRequest(Request.Method.POST, url_mess, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response",response);
                Toast.makeText(context,"Your rating has been recorded",Toast.LENGTH_SHORT).show();
                Utils.saveprefBool(menu.getMenu()+menu.getDay()+menu.getMenutype()+menu.getMenu(),true,context);
                //Utils.saveprefString(UtilStrings.MESS,response,getApplicationContext());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error","Unable to fetch");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("menutype",menu.getMenutype());
                params.put("rating",Float.toString(rating));
                params.put("messtype",messtype);
                params.put("day",menu.getDay());
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
