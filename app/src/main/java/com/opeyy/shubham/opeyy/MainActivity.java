package com.opeyy.shubham.opeyy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {


    ListView list;

    //do get from DB
    String[] name = {

    } ;

    //do get from DB
    String[] age = {
    };

    //do get from DB
    Integer[] imageId = {
    };

    String[] ids={

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list=(ListView)findViewById(R.id.list);

        new SubmitMonthAsyncTask().execute("https://app12548-app12548.wedeploy.io/");


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                intent.putExtra("message", ids[position]);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "You Clicked at " +ids[position], Toast.LENGTH_SHORT).show();



            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("Are you sure,You want to Exit");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override

                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    class SubmitMonthAsyncTask extends AsyncTask<String, Void, Boolean> {

        JSONArray jarray;

        String data;

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
                ids=new String[jarray.length()];
                for(int i=0;i<jarray.length();i++)
                {
                    JSONObject o=jarray.getJSONObject(i);
                    name[i]=o.getString("name");
                    age[i]=o.getString("age");
                    ids[i]=o.getString("id");
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
//            Toast.makeText(MainActivity.this,""+name.length,Toast.LENGTH_SHORT).show();

            CustomAdapter adapter = new
                    CustomAdapter(name, age);

            list.setAdapter(adapter);
        }
    }

    class CustomAdapter extends BaseAdapter {

        String[] name,age;

//        Integer[] roll;

        public CustomAdapter(String[] name, String[] age) {
//            this.roll = roll;
            this.name = name;
            this.age = age;
        }

        @Override
        public int getCount() {
            return age.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView= inflater.inflate(R.layout.row, null, true);


            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
            TextView txtAge = (TextView) rowView.findViewById(R.id.txt_age);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            txtTitle.setText(name[position]);
            txtAge.setText(age[position]);

//            imageView.setImageResource(roll[position]);
            return rowView;
        }
    }
}
