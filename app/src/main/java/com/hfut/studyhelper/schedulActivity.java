package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dialog.EnsureDialog;
import com.dialog.listener.IDialogEnsureClickListener;

public class schedulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedul);
        View statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        Button confimButton=findViewById(R.id.ConfirmButton);
        Button returnButton=findViewById(R.id.ReturnButton);
        final EditText editText1=findViewById(R.id.editText);
        final EditText editText2=findViewById(R.id.editTextplace);
        confimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText1.getText().toString().length()!=0&&editText2.getText().toString().length()!=0){
                    Intent intent = new Intent();
                    intent.putExtra("things",editText1.getText().toString());
                    intent.putExtra("place",editText2.getText().toString());
                    setResult(1,intent);
                    finish();
                }else {
                    final EnsureDialog ensureDialog = new EnsureDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "事件或者地点未填写!");
                    bundle.putString("left", "确认");
                    bundle.putString("right", "取消");
                    ensureDialog.setArguments(bundle);
                    ensureDialog.show(getSupportFragmentManager(), "ensure");

                    ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                        @Override
                        public void onEnsureClick() {
                            ensureDialog.dismiss();
                        }

                        @Override
                        public void onCancelClick() {
                            ensureDialog.dismiss();
                        }
                    });

                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0,intent);
                finish();
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }
    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0; //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
