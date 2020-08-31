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

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    Spinner toolSpinner;
    Boolean flag;
    TextView spruesAmmount;
    EditText projectText;
    TextView runnersAmmount;
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
        Support.fillSprueValues();
        flag = false;
        Support.computeSprue(Support.sprueValArray);
        spruesAmmount.setText(String.valueOf(Support.sprues));
        runnersAmmount.setText(String.valueOf(Support.runnerArms));
        tools = getResources().getStringArray(R.array.tools);
        projectText.setText(Values.getProjectName());
        configureOptionsBtn();
        configureSpinner();
        fillValues();
    }

    private void initialize(){
        spruesAmmount = findViewById(R.id.summary_sprues_txt);
        runnersAmmount = findViewById(R.id.summary_runners_txt);
        projectText = findViewById(R.id.projectNameTxt);
        flowText = findViewById(R.id.summary_initial_flow_text);
        fillTimeText = findViewById(R.id.summary_fill_time_text);
        massText = findViewById(R.id.summary_weight_text);
        yieldText = findViewById(R.id.summary_yield_text);
        ratio1Text = findViewById(R.id.summary_ratio1_text);
        ratio2Text = findViewById(R.id.summary_ratio2_text);
        ratio3Text = findViewById(R.id.summary_ratio3_text);
    }

    private void fillValues(){
        double estimatedWeight = 0;
        double fillTime = 0;
        double yield = 0;
        double area1, area2, area3;

        estimatedWeight = Support.castingMass + Support.runnerMass + Support.totalFeederMass + Support.calculateSprueMass(); // requires sprue mass
        if (estimatedWeight>0){
            massText.setText(String.valueOf(Support.round(estimatedWeight,2)) + " kg");
        } else {
            massText.setText("-");
        }

        if (Support.initialMassFlowrate >0){
            flowText.setText(String.valueOf(Support.round(Support.initialMassFlowrate * Support.sprues, 2)) + " kg/s");
        } else {
            flowText.setText("-");
        }

        if (Support.initialMassFlowrate>0 && estimatedWeight>0){
            fillTime = Support.round(estimatedWeight / (Support.initialMassFlowrate * 0.75) / Support.sprues, 1);
            fillTimeText.setText(String.valueOf(fillTime) + " sec");
        } else {
            fillTimeText.setText("-");
        }

        if (Support.castingMass>0 && (Support.totalFeederMass>0 || Support.runnerMass>0)){
            yield = (Support.castingMass / (Support.castingMass + Support.totalFeederMass + Support.runnerMass)) * 100;
            yieldText.setText(Math.round(yield) + " %");
        } else {
            yieldText.setText("-");
        }

        if (Support.sprueVal3>0){
            if (Values.getSprueTypeSelected() == 0){
                area1 = (Support.sprueVal3/2) * (Support.sprueVal3/2) * Math.PI * Support.sprues;
            } else if (Values.getSprueTypeSelected() == 1){
                area1 = Support.sprueVal3 * Support.sprueVal3 * Support.sprues;
            } else {
                area1 = Support.sprueVal3 * Support.sprueVal4 * Support.sprues;
            }
            ratio1Text.setText("1");

            if (Support.runnerWidth>0 && (Support.runnerHeight>0 || (Support.runnerStartHeight>0 && Support.runnerEndHeight>0))){
                if (Support.runnerHeight>0){
                    area2 = Support.runnerWidth * Support.runnerHeight * Support.runnerArms;

                } else {
                    area2 = Support.runnerWidth * ((Support.runnerStartHeight + Support.runnerEndHeight) / 2) * Support.runnerArms;
                }
                double ratio2 = Support.round(area2 / area1, 1);
                ratio2Text.setText(String.valueOf(ratio2));
            }

            if (Support.ingateDia>0){
                area3 = (Support.ingateDia/2) * (Support.ingateDia/2) * Math.PI * Support.ingateCount;
                double ratio3 = Support.round(area3 / area1, 1);
                ratio3Text.setText(String.valueOf(ratio3));
            }
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