package com.Raf.foundryman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SavesActivity extends AppCompatActivity {

    RecyclerView savesRecyclerView;
    SavesAdapter savesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saves);
        savesRecyclerView = findViewById(R.id.save_recycler);
        savesAdapter = new SavesAdapter(this, Support.saveName, Support.saveMatType, Support.saveMatName, Support.saveWeight);

        Support.saveName.add("TEST1");
        Support.saveMatName.add("TEST1");
        Support.saveMatType.add("TEST1");
        Support.saveWeight.add(66.6);

        savesRecyclerView.setAdapter(savesAdapter);
        savesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        configureBackBtn();
        configureSaveBtn();
        configureLoadBtn();
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
                startActivity(new Intent(SavesActivity.this, SummaryActivity.class));
            }
        });
    }

    private void configureSaveBtn(){
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void configureLoadBtn(){
        Button loadBtn = findViewById(R.id.load_btn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
