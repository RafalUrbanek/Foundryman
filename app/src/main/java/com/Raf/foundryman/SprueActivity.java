package com.Raf.foundryman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner toolSpinner;
    Spinner sprueSpinner;
    TextView topText;
    TextView sprueText2, sprueText3, sprueText5, sprueText8;
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
        inputData[7] = 0.0;
        sprueDims = new EditText[7];

        tools = getResources().getStringArray(R.array.tools);
        sprueTypes = getResources().getStringArray(R.array.sprues);
        topText = findViewById(R.id.summaryTxtLShape);
        sprueText2 = findViewById(R.id.sprueText2);
        sprueText3 = findViewById(R.id.sprueText3);
        sprueText5 = findViewById(R.id.sprueText5);
        sprueText8 = findViewById(R.id.sprueText8);

        sprueDims[0] = findViewById(R.id.sprueDim1);
        sprueDims[1] = findViewById(R.id.sprueDim2);
        sprueDims[2] = findViewById(R.id.sprueDim3);
        sprueDims[3] = findViewById(R.id.sprueDim4);
        sprueDims[4] = findViewById(R.id.sprueDim5);
        sprueDims[5] = findViewById(R.id.sprueDim6);
        sprueDims[6] = findViewById(R.id.sprueDim7);

        populateDataFieldListeners();
        populateDataArray();

        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        configureSprueSpinner();
        configureDisplayState();
        setCalcBtn();
        setResetBtn();
        setHelpButton();
        topText.setText(tools[1].toUpperCase());
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

    // adds listeners to value fields
    public void addCalcBtnListener(final EditText dimInput) {
        dimInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int position = ((int)dimInput.getId() % 10);
                if (userDataInput) Support.modified[position] = false;
                if (!String.valueOf(dimInput.getText()).isEmpty() && userDataInput){
                    Support.modified[position] = true;
                }

                // modify the array based on values in sprueDim values
                populateDataArray();
                inputData = Support.computeSprue(inputData);

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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sprueCalcBtn:
                userDataInput = false;
                values = Support.dataOutput;
                for(int i = 0; i< inputData.length -1; i++){
                    if (values[i] != null){
                        double round;
                        if (i == 5) {
                            sprueDims[i].setText(String.valueOf(Math.round(values[i])));
                        } else if(i == 6){
                            round = Math.round(values[i] * 100);
                            sprueDims[i].setText(String.valueOf(round / 100));
                        } else{
                            round = Math.round(values[i] * 10);
                            sprueDims[i].setText(String.valueOf(round / 10));
                        }
                    }
                }
                userDataInput = true;
                break;

            case R.id.sprueResetBtn:
                for(int i=0; i < Support.modified.length -1; i++){
                    Support.modified[i] = false;
                }

                for(int i=0; i < Support.dataOutput.length -1; i++){
                    Support.dataOutput[i] = null;
                    inputData[i] = null;
                    sprueDims[i].setText("");
                }
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
}


