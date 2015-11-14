package com.example.derekchiu.reconnect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by derekchiu on 11/13/15.
 */
public class MainScreen extends Activity {

    Intent prevIntent = getIntent();
    TextView nameView;
    ImageView idpic;
    String username, userid, useridNum;
    Bundle successExtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        successExtras = prevIntent.getExtras();

        idpic = (ImageView) findViewById(R.id.idpic);
        nameView = (TextView) findViewById(R.id.nameView);
        if (successExtras != null) {
            successExtras = prevIntent.getExtras();
            userid = successExtras.getString("access_token");
        }
        Log.i("userid", userid);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

//        Log.i("getAccessToken", accessToken.toString());

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject objects, GraphResponse response) {
                        Log.d("response", response.toString());
                        try {
                            username = objects.getString("name");
                            Log.d("name", username);
                            nameView.setText(username);
                            nameView.setTextSize(25);
                            idpic.setImageBitmap(getFacebookProfile(userid));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected static Bitmap getFacebookProfile(String userid) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userid + "picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        return bitmap;
    }

}
