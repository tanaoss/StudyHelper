package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dialog.EnsureDialog;
import com.dialog.listener.IDialogEnsureClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class schedule_week extends AppCompatActivity {
    private ArrayList<String> beginWeek = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> endWeek = new ArrayList<>();
    private ArrayList<String> beginTime = new ArrayList<>();
    private ArrayList<String> endTime = new ArrayList<>();
    String totalWeek;
    String nowWeek;
    TextView beginDate;
    TextView myEndDate;
    TextView beginDayDate;
    TextView myEndDayDate;
    TextView beginTimeDate;
    TextView myEndTimeDate;
    OptionsPickerView  pvNoLinkOptions;
    OptionsPickerView  pvNoLinkOptionsTime;
    TimePickerView pvTime;
    String DateString="";
    String DateDayString="";
    String DateTimeString="";
    String things="";
    String place="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week);
        totalWeek= getIntent().getStringExtra("totalWeek");
        nowWeek=getIntent().getStringExtra("nowWeek");
        View statusBar = findViewById(R.id.statusBarView);
        final ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
       final RelativeLayout frameLayout = findViewById(R.id.root);
        final View mask = new View(schedule_week.this);
        mask.setBackgroundColor(Color.DKGRAY);
        mask.setAlpha(0.5f);
        getNoLinkData();//设置时间数据
        final CardView cardView=findViewById(R.id.card_view);
        final  ConstraintLayout linearLayoutVISIBLE = findViewById(R.id.vlayout);//收缩的面板
        final Button confimButton=findViewById(R.id.ConfirmButton);
        final Button returnButton=findViewById(R.id.ReturnButton);
        final TextView thingEdit=findViewById(R.id.editText);
        final TextView placeEdit=findViewById(R.id.editTextplace);
        TextView confimTextView=findViewById(R.id.textView12);
        TextView returnTextView=findViewById(R.id.textView2);
        final TextView timeEdit=findViewById(R.id.editText2);
        confimTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutVISIBLE.setVisibility(View.GONE);
                frameLayout.removeView(mask);
                DateString=beginDate.getText().toString()+"$"+myEndDate.getText().toString();
                DateDayString=beginDayDate.getText().toString()+"$"+myEndDayDate.getText().toString();
                DateTimeString=beginTimeDate.getText().toString()+"$"+myEndTimeDate.getText().toString();
                if(DateString.contains("定义")==false)
                    timeEdit.setText("时间已设置成功!");
            }
        });
        returnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             linearLayoutVISIBLE.setVisibility(View.GONE);
            frameLayout.removeView(mask);
            }
        });
        TextView textView=findViewById(R.id.editText2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                mask.setClickable(true);
                frameLayout.addView(mask);
                linearLayoutVISIBLE.setVisibility(View.VISIBLE);
                linearLayoutVISIBLE.bringToFront();
                if(DateTimeString.equals("")!=true){
                    beginDate.setText(DateString.split("\\$")[0]);
                    myEndDate.setText(DateString.split("\\$")[1]);
                    beginDayDate.setText(DateDayString.split("\\$")[0]);
                    myEndDayDate.setText(DateDayString.split("\\$")[1]);
                    beginTimeDate.setText(DateTimeString.split("\\$")[0]);
                    myEndTimeDate.setText(DateTimeString.split("\\$")[1]);
                }
//                pvNoLinkOptions.show();
            }
        });
        beginDate=findViewById(R.id.textView19);
        beginDate.setText(Utils.getNowpraseDate());
        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkTimePicker(cardView,1);
                pvTime.show();
            }
        });
        myEndDate=findViewById(R.id.textView21);
        myEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkTimePicker(cardView,2);
                pvTime.show();
            }
        });
        beginDayDate=findViewById(R.id.textView24);
        myEndDayDate=findViewById(R.id.textView26);
        beginTimeDate=findViewById(R.id.textView28);
        myEndTimeDate=findViewById(R.id.textView30);
        beginDayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkOptionsPicker(cardView);
                pvNoLinkOptions.show();
            }
        });
        myEndDayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkOptionsPicker(cardView);
                pvNoLinkOptions.show();
            }
        });
        beginTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkOptionsPicker1(cardView);
                pvNoLinkOptionsTime.show();
            }
        });
        myEndTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkOptionsPicker1(cardView);
                initNoLinkOptionsPicker1(cardView);
                pvNoLinkOptionsTime.show();
            }
        });

        confimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                things=thingEdit.getText().toString();
                place=placeEdit.getText().toString();
                if(things.length()!=0&&place.length()!=0&&DateString.length()!=0&&DateString.split("\\$")[1].equals("未定义")==false){
                    Intent intent = new Intent();
                    intent.putExtra("things",things);
                    intent.putExtra("place",place);
                    intent.putExtra("time",DateString+"#"+DateDayString+"#"+DateTimeString);
                    setResult(1,intent);
                    finish();
                }else {
                    final EnsureDialog ensureDialog = new EnsureDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "事件,时间或者地点未填写!");
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
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    //星期选择器
    private void initNoLinkOptionsPicker(ViewGroup v ) {// 不联动的多级选项
         pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(options1!=0&&options3!=0&&options1<=options3) {
                    beginDayDate.setText(beginWeek.get(options1));
                    myEndDayDate.setText(endWeek.get(options3));
                }else {
                   Toast toast= Toast.makeText(schedule_week.this, null, Toast.LENGTH_SHORT);
                   toast.setText("请选择正确的时间");
                   toast.show();
                }
            }
        })
                .setItemVisibleCount(5)
                .setBackgroundId(Color.parseColor("#87CEEB"))
                .setTitleBgColor(Color.WHITE)
                .setDecorView(v)
                .build();
        pvNoLinkOptions.setNPicker(beginWeek, clothes,endWeek);
        pvNoLinkOptions.setSelectOptions(1, 1, 1);
    }
    //节数选择器
    private void initNoLinkOptionsPicker1(ViewGroup v) {// 不联动的多级选项
        pvNoLinkOptionsTime = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(options1!=0&&options3!=0&&options1<options3){
                    beginTimeDate.setText(beginTime.get(options1));
                    myEndTimeDate.setText(endTime.get(options3));
                }else {
                    Toast toast= Toast.makeText(schedule_week.this, null, Toast.LENGTH_SHORT);
                    toast.setText("请选择正确的时间");
                    toast.show();
                }
//                Toast.makeText(schedule_week.this, str, Toast.LENGTH_SHORT).show();
            }
        })
                .setItemVisibleCount(5)
                .setBackgroundId(Color.parseColor("#87CEEB"))
                .setTitleBgColor(Color.WHITE)
                .setDecorView(v)
                .build();
        pvNoLinkOptionsTime.setNPicker(beginTime, clothes,endTime);
        pvNoLinkOptionsTime.setSelectOptions(1, 1, 1);
    }
    //时间选择器
    private void  initNoLinkTimePicker(ViewGroup v, final int flag){
        //时间选择器
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        final Calendar endDate = Calendar.getInstance();
        String nowDate =Utils.getNowpraseDate();
        String[] nowDateArry=nowDate.split("-");
        startDate.set(Integer.parseInt(nowDateArry[0]),Integer.parseInt(nowDateArry[1])-1,Integer.parseInt(nowDateArry[2]));
        endDate.set(2030,11,31);
         pvTime = new TimePickerBuilder(schedule_week.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(flag==1)
                    beginDate.setText(getTime(date));
                else
                    myEndDate.setText(getTime(date));

//                Toast.makeText(schedule_week.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        }) .setCancelText("取消")//取消按钮
                .setSubmitText("确认")//确认按钮文字
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                 .isCyclic(true)//是否循环滚动
                 .setDecorView(v)
                 .setBackgroundId(Color.parseColor("#87CEEB"))
                 .setTitleBgColor(Color.WHITE)

                .build();
    }
    private void getNoLinkData() {
        beginWeek.add("开始星期");
        endWeek.add("结束星期");
        for(int i=1;i<=7;i++){
            beginWeek.add("星期"+Utils.IntegertoString(i));
            endWeek.add("星期"+Utils.IntegertoString(i));
        }
        clothes.add("-"+"-"+"-");

        beginTime.add("开始时间");
        endTime.add("结束时间");
        for(int i=1;i<=12;i++){
            beginTime.add("第"+i+"节");
            endTime.add("第"+i+"节");
        }
    }
    private void  dismissAll(){
        if(pvTime!=null){
            pvTime.dismiss();
        }
        if(pvNoLinkOptionsTime!=null){
            pvNoLinkOptionsTime.dismiss();
        }
        if(pvNoLinkOptions!=null){
            pvNoLinkOptions.dismiss();
        }
    }
}
