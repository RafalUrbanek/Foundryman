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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedersActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    Spinner toolSpinner;
    Boolean flag;
    TextView topText;
    RecyclerView feedersRecyclerView;
    ImageButton addBtn;
    Button sleeveBtn;
    TextView feederDiaInput;
    TextView feederHeightInput;
    TextView feederAmmountInput;

    String[] feederType = {};
    int[] amount = {};
    double[] diameter = {};
    double[] height = {};
    double[] mod = {};
    double[] mass = {};
    int feederBtnCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeders);
        flag = false;
        tools = getResources().getStringArray(R.array.tools);
        topText = findViewById(R.id.summaryTxtFeeders);
        feederDiaInput = findViewById(R.id.feederDiaInput);
        feederHeightInput = findViewById(R.id.feederHeightInput);
        feederAmmountInput = findViewById(R.id.feederAmmountInput);
        feedersRecyclerView = findViewById(R.id.FeederRecycler);

        FeedersAdapter fAdapter = new FeedersAdapter(this, amount, feederType, diameter, height, mod, mass);
        feedersRecyclerView.setAdapter(fAdapter);
        feedersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        configureOptionsBtn();
        configureFeederBtn();
        configureAddBtn();
        configureProjectText();
        configureSpinner();

        topText.setText(tools[5].toUpperCase());

    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(5);
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

    private void configureFeederBtn(){
        sleeveBtn = findViewById(R.id.feederSleeveBtn);
        sleeveBtn.setBackgroundColor(getResources().getColor(R.color.sleeve_gray));
        sleeveBtn.setText("No Sleeve");
        sleeveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++feederBtnCounter > 2){
                    feederBtnCounter = 0;
                }
                switch(feederBtnCounter){
                    case 0:
                        sleeveBtn.setText("No Sleeve");
                        sleeveBtn.setBackgroundColor(getResources().getColor(R.color.sleeve_gray));
                        break;
                    case 1:
                        sleeveBtn.setText("Insulated");
                        sleeveBtn.setBackgroundColor(getResources().getColor(R.color.sleeve_yellow));
                        break;
                    case 2:
                        sleeveBtn.setText("Exothermic");
                        sleeveBtn.setBackgroundColor(getResources().getColor(R.color.sleeve_red));
                        break;
                }
            }
        });
    }

    private void configureAddBtn(){
        addBtn = findViewById(R.id.feederAddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double ammount;
                Double dia;
                Double height;
                Double mass;

                if (!String.valueOf(feederAmmountInput.getText()).isEmpty() &&
                        !String.valueOf(feederDiaInput.getText()).isEmpty() &&
                        !String.valueOf(feederHeightInput.getText()).isEmpty()) {

                    ammount = Double.valueOf(String.valueOf(feederAmmountInput.getText()));
                    dia = Double.valueOf(String.valueOf(feederDiaInput.getText()));
                    height = Double.valueOf(String.valueOf(feederHeightInput.getText()));
                    mass = ammount * height / 1000 * Math.PI * (dia / 2 / 1000) * (dia / 2 / 1000) * Support.density;
                    
                    feederAmmountInput.setText("");
                    feederDiaInput.setText("");
                    feederHeightInput.setText("");

                }
            }
        });
    }

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedersActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (flag == false) {
            flag = true;
        } else {
            Support.spinnerNavigator(FeedersActivity.this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

