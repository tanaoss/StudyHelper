package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.EnsureDialog;
import com.dialog.listener.IDialogEnsureClickListener;

import java.util.Random;

public class TimeActivity extends AppCompatActivity {
   public static int finshFlag=0;
    String[] tags=new String[]{
            "你日渐平庸，甘于平庸，将继续平庸",
            "且视他人之疑目如盏盏鬼火，大胆地去走你的夜路",
            "抱怨身处黑暗，不如提灯前行",
            "在世间,本就是各人下雪,各人有各人的隐晦与皎洁.",
            "没有不可治愈的伤痛，没有不能结束的沉沦，所有失去的，会以另一种方式归来",
            "我喜欢的人很优秀,我努力的理由是配得上他",
            "谁终将声震天下，必长久深自缄默；谁终将点燃闪电，必长久如云漂泊",
            "人的一生是万里山河，来往无数客，有人给山河添色，有人使日月无光，有人改他江流，有人塑他梁骨，大限到时，不过是立在山巅，江河回望。",
            "从不试图摘月,我要这月亮为我而来",
            "同是寒窗苦读,怎愿甘拜下风",
            "本以为可以忘记你，却总是在梦里，角落里，记忆的零散碎片中找到你。",
            "没有人喜欢学习，只是社会需要成绩。",
            "其实人字最孤独，偏旁部首都没有。",
            "躲起来的星星也在努力发光 ,你也要加油",
            "那些看似不起波澜的日复一日,会在某天让你看到坚持的意义",
            "即使辛苦,我还是选择去过滚烫的人生。",
            "不要你唯唯诺诺，我要你风流快活。",
            "在命运为你安排的属于自己的时区里，一切都准时。",
            "在等待的日子里，刻苦读书，谦卑做人，养的深根，日后才能枝繁叶茂。"
    };
   private CountDownProgressBar cpb_countdown1;
    @Override
    public void finish() {
        if(finshFlag==0){
            final EnsureDialog ensureDialog = new EnsureDialog();
            Bundle bundle = new Bundle();
            bundle.putString("message", "确认退出(本次计时将不纳入统计!)?");
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
                    try {
                        if(cpb_countdown1!=null)
                            finshFlag=2;
                            cpb_countdown1.myfinish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finshFlag=1;
                    ensureDialog.dismiss();
                    finish();
                }
            });
        }
        if(finshFlag==1){
            finshFlag=0;
            super.finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Button btn_start = findViewById(R.id.btn_start);
        final String id=getIntent().getStringExtra("id");
        final int time=Integer.parseInt(getIntent().getStringExtra("time"));
        final String name=getIntent().getStringExtra("name");
        TextView tname=findViewById(R.id.name);
        tname.setText(name);
        Random df = new Random();
        int number = df.nextInt(tags.length);
        TextView te=findViewById(R.id.textView8);
        te.setText(tags[number]);
        final CountDownProgressBar cpb_countdown = (CountDownProgressBar) findViewById(R.id.cpb_countdown);
        cpb_countdown1=cpb_countdown;
        btn_start.setOnClickListener(new View.OnClickListener() {
            int flag=0;
            @Override
            public void onClick(View v) {
                if(flag==0){
                    flag=1;
                    cpb_countdown.setMaxValue(time*100*2);
                    cpb_countdown.setDuration(time*10000*6, new CountDownProgressBar.OnFinishListener() {
                        @Override
                        public void onFinish() {
                            if(finshFlag!=2){
                                Utils.updateCost(TimeActivity.this,id,time);
                                Toast.makeText(TimeActivity.this, "完成了", Toast.LENGTH_SHORT).show();
                            }
                            finshFlag=1;
                        }
                    });
                }
            }
        });
    }
}
