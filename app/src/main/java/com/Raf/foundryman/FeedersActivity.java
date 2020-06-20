package com.Raf.foundryman;

import android.content.Intent;
import android.os.Bundle;
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
    ImageButton addBtn;
    Button sleeveBtn;
    TextView feederDiaInput;
    TextView feederHeightInput;
    TextView feederAmmountInput;
    EditText projectText;
    static TextView totalMass;

    RecyclerView feedersRecyclerView;
    FeedersAdapter fAdapter;

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
        totalMass = findViewById(R.id.totalFeederMass);
        projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());

        fAdapter = new FeedersAdapter(this, Support.feederAmount, Support.feederTypeName,
                Support.feederDiameter, Support.feederHeight, Support.feederMod, Support.feederMass);
        feedersRecyclerView.setAdapter(fAdapter);
        feedersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        configureOptionsBtn();
        configureFeederBtn();
        configureAddBtn();
        configureSpinner();

        topText.setText(tools[4].toUpperCase());
    }

    public static void setTotalFeederMass(){
        Support.recalculateFeederMass();
        totalMass.setText(Double.toString(Support.totalFeederMass));
    }

    private void configureSpinner() {
        toolSpinner = findViewById(R.id.spinner);
        toolSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setAdapter(adapter);
        toolSpinner.setSelection(4);
    }

    private void configureFeederBtn(){
        sleeveBtn = findViewById(R.id.feederSleeveBtn);
        sleeveBtn.setBackgroundColor(getResources().getColor(R.color.sleeve_gray));
        sleeveBtn.setText("No Sleeve");
        sleeveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++Support.feederBtnCounter > 2){
                    Support.feederBtnCounter = 0;
                }
                switch(Support.feederBtnCounter){
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

    private String getFeederType(int type){
        String feederName;
        if (type == 0) {
            feederName = "No Sleeve";
        }else if(type == 1){
            feederName = "Insulated";
        } else {
            feederName = "Exothermic";
        }
        return feederName;
    }

    private void configureAddBtn(){
        addBtn = findViewById(R.id.feederAddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int btnAmount;
                Double btnDia;
                Double btnHeight;
                Double btnMass;

                if (!String.valueOf(feederAmmountInput.getText()).isEmpty() &&
                        !String.valueOf(feederDiaInput.getText()).isEmpty() &&
                        !String.valueOf(feederHeightInput.getText()).isEmpty()) {
                    Button btn = findViewById(R.id.save_remove_btn);
                    Support.removeBtn.add(btn);
                    btnAmount = Integer.valueOf(String.valueOf(feederAmmountInput.getText()));
                    btnDia = Double.valueOf(String.valueOf(feederDiaInput.getText()));
                    btnHeight = Double.valueOf(String.valueOf(feederHeightInput.getText()));
                    btnMass = Support.round(btnAmount * btnHeight / 1000 * Math.PI *
                            (btnDia / 2 / 1000) * (btnDia / 2 / 1000) * Support.density, 2);
                    feederAmmountInput.setText("");
                    feederDiaInput.setText("");
                    feederHeightInput.setText("");

                    Support.feederAmount.add(btnAmount);
                    Support.feederTypeName.add(getFeederType(Support.feederBtnCounter));
                    Support.feederDiameter.add(btnDia);
                    Support.feederHeight.add(btnHeight);
                    Support.feederMod.add(Support.modulus(btnHeight, btnDia, Support.feederBtnCounter));
                    Support.feederMass.add(btnMass);

                    fAdapter.notifyItemChanged(Support.feederAmount.size()-1);
                    setTotalFeederMass();
                    if (!Support.feederIndex.isEmpty()){
                        Support.feederIndex.add(Support.feederIndex.get(Support.feederIndex.size() - 1) + 1);
                    } else {
                        Support.feederIndex.add(0);
                    }
                }
            }
        });
    }

    private void configureOptionsBtn(){
        ImageButton optionsBtn = findViewById(R.id.MainOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedersActivity.this, SavesActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (flag == false) {
            flag = true;
        } else {
            Values.setProjectName(String.valueOf(projectText.getText()));
            Support.spinnerNavigator(FeedersActivity.this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

