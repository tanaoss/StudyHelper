package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class configTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_time);
        ImageView imageViewok=findViewById(R.id.ok);
        ImageView imageViewCancle=findViewById(R.id.caccle);
        final EditText editTextT=findViewById(R.id.editText3);
        imageViewCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextT.getText().toString().length()==0){
                    Toast toast=Toast.makeText(configTime.this,null,Toast.LENGTH_SHORT);
                    toast.setText("请填写专注时间");
                    toast.show();
                    return;
                }
                if(editTextT.getText().toString().contains(".")){
                    Toast toast=Toast.makeText(configTime.this,null,Toast.LENGTH_SHORT);
                    toast.setText("请填写正确的专注时间");
                    toast.show();
                    return;
                }
                Intent intent = new Intent(configTime.this,TimeActivity.class);
                intent.putExtra("time",editTextT.getText().toString());
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(intent);
                finish();
            }
        });
    }
}
