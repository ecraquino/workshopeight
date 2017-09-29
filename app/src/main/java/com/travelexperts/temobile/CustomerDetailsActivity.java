package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CustomerDetailsActivity extends Activity {
    TextView txtFirstName, txtLastName, txtAddress, txtCity, txtProv, txtPostal,txtCountry,txtHomePhone,txtBusPhone,txtEmail,txtAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        txtFirstName=(TextView)findViewById(R.id.txtFirstName);
        txtLastName= (TextView)findViewById(R.id.txtLastName);
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtCity=(TextView)findViewById(R.id.txtCity);
        txtProv=(TextView)findViewById(R.id.txtProv);
        txtPostal=(TextView)findViewById(R.id.txtPostal);
        txtCountry=(TextView)findViewById(R.id.txtCountry);
        txtHomePhone=(TextView)findViewById(R.id.txtHomePhone);
        txtBusPhone=(TextView)findViewById(R.id.txtBusPhone);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtAgent=(TextView)findViewById(R.id.txtAgent);

        Intent intent=getIntent();
        Customer customer=(Customer)intent.getSerializableExtra("customer");
        txtFirstName.setText(customer.getCustFirstName());
        txtLastName.setText(customer.getCustLastName());
        txtAddress.setText(customer.getCustAddress());
        txtCity.setText(customer.getCustCity());
        txtProv.setText(customer.getCustProv());
        txtPostal.setText(customer.getCustPostal());
        txtCountry.setText(customer.getCustCountry());
        txtHomePhone.setText(customer.getCustHomePhone());
        txtBusPhone.setText(customer.getCustBusPhone());
        txtEmail.setText(customer.getCustEmail());
        txtAgent.setText(customer.getCustAgentId()+"");

        setTitle("Customer Id: " + customer.getCustomerId());
    }
}
