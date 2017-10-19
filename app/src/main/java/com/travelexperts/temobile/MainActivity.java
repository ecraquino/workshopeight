/*
 *  Ref: PROJ207.W3
 *  Application Name: Android Application
 *  Description: This program controls the Main Activity View
 *  Module: Main Activity Controller
 *  Author: Elmer Raquino
 *  Date Created: Sep 2017
 *  Version 2.0
 *
 */

package com.travelexperts.temobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static com.travelexperts.temobile.R.id.menu_logout;

public class MainActivity extends Activity {
    ImageButton ibAgent;
    ImageButton ibCustomer;
    ImageButton ibSupplier;
    TextView tvUpdate;
    StringBuffer sb;

    int loginTries=0;
    EditText emailaddr;
    EditText password;
    String userId;
    String passWord;
    String teServer=new TEServer().getServerName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_b);

        ibAgent=(ImageButton)findViewById(R.id.ibAgent);
        ibCustomer=(ImageButton)findViewById(R.id.ibCustomer);
        ibSupplier=(ImageButton)findViewById(R.id.ibSupplier);
        tvUpdate=(TextView)findViewById(R.id.tvUpdate);

        ibAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AgentActivity.class);
                startActivity(intent);
            }
        });

        ibCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CustomerActivity.class);
                startActivity(intent);
            }
        });

        ibSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(com.travelexperts.temobile.MainActivity.this,"Supplier Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginDialog();
            }
        });
        getActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main_b,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_logout:
                SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("login","");
                ed.commit();
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void callLoginDialog() {
        loginTries++;
        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.activity_popup_login);
        myDialog.setCancelable(true);

        Button login = (Button) myDialog.findViewById(R.id.yourloginbtnID);
        emailaddr = (EditText) myDialog.findViewById(R.id.youremailID);
        password = (EditText) myDialog.findViewById(R.id.yourpasswordID);
        ImageButton btnCancel=(ImageButton) myDialog.findViewById(R.id.btnCancel);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             userId=emailaddr.getText().toString();
             passWord=password.getText().toString();
             new reCheckAgent().execute();
             myDialog.cancel();
           }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });
    }

    class reCheckAgent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            sb = new StringBuffer();
            String userURL=teServer+"/agents/getuserid/"+userId;
            try {
                url = new URL(userURL);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String json;
                while ((json = br.readLine()) != null)
                {
                    sb.append(json);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String storedPassword="";
            super.onPostExecute(aVoid);
            try {
                JSONObject c = new JSONObject(sb.toString());
                storedPassword = c.getString("agtPassword");
                //String usersIdn=c.getString("agtUsername");

                if (storedPassword.equals(passWord)) {
                    Agent ag=new Agent(
                            c.getInt("agentId"),
                            c.getString("agtFirstName"),
                            c.getString("agtMiddleInitial"),
                            c.getString("agtLastName"),
                            c.getString("agtBusPhone"),
                            c.getString("agtEmail"),
                            c.getString("agtPosition"),
                            c.getInt("agencyId"));
                    Intent intent = new Intent(getApplicationContext(), AgentUpdateActivity.class);
                    intent.putExtra("agentud", ag);
                    intent.putExtra("agentUID", c.getString("agtUsername"));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(com.travelexperts.temobile.MainActivity.this, "Invalid User/Password Combination", Toast.LENGTH_LONG).show();
                    if (loginTries<5){
                        callLoginDialog();
                    }
                    else{
                        lockedOut();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(com.travelexperts.temobile.MainActivity.this, "Invalid User/Password Combination", Toast.LENGTH_LONG).show();
                if (loginTries<5){
                    callLoginDialog();
                }
                else{
                    lockedOut();
                }
            }
        }

        protected void lockedOut(){
            Toast.makeText(com.travelexperts.temobile.MainActivity.this, "You have reached maximum retries, an email has been sent to the Administrator", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
