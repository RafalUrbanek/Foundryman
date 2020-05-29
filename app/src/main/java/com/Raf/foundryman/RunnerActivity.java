package com.Raf.foundryman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    int armsCount, ingateCount;
    double width, startHeight, endHeight, wellFilter1, wellFilter2, ingateFilter1, ingateFilter2;

    EditText armsCountText, ingateCountText, widthText, startHeightText, endHeightText,
            wellFilter1Text, wellFilter2Text, ingateFilter1Text, ingateFilter2Text;

    TextView wellText1, wellText2, ingateText1, ingateText2;

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
        armsCount = 1;
        ingateCount = 1;

        if (Support.sprueWidth != 0.0) {
            double tempWidth = Math.round(Support.sprueWidth * 100);
            width = tempWidth / 100;
            widthText.setText(Double.toString(width));
        } else{
            width = 0.0;
        }

                //, startHeight, endHeight, wellFilter1, wellFilter2, ingateFilter1, ingateFilter2;
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
        ingateText1 = findViewById(R.id.runner_text_7);
        ingateText2 = findViewById(R.id.runner_text_10);
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

