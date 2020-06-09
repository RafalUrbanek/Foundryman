package com.Raf.foundryman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LShapeActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    Button calculateBtn, diaDimBtn;
    ImageButton helpBtn;
    Spinner toolSpinner;
    Boolean flag;
    Boolean diaDimFlag = true;
    EditText bottomDiaEdit, velocityEdit, sprueHeightEdit;
    TextView topText, massFlowText1, massFlowText2, volFlowText, radiusText, runnerHeightText, sprueSizeText;

    double massFlowrate, velocity, sprueHeight, bottomDim, radius, runnerHeight, volFolwrate;

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
        if (Support.sprueHeight > 0){
            sprueHeight = Support.sprueHeight;
        } else {
            sprueHeight = 0;
        }

        if (Support.sprueWidth > 0){
            bottomDim = Support.sprueWidth;
            bottomDiaEdit.setText(String.valueOf(bottomDim));
        }

        if (Support.sprueVelocity > 0){
            velocity = Support.sprueVelocity;
            velocityEdit.setText(String.valueOf(velocity));
        } else velocity = 0;

        if (Support.initialMassFlowrate > 0){
            massFlowrate = Support.initialMassFlowrate;
            massFlowText1.setText(String.valueOf(massFlowrate));
            massFlowText2.setText(String.valueOf(massFlowrate));
            volFolwrate = massFlowrate * Support.density / 1000;
            volFlowText.setText(String.valueOf(volFolwrate));
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
        diaDimBtn = findViewById(R.id.dia_dim_btn);
        helpBtn = findViewById(R.id.lShape_help_btn);

        diaDimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diaDimFlag) {
                    diaDimBtn.setText("DIM");
                    diaDimFlag = false;
                } else {
                    diaDimBtn.setText("DIA");
                    diaDimFlag = true;
                }
            }
        });

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

    private double exitHeightMultiplier(double velocity){
        double exitHeight;

        if (velocity < 1){
            exitHeight = 0.76;
        }else if (velocity < 2){
            exitHeight = ((velocity - 1) / (2 - 1)) * (0.91 - 0.76) + 0.76;
        } else if (velocity < 4){
            exitHeight = ((velocity - 1) / (2 - 1)) * (0.98 - 0.91) + 0.91;
        } else if (velocity < 8){
            exitHeight = ((velocity - 1) / (2 - 1)) * (1 - 0.98) + 0.98;
        } else {
            exitHeight = 1;
        }
        return exitHeight;
    }

    private double radiusMultiplier(double velocity){
        double radM;
        if (velocity < 1){
            radM = 1.21;
        }else if (velocity < 2){
            radM = ((velocity - 1) / (2 - 1)) * (1.38 - 1.21) + 1.21;
        } else if (velocity < 4){
            radM = 1.38;
        } else if (velocity < 8){
            radM = 1.38;
        } else {
            radM = 1.37;
        }
        return radM;
    }

    private void calculate() {
        if (!String.valueOf(bottomDiaEdit.getText()).isEmpty()){
            bottomDim = Double.valueOf(String.valueOf(bottomDiaEdit.getText()));
        } else {
            bottomDim = 0;
        }

        if (!String.valueOf(velocityEdit.getText()).isEmpty()){
            velocity = Double.valueOf(String.valueOf(velocityEdit.getText()));
        } else {
            velocity = 0;
        }

        if (!String.valueOf(sprueHeightEdit.getText()).isEmpty()){
            sprueHeight = Double.valueOf(String.valueOf(sprueHeightEdit.getText()));
        } else {
            sprueHeight = 0;
        }

        if (bottomDim > 0){
            if ( velocity <= 0) {
                if (sprueHeight > 0) {
                    velocity = Support.gravity * Math.sqrt(sprueHeight/ 1000 / (0.5 * Support.gravity));
                    Log.d("LOG", "Velocity: " + String.valueOf(velocity));
                } else {
                    Toast.makeText(this, "Please enter valid metal velocity at sprue " +
                            "bottom or sprue height including pouring cup", Toast.LENGTH_LONG).show();
                }
            }

            if (velocity > 0) {
                double bottomArea;
                if (diaDimFlag){
                    bottomArea = Math.PI * ((bottomDim/2) * (bottomDim/2));
                } else {
                    bottomArea = bottomDim * bottomDim;
                }

                volFolwrate = Math.round(bottomArea * velocity * 1000);
                volFlowText.setText(String.valueOf(volFolwrate));

                double massFlowrateRounded = volFolwrate * Support.density / 1000000000 * 100;
                massFlowrateRounded = Math.round(massFlowrateRounded);
                massFlowrate =  massFlowrateRounded / 100;
                massFlowText1.setText(String.valueOf(massFlowrate));
                massFlowText2.setText(String.valueOf(massFlowrate));

                sprueSizeText.setText(String.valueOf(bottomDim));
                double radiusRounded = Math.round(bottomDim * radiusMultiplier(velocity) * 10);
                radius = radiusRounded / 10;
                radiusText.setText(String.valueOf(radius));

                double runnerHeightRounded;
                if (diaDimFlag){
                    runnerHeightRounded = Math.round(bottomDim * exitHeightMultiplier(velocity) *
                            (Math.PI / 4) * 10);
                    runnerHeight = runnerHeightRounded / 10;
                } else {
                    runnerHeightRounded = Math.round(bottomDim * exitHeightMultiplier(velocity) * 10);
                    runnerHeight = runnerHeightRounded / 10;
                }

                runnerHeightText.setText(String.valueOf(runnerHeight));

            } else {
                sprueSizeText.setText("");
                runnerHeightText.setText("");
                radiusText.setText("");
                massFlowText1.setText("");
                massFlowText2.setText("");
            }

        } else {
            Toast.makeText(this,"Please enter valid sprue bottom diameter or " +
                    "dimension", Toast.LENGTH_LONG).show();
            sprueSizeText.setText("");
            runnerHeightText.setText("");
            radiusText.setText("");
            massFlowText1.setText("");
            massFlowText2.setText("");
        }
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

