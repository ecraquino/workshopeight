package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.travelexperts.temobile.R.id.lvAgents;

public class SignInActivity extends Activity {
    Button btnSignIn;
    EditText txtUserId;
    EditText txtPassword;
    CheckBox cbRemember;
    String userId;
    String passWord;
    StringBuffer sb = new StringBuffer();
    String teServer=new TEServer().getServerName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtUserId=(EditText)findViewById(R.id.txtUserId);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId=txtUserId.getText().toString();
                passWord=txtPassword.getText().toString();
                new CheckAgent().execute();
            }
        });
    }

    class CheckAgent extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
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
            String storedPassword;
            super.onPostExecute(aVoid);
            cbRemember=(CheckBox) findViewById(R.id.cbRemember);

                try {
                    JSONObject c = new JSONObject(sb.toString());
                    storedPassword = c.getString("agtPassword");
                    if (storedPassword.equals(passWord)) {
                        //Toast.makeText(com.travelexperts.temobile.SignInActivity.this, "Successful", Toast.LENGTH_LONG).show();
                        if (cbRemember.isChecked()) {
                            SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("login", userId);
                            ed.commit();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(com.travelexperts.temobile.SignInActivity.this, "Invalid User/Password Combination", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(com.travelexperts.temobile.SignInActivity.this, "Invalid User/Password Combination", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
        }
    }
}
