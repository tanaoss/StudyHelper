package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class dialogActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_activities);
        TextView textViewName=findViewById(R.id.ClassName);
        textViewName.setText(getIntent().getStringExtra("name"));
        TextView textViewCridet=findViewById(R.id.CreditEditText);
        textViewCridet.setText(getIntent().getStringExtra("credit"));
        TextView textViewRoom=findViewById(R.id.ClassEditText);
        textViewRoom.setText(getIntent().getStringExtra("room"));
        TextView textViewCode=findViewById(R.id.CodeEditText);
        textViewCode.setText(getIntent().getStringExtra("code"));
    }
}
