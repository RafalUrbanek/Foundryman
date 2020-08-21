package com.Raf.foundryman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    Spinner toolSpinner;
    Boolean flag;
    TextView spruesAmmount;
    TextView wells;
    EditText projectText;
    TextView flowText;
    TextView fillTimeText;
    TextView massText;
    TextView yieldText;
    TextView ratio1Text;
    TextView ratio2Text;
    TextView ratio3Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        initialize();
        flag = false;
        spruesAmmount.setText(String.valueOf(Support.sprues));
        wells.setText(String.valueOf(Support.wells));
        tools = getResources().getStringArray(R.array.tools);
        projectText.setText(Values.getProjectName());
        configureOptionsBtn();
        configureSpinner();
        fillValues();
    }

    private void initialize(){
        spruesAmmount = findViewById(R.id.mainTxt1);
        projectText = findViewById(R.id.projectNameTxt);
        wells = findViewById(R.id.mainTxt2);
        flowText = findViewById(R.id.summary_initial_flow_text);
        fillTimeText = findViewById(R.id.summary_fill_time_text);
        massText = findViewById(R.id.summary_weight_text);
        yieldText = findViewById(R.id.summary_yield_text);
        ratio1Text = findViewById(R.id.summary_ratio1_text);
        ratio2Text = findViewById(R.id.summary_ratio2_text);
        ratio3Text = findViewById(R.id.summary_ratio3_text);
    }

    private void fillValues(){
        Toast.makeText(this, String.valueOf(Support.initialMassFlowrate), Toast.LENGTH_LONG).show();
        if (Support.initialMassFlowrate >0){
            flowText.setText(String.valueOf(Support.initialMassFlowrate));
        } else {
            flowText.setText("0");
        }
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(5);
    }

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values.setProjectName(String.valueOf(projectText.getText()));
                startActivity(new Intent(SummaryActivity.this, SavesActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (flag == false) {
            flag = true;
        } else {
            Values.setProjectName(String.valueOf(projectText.getText()));
            Support.spinnerNavigator(SummaryActivity.this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

