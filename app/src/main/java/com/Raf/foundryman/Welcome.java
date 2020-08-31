package com.Raf.foundryman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    Button forward, about, author;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        forward = findViewById(R.id.welcome_cont_btn);
        about = findViewById(R.id.welcome_about_btn);
        author = findViewById(R.id.welcome_author_btn);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder helpAlert  = new AlertDialog.Builder(context);
                helpAlert.setMessage(R.string.about);
                helpAlert.setTitle("Foundryman");
                helpAlert.setPositiveButton("OK", null);
                helpAlert.setCancelable(true);
                helpAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                helpAlert.create().show();

            }
        });

        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder helpAlert  = new AlertDialog.Builder(context);
                helpAlert.setMessage(R.string.author);
                helpAlert.setTitle("About the Author");
                helpAlert.setPositiveButton("OK", null);
                helpAlert.setCancelable(true);
                helpAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                helpAlert.create().show();

            }
        });


    }

    private void forward(){
        this.startActivity(new Intent(this, CastingActivity.class));
    }
}
