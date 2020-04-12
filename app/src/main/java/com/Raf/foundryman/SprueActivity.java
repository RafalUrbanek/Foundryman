package com.Raf.foundryman;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SprueActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    Spinner toolSpinner;
    Spinner sprueSpinner;
    TextView topText;
    TextView sprueText2, sprueText3, sprueText5, sprueText8;
    EditText sprueDim1, sprueDim2, sprueDim3, sprueDim4, sprueDim5, sprueDim6, sprueDim7;
    Double[] sprueDimVals = new Double[7];
    Button calcBtn;
    Double[] inputData;

    String[] tools;
    String[] sprueTypes;
    boolean flag;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprue);
        flag = false;
        inputData = new Double[8];
        inputData[7] = 0.0;

        tools = getResources().getStringArray(R.array.tools);
        sprueTypes = getResources().getStringArray(R.array.sprues);
        topText = findViewById(R.id.summaryTxtLShape);
        sprueText2 = findViewById(R.id.sprueText2);
        sprueText3 = findViewById(R.id.sprueText3);
        sprueText5 = findViewById(R.id.sprueText5);
        sprueText8 = findViewById(R.id.sprueText8);

        sprueDim1 = findViewById(R.id.sprueDim1);
        sprueDim2 = findViewById(R.id.sprueDim2);
        sprueDim3 = findViewById(R.id.sprueDim3);
        sprueDim4 = findViewById(R.id.sprueDim4);
        sprueDim5 = findViewById(R.id.sprueDim5);
        sprueDim6 = findViewById(R.id.sprueDim6);
        sprueDim7 = findViewById(R.id.sprueDim7);

        populateDataFieldListeners();
        populateDataArray();

        calcBtn = findViewById(R.id.sprueCalcBtn);
        calcBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        configureSprueSpinner();
        configureDisplayState();

        topText.setText(tools[1].toUpperCase());
    }
    // calls addCalcBtnListener on all sprueDims fields
    private void populateDataFieldListeners() {
        addCalcBtnListener(sprueDim1);
        addCalcBtnListener(sprueDim2);
        addCalcBtnListener(sprueDim3);
        addCalcBtnListener(sprueDim4);
        addCalcBtnListener(sprueDim5);
        addCalcBtnListener(sprueDim6);
        addCalcBtnListener(sprueDim7);
    }

    // Populates array containing field values
    private void populateDataArray(){
        if (!(String.valueOf(sprueDim1.getText())).isEmpty()){
            inputData[0] = Double.valueOf(String.valueOf(sprueDim1.getText()));
        }
        if (!(String.valueOf(sprueDim2.getText())).isEmpty()){
            inputData[1] = Double.valueOf(String.valueOf(sprueDim2.getText()));
        }
        if (!(String.valueOf(sprueDim3.getText())).isEmpty()){
            inputData[2] = Double.valueOf(String.valueOf(sprueDim3.getText()));
        }
        if (!(String.valueOf(sprueDim4.getText())).isEmpty()){
            inputData[3] = Double.valueOf(String.valueOf(sprueDim4.getText()));
        }
        if (!(String.valueOf(sprueDim5.getText())).isEmpty()){
            inputData[4] = Double.valueOf(String.valueOf(sprueDim5.getText()));
        }
        if (!(String.valueOf(sprueDim6.getText())).isEmpty()){
            inputData[5] = Double.valueOf(String.valueOf(sprueDim6.getText()));
        }
        if (!(String.valueOf(sprueDim7.getText())).isEmpty()){
            inputData[6] = Double.valueOf(String.valueOf(sprueDim7.getText()));
        }

    }

    // adds listeners to value fields
    public void addCalcBtnListener(EditText dimInput) {
        dimInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                // modify the array based on values in sprueDim values
                populateDataArray();
                inputData = Support.computeSprue(inputData);

                // set Calculate button background color based on computeSprue method
                if (inputData[7] == 0.0){
                    calcBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
                } else {
                    calcBtn.setBackgroundColor(getResources().getColor(R.color.button_green));
                }
            }
        });
    }

    private void displayToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void configureDisplayState() {
        switch(Values.getSprueTypeSelected()){
            case 0:
                sprueText2.setText(getResources().getString(R.string.diameter));
                sprueText5.setText(getResources().getString(R.string.diameter));
                sprueText3.setVisibility(TextView.INVISIBLE);
                sprueText8.setVisibility(TextView.INVISIBLE);
                sprueDim2.setVisibility(EditText.INVISIBLE);
                sprueDim5.setVisibility(EditText.INVISIBLE);
                break;
            case 1:
                sprueText2.setText(getResources().getString(R.string.width));
                sprueText5.setText(getResources().getString(R.string.width));
                sprueText3.setVisibility(TextView.INVISIBLE);
                sprueText8.setVisibility(TextView.INVISIBLE);
                sprueDim2.setVisibility(EditText.INVISIBLE);
                sprueDim5.setVisibility(EditText.INVISIBLE);
                break;
            case 2:
                sprueText2.setText(getResources().getString(R.string.width));
                sprueText5.setText(getResources().getString(R.string.width));
                sprueText3.setVisibility(TextView.VISIBLE);
                sprueText8.setVisibility(TextView.VISIBLE);
                sprueDim2.setVisibility(EditText.VISIBLE);
                sprueDim5.setVisibility(EditText.VISIBLE);
                break;
        }
    }

    private void configureSprueSpinner() {
        sprueSpinner = findViewById(R.id.sprueSpinner);
        sprueSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sprueTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprueSpinner.setAdapter(adapter);
        sprueSpinner.setSelection(Values.getSprueTypeSelected());
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(1);
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
        ImageButton optionsBtn = findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SprueActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (arg0.getId() == R.id.sprueSpinner){
            if (flag == false) {
                flag = true;
            } else {
                Values.setSprueTypeSelected(position);
                configureDisplayState();
            }
        } else {
            if (flag == false) {
                flag = true;
            } else {
                Support.spinnerNavigator(SprueActivity.this, position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

