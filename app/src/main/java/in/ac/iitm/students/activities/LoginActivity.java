package in.ac.iitm.students.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by sai_praneeth7777 on 03-Sep-16.
 */
public class LoginActivity extends AppCompatActivity {
    ProgressDialog progress,progress2,progress3;
    EditText username, password;
    Button login;
    private Class<?> cls;
    public GoogleSignInClient mGoogleSignInClient;
    private Boolean isSignedIn = false;
    private int  RC_SIGN_IN = 1;
    private String TAG = "SIGNINTag";

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String cls_name = "HomeActivity";
        if (getIntent().hasExtra("class")) {
            cls_name = getIntent().getExtras().get("class").toString();
        }
        cls = HomeActivity.class;
        try {
            cls = Class.forName(cls_name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = HomeActivity.class;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button ldap_login = (Button)this.findViewById(R.id.bt_ldap_login);
//        ldap_login.setSize(SignInButton.SIZE_STANDARD);

//        setGoogleSigninButtonText(ldap_login,"Ldap Login");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        final SignInButton signInButton = (SignInButton)this.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        ldap_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        username.setVisibility(View.GONE);
                        password.setVisibility(View.GONE);
                        login.setVisibility(View.GONE);
                        signIn();
                }
        });


        username = (EditText) this.findViewById(R.id.rollno);
        password = (EditText) this.findViewById(R.id.password);
        login = (Button) this.findViewById(R.id.loginButton);

        String roll_no = Utils.getprefString(UtilStrings.ROLLNO, this);
        String name = Utils.getprefString(UtilStrings.NAME, this);
        if (!roll_no.equals("") && !name.equals("")) {
            Intent downloadIntent;
            downloadIntent = new Intent(getBaseContext(), cls);
            startActivity(downloadIntent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                    if (Utils.isNetworkAvailable(getBaseContext())) {
                        progress3 = new ProgressDialog(LoginActivity.this);
                        progress3.setCancelable(false);
                        progress3.setMessage("Logging in...");
                        progress3.show();
                        PlacementLdaplogin(getBaseContext());
                    } else {
                        MakeSnSnackbar("No internet connection");
                    }

                } else {
                    MakeSnSnackbar("Enter your username and password");
                }

            }
        });

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    protected void setGoogleSigninButtonText(SignInButton signInButton, String buttonText) {
//        // Find the TextView that is inside of the SignInButton and set its text
////        signInButton.getBackground().setVisible(false,true);
//        signInButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setText(buttonText);
//            }
//            if(v instanceof ImageView){
//                ImageView iv = (ImageView) v;
//                iv.setVisibility(View.GONE);
//            }
//            if(v instanceof ImageButton){
//                ImageButton ib = (ImageButton) v;
//                ib.setVisibility(View.GONE);
//            }
//        }
//        return;
//    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Logging In...");
        progress.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progress.dismiss();
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
//            progress2 = new ProgressDialog(this);
//            progress2.setCancelable(false);
//            progress2.set("Signing in...");
//            progress2.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                        progress.dismiss();
                        Snackbar.make(findViewById(R.id.loginButton),"SignIn with your Smail",1);
                    }
                });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account.getEmail().contains("smail.iitm.ac.in")){
                // Signed in successfully, show authenticated UI.
                updateUI(account);
            }else{
                signOut();
                Toast.makeText(this,"SignIn only with your Smail",Toast.LENGTH_LONG).show();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"SignIn failed",Toast.LENGTH_LONG);
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    public void updateUI(GoogleSignInAccount account){
        if(account!=null){
            if(account.getEmail().contains("smail.iitm.ac.in")){
                isSignedIn = true;
                progress.dismiss();
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("sigin",account.getEmail().split("@")[0]);
                startActivity(intent);
            }else {
                signOut();
            }
        }else {
            Snackbar.make(findViewById(R.id.loginButton),"Error Occured",1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void PlacementLdaplogin(final Context context) {

        final String[] message = new String[1];
        final String[] DisplayName = new String[1];
        final String[] Hostel = new String[1];
        final String[] room = new String[1];
        final String[] reveal_place = new String[1];
        final String[] reveal_photo = new String[1];
        final int[] success = new int[1];
        final JSONObject[] responseJson = new JSONObject[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getString(R.string.LoginURl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("brr", response);

                        try {
                            responseJson[0] = new JSONObject(response);
                            success[0] = responseJson[0].getInt("success");
                            message[0] = responseJson[0].getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (success[0] == 1) {
                            JSONObject jsonResultObjuct = null;
                            try {
                                jsonResultObjuct = responseJson[0].getJSONArray("result").getJSONObject(0);
                                DisplayName[0] = jsonResultObjuct.getString("fullname");
                                room[0] = jsonResultObjuct.getString("room");
                                reveal_place[0] = jsonResultObjuct.getString("reveal_place");
                                reveal_photo[0] = jsonResultObjuct.getString("reveal_photo");
                                Hostel[0] = jsonResultObjuct.getString("hostel");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Log.d("valid login", responseBody + "ok");
                            Intent downloadIntent;
                            downloadIntent = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(downloadIntent);
                            Utils.saveprefString(UtilStrings.NAME, DisplayName[0], getBaseContext());
                            Utils.saveprefString(UtilStrings.HOSTEl, Hostel[0], getBaseContext());
                            Utils.saveprefString(UtilStrings.ROOM, room[0], getBaseContext());
                            Utils.saveprefInt(UtilStrings.REVEAL_PHOTO, Integer.parseInt(reveal_photo[0]), getBaseContext());
                            Utils.saveprefInt(UtilStrings.REVEAL_PLACE, Integer.parseInt(reveal_place[0]), getBaseContext());
                            Utils.saveprefString(UtilStrings.ROLLNO, username.getText().toString().toUpperCase(), getBaseContext());
                            Utils.saveprefBool(UtilStrings.LOGEDIN, true, context);

                            finish();
                        } else if (success[0] == 0) {
                            MakeSnSnackbar(message[0]);
                            Log.d("invalid login", response + "Error connecting to server !!");
                            Utils.clearpref(context);
                        } else {
                            MakeSnSnackbar(getString(R.string.error_connection));
                            Log.d("invalid login", response + "Error connecting to server !!");
                            Utils.clearpref(context);
                        }
                        progress3.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MakeSnSnackbar(getString(R.string.error_connection));
                Log.d("invalid login: ", error.toString() + "; Error connecting to server!");
                Utils.clearpref(context);
                Log.d("brr", "onError");
                progress.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("roll", username.getText().toString());
                params.put("pass", password.getText().toString());
                return params;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void MakeSnSnackbar(String text) {
        hideKeyboard();
        Snackbar snack = Snackbar.make(findViewById(R.id.container), text, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v instanceof TextView) {
                TextView t = (TextView) v;
                t.setTextColor(Color.RED);
            }
        }
        snack.show();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
