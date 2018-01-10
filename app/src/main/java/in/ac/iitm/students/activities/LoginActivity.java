package in.ac.iitm.students.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.HomeActivity;
import in.ac.iitm.students.others.MySingleton;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

/**
 * Created by sai_praneeth7777 on 03-Sep-16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "SIGNINTag";
    public GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progress,progress3;
    EditText username, password;
    Button login;
    private Class<?> cls;
    private Boolean isSignedIn = false;
    private int  RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;

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
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button ldap_login = (Button)this.findViewById(R.id.bt_ldap_login);
//        ldap_login.setSize(SignInButton.SIZE_STANDARD);

//        setGoogleSigninButtonText(ldap_login,"Ldap Login");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webServerId))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

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
                        showProgressDialog("Logging in...");
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog("Loading...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                hideProgressDialog();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account.getEmail().contains("smail.iitm.ac.in")){
                    // Signed in successfully, show authenticated UI.
                    firebaseAuthWithGoogle(account);
                }else{
                    signOut();
                    Toast.makeText(this,"SignIn only with your Smail",Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("panz", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show();
                updateUI(null);
            }
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                        Snackbar.make(findViewById(R.id.loginButton),"SignIn with your Smail",1);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog("Logging in...");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.bt_ldap_login), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }


    private void showProgressDialog(String message){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage(message);
        progress.show();
    }

    private void hideProgressDialog(){
        if(progress!=null)
            progress.dismiss();
    }

    public void updateUI(FirebaseUser account){
        if(account!=null){
            if(account.getEmail().contains("smail.iitm.ac.in")){
                isSignedIn = true;
                hideProgressDialog();
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
                            //Log.d("invalid login", response + "Error connecting to server !!");
                            Utils.clearpref(context);
                        } else {
                            MakeSnSnackbar(getString(R.string.error_connection));
                            //Log.d("invalid login", response + "Error connecting to server !!");
                            Utils.clearpref(context);
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MakeSnSnackbar(getString(R.string.error_connection));
                //Log.d("invalid login: ", error.toString() + "; Error connecting to server!");
                Utils.clearpref(context);
                //Log.d("brr", "onError");
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
