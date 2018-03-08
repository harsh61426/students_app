package in.ac.iitm.students.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.ProfileActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class MessMenuActivity extends AppCompatActivity {

    TextView text_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_menu);
        text_menu = (TextView) findViewById(R.id.text_menu);
        getMenu("South Indian 1");
    }
    private void getMenu(final String menuname)
    {

        String url_mess = "https://students.iitm.ac.in/studentsapp/messmenu/getmessmenu.php";
        StringRequest request = new StringRequest(Request.Method.POST, url_mess, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                text_menu.setText(response);
                //Utils.saveprefString(UtilStrings.MESS,response,getApplicationContext());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("Error","Unable to fetch");
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

        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
