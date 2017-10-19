package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    ArrayAdapter<Agent> adapter;
    ListView lvAgents;
    StringBuffer sb = new StringBuffer();
    String teServer=new TEServer().getServerName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_b);

        lvAgents = findViewById(R.id.lvAgents);
        new GetAgents().execute();

        lvAgents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),AgentDetailsActivity.class);
                intent.putExtra("agentd", adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    class GetAgents extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(teServer+"/agents");
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
            //ArrayAdapter<Agent> aadapter=new ArrayAdapter<Agent>(getApplicationContext(),android.R.layout.simple_list_item_1,alist);
            adapter=new ArrayAdapter<Agent>(AgentActivity.this,android.R.layout.simple_list_item_1,alist);
            try {
                JSONArray jsonArray= new JSONArray(sb.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    //Toast.makeText(com.travelexperts.temobile.AgentActivity.this,c.getString("AgtFirstName"),Toast.LENGTH_LONG).show();
                    Agent ag=new Agent(
                          c.getInt("agentId"),
                          c.getString("agtFirstName"),
                          c.getString("agtMiddleInitial"),
                          c.getString("agtLastName"),
                          c.getString("agtBusPhone"),
                          c.getString("agtEmail"),
                          c.getString("agtPosition"),
                          c.getInt("agencyId"));
                    alist.add(ag);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lvAgents.setAdapter(adapter);
        }
    }
}
