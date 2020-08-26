package com.Raf.foundryman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner toolSpinner;
    Spinner sprueSpinner;
    TextView topText;
    TextView sprueText2, sprueText3, sprueText5, sprueText8;
    EditText projectText;
    EditText[] sprueDims;
    Button calcBtn;
    Button resetBtn;
    ImageButton helpBtn;
    Double[] inputData;
    Double[] values;
    String[] tools;
    String[] sprueTypes;
    boolean flag;
    boolean userDataInput = true;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprue);
        flag = false;
        inputData = new Double[8];
        importValues();
        inputData[7] = 0.0;

        sprueDims = new EditText[7];

        tools = getResources().getStringArray(R.array.tools);
        sprueTypes = getResources().getStringArray(R.array.sprues);
        topText = findViewById(R.id.summaryTxtLShape);
        sprueText2 = findViewById(R.id.sprueText2);
        sprueText3 = findViewById(R.id.sprueText3);
        sprueText5 = findViewById(R.id.sprueText5);
        sprueText8 = findViewById(R.id.sprueText8);
        projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());

        sprueDims[0] = findViewById(R.id.sprueDim1);
        sprueDims[1] = findViewById(R.id.sprueDim2);
        sprueDims[2] = findViewById(R.id.sprueDim3);
        sprueDims[3] = findViewById(R.id.sprueDim4);
        sprueDims[4] = findViewById(R.id.sprueDim5);
        sprueDims[5] = findViewById(R.id.sprueDim6);
        sprueDims[6] = findViewById(R.id.sprueDim7);

        populateDataFieldListeners();


        configureOptionsBtn();
        configureSpinner();
        configureSprueSpinner();
        configureDisplayState();
        setCalcBtn();
        setResetBtn();
        setHelpButton();
        topText.setText(tools[1].toUpperCase());
        importValues();
        loadValues();
    }

    private void loadValues() {
        for(int i = 0; i<7; i++){
            if (inputData[i] != null && inputData[i] >0) {
                sprueDims[i].setText(String.valueOf(Support.round(inputData[i], 2)));
            }
        }
    }

    void setCalcBtn() {
        calcBtn = findViewById(R.id.sprueCalcBtn);
        calcBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
        calcBtn.setOnClickListener(this);
    }

    private void setResetBtn(){
        resetBtn = findViewById(R.id.sprueResetBtn);
        resetBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
        resetBtn.setOnClickListener(this);
    }

    void setHelpButton(){
        helpBtn = findViewById(R.id.sprueHelpBtn);
        helpBtn.setOnClickListener(this);
    }

    // calls addCalcBtnListener on all sprueDims fields
    private void populateDataFieldListeners() {
        for (int i = 0; i < 6; i++) {
            addCalcBtnListener(sprueDims[i]);
        }
    }

    // Populates array containing field values
    private void populateDataArray(){
        for(int i = 0; i <6; i++) {
            if (!(String.valueOf(sprueDims[i].getText())).isEmpty()) {
                inputData[i] = Double.valueOf(String.valueOf(sprueDims[i].getText()));
            }
        }
    }

    private int fieldPosition(int idNumber){
        switch (idNumber) {
            case R.id.sprueDim1:
                return 0;
            case R.id.sprueDim2:
                return 1;
            case R.id.sprueDim3:
                return 2;
            case R.id.sprueDim4:
                return 3;
            case R.id.sprueDim5:
                return 4;
            case R.id.sprueDim6:
                return 5;
            case R.id.sprueDim7:
                return 6;
            default:
                return -1;
        }
    }

    // adds listeners to value fields
    public void addCalcBtnListener(final EditText dimInput) {
        dimInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                int position = fieldPosition(dimInput.getId());
                if (userDataInput) Support.modified[position] = false;
                if (!String.valueOf(dimInput.getText()).isEmpty() && userDataInput){
                    Support.modified[position] = true;
                }

                // modify the array based on values in sprueDim values
                populateDataArray();
                inputData = Support.computeSprue(inputData);
                exportValues();

                // set Calculate button background color based on computeSprue method
                if (inputData[7] == 0.0){
                    calcBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
                } else {
                    calcBtn.setBackgroundColor(getResources().getColor(R.color.button_green));
                }

                dataColorSetter();
            }
        });
    }

    private void dataColorSetter() {
        for (int i = 0; i < Support.modified.length; i++){
            if (Support.modified[i] == true) {
                sprueDims[i].setTextColor(getResources().getColor(R.color.dark_green));
            } else{
                sprueDims[i].setTextColor(Color.BLACK);
            }
        }
    }

    private void configureDisplayState() {
        switch(Values.getSprueTypeSelected()){
            case 0:
                sprueText2.setText(getResources().getString(R.string.diameter));
                sprueText5.setText(getResources().getString(R.string.diameter));
                sprueText3.setVisibility(TextView.INVISIBLE);
                sprueText8.setVisibility(TextView.INVISIBLE);
                sprueDims[1].setVisibility(EditText.INVISIBLE);
                sprueDims[4].setVisibility(EditText.INVISIBLE);
                break;
            case 1:
                sprueText2.setText(getResources().getString(R.string.width));
                sprueText5.setText(getResources().getString(R.string.width));
                sprueText3.setVisibility(TextView.INVISIBLE);
                sprueText8.setVisibility(TextView.INVISIBLE);
                sprueDims[1].setVisibility(EditText.INVISIBLE);
                sprueDims[4].setVisibility(EditText.INVISIBLE);
                break;
            case 2:
                sprueText2.setText(getResources().getString(R.string.width));
                sprueText5.setText(getResources().getString(R.string.width));
                sprueText3.setVisibility(TextView.VISIBLE);
                sprueText8.setVisibility(TextView.VISIBLE);
                sprueDims[1].setVisibility(EditText.VISIBLE);
                sprueDims[4].setVisibility(EditText.VISIBLE);
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

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values.setProjectName(String.valueOf(projectText.getText()));
                startActivity(new Intent(SprueActivity.this, SavesActivity.class));
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
                Support.sprueValArray = inputData;
                exportValues();

                if (inputData[6] != null){
                        Support.initialMassFlowrate = inputData[6];
                } else {
                    Support.initialMassFlowrate = 0;
                }

                Values.setProjectName(String.valueOf(projectText.getText()));
                Support.spinnerNavigator(SprueActivity.this, position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sprueCalcBtn:
                userDataInput = false;
                values = Support.dataOutput;
                if (inputData[7] == 1) {
                    for (int i = 0; i < inputData.length - 1; i++) {
                        if (values[i] != null) {
                            if (i == 5) {
                                sprueDims[i].setText(String.valueOf(Math.round(values[i])));
                            } else if (i == 6) {
                                sprueDims[i].setText((String.valueOf(Support.round(values[i], 2))));
                            } else {
                                sprueDims[i].setText((String.valueOf(Support.round(values[i], 1))));
                            }
                        }
                    }
                    Support.sprueWidth = Double.valueOf(String.valueOf(values[3]));
                    userDataInput = true;
                } else {
                    Toast.makeText(this, "invalid input data", Toast.LENGTH_LONG).show();
                    userDataInput = true;
                }
                break;

            case R.id.sprueResetBtn:

                for(int i=0; i < Support.dataOutput.length -1; i++){
                    Support.modified[i] = false;
                    Support.dataOutput[i] = null;
                    Support.sprueValArray[i] = null;
                    inputData[i] = null;
                    cleanData();
                    sprueDims[i].setText("");
                }
                inputData[7] = 0.0;
                Support.computeSprue(inputData);
                calcBtn.setBackgroundColor(getResources().getColor(R.color.button_red));
                break;

            case R.id.sprueHelpBtn:
                AlertDialog.Builder helpAlert  = new AlertDialog.Builder(this);
                helpAlert.setMessage(R.string.sprueHelp);
                helpAlert.setTitle("SPRUE");
                helpAlert.setPositiveButton("OK", null);
                helpAlert.setCancelable(true);
                helpAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                helpAlert.create().show();
                break;
        }
    }

    private void cleanData() {
        Support.sprueVal0 = 0;
        Support.sprueVal1 = 0;
        Support.sprueVal2 = 0;
        Support.sprueVal3 = 0;
        Support.sprueVal4 = 0;
        Support.sprueVal5 = 0;
        Support.sprueVal6 = 0;
        Support.sprueVal7 = 0;
    }

    private void exportValues() {
        if (inputData[0] != null){Support.sprueVal0 = inputData[0];}
        if (inputData[1] != null){Support.sprueVal1 = inputData[1];}
        if (inputData[2] != null){Support.sprueVal2 = inputData[2];}
        if (inputData[3] != null){Support.sprueVal3 = inputData[3];}
        if (inputData[4] != null){Support.sprueVal4 = inputData[4];}
        if (inputData[5] != null){Support.sprueVal5 = inputData[5];}
        if (inputData[6] != null){Support.sprueVal6 = inputData[6];}
        if (inputData[7] != null){Support.sprueVal7 = inputData[7];}
    }

    private void importValues() {
        if (Support.sprueValArray[0] !=null){
            inputData[0] = Support.sprueValArray[0];
        }else if (Support.sprueVal0 != 0){inputData[0] = Support.sprueVal0;}

        if (Support.sprueValArray[1] != null){
            inputData[1] = Support.sprueValArray[1];
        }else if (Support.sprueVal1 != 0){inputData[1] = Support.sprueVal0;}

        if (Support.sprueValArray[2] != null){
            inputData[2] = Support.sprueValArray[2];
        }else if (Support.sprueVal2 != 0){inputData[2] = Support.sprueVal0;}

        if (Support.sprueValArray[3] != null){
            inputData[3] = Support.sprueValArray[3];
        }else if (Support.sprueVal3 != 0){inputData[3] = Support.sprueVal0;}

        if (Support.sprueValArray[4] != null){
            inputData[4] = Support.sprueValArray[4];
        }else if (Support.sprueVal4 != 0){inputData[4] = Support.sprueVal0;}

        if (Support.sprueValArray[5] != null){
            inputData[5] = Support.sprueValArray[5];
        }else if (Support.sprueVal5 != 0){inputData[5] = Support.sprueVal0;}

        if (Support.sprueValArray[6] != null){
            inputData[6] = Support.sprueValArray[6];
        }else if (Support.sprueVal6 != 0){inputData[6] = Support.sprueVal0;}

        if (Support.sprueValArray[7] != null){
            inputData[7] = Support.sprueValArray[7];
        }else if (Support.sprueVal7 != 0){inputData[7] = Support.sprueVal0;}
    }
}


