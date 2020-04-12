package com.Raf.foundryman;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String[] tools;
    String projectName;
    Spinner toolSpinner;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = false;
        tools = getResources().getStringArray(R.array.tools);
        projectName = Values.getProjectName();
        configureOptionsBtn();
        configureProjectText();
        configureSpinner();
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(0);
    }

    private void configureProjectText() {
        final EditText projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());
        projectText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Values.setProjectName(projectText.getText().toString());
                //Toast.makeText (MainActivity.this, "name has been modified", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OptionsActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (flag == false) {
            flag = true;
        } else {
            Support.spinnerNavigator(MainActivity.this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

