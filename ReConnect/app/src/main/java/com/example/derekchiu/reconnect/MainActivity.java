package com.example.derekchiu.reconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView descrip;
//    private Bundle successBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        descrip = (TextView) findViewById(R.id.descrip);
        descrip.setText("Here at reConnect we hope to bring Facebook friends into friends " +
                "maybe more than friends, a friendship that extends more than a scroll through " +
                "your newsfeed.");

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        info = (TextView) findViewById(R.id.info);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("ID/Auth Token", "User ID: " +
                        loginResult.getAccessToken().getUserId() +
                        "\n " +
                        "Auth Token: " +
                        loginResult.getAccessToken().getToken());
                Intent successScreen = new Intent(getApplicationContext(), MainScreen.class);
                successScreen.putExtra("access_token", loginResult.getAccessToken().getToken());
                successScreen.putExtra("userid", loginResult.getAccessToken().getUserId());
                startActivity(successScreen);
            }

            @Override
            public void onCancel() {
                info.setText("Login Attempted Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login Attempt failed.");
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


