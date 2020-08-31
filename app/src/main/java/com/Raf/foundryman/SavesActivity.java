package com.Raf.foundryman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class SavesActivity extends AppCompatActivity {

    RecyclerView savesRecyclerView;
    SavesAdapter savesAdapter;
    static EditText projectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Support.updateSaveList(this);
        setContentView(R.layout.activity_saves);
        projectText = findViewById(R.id.projectNameTxt);
        projectText.setText(Values.getProjectName());
        savesRecyclerView = findViewById(R.id.save_recycler);
        savesAdapter = new SavesAdapter(this, Support.saveName, Support.saveMatType);

        savesRecyclerView.setAdapter(savesAdapter);
        savesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        configureBackBtn();
        configureSaveBtn();
        constructSavedList();
    }

    private void constructSavedList() {
        for (int i = 0; i<Support.saveName.size(); i++){
            savesAdapter.notifyItemChanged(i);
        }
    }

    private void configureBackBtn(){
            ImageButton backBtn = findViewById(R.id.optionsBtnExit);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Values.setProjectName(String.valueOf(projectText.getText()));
                startActivity(new Intent(SavesActivity.this, SummaryActivity.class));
            }
        });
    }

    private void display(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void configureSaveBtn(){
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Values.setProjectName(String.valueOf(projectText.getText()));
                String projectList = MemAccess.load(SavesActivity.this, Support.projectListAddress);

                if (!projectList.contains(Values.getProjectName())){

                    int index = Support.saveName.size();
                    Support.saveName.add(Values.getProjectName());
                    Support.saveMatName.add(Support.materialName);
                    Support.saveMatType.add(Support.materialType);
                    MemAccess.exportProject(SavesActivity.super.getApplicationContext());
                    savesAdapter.notifyItemChanged(index);
                    display("Project " + Values.getProjectName() + " has been saved");
                } else {
                    display("Project with this filename already exist");
                }
            }
        });
    }
}
