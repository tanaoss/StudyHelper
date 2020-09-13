package com.hfut.studyhelper.Server;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hfut.studyhelper.LoadingDialog;
import com.hfut.studyhelper.LogLog;
import com.hfut.studyhelper.MainActivity;
import com.hfut.studyhelper.SHA1Util;
import com.hfut.studyhelper.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

public class NetServer {
    static final String course_table="course";
    static final String date_table="date";
    static final String total_table="total";
    static final String my_course_table="my_course";
    static final String my_course_table_1="my_course_1";//自定义课程元数据
    static final String db_name="user_db";
    static final String user_table="user";
    static  final String picture_table="picture";
    public  static  String addString="";//封网的时候用这个，平时可以置空
//    SQLiteDatabase db;//数据库对象
    public static String getCourse(String coo)  {
        try {
            String body1 = "";
            Connection connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/course-table")
                    .header("Accept", "text/html, application/xhtml+xml, image/jxr, */*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .method(Connection.Method.GET).ignoreContentType(true).followRedirects(false);
            String meesage = connection.execute().header("Location");
            String[] tr = meesage.split("/");
            meesage = tr[tr.length - 1];
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/course-table/info/"+meesage)
                    .header("Accept", "text/html, application/xhtml+xml, image/jxr, */*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .method(Connection.Method.GET).ignoreContentType(true).followRedirects(false);
            String semesterId=connection.execute().body();
            String StrFlag="<option selected=\"selected\" value=\"";
            int ind1=semesterId.indexOf(StrFlag);
            int ind2=semesterId.substring(ind1+StrFlag.length(),ind1+50).indexOf("\"");
            semesterId=semesterId.substring(ind1+StrFlag.length(),ind2+ind1+StrFlag.length());
            Log.i("SDSD",semesterId+"");
            semesterId=74+"";
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/course-table/get-data?bizTypeId=23&semesterId="+semesterId+"&dataId=" + meesage)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Referer", "http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/course-table/info/" + meesage)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .method(Connection.Method.GET).ignoreContentType(true);
            body1 = connection.execute().body();
            return body1;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public static void praseDataToDatabase(String body,SQLiteDatabase db){
        //创建课表
        String CreateTable="create table if not exists "+course_table+
                "(id varchar(32),"+"name varchar(32),"+"days varchar(32),"+"be varchar(32),"+"credits varchar(12),"
                +"place varchar(32)," +"weeks int,"+"code varchar(32))";
        db.execSQL(CreateTable);
        JSONObject mesObj= JSON.parseObject(body);//将json字符串转换为json对象
        int totalWeek=0;
        JSONArray jsonArray = (JSONArray)mesObj.get("weekIndices");
        totalWeek=jsonArray.size();
        String table1="create table if not exists "+total_table+
                "(count int)";
        db.execSQL(table1);
        ContentValues contentValues00=new ContentValues(1);
        contentValues00.put("count",totalWeek);
        db.insert(total_table,null,contentValues00);
        LogLog.loge(Integer.toString(totalWeek));
        JSONArray lessonObj=((JSONArray)mesObj.get("lessons"));
        String term=((JSONObject)((JSONObject)lessonObj.get(0)).get("semester")).get("nameZh").toString();
        String StartTiem=((JSONObject)((JSONObject)lessonObj.get(0)).get("semester")).get("startDate").toString();
        String dateTable="create table if not exists "+date_table+
                "(term varchar(42),"+"date varchar(32))";
        db.execSQL(dateTable);
        ContentValues contentValues1=new ContentValues(2);
        contentValues1.put("term",term);
        contentValues1.put("date",StartTiem);
        db.insert(date_table,null,contentValues1);
        for (int i=0;i<lessonObj.size();i++) {
            JSONObject jo=(JSONObject)lessonObj.get(i);
            JSONObject table=(JSONObject)((JSONObject)jo.get("scheduleText")).get("dateTimePlaceText");
            if(table.get("text")!=null){
                String text=table.get("text").toString();
                String[] courseData=text.split("; \\n");
                for(String str:courseData){
                    String[] DayData=str.split("\\s+");
                   int ty=str.indexOf(",");
                   if(ty==-1){
                       ty=DayData[0].length()-1;
                   }
                    Log.i("WEB",DayData[0]);
                    int wkStart=Integer.parseInt(DayData[0].substring(0,ty).split("~")[0]);
                    int wkEnd=Integer.parseInt(DayData[0].substring(0,ty).split("~")[1]);
                    String days=DayData[1];
                    String EndAndBegin=DayData[2];
                    String place=DayData[4];
                    for(int ii=wkStart;ii<=wkEnd;ii++){
                        ContentValues contentValues=new ContentValues(8);
                        contentValues.put("id",jo.get("id").toString());
                        contentValues.put("name",((JSONObject)jo.get("course")).get("nameZh").toString());
                        contentValues.put("code",jo.get("code").toString());
                        contentValues.put("credits",((JSONObject)jo.get("course")).get("credits").toString());
                        contentValues.put("days",days);
                        contentValues.put("be",EndAndBegin);
                        contentValues.put("place",place);
                        contentValues.put("weeks",ii);
//                        Log.i("Cs",jo.get("id").toString()+"  "+
//                                ((JSONObject)jo.get("course")).get("nameZh").toString()+"  "+jo.get("code").toString()+"   "
//                                +days+ EndAndBegin+place+ii);
                        db.insert(course_table,null,contentValues);
                    }
                }

            }

        }
    }
    public static void reLogin(final  Activity activity,SQLiteDatabase db){
        try {
            Handler handler=new Handler(activity.getMainLooper());
            handler.postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    MainActivity.loadingDialog=LoadingDialog.getInstance(activity);
                    MainActivity.loadingDialog.show();
                }
            });
            db.execSQL("delete from " + course_table);
            db.execSQL("delete from " + date_table);
            db.execSQL("delete from " + total_table);
            db.execSQL("delete from " + my_course_table);

            String pass = "";
            String user = "";
            db=activity.openOrCreateDatabase(db_name,0, null);
            String CreateTable="create table if not exists "+user_table+
                    "(username varchar(32),"+"password varchar(32))";
            db.execSQL(CreateTable);
            Cursor cursor=db.rawQuery("select * from "+user_table,null);
            if(cursor.getCount()!=0){
                cursor.moveToFirst();
                pass=cursor.getString(cursor.getColumnIndex("password"));
                user=cursor.getString(cursor.getColumnIndex("username"));
            }
            Connection connection;
            Connection.Response response;
            connection = Jsoup.connect("http://jxglstu.hfut.edu.cn/eams5-student/login-salt")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Content-Type", "application/json")
                    .header("Host", "jxglstu.hfut.edu.cn")
                    .header("Referer", "http://jxglstu.hfut.edu.cn/eams5-student/login")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .method(Connection.Method.GET).ignoreContentType(true);
            response = connection.execute();
            Map<String, String> cookie = response.cookies();
            String coo = cookie.toString().
                    substring(1, cookie.toString().length() - 1).
                    replace(",", ";");
            String myPass = pass;
            String codeMyPass = SHA1Util.getSHA(response.body() + "-" + myPass);
            JSONObject payload = new JSONObject(true);
            payload.put("username", user);
            payload.put("password", codeMyPass);
            payload.put("captcha", "");
            connection = Jsoup.
                    connect("http://jxglstu.hfut.edu.cn/eams5-student/login")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Content-Type", "application/json")
                    .header("Host", "jxglstu.hfut.edu.cn")
                    .header("Referer", "http://jxglstu.hfut.edu.cn/eams5-student/home")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .requestBody(payload.toJSONString())
                    .maxBodySize(100)
                    .timeout(1000 * 10)
                    .method(Connection.Method.POST).ignoreContentType(true);
            response = connection.execute();
            int flag = response.statusCode();
            String urlString = response.body();
            if(urlString.equals("{\"result\":true,\"needCaptcha\":false}")){
               praseDataToDatabase(getCourse(coo), db);
                String[] res=new String[6];
                cursor=db.rawQuery("select * from "+my_course_table_1,null);
                if(cursor.moveToFirst()){
                    do{
                        res[0]=cursor.getString(cursor.getColumnIndex("name"));
                        res[1]=cursor.getString(cursor.getColumnIndex("place"));
                        res[2]=cursor.getString(cursor.getColumnIndex("DateString"));
                        res[3]=cursor.getString(cursor.getColumnIndex("DateDay"));
                        res[4]=cursor.getString(cursor.getColumnIndex("DayTime"));
                        res[5]=cursor.getString(cursor.getColumnIndex("id"));
                        db.execSQL("delete from " + my_course_table_1+" where id=="+"\""+res[5]+"\"");//删除重复元数据
                        Utils.processScheduleWeekData(activity,res[2],res[3],res[4],res[0],res[1],res[5]);
                    }while (cursor.moveToNext());
                }
            }
            handler.postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    if(MainActivity.loadingDialog!=null){
                        MainActivity.loadingDialog.dismiss();
                        MainActivity.loadingDialog=null;
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
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

    }
    public static void exitLogin(Activity activity){
        SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
        try{
            db.execSQL("delete from " + course_table);
            db.execSQL("delete from " + date_table);
            db.execSQL("delete from " + total_table);
            db.execSQL("delete from " + user_table);
        }catch (Exception e){

        }
        try{
            db.execSQL("delete from " + my_course_table);
            db.execSQL("delete from " + my_course_table_1);
        }catch (Exception e){

        }
        try{
            db.execSQL("delete from "+picture_table);
        }catch (Exception e){

        }
    }

    public static void clearData(Activity activity){
       SQLiteDatabase db=activity.openOrCreateDatabase(db_name,0, null);
       try {
           db.execSQL("delete from " + my_course_table);
           db.execSQL("delete from " + my_course_table_1);
       }catch (Exception e){

       }
        try{
            db.execSQL("delete from "+picture_table);
        }catch (Exception e){

        }
    }
}
