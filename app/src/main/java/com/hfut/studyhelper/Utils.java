package com.hfut.studyhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;


public class Utils {
    static final String db_name="user_db";
    static final String my_course_table="my_course";
    static final String my_course_table_1="my_course_1";//自定义课程元数据
    static final String date_table="date";
    static  final String date_table_1="date_1";
    static  final String picture_table="picture";
    static final String user_table="user";
    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 计算当前日期与{@code endDate}的间隔天数
     *
     * @param endDate
     * @return 间隔天数
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static long until(LocalDate endDate){
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static long until(LocalDate startDate, LocalDate endDate){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return startDate.until(endDate, ChronoUnit.DAYS);
        }
        return -1;
    }

    public static int toInteger(String s){
        int n=0;
        switch (s){
            case "一":n=1;break;
            case "二":n=2;break;
            case "三":n=3;break;
            case "四":n=4;break;
            case "五":n=5;break;
            case "六":n=6;break;
            case "七":
            case "天":
            case "日":
                n=7;break;
            case "八":n=8;break;
            case "九":n=9;break;
            case "十":n=10;break;
            case "十一":n=11;break;
            case "十二":n=12;break;
            default:n=0;
        }
        return n;
    }
    public static String IntegertoString(int i){
        String res="";
        switch (i){
            case 1:res="一";break;
            case 2:res="二";break;
            case 3:res="三";break;
            case 4:res="四";break;
            case 5:res="五";break;
            case 6:res="六";break;
            case 7:res="天";break;
            default:res="天";
        }
        return res;
    }
    public static int getWeeks(String s){
        String today = s;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(today);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return  calendar.get(Calendar.WEEK_OF_YEAR);
    }
    public static int getDays(String s){
        String today = s;
        if(Integer.parseInt(s.substring(0,4))>Integer.parseInt(getNowpraseDate().substring(0,4))){
            return 53;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(today);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        if(calendar.get(Calendar.DAY_OF_WEEK)-1==0)
            return 7;
        return   calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    public  static int getWeekCount(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String temps=simpleDateFormat.format(date);
        Log.i("SDSD",getWeeks(temps)-getWeeks(s)+1+"+"+getWeeks(temps)+"+"+getWeeks(s));
            if(getWeeks(temps)-getWeeks(s)+1>0)
                 return getWeeks(temps)-getWeeks(s)+1;
            else
                return 12-getWeeks(s)+getWeeks(temps)+1;//下半学期，冬天的时候
    }
    public  static String getNowpraseDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String temps=simpleDateFormat.format(date);
        return temps;
    }
    public static int getNowWeeks(){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return   calendar.get(Calendar.WEEK_OF_YEAR);
    }
    public static int getNowDays(){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK)-1==0)
            return 7;
        return   calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    public static String processScheduleWeekData(final Activity activity, String DateString,
                                               String DateDayString, final String DateTimeString,
                                                 final String name, final String place,String id){
        final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
        Cursor cursor=db.rawQuery("select * from "+date_table,null);
        int thisTermBeginWeek=0;
        String kaixueDate="";
        if(cursor.getCount()!=0) {
            cursor.moveToFirst();
            kaixueDate=cursor.getString(cursor.getColumnIndex("date"));
            thisTermBeginWeek=Utils.getWeeks(cursor.getString(cursor.getColumnIndex("date")));
        }
        int firstWeek=0;
        int lastWeek=0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                firstWeek=(int)until( LocalDate.parse(kaixueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse(DateString.split("\\$")[0],DateTimeFormatter.ofPattern("yyyy-MM-dd")))/7+1;
            }else {
                firstWeek=getWeeks(DateString.split("\\$")[0])-thisTermBeginWeek+1;
            }
        int firstDay=getDays(DateString.split("\\$")[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastWeek=(int)until( LocalDate.parse(kaixueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse(DateString.split("\\$")[1],DateTimeFormatter.ofPattern("yyyy-MM-dd")))/7+1;
        }else {
            lastWeek=getWeeks(DateString.split("\\$")[1])-thisTermBeginWeek+1;
        }
        int lastDay=getDays(DateString.split("\\$")[1]);
        String[]  DateDayStringArray=DateDayString.split("\\$");
        ContentValues contentValues=new ContentValues(9);
        int beginDay=toInteger( DateDayStringArray[0].replace("星期",""));
        int endDay=toInteger( DateDayStringArray[1].replace("星期",""));
//        Log.i("CC",firstWeek+"-"+lastWeek);
//        Log.i("CC",firstDay+"-"+lastDay);
        //如果间隔超过一周
        if(firstWeek!=lastWeek){
            int temp=Math.max(beginDay,firstDay);
            for(int i=temp;i<=7;i++){
                contentValues.put("id",id+"");
                contentValues.put("name",name);
                contentValues.put("code","private");
                contentValues.put("statute","unfinish");
                contentValues.put("days","周"+IntegertoString(i));
                contentValues.put("be",DateTimeString.replace("$","~"));
                contentValues.put("place",place);
                contentValues.put("weeks",firstWeek);
                contentValues.put("flag",firstWeek+"#"+i+"#"+id);
                db.insert(my_course_table,null,contentValues);
            }
            for(int i=1;i<=lastDay;i++){
                contentValues.put("id",id+"");
                contentValues.put("name",name);
                contentValues.put("code","private");
                contentValues.put("statute","unfinish");
                contentValues.put("days","周"+IntegertoString(i));
                contentValues.put("be",DateTimeString.replace("$","~"));
                contentValues.put("place",place);
                contentValues.put("weeks",lastWeek);
                contentValues.put("flag",lastWeek+"#"+i+"#"+id);
               db.insert(my_course_table,null,contentValues);

            }
                    for(int i =firstWeek+1;i<lastWeek;i++){
                        for(int j=beginDay;j<=endDay;j++){
                            contentValues.put("id",id+"");
                            contentValues.put("name",name);
                            contentValues.put("code","private");
                            contentValues.put("statute","unfinish");
                            contentValues.put("days","周"+IntegertoString(j));
                            contentValues.put("be",DateTimeString.replace("$","~"));
                            contentValues.put("place",place);
                            contentValues.put("weeks",i);
                            contentValues.put("flag",i+"#"+j+"#"+id);
                            db.insert(my_course_table,null,contentValues);
                                          }
                    }
        }else {
            for(int i=firstDay;i<=lastDay;i++){
                contentValues.put("id",id+"");
                contentValues.put("name",name);
                contentValues.put("code","private");
                contentValues.put("statute","unfinish");
                contentValues.put("days","周"+IntegertoString(i));
                contentValues.put("be",DateTimeString.replace("$","~"));
                contentValues.put("place",place);
                contentValues.put("weeks",lastWeek);
//                Log.i("CC",i+"-");
                contentValues.put("flag",firstWeek+"#"+i+"#"+id);
                db.insert(my_course_table,null,contentValues);
            }

        }
        String   CreateTable="create table if not exists "+my_course_table_1+
                "(id varchar(62),"+"name varchar(32),"+"DateString varchar(32),"+"DateDay varchar(32),"+
                " DayTime varchar(32)," +" cost int default(0) ,"+"place varchar(32))";
        db.execSQL(CreateTable);
        ContentValues contentValues1=new ContentValues(6);
        contentValues1.put("id",id+"");
        contentValues1.put("name",name);
        contentValues1.put("DateString",DateString);
        contentValues1.put("DateDay",DateDayString);
        contentValues1.put("DayTime",DateTimeString);
        contentValues1.put("place",place);
        db.insert(my_course_table_1,null,contentValues1);
        return id;
    }
    public static String[] openTable_course(Activity activity,String id,int flag){
        String[] res=new String[5];
        String condition=" where id == "+"\""+id+"\"";
        final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
        if(flag==2){
            Cursor cursor=db.rawQuery("select * from "+my_course_table_1+condition,null);
            if(cursor.moveToFirst()){
                do{
                    res[0]=cursor.getString(cursor.getColumnIndex("name"));
                    res[1]=cursor.getString(cursor.getColumnIndex("place"));
                    res[2]=cursor.getString(cursor.getColumnIndex("DateString"));
                    res[3]=cursor.getString(cursor.getColumnIndex("DateDay"));
                    res[4]=cursor.getString(cursor.getColumnIndex("DayTime"));
                }while (cursor.moveToNext());
            }
        }else if(flag==1){
            Cursor cursor=db.rawQuery("select * from "+date_table_1+condition,null);
            if(cursor.moveToFirst()){
                do{
                    res[2]=cursor.getString(cursor.getColumnIndex("time"));
                    res[0]=cursor.getString(cursor.getColumnIndex("thing"));
                    res[1]=cursor.getString(cursor.getColumnIndex("place"));
                }while (cursor.moveToNext());
            }
        }
        return res;
    }
    static void deleteTable(final Activity activity,String id,int flag){
        final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
        if(flag==0){
            db.execSQL("delete from " +date_table_1+" where id ="+id );
        }else if(flag==1){
            db.execSQL("delete from " +my_course_table+" where id ="+id );
            db.execSQL("delete from " +my_course_table_1+" where id ="+id );
        }
    }
    public static void setPicture(Activity activity,String uri){
        try{
            final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
            String   CreateTable="create table if not exists "+picture_table+
                    "(uri  varchar(32))";
            db.execSQL(CreateTable);
            db.execSQL("delete from " +picture_table);
            ContentValues contentValues1=new ContentValues(1);
            contentValues1.put("uri",uri+"");
            db.insert(picture_table,null,contentValues1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Drawable getDrawable(Activity activity)  {
        try {
            final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
            Cursor cursor=db.rawQuery("select * from "+picture_table,null);
            cursor.moveToFirst();
            String me=cursor.getString(cursor.getColumnIndex("uri"));
            Uri uri=Uri.parse(me);
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
            Drawable drawable=new BitmapDrawable(bitmap);
            return drawable;
        }catch (Exception e){
            return null;
        }
    }
    public static void updateCost(Activity activity ,String id , int time){
        final SQLiteDatabase sqLiteDatabase=activity.openOrCreateDatabase(db_name,0, null);
        String condition=" WHERE id==\""+id+ "\"";
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table_1+" "+condition,null);
        cursor.moveToFirst();
        int me=cursor.getInt(cursor.getColumnIndex("cost"));
        int cost=me+time;
        String update="UPDATE "+my_course_table_1 +" SET cost = "+cost+" "+condition;
        sqLiteDatabase.execSQL(update);
    }
    public static  String[] getUserInformation(Activity activity){
        String[] TS=new String[]{"0","0"};
        final SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
        String CreateTable="create table if not exists "+user_table+
                "(username varchar(32),"+"password varchar(32))";
        db.execSQL(CreateTable);
        Cursor cursor=db.rawQuery("select * from "+user_table,null);
        if(cursor.moveToFirst()){
            do{
                TS[0]=cursor.getString(cursor.getColumnIndex("username"));
                TS[1]=cursor.getString(cursor.getColumnIndex("password"));
            }while (cursor.moveToNext());
        }
        return TS;
    }
}
