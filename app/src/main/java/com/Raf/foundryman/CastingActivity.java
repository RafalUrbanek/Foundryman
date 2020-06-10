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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CastingActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    String[] alloys;
    Spinner toolSpinner;
    Boolean flag;
    TextView topText;
    EditText matNameText, matDensityText, partWeightText, PartsPerMouldText;
    Button applyBtn;
    Spinner alloySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casting);
        flag = false;

        initialize();
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
        configureAlloySpinner();
        configureApplyBtn();


        topText.setText(tools[4].toUpperCase());
    }

    private void configureApplyBtn() {
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(matNameText.getText()).isEmpty()){
                    Support.materialName = String.valueOf(matNameText);
                }

                if (!String.valueOf(matDensityText.getText()).isEmpty() &&
                        Double.valueOf(String.valueOf(matDensityText.getText())) > 0){
                    Support.density = Double.valueOf(String.valueOf(matDensityText.getText()));
                }

                if (!String.valueOf(partWeightText.getText()).isEmpty() &&
                        Double.valueOf(String.valueOf(partWeightText.getText())) > 0){
                    Support.castingMass = Double.valueOf(String.valueOf(partWeightText.getText()));
                }

                if (!String.valueOf(PartsPerMouldText.getText()).isEmpty() &&
                        Double.valueOf(String.valueOf(PartsPerMouldText.getText())) > 0){
                    Support.partsPerMould = Integer.valueOf(String.valueOf(PartsPerMouldText.getText()));
                }

                displayToast("Changes applied");
            }
        });
    }

    private void displayToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void configureAlloySpinner() {
        alloySpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alloys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alloySpinner.setAdapter(adapter);
        alloySpinner.setSelection(0);
    }

    private void initialize() {
        tools = getResources().getStringArray(R.array.tools);
        alloys = getResources().getStringArray(R.array.alloys);
        topText = findViewById(R.id.summaryTxtLShape);
        matNameText = findViewById(R.id.casting_mat_name);
        matDensityText = findViewById(R.id.casting_mat_density);
        partWeightText = findViewById(R.id.casting_part_weight);
        PartsPerMouldText = findViewById(R.id.casting_parts_per_mould);
        applyBtn = findViewById(R.id.casting_apply_btn);
        alloySpinner = findViewById(R.id.casting_alloy_spinner);
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(4);
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
                startActivity(new Intent(CastingActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (arg0.getId() == R.id.casting_alloy_spinner){
            if (flag == false) {
                flag = true;
            } else {
                switch (arg0.getSelectedItemPosition()){
                    case 0:
                        // Aluminium
                        matDensityText.setText("2400");
                        break;

                    case 1:
                        // Copper
                        matDensityText.setText("8000");
                        break;

                    case 2:
                        // Iron
                        matDensityText.setText("7000");
                        break;

                    case 3:
                        // Magnesium
                        matDensityText.setText("1600");
                        break;
                }
            }
        } else {
            if (flag == false) {
                flag = true;
            } else {
                Support.spinnerNavigator(CastingActivity.this, position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

