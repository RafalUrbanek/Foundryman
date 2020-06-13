package com.Raf.foundryman;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ToolsActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    String[] modGeometries;
    Spinner toolSpinner;
    Boolean flag, converterFlag;
    TextView topText;
    Spinner modSpinner;
    TextView modText1, modText2, modText3, modText4, modText5;
    TextView volumeResult, modulusResult;
    EditText modulusEdit1, modulusEdit2, modulusEdit3, mmText, inchText;
    ImageView geometryPic;
    Button modCalculateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        flag = false;
        converterFlag = true;

        initialize();
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        configureModSpinner();
        configureModBtn();
        configureConverterFields();

        topText.setText(tools[6].toUpperCase());
    }

    private void configureConverterFields() {

        mmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                double mmVal;

                if (!String.valueOf(mmText.getText()).isEmpty()) {
                    mmVal = Double.valueOf(String.valueOf(mmText.getText()));
                } else {
                    mmVal = 0;
                }
                if (converterFlag){
                    converterFlag = false;
                    inchText.setText(String.valueOf(mmToInch(mmVal)));
                } else {
                    converterFlag = true;
                }
            }
        });

        inchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                double inchVal;

                if (!String.valueOf(inchText.getText()).isEmpty()) {
                    inchVal = Double.valueOf(String.valueOf(inchText.getText()));
                } else {
                    inchVal = 0;
                }

                if (converterFlag){
                    converterFlag = false;
                    mmText.setText(String.valueOf(inchToMm(inchVal)));
                } else {
                    converterFlag = true;
                }
            }
        });
    }

    private double mmToInch(double mm){
        double inch = mm / 25.4;
        inch *= 100;
        inch = Math.round(inch);
        inch /= 100;
        return inch;
    }

    private double inchToMm(double inch){
        double mm = inch * 25.4;
        mm *= 100;
        mm = Math.round(mm);
        mm /= 100;
        return mm;
    }

    private void displayToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    private void configureModBtn() {

        modCalculateBtn.setOnClickListener(new View.OnClickListener() {
            double mod = 0;
            double vol = 0;
            double area = 0;
            double height = 0;
            double length = 0;
            double width = 0;
            double rad = 0;
            @Override
            public void onClick(View v) {
                if (modSpinner.getSelectedItemPosition() == 0){
                    if (!String.valueOf(modulusEdit1.getText()).isEmpty() &&
                            !String.valueOf(modulusEdit2.getText()).isEmpty() ){
                        height =  Double.valueOf(String.valueOf(modulusEdit1.getText()));
                        rad = Double.valueOf(String.valueOf(modulusEdit2.getText()));
                        if (height > 0 && rad > 0) {
                            vol = Math.PI * rad * rad * height;
                            area = ((Math.PI * rad * rad) + (2 * (2 * Math.PI * rad) * height));
                            mod = vol / area;

                            volumeResult.setText(String.valueOf(Math.round(vol)));
                            modulusResult.setText(String.valueOf(Support.round(mod, 2)));
                        } else {
                            displayToast("Please enter valid numbers for height and radius");
                        }

                    } else {
                        displayToast("Please enter all required values");
                    }
                } else if (modSpinner.getSelectedItemPosition() == 1){
                    if (!String.valueOf(modulusEdit1.getText()).isEmpty() &&
                            !String.valueOf(modulusEdit2.getText()).isEmpty() &&
                            !String.valueOf(modulusEdit3.getText()).isEmpty()){
                        height =  Double.valueOf(String.valueOf(modulusEdit1.getText()));
                        length = Double.valueOf(String.valueOf(modulusEdit2.getText()));
                        width = Double.valueOf(String.valueOf(modulusEdit3.getText()));
                        if (height > 0 && length > 0 && width > 0) {
                            vol = height * length * width;
                            area = 2 * (length * width) + 2 * (length * height) + 2 * (width * height);
                            mod = vol / area;

                            volumeResult.setText(String.valueOf(Math.round(vol)));
                            modulusResult.setText(String.valueOf(Support.round(mod, 2)));
                        } else {
                            displayToast("Please enter valid numbers for height and width and length");
                        }

                    } else {
                        displayToast("Please enter all required values");
                    }

                }else if(modSpinner.getSelectedItemPosition() == 2){
                    if (!String.valueOf(modulusEdit1.getText()).isEmpty()){
                        rad = Double.valueOf(String.valueOf(modulusEdit1.getText()));
                        if (rad > 0){
                            area = 4 * Math.PI * rad * rad;
                            vol = (4/3) * Math.PI * (rad * rad * rad);
                            mod = vol / area;

                            volumeResult.setText(String.valueOf(Math.round(vol)));
                            modulusResult.setText(String.valueOf(Support.round(mod, 2)));
                        } else {
                            displayToast("Please enter valid radius value");
                        }

                    } else {
                        displayToast("Please enter valid radius value");
                    }
                }
            }
        });
    }

    private void configureModSpinner() {
        modSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, modGeometries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modSpinner.setAdapter(adapter);
        modSpinner.setSelection(0);
    }

    private void initialize() {
        modSpinner = findViewById(R.id.tools_mod_spinner);
        modText1 = findViewById(R.id.tools_text1);
        modText2 = findViewById(R.id.tools_text2);
        modText3 = findViewById(R.id.tools_text3);
        modText4 = findViewById(R.id.tools_text4);
        modText5 = findViewById(R.id.tools_text5);
        volumeResult = findViewById(R.id.tools_volume_text);
        modulusResult = findViewById(R.id.tools_modulus_text);
        modulusEdit1 = findViewById(R.id.tools_editText1);
        modulusEdit2 = findViewById(R.id.tools_editText2);
        modulusEdit3 = findViewById(R.id.tools_editText3);
        mmText = findViewById(R.id.tools_cm_text);
        inchText = findViewById(R.id.tools_inch_text);
        topText = findViewById(R.id.summaryTxtTools);
        tools = getResources().getStringArray(R.array.tools);
        modGeometries = getResources().getStringArray(R.array.mod_geometries);
        geometryPic = findViewById(R.id.tools_pic);
        modCalculateBtn = findViewById(R.id.tools_mod_calc_btn);
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(6);
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
                startActivity(new Intent(ToolsActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (arg0.getId() == R.id.tools_mod_spinner){
            if (flag == false) {
                flag = true;
            } else {
                setModToolLayout(arg0.getSelectedItemPosition());
            }
        } else {
            if (flag == false) {
                flag = true;
            } else {
                Support.spinnerNavigator(ToolsActivity.this, position);
            }
        }
    }

    private void setModToolLayout(int position) {
        modulusResult.setText("");
        volumeResult.setText("");
        modulusEdit1.setText("");
        modulusEdit2.setText("");
        modulusEdit3.setText("");
        switch(position){
            case 0:
                // cylinder
                geometryPic.setImageResource(R.mipmap.cylinder);
                modText2.setVisibility(View.VISIBLE);
                modulusEdit2.setVisibility(View.VISIBLE);
                modText3.setVisibility(View.INVISIBLE);
                modulusEdit3.setVisibility(View.INVISIBLE);
                modText1.setText("height h");
                modText2.setText("radius r");

                break;

            case 1:
                // cuboid
                geometryPic.setImageResource(R.mipmap.cuboid);
                modText2.setVisibility(View.VISIBLE);
                modulusEdit2.setVisibility(View.VISIBLE);
                modText3.setVisibility(View.VISIBLE);
                modulusEdit3.setVisibility(View.VISIBLE);
                modText1.setText("height h");
                modText2.setText("length x");
                modText3.setText("width y");
                break;

            case 2:
                // sphere
                geometryPic.setImageResource(R.mipmap.sphere);
                modText2.setVisibility(View.INVISIBLE);
                modulusEdit2.setVisibility(View.INVISIBLE);
                modText3.setVisibility(View.INVISIBLE);
                modulusEdit3.setVisibility(View.INVISIBLE);
                modText1.setText("radius r");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

