package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    ImageButton ibAgent;
    ImageButton ibCustomer;
    ImageButton ibSupplier;
    TextView tvUpdate;

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
                Toast ts=Toast.makeText(com.travelexperts.temobile.MainActivity.this,"Loading Agents List", Toast.LENGTH_SHORT);
                ts.show();
                Intent intent=new Intent(getApplicationContext(),AgentActivity.class);
                startActivity(intent);
            }
        });

        ibCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast ts=Toast.makeText(com.travelexperts.temobile.MainActivity.this,"Loading Customers List", Toast.LENGTH_SHORT);
                ts.show();
                Intent intent=new Intent(getApplicationContext(),CustomerActivity.class);
                startActivity(intent);
            }
        });

        ibSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast ts=Toast.makeText(com.travelexperts.temobile.MainActivity.this,"Supplier Clicked", Toast.LENGTH_SHORT);
                ts.show();
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast ts=Toast.makeText(com.travelexperts.temobile.MainActivity.this,"User Update will pop-up", Toast.LENGTH_SHORT);
                ts.show();
            }
        });
    }
}
