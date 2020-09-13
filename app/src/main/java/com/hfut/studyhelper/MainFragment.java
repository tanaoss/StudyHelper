package com.hfut.studyhelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hfut.studyhelper.Server.NetServer;
import com.hfut.studyhelper.adpter.schedule;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.hfut.studyhelper.Utils.my_course_table_1;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    //用一个数组保存三个View的实例
    private View listViews[] = {null, null, null};
    private int[][] visit=new int[8][13];
    Map<String, Integer> map=new HashMap<String, Integer>();
    Map<String, Integer> map1=new HashMap<String, Integer>();
    Map<String, Integer> map2=new HashMap<String, Integer>();
    Map<String, Integer> map3=new TreeMap<String, Integer>();
    Object[] arry;
    private TabLayout tabLayout;//引用TabLayout控件
    private ViewPager viewPager;
    static final String db_name="user_db";
    static final String course_table="course";
    static final String date_table_1="date_1";
    static final String total_table="total";
    static final String my_course_table="my_course";
    schedule tempS;
    private ScrollView scrollView;
    TextView editText;
    //课表参数
    private int gridHeight,gridWidth;
    //星期几
    private RelativeLayout day;
    static final String date_table="date";
    String  date;
    String term;
    private Thread temp;
    int weekCont=0;//导航栏的周数
    int WeekTotal=0;
    SQLiteDatabase sqLiteDatabase;
    List<String> data;
    int nowWeek;
    private PieChart pc;
    private PieChart pc1;
    private BarChart  mBarChart;
    private BarChart  mBarChart1;
    private LineChart mLineChar;
    List<List<Integer>> aList=new ArrayList<List<Integer>>(){
        {
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
            add(new ArrayList<Integer>());
        }
    };//自定义ViewName
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       final SQLiteDatabase db=getActivity().openOrCreateDatabase(db_name,0, null);
       sqLiteDatabase=db;
        final Thread t=  new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.tesCoo!=null){
                    NetServer.praseDataToDatabase(NetServer.getCourse(MainActivity.tesCoo),db);
                }
            }
        });
      t.start();
    temp=  new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  t.join();
                  Cursor cursor=db.rawQuery("select * from "+date_table,null);
                  if(cursor.getCount()!=0) {
                      cursor.moveToFirst();
                      term=cursor.getString(cursor.getColumnIndex("term"));
                      date=cursor.getString(cursor.getColumnIndex("date"));
                      nowWeek=Utils.getWeekCount(date);
                      updaWeek(Utils.getWeekCount(date),db);
                  }
                  //在这里执行更新课表操作
              }catch (Exception e){
                  e.printStackTrace();
              }
          }
      });
    temp.start();
        //创建三个RecycleView，分别对应消息页...
        View v1 = getLayoutInflater().inflate(R.layout.book_layout,null);
        View v3=getLayoutInflater().inflate(R.layout.date_course,null);
        RecyclerView v2= new RecyclerView(getContext());
        v2.setLayoutManager(new LinearLayoutManager(getContext()));
        data=new ArrayList<>();
        data.add("");
        tempS=  new schedule(getActivity(),getFragmentManager().findFragmentByTag("mainfragement"),db);
        tempS.setData(data);
        refreshDateTable();
        v2.setAdapter(tempS);
        listViews[0]=v1;
        listViews[1]=v2;
        listViews[2]=v3;
//        //测试

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            temp.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cursor cursor1=sqLiteDatabase.rawQuery("select * from "+total_table,null);
        cursor1.moveToFirst();
        WeekTotal=Integer.parseInt(cursor1.getString(0));
        ViewGroup v=(ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        final ViewGroup fView=v;
        View statusBar = v.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        if(MainActivity.loadingDialog!=null&&MainActivity.tesCoo!=null){
            MainActivity.loadingDialog.dismiss();
        }
        editText =v.findViewById(R.id.textView3);
        editText.setTextColor(Color.WHITE);
        editText.setText(term.replace("学年","")+ "第"+Utils.getWeekCount(date)+"周");
        weekCont=Utils.getWeekCount(date);
        FloatingActionButton forward=listViews[0].findViewById(R.id.my_forward);
        FloatingActionButton back=listViews[0].findViewById(R.id.my_back);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                            updaWeek(--weekCont,sqLiteDatabase);

                    }
                }).start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            updaWeek(++weekCont,sqLiteDatabase);
                    }
                }).start();
            }

        });
        final DrawerLayout drawerLayout=getActivity().findViewById(R.id.drawer_container);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
       final RoundImageView imageView=(RoundImageView) v.findViewById(R.id.headImageMain);
       try {
           Drawable drawable=Utils.getDrawable(getActivity());
           if(drawable!=null)
                imageView.setDrawable(drawable);
       }catch (Exception e){
           Log.i("CC","错误1");
           e.printStackTrace();
       }
//        Drawable drawable =getResources().getDrawable(R.drawable.noramlhead2);
//        imageView.setDrawable(drawable);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                try {
                    Drawable temp=Utils.getDrawable(getActivity());
                    if(temp!=null){
                                imageView.setDrawable(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("CC","错误");
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        scrollView=listViews[0].findViewById(R.id.scrollViewMain);
        viewPager =v.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPageAdapter());
        tabLayout=v.findViewById(R.id.tabLayout);
        final TextView textViewPopMenu=v.findViewById(R.id.textViewPopMenu);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int id=tab.getPosition();
                if(id==0){
                    tab.setIcon(R.drawable.booknormal);
                    textViewPopMenu.setVisibility(View.VISIBLE);
                    updaWeek(weekCont,sqLiteDatabase);
                }else if(id==1){
                    textViewPopMenu.setVisibility(View.VISIBLE);
                    editText.setText("日程");
                    tab.setIcon(R.drawable.daysfoucks);
                }else if(id==2){
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    editText.setText("数据");
                    textViewPopMenu.setVisibility(View.GONE);
                    if(mLineChar!=null&&mLineChar.getVisibility()== View.VISIBLE)
                        mLineChar.setVisibility(View.GONE);
                    if(mBarChart!=null&&mBarChart.getVisibility()== View.VISIBLE)
                        mBarChart.setVisibility(View.GONE);
                    if(mBarChart1!=null&&mBarChart1.getVisibility()== View.VISIBLE)
                        mBarChart1.setVisibility(View.GONE);
                    if(pc!=null)
                        pc.clear();
                    if(pc1!=null)
                        pc1.clear();
                    getPie(pc,0);
                    getPie(pc1,1);
                    pc.setVisibility(View.VISIBLE);
                    pc1.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.datafoucks);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int id=tab.getPosition();
                if(id==0){
                    tab.setIcon(R.drawable.bookfoucks);
                }else if(id==1){
                    tab.setIcon(R.drawable.daysnormal);
                }else if(id==2){
                    tab.setIcon(R.drawable.datanormal);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int id=tab.getPosition();
                if(id==0){
                    tab.setIcon(R.drawable.booknormal);
                }else if(id==1){
                    tab.setIcon(R.drawable.daysfoucks);
                }else if(id==2){
                    tab.setIcon(R.drawable.datafoucks);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.booknormal);
        tabLayout.getTabAt(0).setText("课表");
        tabLayout.getTabAt(1).setIcon(R.drawable.daysnormal);
        tabLayout.getTabAt(1).setText("日程");
        tabLayout.getTabAt(2).setIcon(R.drawable.datanormal);
        tabLayout.getTabAt(2).setText("数据");
        textViewPopMenu.setOnClickListener(new View.OnClickListener() {
            PopupWindow pop=null;
            @Override
            public void onClick(View vi) {
//                Log.i("View",viewPager.getCurrentItem()+"");
                    //if弹出窗口还未建
                pop=null;
                    if(pop==null){
                        //创建
                        pop = new PopupWindow(getActivity());
                        LinearLayout menu =null;
                        if(viewPager.getCurrentItem()==0) {
                            menu =(LinearLayout) LayoutInflater.from(getActivity()).
                                    inflate(R.layout.pop_menu_layout, null);
                        }else if(viewPager.getCurrentItem()==1){
                            menu =(LinearLayout) LayoutInflater.from(getActivity()).
                                    inflate(R.layout.pop_menu_layout_date, null);
                        }else {
                            menu =(LinearLayout) LayoutInflater.from(getActivity()).
                                    inflate(R.layout.pop_menu_layout, null);
                        }
                            //计算实际大小然后获取
                            menu.measure(0, 0);
                            int w = menu.getMeasuredWidth();
                            int h = menu.getMeasuredHeight();
                            pop.setHeight(h + 100);
                            pop.setWidth(w + 120);
                            pop.setContentView(menu);
                            //加载气泡图像
                            Drawable drawable = getResources().getDrawable(R.drawable.pop_bk);
                            pop.setBackgroundDrawable(drawable);


                            // pop.setAnimationStyle(R.style.popMenuAnim);

                            //设置窗口消失的侦听器，在窗口消失后把蒙版去掉
                            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {

                                }
                            });
                            //设置窗口出现时的焦点，这样按下返回键，窗口才会消失
                            pop.setFocusable(true);
                            LinearLayout pop_line1 = menu.findViewById(R.id.pop_line1);
                            LinearLayout pop_line2 = menu.findViewById(R.id.pop_line2);
                            pop_line1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (viewPager.getCurrentItem() == 0) {
                                        pop.dismiss();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                NetServer.reLogin(getActivity(), sqLiteDatabase);
                                                Cursor cursor = sqLiteDatabase.rawQuery("select * from " + date_table, null);
                                                if (cursor.getCount() != 0) {
                                                    cursor.moveToFirst();
                                                    term = cursor.getString(cursor.getColumnIndex("term"));
                                                    date = cursor.getString(cursor.getColumnIndex("date"));
                                                    weekCont = Utils.getWeekCount(date);
                                                    updaWeek(Utils.getWeekCount(date), sqLiteDatabase);
                                                    Handler handler = new Handler(getActivity().getMainLooper());
                                                    handler.postAtFrontOfQueue(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (MainActivity.loadingDialog != null) {
                                                                MainActivity.loadingDialog.dismiss();
                                                                MainActivity.loadingDialog=null;
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }).start();
                                    }else if(viewPager.getCurrentItem()==1){
                                        Intent intent= new Intent(getActivity(),schedulActivity.class);
                                        startActivityForResult(intent,1);
                                        getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);
                                        pop.dismiss();
                                    }
                                }
                            });
                            pop_line2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(viewPager.getCurrentItem()==1){
                                        Intent intent= new Intent(getActivity(),schedule_week.class);
                                        intent.putExtra("totalWeek",WeekTotal+"");
                                        intent.putExtra("nowWeek",Utils.getWeekCount(date)+"");
                                        startActivityForResult(intent,2);
                                        getActivity().overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);
                                        pop.dismiss();
                                    }else if(viewPager.getCurrentItem()==0){
                                        final LoadingDialog loadingDialog=LoadingDialog.getInstance(getActivity());
                                        loadingDialog.show();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                updaWeek(Utils.getWeekCount(date),sqLiteDatabase);
                                                weekCont=Utils.getWeekCount(date);
                                                loadingDialog.dismiss();
                                            }
                                        }).start();
                                    }
                                    pop.dismiss();
                                }
                            });

                    }

                //显示窗口
                if(viewPager.getCurrentItem()==0)
                    pop.showAsDropDown(vi,-pop.getWidth()+40,-10);
                else if(viewPager.getCurrentItem()==1)
                    pop.showAsDropDown(vi,-pop.getWidth()+40,-10);
            }
        });
        FloatingActionButton addButton=listViews[2].findViewById(R.id.add1);
        FloatingActionButton lineButton=listViews[2].findViewById(R.id.lineView);
        FloatingActionButton pieButton=listViews[2].findViewById(R.id.PieChart);
        FloatingActionButton barButton=listViews[2].findViewById(R.id.barView);
        final ConstraintLayout constraintLayout1=listViews[2].findViewById(R.id.lineLayout);
        final ConstraintLayout constraintLayout2=listViews[2].findViewById(R.id.barLayout);
        final ConstraintLayout constraintLayout3=listViews[2].findViewById(R.id.pieLayout);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constraintLayout1.getVisibility()==View.GONE) {
                    constraintLayout1.setVisibility(View.VISIBLE);
                    constraintLayout2.setVisibility(View.VISIBLE);
                    constraintLayout3.setVisibility(View.VISIBLE);
                } else
                {
                    constraintLayout1.setVisibility(View.GONE);
                    constraintLayout2.setVisibility(View.GONE);
                    constraintLayout3.setVisibility(View.GONE);
                }
            }
        });
        pc=listViews[2].findViewById(R.id.pc);
        pc1=listViews[2].findViewById(R.id.pc1);
        getPie(pc,0);
        getPie(pc1,1);
        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pc.getVisibility()== View.VISIBLE)
                    pc.setVisibility(View.GONE);
                if(pc1.getVisibility()== View.VISIBLE)
                    pc1.setVisibility(View.GONE);
                if(mBarChart!=null&&mBarChart.getVisibility()== View.VISIBLE)
                    mBarChart.setVisibility(View.GONE);
                if(mBarChart1!=null&&mBarChart1.getVisibility()== View.VISIBLE)
                    mBarChart1.setVisibility(View.GONE);
                if(mLineChar!=null)
                     mLineChar.clear();
                getLine();
                mLineChar.setVisibility(View.VISIBLE);
                mLineChar.invalidate();
            }
        });
        try {
            barButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pc.getVisibility()== View.VISIBLE)
                        pc.setVisibility(View.GONE);
                    if(pc1.getVisibility()== View.VISIBLE)
                        pc1.setVisibility(View.GONE);
                    if(mLineChar!=null&&mLineChar.getVisibility()== View.VISIBLE)
                        mLineChar.setVisibility(View.GONE);
                    if(mBarChart!=null)
                        mBarChart.clear();
                    getBar();
                    mBarChart.setVisibility(View.VISIBLE);
                    mBarChart.invalidate();
                    getBar1();
                    mBarChart1.setVisibility(View.VISIBLE);
                    mBarChart1.invalidate();
                }
            });
        }catch (Exception e){

        }

        pieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLineChar!=null&&mLineChar.getVisibility()== View.VISIBLE)
                    mLineChar.setVisibility(View.GONE);
                if(mBarChart!=null&&mBarChart.getVisibility()== View.VISIBLE)
                    mBarChart.setVisibility(View.GONE);
                if(mBarChart1!=null&&mBarChart1.getVisibility()== View.VISIBLE)
                    mBarChart1.setVisibility(View.GONE);
                if(pc!=null)
                    pc.clear();
                if(pc1!=null)
                    pc1.clear();
                getPie(pc,0);
                getPie(pc1,1);
                pc.setVisibility(View.VISIBLE);
                pc1.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }
    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
            Toast.makeText(getActivity(), "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = listViews[0].findViewById(dayId);
            int height = Utils.dip2px(getContext(),650)/12;
            //默认加载这个布局
            View v ;
            int n=(Integer.parseInt(course.getId()))%5;
            switch (n){
                case 1: v = getLayoutInflater().inflate(R.layout.course_card1,null); break;//加载单个课程布局 break;
                case 2: v = getLayoutInflater().inflate(R.layout.course_card2,null);break; //加载单个课程布局 break;
                case 3: v = getLayoutInflater().inflate(R.layout.course_card3,null); break;//加载单个课程布局 break;
                case 4: v = getLayoutInflater().inflate(R.layout.coure_card4,null); break;//加载单个课程布局 break;
                default: v= getLayoutInflater().inflate(R.layout.course_card,null);break;
            }
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            v.getBackground().setAlpha(150);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
            updateVisit(getDay,course.getStart(),course.getEnd());
            params.setMargins(5,0,5,0);
            View temp=getLayoutInflater().inflate(R.layout.course_card00,null);
            temp.getBackground().setAlpha(255);
            temp.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            temp.setLayoutParams(params);
            v.setLayoutParams(params);
            int viewId=0;
            if(aList.get(getDay).size()==0){
                viewId=0;
                aList.get(getDay).add(getDay*12+viewId);
                aList.get(getDay).add(getDay*12+viewId+1);
                v.setId(getDay*12+viewId);
                temp.setId(getDay*12+viewId+1);
            } else {
                viewId=aList.get(getDay).get(aList.get(getDay).size()-1)+1;
                aList.get(getDay).add(getDay*12+viewId);
                aList.get(getDay).add(getDay*12+viewId+1);
                v.setId(getDay*12+viewId);
                temp.setId(getDay*12+viewId+1);
            }
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName()  + "\n" + course.getClassRoom()); //显示课程名
            day.addView(temp);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getActivity(),dialogActivities.class);
                    intent.putExtra("name",course.getCourseName());
                    intent.putExtra("room",course.getClassRoom());
                    intent.putExtra("credit",course.getCredits());
                    intent.putExtra("code",course.getCode());
                    startActivity(intent);

                }
            });
            day.addView(v);
        }
    }

    private void updaWeek(final int week, final SQLiteDatabase db){
        Cursor cursor=db.rawQuery("select * from "+course_table+" where weeks= "+week+"",null);
      final   int size=cursor.getCount();
        final Course[] courseList=new Course[size];
        int i=0;
        if(cursor.moveToFirst()){
            do {
                String be=cursor.getString(cursor.getColumnIndex("be"));
                String[] temp =be.split("~");
                int Start=Utils.toInteger(temp[0].substring(1,temp[0].length()-1));
                int end=Utils.toInteger(temp[1].substring(1,temp[1].length()-1));
                String code=cursor.getString(cursor.getColumnIndex("code"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String credits=cursor.getString(cursor.getColumnIndex("credits"));
                String Tempdays=cursor.getString(cursor.getColumnIndex("days"));
                String id=cursor.getString(cursor.getColumnIndex("id"));
                int days=Utils.toInteger(Tempdays.substring(1,Tempdays.length()));
                String  place=cursor.getString(cursor.getColumnIndex("place"));
                courseList[i]=new Course(name,id,code,credits,place,days,Start,end);
                i++;
            }while (cursor.moveToNext());
        }
        cursor=db.rawQuery("select * from "+my_course_table+" where weeks= "+week+"",null);
        final int mySize=cursor.getCount();
        final MyCourse[] myCourseList=new MyCourse[mySize];
        i=0;
        if(cursor.moveToFirst()){
            do {
                String be=cursor.getString(cursor.getColumnIndex("be"));
                String[] temp =be.split("~");
                int Start=Integer.parseInt(temp[0].substring(1,temp[0].length()-1));
                int end=Integer.parseInt(temp[1].substring(1,temp[1].length()-1));
                String code=cursor.getString(cursor.getColumnIndex("code"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String Tempdays=cursor.getString(cursor.getColumnIndex("days"));
                String id=cursor.getString(cursor.getColumnIndex("id"));
                String statute=cursor.getString(cursor.getColumnIndex("statute"));
                int days=Utils.toInteger(Tempdays.substring(1,Tempdays.length()));
//                Log.i("CC",week+"-"+be+"-"+Tempdays+"-"+days);
                String  place=cursor.getString(cursor.getColumnIndex("place"));
                myCourseList[i]=new MyCourse(name,place,code,days,Start,end,id,statute);
                i++;
            }while (cursor.moveToNext());
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postAtFrontOfQueue(new Runnable() {

            @Override
            public void run() {
                MainActivity.loadingDialog=LoadingDialog.getInstance(getContext());
                MainActivity.loadingDialog.show();
                for(int i=1;i<=7;i++){
                    clearBookTable(i);
                }
                for(int j=0;j<size;j++){
                    createItemCourseView(courseList[j]);
                }
                for(int j=0;j<mySize;j++){
                    createItemPrivateCoureseView(myCourseList[j]);
                }
                if(weekCont>0&&week<WeekTotal){
                    editText.setText(term.replace("学年","")+ "第"+weekCont+"周");
                }else if(weekCont<=0){
                    editText.setText("放假中");
                }else if(weekCont>=WeekTotal){
                    editText.setText("放假中");
                }
                if(viewPager.getCurrentItem()==1){
                    editText.setText("日程");
                }
                MainActivity.loadingDialog.dismiss();
                MainActivity.loadingDialog=null;
            }
        });
    }
    //创建单个自定义课程视图
    private void createItemPrivateCoureseView(final MyCourse course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
            Toast.makeText(getActivity(), "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = listViews[0].findViewById(dayId);
            int height = Utils.dip2px(getContext(),650)/12;
            //默认加载这个布局
            View v ;
            int tempL=course.getId().length();
            int n=(Integer.parseInt(course.getId().substring(tempL-3,tempL))+1)%6;
            switch (n){
                case 1: v = getLayoutInflater().inflate(R.layout.coure_card5,null); break;//加载单个课程布局 break;
                case 2: v = getLayoutInflater().inflate(R.layout.coure_card6,null);break; //加载单个课程布局 break;
                case 3: v = getLayoutInflater().inflate(R.layout.coure_card7,null); break;//加载单个课程布局 break;
                case 4: v = getLayoutInflater().inflate(R.layout.coure_card8,null); break;//加载单个课程布局 break;
                case 5: v = getLayoutInflater().inflate(R.layout.coure_card9,null); break;//加载单个课程布局 break;
                default: v= getLayoutInflater().inflate(R.layout.course_card1,null);break;
            }
            if (course.getStatute().equals("finish")){
                v = getLayoutInflater().inflate(R.layout.coure_card10,null);
            }
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            v.getBackground().setAlpha(150);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
//            Log.i("CC",getDay+"~"+course.getStart()+"~"+course.getEnd());
            if(!checkVisit(getDay,course.getStart(),course.getEnd()))
                return;
            updateVisit(getDay,course.getStart(),course.getEnd());
            params.setMargins(5,0,5,0);
            View temp=getLayoutInflater().inflate(R.layout.course_card00,null);
            temp.getBackground().setAlpha(255);
            temp.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            temp.setLayoutParams(params);
            v.setLayoutParams(params);
            int viewId=0;
            if(aList.get(getDay).size()==0){
                viewId=0;
                aList.get(getDay).add(getDay*12+viewId);
                aList.get(getDay).add(getDay*12+viewId+1);
                v.setId(getDay*12+viewId);
                temp.setId(getDay*12+viewId+1);
            } else {
                viewId=aList.get(getDay).get(aList.get(getDay).size()-1)+1;
                aList.get(getDay).add(getDay*12+viewId);
                aList.get(getDay).add(getDay*12+viewId+1);
                v.setId(getDay*12+viewId);
                temp.setId(getDay*12+viewId+1);
            }
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName()  +"\n"+ "自定义课程"); //显示课程名
            day.addView(temp);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getActivity(),editDiologActiity.class);
                    intent.putExtra("name",course.getCourseName());
                    intent.putExtra("room",course.getClassRoom());
                    intent.putExtra("statute",course.getStatute());
                    intent.putExtra("code",course.getCode());
                    startActivity(intent);

                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent= new Intent(getActivity(), editActivity.class);
                    intent.putExtra("flag","2");
                    intent.putExtra("id",course.getId());
                    startActivityForResult(intent,3);
                    return false;
                }
            });
            day.addView(v);
        }
    }
    private  void clearBookTable(int getDay){
        int dayId = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<13;j++)
                visit[i][j]=0;
        }
        switch (getDay) {
            case 1: dayId = R.id.monday; break;
            case 2: dayId = R.id.tuesday; break;
            case 3: dayId = R.id.wednesday; break;
            case 4: dayId = R.id.thursday; break;
            case 5: dayId = R.id.friday; break;
            case 6: dayId = R.id.saturday; break;
            case 7: dayId = R.id.weekday; break;
        }
            day = listViews[0].findViewById(dayId);
            Iterator<Integer> iter = aList.get(getDay).iterator();
            if(aList.get(getDay).size()!=0){
                while (iter.hasNext()){
                    Integer integer=iter.next();
                    day.removeView(day.findViewById(integer));
                    iter.remove();
                }
            }
    }
    class ViewPageAdapter extends PagerAdapter {


        public ViewPageAdapter() {
        }

        @Override
        public int getItemPosition(Object object) {
            //注意：默认是PagerAdapter.POSITION_UNCHANGED，不会重新加载
            return PagerAdapter.POSITION_NONE;
        }
        @Override
        public int getCount() {
            return listViews.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        //实例化一个子View，container是子View容器，就是ViewPager，
        //position是当前的页数，从0开始计数


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v=listViews[position];
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
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
    private void refreshDateTable(){

        try {
            String   CreateTable="create table if not exists "+my_course_table+
                    "(id varchar(62),"+"flag varchar(80),"+"name varchar(32),"+"days varchar(32),"+"be varchar(32),"+
                    " place varchar(32)," +"weeks int,"+"statute varchar(32),"+"code varchar(32))";
            sqLiteDatabase.execSQL(CreateTable);
            String where=" weeks=="+ nowWeek +" and "+ " days=="+"\"周"+Utils.IntegertoString(Utils.getNowDays())+"\"";
            Cursor cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table+" where "+where,null);
            if(cursor.moveToFirst()){
                do{

                    String time="自定义计划";
                    String thing=cursor.getString(cursor.getColumnIndex("name"));
                    String place=cursor.getString(cursor.getColumnIndex("place"));
                    String statute=cursor.getString(cursor.getColumnIndex("statute"));
                    String id=cursor.getString(cursor.getColumnIndex("id"));
                    String flag=""+nowWeek+"#"+Utils.getNowDays()+"#"+id;
                    if(statute.equals("finish"))
                        tempS.add(thing+"-"+time+"-"+2+"-"+place+"-"+statute+"-"+flag);
                }while (cursor.moveToNext());
            }

            cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table+" where "+where,null);
            if(cursor.moveToFirst()){
                do{
                    String time="自定义计划";
                    String thing=cursor.getString(cursor.getColumnIndex("name"));
                    String place=cursor.getString(cursor.getColumnIndex("place"));
                    String statute=cursor.getString(cursor.getColumnIndex("statute"));
                    String id=cursor.getString(cursor.getColumnIndex("id"));
                    String flag=""+nowWeek+"#"+Utils.getNowDays()+"#"+id;
                    if(statute.equals("unfinish"))
                        tempS.add(thing+"-"+time+"-"+2+"-"+place+"-"+statute+"-"+flag);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.i("CC",e.getMessage());
        }
        //日程表
        String CreateTable="create table if not exists "+date_table_1+
                "(thing varchar(80),"+"id varchar(32),"+"place varchar(32),"+"statute varchar(30),"+" time varchar(32))";
        sqLiteDatabase.execSQL(CreateTable);
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+ date_table_1,null);
        if(cursor.moveToFirst()){
            do{
                int statuteCode=1;
                String time=cursor.getString(cursor.getColumnIndex("time"));
                String thing=cursor.getString(cursor.getColumnIndex("thing"));
                String place=cursor.getString(cursor.getColumnIndex("place"));
                String statute=cursor.getString(cursor.getColumnIndex("statute"));
                String id=cursor.getString(cursor.getColumnIndex("id"));
                if(time.equals("学期计划")){
                    statuteCode=1;
                }
                String insertData=thing+"-"+time+"-"+statuteCode+"-"+place+"-"+statute+"-"+"##"+id;
                if(statute.equals("finish"))
                    tempS.add(insertData);
            }while (cursor.moveToNext());
        }
        cursor=sqLiteDatabase.rawQuery("select * from "+ date_table_1,null);
        if(cursor.moveToFirst()){
            do{
                int statuteCode=1;
                String time=cursor.getString(cursor.getColumnIndex("time"));
                String thing=cursor.getString(cursor.getColumnIndex("thing"));
                String place=cursor.getString(cursor.getColumnIndex("place"));
                String statute=cursor.getString(cursor.getColumnIndex("statute"));
                String id=cursor.getString(cursor.getColumnIndex("id"));
                if(time.equals("学期计划")){
                    statuteCode=1;
                }
                String insertData=thing+"-"+time+"-"+statuteCode+"-"+place+"-"+statute+"-"+"##"+id;
                if(statute.equals("unfinish"))
                    tempS.add(insertData);
            }while (cursor.moveToNext());
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent redata) {
        super.onActivityResult(requestCode, resultCode, redata);
        if(requestCode==1){
            if(resultCode==1){
                String things=redata.getStringExtra("things");
                String place=redata.getStringExtra("place");
                String time="学期计划";
                String id=System.currentTimeMillis()-1000000+"";
                ContentValues contentValues1=new ContentValues(5);
                contentValues1.put("thing",things);
                contentValues1.put("place",place);
                contentValues1.put("time",time);
                contentValues1.put("id",id);
                contentValues1.put("statute","unfinish");
                long  t=sqLiteDatabase.insert(date_table_1,null,contentValues1);
                tempS.add(things+"-"+time+"-"+1+"-"+place+"-unfinish-"+"##"+id);
            }
        }else if(requestCode==2){
            if (resultCode==1){
                final String things=redata.getStringExtra("things");
                final String place=redata.getStringExtra("place");
                final String[] timeArry=redata.getStringExtra("time").split("#");
                final String  id=System.currentTimeMillis()-1000000+"";
                MainActivity.loadingDialog=LoadingDialog.getInstance(getContext());
                MainActivity.loadingDialog.show();
                if(MainActivity.loadingDialog==null){
                    Toast.makeText(getActivity(),"错误",Toast.LENGTH_LONG);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.processScheduleWeekData(getActivity(),timeArry[0],timeArry[1],timeArry[2],
                                things,place,id);
                       new Handler(Looper.getMainLooper()).postAtFrontOfQueue(new Runnable() {
                            @Override
                            public void run() {
                                if(MainActivity.loadingDialog!=null){
                                    MainActivity.loadingDialog.dismiss();
                                    MainActivity.loadingDialog=null;
                                }
                            }
                        });
                    }
                }).start();
                Log.i("CC",timeArry[1]+"0");
                if(timeArry[0].split("\\$")[0].equals(Utils.getNowpraseDate())&&Utils.toInteger(timeArry[1].split("\\$")[0].substring(2,3))
                <=Utils.getNowDays()){
                    String time="自定义计划";
                    String flag=""+nowWeek+"#"+Utils.getNowDays()+"#"+id;
                    tempS.add(things+"-"+time+"-"+2+"-"+place+"-unfinish-"+flag);
                }
            }
        }else if(requestCode==3){
            editText.setText("日程");
            if(resultCode==1){
                final String things=redata.getStringExtra("things");
                final String place=redata.getStringExtra("place");
                final String[] timeArry=redata.getStringExtra("time").split("#");
                final String  id=redata.getStringExtra("id");
                String flag=""+nowWeek+"#"+Utils.getNowDays()+"#"+id;
                tempS.remove(things+"-"+"自定义计划"+"-"+2+"-"+place+"-"+"finish"+"-"+flag);
                tempS.remove(things+"-"+"自定义计划"+"-"+2+"-"+place+"-"+"unfinish"+"-"+flag);
                MainActivity.loadingDialog=LoadingDialog.getInstance(getContext());
                MainActivity.loadingDialog.show();
                if(MainActivity.loadingDialog==null){
                    Toast.makeText(getActivity(),"错误",Toast.LENGTH_LONG);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Utils.deleteTable(getActivity(),id,1);
                        Utils.processScheduleWeekData(getActivity(),timeArry[0],timeArry[1],timeArry[2],
                                things,place,id);
                        new Handler(Looper.getMainLooper()).postAtFrontOfQueue(new Runnable() {
                            @Override
                            public void run() {
                                if(MainActivity.loadingDialog!=null){
                                    MainActivity.loadingDialog.dismiss();
                                    MainActivity.loadingDialog=null;
                                }
                            }
                        });
                    }
                }).start();
                if(timeArry[0].split("\\$")[0].equals(Utils.getNowpraseDate())&&Utils.toInteger(timeArry[1].split("\\$")[0].substring(2,3))
                        <=Utils.getNowDays()){
                    String time="自定义计划";
                    tempS.add(things+"-"+time+"-"+2+"-"+place+"-unfinish-"+flag);
                }
            }else if(resultCode==2){
                final String things=redata.getStringExtra("things");
                final String place=redata.getStringExtra("place");
                final String time=redata.getStringExtra("time");
                final String  id=redata.getStringExtra("id");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.deleteTable(getActivity(),id,0);
                    }
                }).start();
                tempS.remove(things+"-"+time+"-"+1+"-"+place+"-finish-"+"##"+id);
                tempS.remove(things+"-"+time+"-"+1+"-"+place+"-unfinish-"+"##"+id);
            }else if(resultCode==3){
                final String things=redata.getStringExtra("things");
                final String place=redata.getStringExtra("place");
                final String time=redata.getStringExtra("time");
                final String  id=redata.getStringExtra("id");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.deleteTable(getActivity(),id,1);
                        updaWeek(weekCont,sqLiteDatabase);
                    }
                }).start();
                String flag=""+nowWeek+"#"+Utils.getNowDays()+"#"+id;
                tempS.remove(things+"-"+time+"-"+2+"-"+place+"-"+"finish"+"-"+flag);
                tempS.remove(things+"-"+time+"-"+2+"-"+place+"-"+"unfinish"+"-"+flag);
            }
        }
    }
    private void updateVisit(int day,int start,int end){
        for(int i=start;i<=end;i++){
            visit[day][i]=1;
        }
    }
    private boolean checkVisit(int day,int start,int end){
        for(int i=start;i<=end;i++){
            if(visit[day][i]==1)
            {
                return false;
            }

        }
        return true;
    }
    private void getPie(PieChart pieChart,int flag){
        map=new HashMap<String, Integer>();
        map1=new HashMap<String, Integer>();
        map2=new HashMap<String, Integer>();
        for(int i=1;i<=7;i++){
            map3.put("周"+i,0);
        }
        arry=null;
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+date_table,null);
        int thisTermBeginWeek=0;
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            thisTermBeginWeek=Utils.getWeeks(cursor.getString(cursor.getColumnIndex("date")));
        }
        int nowWeek=Utils.getNowWeeks()-thisTermBeginWeek+1;
        cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table,null);
        float total=cursor.getCount()+0.0f;
        int finish=0;
        int unfinish=0;
        int haveTimeFinish=0;
        if(cursor.moveToFirst()){
            do{
                int week=Integer.parseInt(cursor.getString(cursor.getColumnIndex("weeks")));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                if(map.get(name)==null){
                    map.put(name,0);
                }else {
                    map.put(name,map.get(name)+1);
                }
                String Tempdays=cursor.getString(cursor.getColumnIndex("days"));
                int days=Utils.toInteger(Tempdays.substring(1,Tempdays.length()));
                String statute=cursor.getString(cursor.getColumnIndex("statute"));
                if(statute.equals("finish")){
                    finish++;
                    if(nowWeek==week && flag==1){
                        if(map3.get("周"+Utils.toInteger(Tempdays.substring(1,Tempdays.length())))==null){
                            map3.put("周"+Utils.toInteger(Tempdays.substring(1,Tempdays.length())),0);
                        }else {
                            map3.put("周"+Utils.toInteger(Tempdays.substring(1,Tempdays.length())),map3.get("周"+Utils.toInteger(Tempdays.substring(1,Tempdays.length())))+1);
                            Log.i("CC",Tempdays+"-"+finish);
                        }
                    }
                    if(map1.get(name)==null){
                        map1.put(name,1);
                    }else {
                        map1.put(name,map1.get(name)+1);
                    }

                    Log.i("CC",name+"--"+finish+"-");
                }else {
                    if(nowWeek>week){
                        unfinish++;
                    }else if(nowWeek<week){
                        haveTimeFinish++;
                    }else {
                        if(days<Utils.getNowDays()){
                            unfinish++;
                        }else {
                            haveTimeFinish++;
                        }
                    }
                    if(map2.get(name)==null){
                        map2.put(name,1);
                    }else {
                        map2.put(name,new Integer((int)unfinish+haveTimeFinish));
                    }
                }

            }while (cursor.moveToNext());
        }
       arry=map.keySet().toArray();
        if(total>0){
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);

            pieChart.setDrawCenterText(true);

            pieChart.setRotationAngle(0);
            // 触摸旋转
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);
            //变化监听
            //模拟数据
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            Set<String> keySet = map.keySet();
            Iterator<String> it =keySet.iterator();
            if(flag==1){
                while(it.hasNext()){                         //利用了Iterator迭代器**
                    //得到每一个key
                    String key = it.next();
                    //通过key获取对应的value
                    Integer value = map.get(key);
                    entries.add(new PieEntry(value, key));
                }
            }else {
                float mytotal=finish+unfinish+haveTimeFinish+0.0f;
                entries.add(new PieEntry(finish, "已完成"));
                if(finish/mytotal>0.05)
                    entries.add(new PieEntry(unfinish, "未完成"));
                entries.add(new PieEntry(haveTimeFinish, "待完成"));
            }

            mySetData(entries,pieChart,flag);
            pieChart.animateY(1400,  Easing.EaseInOutQuad);

            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
            // 输入标签样式
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.setEntryLabelTextSize(12f);
        }else {
            pieChart.clear();
            pieChart.notifyDataSetChanged();
            pieChart.setNoDataText("你还没有记录自定义事件");
            pieChart.setNoDataTextColor(Color.parseColor("#FFD39B"));
            pieChart.invalidate();
        }

    }
    //设置数据
    private void mySetData(ArrayList<PieEntry> entries,PieChart selpc,int flag) {
        PieDataSet dataSet = new PieDataSet(entries, "自定义事件完成情况");
        if(flag==1)
            dataSet = new PieDataSet(entries, "自定义事件占比");


        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int temp=0;
        colors.add(Color.parseColor("#FFD39B"));
        colors.add(Color.parseColor("#87CEEB"));
        for (int c : ColorTemplate.VORDIPLOM_COLORS){
            if(temp==1)
                continue;
            colors.add(c);
            temp++;
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(selpc));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        selpc.setData(data);
        selpc.highlightValues(null);
        //刷新
        selpc.invalidate();
    }
    private void getBar(){
        //条形图
        if(mBarChart!=null)
            mBarChart.clear();
        mBarChart = (BarChart) listViews[2].findViewById(R.id.mBarChart);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        // 如果60多个条目显示在图表,drawn没有值
        mBarChart.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(false);
        //是否显示表格颜色
        mBarChart.setDrawGridBackground(false);
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                if (value >= 0) {
                    return  (String)arry[(int)value%arry.length];
                } else {
                    return "";
                }

            }


        };
        // 只有1天的时间间隔
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(arry.length);
        xAxis.setValueFormatter(formatter);
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(1f);
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(1f);
        // 设置标示，就是那个一组y的value的
        Legend l = mBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //样式
        l.setForm(Legend.LegendForm.SQUARE);
        //字体
        l.setFormSize(9f);
        //大小
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        //模拟数据
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        Set<String> keySet = map.keySet();
        Iterator<String> it =keySet.iterator();
            while(it.hasNext()) {                         //利用了Iterator迭代器**
                //得到每一个key
                String key = it.next();
                //通过key获取对应的value
                Integer finish = map1.get(key);
                if(finish==null)
                    finish=0;
                Integer unfinish = map2.get(key);
                if(unfinish==null)
                    unfinish=0;
                int pos=findPos(arry,key);
                yVals1.add(new BarEntry(pos, finish/(finish+unfinish+0.0f)));
            }
        barSetData(yVals1);

    }
    //设置数据
    private void barSetData(ArrayList yVals1) {
        float start = 1f;
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "各类事件完成度");
            //设置有四种颜色
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            //设置数据
            mBarChart.setData(data);
            mBarChart.animateX(3000);
            mBarChart.animateY(3000);
        }
    }
    private  int findPos(Object [] arr,String t){
        for(int i=0;i<arr.length;i++){
            if(t.equals((String) arr[i])){
                return i;
            }
        }
        return 0;
    }
    private void getLine(){

        mLineChar = (LineChart)listViews[2].findViewById(R.id.mLineChar);
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(true);
        //设置推动
        mLineChar.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);
        XAxis xAxis = mLineChar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        final Object[] tempA=map3.keySet().toArray();
        if(tempA.length==0){
            return;
        }
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                if (value >= 0) {
                    if(value<tempA.length)
                    return  (String)tempA[(int)value];
                    else
                        return "";
                } else {
                    return "";
                }

            }


        };
        // 只有1天的时间间隔
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(tempA.length);
        xAxis.setValueFormatter(formatter);
        //这里我模拟一些数据
        ArrayList<Entry> values = new ArrayList<Entry>();
        Set<String> keySet = map3.keySet();
        Iterator<String> it =keySet.iterator();
        while(it.hasNext()) {                         //利用了Iterator迭代器**
            //得到每一个key
            String key = it.next();
            //通过key获取对应的value
            Integer finish = map3.get(key);
            if(finish==null)
                finish=0;
            int pos=findPos(tempA,key);
            values.add(new Entry(pos, finish));
        }

        //设置数据
        lineSetData(values);
        //默认动画
        mLineChar.animateXY(3000, 3000);
        //刷新
        mLineChar.invalidate();
        // 得到这个文字
        Legend l = mLineChar.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
    }
    private void lineSetData(ArrayList<Entry> values) {
        LineDataSet set1;
        if (mLineChar.getData() != null && mLineChar.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChar.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChar.getData().notifyDataChanged();
            mLineChar.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, "本周任务完成情况");
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
                // 填充背景只支持18以上
                //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                //set1.setFillDrawable(drawable);
                set1.setFillColor(Color.YELLOW);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);
            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);
            //谁知数据
            mLineChar.setData(data);
        }
    }
    private void getBar1(){
        //条形图
        if(mBarChart1!=null)
            mBarChart1.clear();
        mBarChart1 = (BarChart) listViews[2].findViewById(R.id.mBarChart1);
        mBarChart1.setVisibility(View.VISIBLE);
        mBarChart1.setDrawBarShadow(false);
        mBarChart1.setDrawValueAboveBar(true);
        mBarChart1.getDescription().setEnabled(false);
        // 如果60多个条目显示在图表,drawn没有值
        mBarChart1.setMaxVisibleValueCount(40);
        // 扩展现在只能分别在x轴和y轴
        mBarChart1.setPinchZoom(false);
        //是否显示表格颜色
        mBarChart1.setDrawGridBackground(false);
        XAxis xAxis = mBarChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                if (value >= 0) {
                    return  (String)arry[(int)value%arry.length];
                } else {
                    return "";
                }

            }


        };
        // 只有1天的时间间隔
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(arry.length);
        xAxis.setValueFormatter(formatter);
        YAxis leftAxis = mBarChart1.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        YAxis rightAxis = mBarChart1.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(1f);
        // 设置标示，就是那个一组y的value的
        Legend l = mBarChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //样式
        l.setForm(Legend.LegendForm.SQUARE);
        //字体
        l.setFormSize(9f);
        //大小
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        String   CreateTable="create table if not exists "+my_course_table_1+
                "(id varchar(62),"+"name varchar(32),"+"DateString varchar(32),"+"DateDay varchar(32),"+
                " DayTime varchar(32)," +" cost int default(0) ,"+"place varchar(32))";
        sqLiteDatabase.execSQL(CreateTable);
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table_1,null);
        Map<String, Integer> mapTime=new HashMap<String, Integer>();
        Log.i("CC","0sda");
        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex("name"));
                int cost=cursor.getInt(cursor.getColumnIndex("cost"));
                mapTime.put(name,cost);
                Log.i("CC",cost+"0sda");
            }while (cursor.moveToNext());
        }
        //模拟数据
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        Set<String> keySet = map.keySet();
        Iterator<String> it =keySet.iterator();
        while(it.hasNext()) {                         //利用了Iterator迭代器**
            //得到每一个key
            String key = it.next();
            //通过key获取对应的value
            Integer cost = mapTime.get(key);
            int pos=findPos(arry,key);
            yVals1.add(new BarEntry(pos, cost));
        }
        barSetData1(yVals1);

    }
    //设置数据
    private void barSetData1(ArrayList yVals1) {
        float start = 1f;
        BarDataSet set1;
        if (mBarChart1.getData() != null &&
                mBarChart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart1.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart1.getData().notifyDataChanged();
            mBarChart1.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "各类事件所花费时间(分钟)");
            //设置有四种颜色
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            //设置数据
            mBarChart1.setData(data);
            mBarChart1.animateX(3000);
            mBarChart1.animateY(3000);
        }
    }
}
