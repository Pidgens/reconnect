package com.example.derekchiu.reconnect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by derekchiu on 11/13/15.
 */
public class MainScreen extends Activity {

    TextView nameView;
    ImageView idpic;
    String username, userid, useridNum, link;
    Bundle successExtras;
    Bitmap bm;
    Button create, set, search;
    boolean forward = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Intent prevIntent = getIntent();
        successExtras = prevIntent.getExtras();

        nameView = (TextView) findViewById(R.id.nameView);

//        Log.i("userid", userid);
        userid = prevIntent.getStringExtra("userid");
        Log.i("userid", userid);
//        new getPicture().execute(userid);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

//        Log.i("getAccessToken", accessToken.toString());

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject objects, GraphResponse response) {
                        Log.d("response", response.toString());
                        try {
                            if (!false) {
                                username = objects.getString("name");
                                link = objects.getString("link");
                                new getPicture().execute(objects.getString("id"));
                                nameView.setText("Hey " + username.split(" ")[0] + "!");
                                nameView.setTextSize(28);
                            }
                        } catch (JSONException e) {
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

    protected class getPicture extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... userid) {
            try {
//                URL imageURL = new URL("https://graph.facebook.com/" + userid + "/picture?type=large");
                URL imageURL = new URL("https://graph.facebook.com/" + userid[0] + "/picture?type=large");
                Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }

        protected void onPostExecute(Bitmap bm) {
            idpic = (ImageView) findViewById(R.id.idpic);
            idpic.setImageBitmap(bm);
        }
    }

}
