package in.ac.iitm.students.others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.LoginActivity;
import in.ac.iitm.students.activities.main.HomeActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sathwik on 14-01-2017.
 */

public class LogOutAlertClass {

    GoogleApiClient mGoogleApiClient;

//    GoogleSignInClient mGoogleSignInClient;
    public void isSure(final Context context) {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();


//         Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        new AlertDialog.Builder(context)
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(mGoogleApiClient.isConnected()){
                            signOut(context);
                        }else {
                            Utils.clearpref(context);
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }



                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                .show();
    }

    private void signOut(final Context context){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
//                                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
//                                        context.startActivity(i);
                        Utils.clearpref(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                });

    }
//    private void signOut(final Context context) {
//
//    }
}
