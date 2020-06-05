package com.Raf.foundryman;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LShapeActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    Button calculateBtn;
    ImageButton helpBtn;
    Spinner toolSpinner;
    Boolean flag;
    EditText bottomDiaEdit, velocityEdit, sprueHeightEdit;
    TextView topText, massFlowText1, massFlowText2, volFlowText, radiusText, runnerHeightText, sprueSizeText;

    double massFlowrate, weightFlowrate, velocity, sprueHeight, bottomDim, radius, runnerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_shape);
        flag = false;

        initializeFields();
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        initializeValues();

        topText.setText(tools[2].toUpperCase());
    }

    private void initializeValues() {
        if (Support.sprueVelocity > 0){
            velocity = Support.sprueVelocity;
            velocityEdit.setText(String.valueOf(velocity));
        } else velocity = 0;

        if (Support.initialMassFlowrate > 0){
            massFlowrate = Support.initialMassFlowrate;
            massFlowText1.setText(String.valueOf(massFlowrate));
            massFlowText2.setText(String.valueOf(massFlowrate));
        } else massFlowrate = 0;
    }

    private void initializeFields() {
        tools = getResources().getStringArray(R.array.tools);
        topText = findViewById(R.id.summaryTxtLShape);
        bottomDiaEdit = findViewById(R.id.lShape_input_bottom_dim);
        velocityEdit = findViewById(R.id.lShape_input_velocity);
        sprueHeightEdit = findViewById(R.id.lShape_input_sprue_height);
        massFlowText1 = findViewById(R.id.lShape_text_mass_flow);
        massFlowText2 = findViewById(R.id.lShape_text_mass_flow2);
        volFlowText = findViewById(R.id.lShape_text_vol_flow);
        radiusText = findViewById(R.id.lShape_text_radius);
        runnerHeightText = findViewById(R.id.lShape_text_height);
        sprueSizeText = findViewById(R.id.lShape_text_sprue);
        calculateBtn = findViewById(R.id.lShape_calc_btn);
        helpBtn = findViewById(R.id.lShape_help_btn);

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHelp();
            }
        });
    }

    private void displayHelp() {

    }

    private void calculate() {

    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(2);
    }

    private void configureProjectText() {
        final EditText projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());
        projectText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Values.setProjectName(projectText.getText().toString());
                return false;
            }
        });
    }

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LShapeActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (flag == false) {
            flag = true;
        } else {
            Support.spinnerNavigator(LShapeActivity.this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

