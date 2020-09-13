package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class editDiologActiity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diolog_actiity);
        TextView textViewName=findViewById(R.id.ClassName);
        textViewName.setText(getIntent().getStringExtra("name"));
        TextView textViewCridet=findViewById(R.id.CreditEditText);
        String temp=getIntent().getStringExtra("statute");
        if(temp.equals("unfinish")){
            textViewCridet.setText("当前尚未完成");
        }else {
            textViewCridet.setText("今日已完成");
        }
        TextView textViewRoom=findViewById(R.id.ClassEditText);
        textViewRoom.setText(getIntent().getStringExtra("room"));
    }
}
