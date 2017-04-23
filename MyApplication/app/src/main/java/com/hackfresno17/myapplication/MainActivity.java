package com.hackfresno17.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentActivity;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.Api;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

//import java.io.FileDescriptor;
//import java.io.PrintWriter;
//import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;
    List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        providers = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            //user already signed in
            Log.d("AUTH", auth.getCurrentUser().getEmail());

        } else {
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setProviders(providers)
                .build(),
                RC_SIGN_IN);
        }
        findViewById(R.id.log_out_button).setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                Log.d("AUTH", auth.getCurrentUser().getEmail());
                startActivity(new Intent(this, Questionnaire.class)
                    .putExtra("my_token", response.getIdpToken()));
                finish();
                return;
            } else {
                Log.d("AUTH", "NOT AUTHENTICATED");
                if (response == null){

                    return;
                }

                if(response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    return;
                }

                if(response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    return;
                }
            }
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.log_out_button) {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AUTH", "USER LOGGED OUT");
                        //startActivity(new Intent(MyActivity.this, AuthUI.SignInActivity.class));
                        finish();
                    }
                });
        }

    }
}