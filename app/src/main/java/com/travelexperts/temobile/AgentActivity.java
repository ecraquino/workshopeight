package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class AgentActivity extends Activity {
    ListView lvAgents;
//    Button btnBack;
    StringBuffer sb = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_b);

        lvAgents = findViewById(R.id.lvAgents);
        new GetAgents().execute();
//        btnBack=findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    class GetAgents extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                //url = new URL("http://travelexperts.ddns.net:8080/workshopseven/webapi/agents");
                url = new URL("http://travelexperts.ddns.net:8080/TEdata/cal/agents");
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
            super.onPostExecute(aVoid);
            //JSONObject jsonObject=new JSONObject();
            ArrayList alist=new ArrayList();
            ArrayAdapter<Agent> aadapter=new ArrayAdapter<Agent>(getApplicationContext(),android.R.layout.simple_list_item_1,alist);

            try {
                JSONArray jsonArray= new JSONArray(sb.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);

                    //Toast t=Toast.makeText(com.travelexperts.temobile.AgentActivity.this,c.getString("AgtFirstName"),Toast.LENGTH_LONG);
                    //t.show();

                    Agent ag=new Agent(c.getInt("AgentId"),c.getString("AgtFirstName"),sb.toString(),c.getString("AgtLastName"),c.getString("AgtBusPhone"),c.getString("AgtEmail"),c.getString("AgtPosition"),c.getInt("AgencyId"));
                    alist.add(ag);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            lvAgents.setAdapter(aadapter);
        }
    }
}
