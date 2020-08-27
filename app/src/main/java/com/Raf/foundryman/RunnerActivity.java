package com.Raf.foundryman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RunnerActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    String[] wellTypes = {"L-shape", "Standard", "No well"};
    Spinner toolSpinner;
    Button calculateBtn;
    ImageButton helpBtn;
    Switch wellFilterSwitch, ingatesFilterSwitch;
    EditText projectText;
    Boolean flag;
    TextView topText;
    Spinner wellType;
    int ingateCount;
    int filterWidth = 22;
    int armsAmmount;
    Context cont;

    EditText armsCountText, wellCountText, ingateCountText, widthText, startHeightText, endHeightText,
            armLengthText, wellFilter1Text, wellFilter2Text, ingateFilter1Text, ingateFilter2Text, ingateDia;

    TextView wellText1, wellText2, ingateText1, ingateText2, weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cont = this;
        setContentView(R.layout.activity_runner);
        flag = false;
        tools = getResources().getStringArray(R.array.tools);
        topText = findViewById(R.id.summaryTxtRatios);
        configureOptionsBtn();
        configureSpinner();
        configureWellSpinner();
        configureSwitches();
        initializeTexts();
        initializeValues();
        initializeButtons();

        topText.setText(tools[3].toUpperCase());
    }

    private void initializeButtons() {

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMass();
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder helpAlert  = new AlertDialog.Builder(cont);
                helpAlert.setMessage(R.string.runnerHelp);
                helpAlert.setTitle("RUNNER BAR");
                helpAlert.setPositiveButton("OK", null);
                helpAlert.setCancelable(true);
                helpAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                helpAlert.create().show();
            }
        });
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

        wellType.setSelection(Support.wellType);
        wellFilterSwitch.setChecked(Support.wellFilterState);
        ingatesFilterSwitch.setChecked(Support.ingateFilterSwitch);

        if (Support.ingateDia > 0){
            ingateDia.setText(String.valueOf(Support.round(Support.ingateDia, 2)));
        }

        if (Support.runnerWidth > 0.0) {
            widthText.setText(Double.toString(Support.round(Support.runnerWidth, 2)));
        } else if (Support.sprueWidth > 0) {
            widthText.setText(Double.toString(Support.round(Support.sprueWidth, 2)));
        }

        if (Support.wells > 0) {
            wellCountText.setText(Integer.toString(Support.wells));
        }

        if (Support.runnerArms > 0) {
            armsAmmount = Support.runnerArms;
            armsCountText.setText(String.valueOf(armsAmmount));
        }

        if (Support.runnerArmLength > 0.0) {
            armLengthText.setText(Double.toString(Support.round(Support.runnerArmLength, 2)));
        }

        if (Support.ingateCount > 0) {
            ingateCount = Support.ingateCount;
            ingateCountText.setText(String.valueOf(ingateCount));
        }

        if (Support.runnerStartHeight > 0.0) {
            startHeightText.setText(Double.toString(Support.round(Support.runnerStartHeight, 2)));
        }

        if (Support.runnerEndHeight > 0.0) {
            endHeightText.setText(Double.toString(Support.round(Support.runnerEndHeight, 2)));
        }

        if (Support.wellFilter1 > 0.0) {
            wellFilter1Text.setText(Double.toString(Support.round(Support.wellFilter1,2)));
        }

        if (Support.wellFilter2 > 0.0) {
            wellFilter2Text.setText(Double.toString(Support.round(Support.wellFilter2,2)));
        }

        if (Support.ingateFilter1 > 0.0) {
            ingateFilter1Text.setText(Double.toString(Support.round(Support.ingateFilter1,2)));
        }

        if (Support.ingateFilter2 > 0.0) {
            ingateFilter2Text.setText(Double.toString(Support.round(Support.ingateFilter2,2)));
        }

        if (Support.runnerMass > 0.0) {
            weightText.setText(Double.toString(Support.round(Support.runnerMass,2)));
        }
    }

    private double ingateFilterVol(){
    double volume = 0;
    if (Support.ingateFilter1 > 0){
        if (Support.ingateFilter2 > 0){
            volume = Support.ingateFilter1 * Support.ingateFilter2 * filterWidth;
        } else {
            volume = Support.ingateFilter1 * Support.ingateFilter1 * Math.PI;
        }
    }
    return volume;
    }

    // method calculates mass based on the input and results from other tabs
    private void calculateMass(){

        if (!String.valueOf(armsCountText.getText()).isEmpty()){
            Support.runnerArms = Integer.valueOf(String.valueOf(armsCountText.getText()));
        }

        if (!String.valueOf(wellCountText.getText()).isEmpty()){
            Support.wells = Integer.valueOf(String.valueOf(wellCountText.getText()));
        }

        if (!String.valueOf(ingateCountText.getText()).isEmpty()){
            Support.ingateCount = Integer.valueOf(String.valueOf(ingateCountText.getText()));
        }

        if (!String.valueOf(widthText.getText()).isEmpty()){
            Support.runnerWidth = Double.valueOf(String.valueOf(widthText.getText()));
        }

        if (!String.valueOf(ingateCountText.getText()).isEmpty()){
            Support.ingateCount = Integer.valueOf(String.valueOf(ingateCountText.getText()));
        }

        if (!String.valueOf(startHeightText.getText()).isEmpty()){
            Support.runnerStartHeight = Double.valueOf(String.valueOf(startHeightText.getText()));
        }

        if (!String.valueOf(endHeightText.getText()).isEmpty()){
            Support.runnerEndHeight = Double.valueOf(String.valueOf(endHeightText.getText()));
        }

        if (!String.valueOf(armLengthText.getText()).isEmpty()){
            Support.runnerArmLength = Double.valueOf(String.valueOf(armLengthText.getText()));
        }

        if (!String.valueOf(wellFilter1Text.getText()).isEmpty()){
            Support.wellFilter1 = Double.valueOf(String.valueOf(wellFilter1Text.getText()));
        }

        if (!String.valueOf(wellFilter2Text.getText()).isEmpty()){
            Support.wellFilter2 = Double.valueOf(String.valueOf(wellFilter2Text.getText()));
        }

        if (!String.valueOf(ingateFilter1Text.getText()).isEmpty()){
            Support.ingateFilter1 = Double.valueOf(String.valueOf(ingateFilter1Text.getText()));
        }

        if (!String.valueOf(ingateFilter2Text.getText()).isEmpty()){
            Support.ingateFilter2 = Double.valueOf(String.valueOf(ingateFilter2Text.getText()));
        }

        if (!String.valueOf(ingateDia.getText()).isEmpty()){
            Support.ingateDia = Double.valueOf(String.valueOf(ingateDia.getText()));
        }

        double ingateMass = 0;
        double armMass = 0;
        double wellMass = 0;

        // calculate in-gates mass
        if (ingatesFilterSwitch.isChecked()){
            if (Support.ingateDia > 0 && Support.ingateHeight > 0){
                ingateMass = (((Support.ingateDia / 2) * (Support.ingateDia / 2) * Math.PI *
                        Support.ingateHeight * Support.ingateCount * Support.density) + (ingateFilterVol() *
                        Support.ingateCount * Support.density)) / 1000000000;
            } else {
                ingateMass = ((100 * Math.PI * 80 * Support.ingateCount * Support.density) + (ingateFilterVol() *
                        Support.ingateCount * Support.density)) / 1000000000;
            }
        } else {
            if (Support.ingateDia > 0 && Support.ingateHeight > 0){
                ingateMass = ((Support.ingateDia / 2) * (Support.ingateDia / 2) * Math.PI *
                        Support.ingateHeight * Support.ingateCount * Support.density) / 1000000000;
            } else {
                ingateMass = 100 * Math.PI * 80 * Support.ingateCount * Support.density / 1000000000;
            }
        }

        // calculate arm(s) mass
        if (Support.runnerWidth > 0 && Support.runnerStartHeight > 0 && Support.runnerEndHeight > 0 &&
                Support.runnerArms > 0 && Support.runnerArmLength > 0) {
            armMass = Support.density * Support.runnerArms * Support.runnerWidth * ((Support.runnerStartHeight
                    + Support.runnerEndHeight) / 2) * Support.runnerArmLength / 1000000000;
        }

        // calculate well mass
        if (wellFilterSwitch.isChecked()) {
            if (wellType.getSelectedItemPosition() == 0) {
                if (Support.wellFilter1 > 5 && Support.wellFilter2 > 10) {
                    wellMass = ((Support.wellFilter1 - 5) * (Support.wellFilter2 - 10) * (filterWidth + 30) *
                            Support.density * Support.wells / 1000000000) + (Support.runnerStartHeight
                            * Support.runnerWidth * 50 * Support.density / 1000000000);
                }
            } else if (wellType.getSelectedItemPosition() == 1) {
                if (Support.wellFilter1 > 0 && Support.wellFilter2 > 0) {
                    wellMass = (Support.wellFilter1 - 5) * (Support.wellFilter2 - 10) * (filterWidth + 125) *
                            Support.density * Support.wells / 1000000000;
                }
            } else {
                if (Support.wellFilter1 > 0 && Support.wellFilter2 > 0) {
                    wellMass = ((Support.wellFilter1 - 5) * (Support.wellFilter2 - 10) * (filterWidth + 30) *
                            Support.density * Support.wells / 1000000000);
                }
            }
        } else{
            if (wellType.getSelectedItemPosition() == 0 || wellType.getSelectedItemPosition() == 2 ) {
                    wellMass = Support.runnerStartHeight * Support.runnerWidth * 50 * Support.density *
                            Support.wells / 1000000000;
            } else {
                // Assumption that well & no filter combination is only used for small parts
                    wellMass = 50 * 50 * 50 * Support.density * Support.wells / 1000000000;
            }
        }

        double weight = Support.round((ingateMass + armMass + wellMass), 2);
        weightText.setText(Double.toString(weight));
        Support.runnerMass = weight;
    }

    private void initializeTexts() {
        armLengthText = findViewById(R.id.runner_arm_length);
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
        calculateBtn = findViewById(R.id.runner_calc_btn);
        helpBtn = findViewById(R.id.runner_help_btn);
        ingateDia = findViewById(R.id.runner_ingateDia);
        projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());
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

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values.setProjectName(String.valueOf(projectText.getText()));
                startActivity(new Intent(RunnerActivity.this, SavesActivity.class));
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
                Values.setProjectName(String.valueOf(projectText.getText()));
                Support.spinnerNavigator(RunnerActivity.this, position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

