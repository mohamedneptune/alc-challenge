package com.alc.diarymohamed.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alc.diarymohamed.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by mohamed on 26/06/18.
 */


public class AuthenticationActivity extends AppCompatActivity {

    // TAG is for show some tag logs in LOG screen.
    public static final String TAG = AuthenticationActivity.class.getSimpleName();

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 22;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Sing out button.
    Button SignOutButton;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        SignOutButton= (Button) findViewById(R.id.sign_out);

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(AuthenticationActivity.this)
                .enableAutoManage(AuthenticationActivity.this , new GoogleApiClient
                        .OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignInMethod();

            }
        });

        // Adding Click Listener to User Sign Out button.
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSignOutFunction();

            }
        });

        setTitle(getResources().getString(R.string.activity_authentication_title));

    }


    // Sign In function Starts From Here.
    public void UserSignInMethod(){

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()){

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);
            }

        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount
                .getIdToken(), null);

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // Showing Log out button.
                            SignOutButton.setVisibility(View.VISIBLE);

                            // Hiding Login in button.
                            signInButton.setVisibility(View.GONE);

                            SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString("user_name", firebaseUser.getDisplayName().toString());
                            editor.apply();

                            Intent intent = new Intent(AuthenticationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(AuthenticationActivity.this,
                                    "Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UserSignOutFunction() {

        // Sing Out the User.
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        // Write down your any code here which you want to execute After Sign Out.

                        // Printing Logout toast message on screen.
                        Toast.makeText(AuthenticationActivity.this,
                                "Logout Successfully", Toast.LENGTH_LONG).show();

                    }
                });

        // After logout Hiding sign out button.
        SignOutButton.setVisibility(View.GONE);

        // After logout setting up login button visibility to visible.
        signInButton.setVisibility(View.VISIBLE);
    }
}
