package com.opeyy.shubham.opeyy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        new GetProfileAsyncTask().execute("https://app12548-app12548.wedeploy.io/person/"+message);

//        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

        //get from REST API
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to go back", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    class GetProfileAsyncTask extends AsyncTask<String, Void, Boolean> {

        JSONArray jarray;

        String data;

        String[] name,age,height,education,about;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                //establishing http connection
                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();

                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);


                JSONObject jsono = new JSONObject(data);
                jarray = jsono.getJSONArray("User");
                name=new String[jarray.length()];
                age=new String[jarray.length()];
                height=new String[jarray.length()];
                education=new String[jarray.length()];
                about=new String[jarray.length()];
                for(int i=0;i<jarray.length();i++)
                {
                    JSONObject o=jarray.getJSONObject(i);
                    name[i]=o.getString("name");
                    age[i]=o.getString("age");
                    height[i]=o.getString("height");
                    education[i]=o.getString("education");
                    about[i]=o.getString("about");
                }

                return true;

                //------------------>>

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
//            Toast.makeText(ProfileActivity.this,""+name.length,Toast.LENGTH_SHORT).show();

            TextView txtName,txtAge,txtHeight,txtEducation,txtAbout;

            txtName=(TextView)findViewById(R.id.txt);
            txtAge=(TextView)findViewById(R.id.txt_age);
            txtHeight=(TextView)findViewById(R.id.txt_height);
            txtEducation=(TextView)findViewById(R.id.txt_education);
            txtAbout=(TextView)findViewById(R.id.txt_about);

            txtName.setText("Name - "+name[0]);
            txtAge.setText("Age - "+age[0]);
            txtHeight.setText("Height - "+height[0]);
            txtEducation.setText("Education - "+education[0]);
            txtAbout.setText("About - "+about[0]);
        }
    }
}
