package com.Raf.foundryman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RunnerActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    String[] wellTypes = {"L-shape", "Standard", "No well"};
    Spinner toolSpinner;
    Switch wellFilterSwitch, ingatesFilterSwitch;
    Boolean flag;
    TextView topText;
    Spinner wellType;
    int ingateCount = 1;
    int filterWidth = 22;
    double width, startHeight, endHeight, wellFilter1, wellFilter2, ingateFilter1, ingateFilter2,
            weight, armLength;

    EditText armsCountText, wellCountText, ingateCountText, widthText, startHeightText, endHeightText,
            runnerarmLength, wellFilter1Text, wellFilter2Text, ingateFilter1Text, ingateFilter2Text;

    TextView wellText1, wellText2, ingateText1, ingateText2, weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);
        flag = false;
        tools = getResources().getStringArray(R.array.tools);
        topText = findViewById(R.id.summaryTxtRatios);
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        configureWellSpinner();
        configureSwitches();
        initializeTexts();
        initializeValues();

        topText.setText(tools[3].toUpperCase());
    }

    private void configureSwitches() {
        wellFilterSwitch = findViewById(R.id.runner_switch_1);
        ingatesFilterSwitch = findViewById(R.id.runner_switch_2);

        wellFilterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wellFilterSwitch.isChecked()){
                    wellFilter1Text.setVisibility(View.VISIBLE);
                    wellFilter2Text.setVisibility(View.VISIBLE);
                    wellText1.setVisibility(View.VISIBLE);
                    wellText2.setVisibility(View.VISIBLE);
                }else{
                    wellFilter1Text.setVisibility(View.INVISIBLE);
                    wellFilter2Text.setVisibility(View.INVISIBLE);
                    wellText1.setVisibility(View.INVISIBLE);
                    wellText2.setVisibility(View.INVISIBLE);
                }
            }
        });

        ingatesFilterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingatesFilterSwitch.isChecked()){
                    ingateFilter1Text.setVisibility(View.VISIBLE);
                    ingateFilter2Text.setVisibility(View.VISIBLE);
                    ingateText1.setVisibility(View.VISIBLE);
                    ingateText2.setVisibility(View.VISIBLE);
                }else{
                    ingateFilter1Text.setVisibility(View.INVISIBLE);
                    ingateFilter2Text.setVisibility(View.INVISIBLE);
                    ingateText1.setVisibility(View.INVISIBLE);
                    ingateText2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initializeValues() {
        armsCountText.setText(String.valueOf(Support.runnerArms));
        armsCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wellCountText.setText(String.valueOf(Support.wells));
        wellCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ingateCountText.setText(String.valueOf(ingateCount));
        ingateCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        widthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        widthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        startHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        endHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wellFilter1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wellFilter2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ingateFilter1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ingateFilter2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!String.valueOf(ingateFilter2Text.getText()).isEmpty()) {
                    ingateFilter2 = Double.valueOf(String.valueOf(ingateFilter2Text.getText()));
                    calculateMass();
                } else {
                    ingateFilter2 = 0;
                }
            }
        });


        if (Support.sprueWidth != 0.0) {
            double tempWidth = Math.round(Support.sprueWidth * 100);
            width = tempWidth / 100;
            widthText.setText(Double.toString(width));
        } else {
            width = 0.0;
        }
    }

    private double ingateFilterVol(){
    double volume = 0;
    if (ingateFilter1 != 0){
        if (ingateFilter2 != 0){
            volume = ingateFilter1 * ingateFilter2 * filterWidth;
        } else {
            volume = ingateFilter1 * ingateFilter1 * Math.PI;
        }
    }
    return volume;
    }

    // method calculates mass based on the input and results from other tabs
    private void calculateMass(){
        double ingateMass = 0;
        double armMass = 0;
        double wellMass = 0;
        double totalMassRounded;

        // calculate in-gates mass
        if (ingatesFilterSwitch.isChecked()){
            if (Support.ingateDia != 0 && Support.ingateHeight != 0){
                ingateMass = (Support.ingateDia / 2) * (Support.ingateDia / 2) * Math.PI *
                        Support.ingateHeight * ingateCount + ingateFilterVol();
            } else {
                ingateMass = 100 * Math.PI * 80 * ingateCount + ingateFilterVol();
            }
        }

        // calculate arm(s) mass
        if (width > 0 && startHeight > 0 && endHeight > 0 && Support.runnerArms > 0 && armLength > 0) {
            armMass = Support.density * Support.runnerArms * width * ((startHeight + endHeight) / 2)
                    * armLength / 1000000;
        }

        // calculate well mass
        if (wellFilterSwitch.isChecked()) {
            if (wellType.getSelectedItemPosition() == 0) {
                if (wellFilter1 > 0 && wellFilter2 > 0) {
                    wellMass = ((wellFilter1 - 5) * (wellFilter2 - 10) * (filterWidth + 30) * Support.density
                            / 1000000) + (startHeight * width * 50);
                }
            } else if (wellType.getSelectedItemPosition() == 1) {
                if (wellFilter1 > 0 && wellFilter2 > 0) {
                    wellMass = (wellFilter1 - 5) * (wellFilter2 - 10) * (filterWidth + 125) * Support.density
                            / 1000000;
                }
            } else {
                if (wellFilter1 > 0 && wellFilter2 > 0) {
                    wellMass = ((wellFilter1 - 5) * (wellFilter2 - 10) * (filterWidth + 30) * Support.density
                            / 1000000);
                }
            }
        } else{
            if (wellType.getSelectedItemPosition() == 0 || wellType.getSelectedItemPosition() == 2 ) {
                    wellMass = startHeight * width * 50 * Support.density
                            / 1000000;
            } else {
                // Assumption that well & no filter combination is only used for small parts
                    wellMass = 50 * 50 * 50 * Support.density
                            / 1000000;
            }
        }

        double totalMass = (ingateMass + armMass + wellMass) * 100;
        totalMassRounded = Math.round(totalMass) / 100;
        weight = totalMassRounded;
        weightText.setText(Double.toString(weight));
    }

    private void initializeTexts() {
        armsCountText = findViewById(R.id.runner_arms);
        ingateCountText  = findViewById(R.id.runner_ingates);
        widthText = findViewById(R.id.runner_width);
        startHeightText = findViewById(R.id.runner_height_1);
        endHeightText = findViewById(R.id.runner_height_2);
        wellFilter1Text = findViewById(R.id.runner_well_filter_1);
        wellFilter2Text = findViewById(R.id.runner_well_filter_2);
        ingateFilter1Text = findViewById(R.id.runner_ingate_filter_1);
        ingateFilter2Text = findViewById(R.id.runner_ingate_filter_2);
        wellText1 = findViewById(R.id.runner_text_9);
        wellText2 = findViewById(R.id.runner_text_11);
        wellCountText = findViewById(R.id.runner_wells);
        ingateText1 = findViewById(R.id.runner_text_7);
        ingateText2 = findViewById(R.id.runner_text_10);
        weightText = findViewById(R.id.runner_text_13);
    }

    private void configureWellSpinner(){
        wellType = findViewById(R.id.runner_type_spinner);
        wellType.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,wellTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wellType.setAdapter(adapter);
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(3);
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
                startActivity(new Intent(RunnerActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (arg0.getId() == R.id.runner_type_spinner){
            if (flag == false) {
                flag = true;
            } else {
                Values.setSprueTypeSelected(position);
            }
        } else {
            if (flag == false) {
                flag = true;
            } else {
                Support.spinnerNavigator(RunnerActivity.this, position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

