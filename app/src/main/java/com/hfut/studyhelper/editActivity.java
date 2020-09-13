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

public class editActivity extends AppCompatActivity {
    final String date_table_1="date_1";
    final String my_course_table="my_course";
    String activityMode="";
    private ArrayList<String> beginWeek = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> endWeek = new ArrayList<>();
    private ArrayList<String> beginTime = new ArrayList<>();
    private ArrayList<String> endTime = new ArrayList<>();
    String DateString="";
    String DateDayString="";
    String DateTimeString="";
    TextView beginDate;
    TextView myEndDate;
    TextView beginDayDate;
    TextView myEndDayDate;
    TextView beginTimeDate;
    TextView myEndTimeDate;
    OptionsPickerView pvNoLinkOptions;
    OptionsPickerView  pvNoLinkOptionsTime;
    TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent=getIntent();
        getNoLinkData();
        final String flag=intent.getStringExtra("flag");
        final String id=intent.getStringExtra("id");
        if(flag.equals("1")){
            activityMode=date_table_1;
        }else {
            activityMode=my_course_table;
        }
        View statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        layoutParams.height = getStatusBarHeight();
        final RelativeLayout frameLayout = findViewById(R.id.root);
        final View mask = new View(editActivity.this);
        final CardView cardView=findViewById(R.id.card_view);
        beginDate=findViewById(R.id.textView19);
        myEndDate=findViewById(R.id.textView21);
        beginDayDate=findViewById(R.id.textView24);
        myEndDayDate=findViewById(R.id.textView26);
        beginTimeDate=findViewById(R.id.textView28);
        myEndTimeDate=findViewById(R.id.textView30);
        TextView  returnText=findViewById(R.id.textView14);
        final ConstraintLayout linearLayoutVISIBLE = findViewById(R.id.vlayout);//收缩的面板
        mask.setBackgroundColor(Color.DKGRAY);
        mask.setAlpha(0.5f);
        final String [] res=Utils.openTable_course(editActivity.this,id,Integer.parseInt(flag));
        TextView edit=findViewById(R.id.textView13);
        final TextView thing=findViewById(R.id.editText);
        thing.setText(res[0]);
        final TextView place=findViewById(R.id.editTextplace);
        place.setText(res[1]);
        final TextView time=findViewById(R.id.editText2);
        if(flag.equals("1")){
            time.setText("学期计划");
            edit.setVisibility(View.GONE);
            returnText.setVisibility(View.GONE);
        }else {
            time.setText("自定义计划");
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast= Toast.makeText(editActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("点击时间栏即可编辑时间");
                    toast.show();
                    place.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast= Toast.makeText(editActivity.this, null, Toast.LENGTH_SHORT);
                            toast.setText("地点不可改变");
                            toast.show();
                        }
                    });
                    thing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast= Toast.makeText(editActivity.this, null, Toast.LENGTH_SHORT);
                            toast.setText("事件不可改变");
                            toast.show();
                        }
                    });
                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                            mask.setClickable(true);
                            frameLayout.addView(mask);
                            linearLayoutVISIBLE.setVisibility(View.VISIBLE);
                            linearLayoutVISIBLE.bringToFront();
                            if(time.getText().toString().equals("自定义计划")){
                                beginDate.setText(res[2].split("\\$")[0]);
                                myEndDate.setText(res[2].split("\\$")[1]);
                                beginDayDate.setText(res[3].split("\\$")[0]);
                                myEndDayDate.setText(res[3].split("\\$")[1]);
                                beginTimeDate.setText(res[4].split("\\$")[0]);
                                myEndTimeDate.setText(res[4].split("\\$")[1]);
                            }
                        }
                    });
                }
            });
        }
        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkTimePicker(cardView,1);
                pvTime.show();
            }
        });
        myEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAll();
                initNoLinkTimePicker(cardView,2);
                pvTime.show();
            }
        });
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
        TextView confimTextView=findViewById(R.id.textView12);
        TextView returnTextView=findViewById(R.id.textView2);
        confimTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutVISIBLE.setVisibility(View.GONE);
                frameLayout.removeView(mask);
                res[2]=beginDate.getText().toString()+"$"+myEndDate.getText().toString();
                res[3]=beginDayDate.getText().toString()+"$"+myEndDayDate.getText().toString();
                res[4]=beginTimeDate.getText().toString()+"$"+myEndTimeDate.getText().toString();
                if(DateString.contains("定义")==false)
                    time.setText("时间已设置成功!");
            }
        });
        returnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutVISIBLE.setVisibility(View.GONE);
                frameLayout.removeView(mask);
            }
        });
        final Button deleteButton=findViewById(R.id.ReturnButton);
        final Button confimButton=findViewById(R.id.ConfirmButton);
        confimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("things",res[0]);
                intent.putExtra("place",res[1]);
                if(flag.equals("2")){
                    intent.putExtra("time",res[2]+"#"+res[3]+"#"+res[4]);
                    intent.putExtra("id",id);
                    setResult(1,intent);
                    finish();
                }else if(flag.equals("1")){
                    setResult(0,intent);//不做任何处理
                    finish();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "确认要删除所有数据?");
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
                        Intent intent = new Intent();
                        intent.putExtra("things",res[0]);
                        intent.putExtra("place",res[1]);
                        intent.putExtra("id",id);
                        if(flag.equals("1")){
                            intent.putExtra("time","学期计划");
                            setResult(2,intent);
                        }else if(flag.equals("2")){
                            setResult(3,intent);
                            intent.putExtra("time","自定义计划");
                        }
                        ensureDialog.dismiss();

                        finish();
                    }
                });

            }
        });
        returnText.setOnClickListener(new View.OnClickListener() {
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
                    Toast toast= Toast.makeText(editActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("请选择正确的时间");
                    toast.show();
                }
            }
        })
                .setItemVisibleCount(5)
                .setBackgroundId(Color.parseColor("#FCDDB6"))
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
                    Toast toast= Toast.makeText(editActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("请选择正确的时间");
                    toast.show();
                }
//                Toast.makeText(schedule_week.this, str, Toast.LENGTH_SHORT).show();
            }
        })
                .setItemVisibleCount(5)
                .setBackgroundId(Color.parseColor("#FCDDB6"))
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
        pvTime = new TimePickerBuilder(editActivity.this, new OnTimeSelectListener() {
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
                .setBackgroundId(Color.parseColor("#FCDDB6"))
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
