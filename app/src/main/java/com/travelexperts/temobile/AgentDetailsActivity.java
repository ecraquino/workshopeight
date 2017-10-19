package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AgentDetailsActivity extends Activity {
    TextView txtFirstName, txtMiddleInitial, txtLastName, txtBusPhone, txtEmail, txtPosition, txtAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_details);

        txtFirstName=(TextView)findViewById(R.id.txtFirstName);
        txtMiddleInitial= (TextView)findViewById(R.id.txtMiddleInitial);
        txtLastName=(TextView)findViewById(R.id.txtLastName);
        txtBusPhone=(TextView)findViewById(R.id.txtBusPhone);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtPosition=(TextView)findViewById(R.id.txtPosition);
        txtAgency=(TextView)findViewById(R.id.txtAgency);

        Intent intent=getIntent();
        Agent agent=(Agent)intent.getSerializableExtra("agentd");
        txtFirstName.setText(agent.getAgtFirstName());
        txtMiddleInitial.setText(agent.getAgtMiddleInitial());
        txtLastName.setText(agent.getAgtLastName());
        txtBusPhone.setText(agent.getAgtBusPhone());
        txtEmail.setText(agent.getAgtEmail());
        txtPosition.setText(agent.getAgtPosition());
        txtAgency.setText(agent.getAgencyId()+"");
        setTitle("Agent Id: " + agent.getAgentId());
    }
}
