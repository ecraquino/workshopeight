package com.travelexperts.temobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AgentUpdateActivity extends Activity {

    EditText txtFirstName;
    EditText txtMiddleInitial;
    EditText txtLastName;
    EditText txtBusPhone;
    EditText txtEmail;
    EditText txtPosition;
    EditText txtAgency;
    Button btnSave,btnPassword;

    EditText emailaddr;
    EditText password;

    String userName;
    String password1;
    String password2;


    String agentId;
    JSONObject agentJSON = new JSONObject();
    JSONObject agentPWJSON = new JSONObject();

    String teServer=new TEServer().getServerName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_update);

        txtFirstName= (EditText) findViewById(R.id.txtFirstName);
        txtMiddleInitial= (EditText)findViewById(R.id.txtMiddleInitial);
        txtLastName=(EditText)findViewById(R.id.txtLastName);
        txtBusPhone=(EditText)findViewById(R.id.txtBusPhone);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtPosition=(EditText)findViewById(R.id.txtPosition);
        txtAgency=(EditText)findViewById(R.id.txtAgency);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnPassword=(Button)findViewById(R.id.btnPassword);

        Intent intent=getIntent();
        Agent agent=(Agent)intent.getSerializableExtra("agentud");
        userName=intent.getStringExtra("agentUID");
        txtFirstName.setText(agent.getAgtFirstName());
        txtMiddleInitial.setText(agent.getAgtMiddleInitial());
        txtLastName.setText(agent.getAgtLastName());
        txtBusPhone.setText(agent.getAgtBusPhone());
        txtEmail.setText(agent.getAgtEmail());
        txtPosition.setText(agent.getAgtPosition());
        txtAgency.setText(agent.getAgencyId()+"");
        setTitle("Agent Id: " + agent.getAgentId());
        agentId=agent.getAgentId()+"";

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    agentJSON.put("agentId",agentId);
                    agentJSON.put("agtFirstName",txtFirstName.getText());
                    agentJSON.put("agtMiddleInitial",txtMiddleInitial.getText());
                    agentJSON.put("agtLastName",txtLastName.getText());
                    agentJSON.put("agtBusPhone",txtBusPhone.getText());
                    agentJSON.put("agtEmail",txtEmail.getText());
                    agentJSON.put("agtPosition",txtPosition.getText());
                    agentJSON.put("agencyId",txtAgency.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PutAgent().execute();
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginDialog();
            }
        });

    }

    class PutAgent extends AsyncTask<Void, Void, Void> {
        String response = "";
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;

            try {
                //url = new URL(teServer + "/agents/" + agentId);
                url = new URL(teServer + "/agents");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(agentJSON.toString());
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response=responseCode+"";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(com.travelexperts.temobile.AgentUpdateActivity.this,response, Toast.LENGTH_LONG).show();
        }
    }

    private void callLoginDialog() {
        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.activity_popup_password);
        myDialog.setCancelable(true);

        Button login = (Button) myDialog.findViewById(R.id.yourloginbtnID);
        emailaddr = (EditText) myDialog.findViewById(R.id.youremailID);
        password = (EditText) myDialog.findViewById(R.id.yourpasswordID);
        ImageButton btnCancel=(ImageButton) myDialog.findViewById(R.id.btnCancel);
        final CheckBox chkShowPW=(CheckBox) myDialog.findViewById(R.id.chkShowPW);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password1=emailaddr.getText().toString();
                password2=password.getText().toString();
                myDialog.cancel();
                if (password1.equals(password2)){
                    try {
                        agentPWJSON.put("agtUserId",userName);
                        agentPWJSON.put("agtPassword",password1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new PutAgentPassword().execute();
                }
                else{
                    Toast.makeText(com.travelexperts.temobile.AgentUpdateActivity.this,"PASSWORDS DO NOT MATCH. TRY AGAIN", Toast.LENGTH_LONG).show();
                    callLoginDialog();
                }
            }
        });

        chkShowPW.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (chkShowPW.isChecked()){
                    emailaddr.setInputType(InputType.TYPE_CLASS_TEXT );
                    password.setInputType(InputType.TYPE_CLASS_TEXT );
                }
                else{
                    emailaddr.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });
    }

    class PutAgentPassword extends AsyncTask<Void, Void, Void> {
        String response = "";
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;

            try {
                //url = new URL(teServer + "/agents/" + agentId);

                url = new URL(teServer + "/agents/password/"+ userName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(agentPWJSON.toString());
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response=responseCode+"";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(com.travelexperts.temobile.AgentUpdateActivity.this,response, Toast.LENGTH_LONG).show();
        }
    }
}
