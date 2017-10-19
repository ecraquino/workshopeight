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

public class CustomerActivity extends Activity {
    ArrayAdapter<Customer> adapter;
    ListView lvCustomers;
    StringBuffer sb = new StringBuffer();
    String teServer=new TEServer().getServerName2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_b);

        lvCustomers = findViewById(R.id.lvCustomers);
        new GetCustomers().execute();

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),CustomerDetailsActivity.class);
                intent.putExtra("customer", adapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    class GetCustomers extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {

                url = new URL(teServer+"/customers");
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
            ArrayList list=new ArrayList();
            //ArrayAdapter<Customer>
            //adapter=new ArrayAdapter<Customer>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
            adapter=new ArrayAdapter<Customer>(CustomerActivity.this,android.R.layout.simple_list_item_1,list);
            try {
                JSONArray jsonArray= new JSONArray(sb.toString());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    Customer customer=new Customer(
                            c.getInt("CustomerId"),
                            c.getString("CustFirstName"),
                            c.getString("CustLastName"),
                            c.getString("CustAddress"),
                            c.getString("CustCity"),
                            c.getString("CustProv"),
                            c.getString("CustPostal"),
                            c.getString("CustCountry"),
                            c.getString("CustHomePhone"),
                            c.getString("CustBusPhone"),
                            c.getString("CustEmail"),
                            c.getInt("AgentId")
                    );
                    //Toast t=Toast.makeText(com.elmer.a915.MainActivity.this,customer.CustFirstName,Toast.LENGTH_LONG);
                    //t.show();

                    list.add(customer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            lvCustomers.setAdapter(adapter);
        }
    }
}


