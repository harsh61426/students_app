package in.ac.iitm.students.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.activities.main.MessMenuActivity;
import in.ac.iitm.students.adapters.MessMenuAdapter;
import in.ac.iitm.students.objects.MessMenu;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class MessMenuFragment extends Fragment {

    private static final String ARG_MENU_TITLE = "menu";

    private String menutitle;

    private ArrayList<MessMenu> menuArrayList;
    private MessMenuAdapter adapter;
    int currentDay;
    String day;
    ListView listView;


    public MessMenuFragment() {
        // Required empty public constructor
    }

    public static MessMenuFragment newInstance(String menutitle) {
        MessMenuFragment fragment = new MessMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MENU_TITLE, menutitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menutitle = getArguments().getString(ARG_MENU_TITLE);
        }
        menuArrayList = new ArrayList<>();
        adapter = new MessMenuAdapter(getContext(),menuArrayList,menutitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mess_menu, container, false);
        listView = (ListView)view.findViewById(R.id.list_menu);
        listView.setAdapter(adapter);
        getMenu(menutitle);

        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return view;
    }


    private void getMenu(final String menuname)
    {
        String url_mess = "https://students.iitm.ac.in/studentsapp/messmenu/getmessmenu.php";
        StringRequest request = new StringRequest(Request.Method.POST, url_mess, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    response = jsonArray.toString();
                    Utils.saveprefString(UtilStrings.messmenu+menutitle, response, getContext());
                    int length = jsonArray.length();
                    for(int i=0;i<length;i++) {
                        Log.i("JSON", jsonArray.getString(i));
                        JSONObject object = jsonArray.getJSONObject(i);
                        menuArrayList.add(new MessMenu(object.getString("day"),
                                object.getString("menutype"), object.getString("menu"),
                                Float.parseFloat(object.getString("rating")),object.getInt("raters")));

                        listView.smoothScrollToPosition((currentDay*3)-1);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Utils.saveprefString(UtilStrings.MESS,response,getApplicationContext());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("Error","Unable to fetch");
                try {
                    String dataBuffer = Utils.getprefString(UtilStrings.messmenu+menutitle, getContext());
                    if (!dataBuffer.equals("")) {
                        String response = dataBuffer;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int length = jsonArray.length();
                            for(int i=0;i<length;i++) {
                                Log.i("JSON", jsonArray.getString(i));
                                JSONObject object = jsonArray.getJSONObject(i);
                                menuArrayList.add(new MessMenu(object.getString("day"),
                                        object.getString("menutype"), object.getString("menu"),
                                        Float.parseFloat(object.getString("rating")),object.getInt("raters")));
                                listView.smoothScrollToPosition((currentDay*3)-1);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        error.printStackTrace();


                    }
                } catch (EmptyStackException e) {
                    error.printStackTrace();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("menutype",menuname);
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
