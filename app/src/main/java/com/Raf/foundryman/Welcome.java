package com.Raf.foundryman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        updateSaveList();
        this.startActivity(new Intent(this, CastingActivity.class));
    }

    private void updateSaveList() {
        String projects = MemAccess.load(this, Support.projectListAddress);
        int position = 0;
        int index = 0;
        boolean stop = false;
        int newIndex;

        while(true){
            if (projects.isEmpty()){
                break;
            }
            newIndex = projects.indexOf("~", index + 1);
            if (newIndex == -1){
                stop = true;
                newIndex = projects.length()-1;
            }
            String project = projects.substring(index, newIndex);
            int indexOfmarker = project.indexOf("|");
            index = newIndex;
            Support.projectIndex.add(position);
            position++;
            Support.saveName.add(project.substring(1, project.indexOf("|")));
            Support.saveMatType.add(project.substring(project.indexOf("|") + 1, project.indexOf("|", indexOfmarker + 1)));
            Support.saveMatName.add(project.substring(project.indexOf("|", indexOfmarker + 1) + 1));
            if (stop){
                break;
            }
        }
    }
}
