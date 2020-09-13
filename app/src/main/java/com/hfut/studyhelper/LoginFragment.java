package com.hfut.studyhelper;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    String addString="";//封网的时候用这个，平时可以置空
    static final String db_name="user_db";
    static final String user_table="user";
    private EditText userName;
    private EditText passWord;
    private boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true
    private Button logInButton;
    SQLiteDatabase db;//数据库对象

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      final View v= inflater.inflate(R.layout.fragment_login, container, false);
        DrawerLayout drawerLayout=getActivity().findViewById(R.id.drawer_container);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        final ImageView imageView1=v.findViewById(R.id.eye);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView1.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.eye_close).getConstantState())){
                    imageView1.setImageDrawable(getResources().getDrawable(R.drawable.eyes_open));
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    passWord.setTransformationMethod(method1);
                }else {
                    imageView1.setImageDrawable(getResources().getDrawable(R.drawable.eye_close));
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    passWord.setTransformationMethod(method);
                }
                int index = passWord.getText().toString().length();
                passWord.setSelection(index);
            }
        });
       logInButton = v.findViewById(R.id.LoginButton);
       logInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View loginV) {
              if( configData(v)){
                  MainActivity.loadingDialog=LoadingDialog.getInstance(getActivity());
                  MainActivity.loadingDialog.show();
                  Toast toast= Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                  toast.setText("正在登陆");
                  toast.show();
                  Cursor cursor=db.rawQuery("select * from "+user_table,null);
                  cursor.moveToFirst();
                final String pass1=cursor.getString(cursor.getColumnIndex("password"));
                final  String user1=cursor.getString(cursor.getColumnIndex("username"));
                if(pass1!=null && user1!=null &&pass1.length()!=0 && user1.length()!=0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginNet(pass1,user1);
                        }
                    }).start();
                }else {
                    if(MainActivity.loadingDialog!=null)
                        MainActivity.loadingDialog.dismiss();
                    delte();
                    Toast toast1= Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                    toast1.setText("用户名或密码不能为空");
                    toast1.show();
                }
              }else {
                  if(MainActivity.loadingDialog!=null)
                      MainActivity.loadingDialog.dismiss();
                  delte();
                  Toast toast1= Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                  toast1.setText("请填写正确的的账号或密码");
                  toast1.show();
              }
           }
       });
       autoGetData(v);
        return v;
    }
    private boolean configData( View v){
        passWord=v.findViewById(R.id.editTextPass);
        userName=v.findViewById(R.id.editTextNum);
        //打开或者创建数据库
        db=getActivity().openOrCreateDatabase(db_name,0, null);
        String CreateTable="create table if not exists "+user_table+
                "(username varchar(32),"+"password varchar(32))";
        db.execSQL(CreateTable);
        Cursor cursor=db.rawQuery("select * from "+user_table,null);
        if(cursor.getCount()==0){//无数据
            String pass=passWord.getText().toString();
            String user=userName.getText().toString();
            if (pass!=null&&user!=null){
                addData(user,pass);
                return true;
            }
            return false;
        }else {//有数据
                delte();
                String pass=passWord.getText().toString();
                String user=userName.getText().toString();
                if (pass!=null&&user!=null){
                    addData(user,pass);
                    return true;
                }
                return false;
        }
    }
    private boolean autoGetData(View v){
        passWord=v.findViewById(R.id.editTextPass);
        userName=v.findViewById(R.id.editTextNum);
        //打开或者创建数据库
        db= getActivity().openOrCreateDatabase(db_name, 0,null);
        String CreateTable="create table if not exists "+user_table+
                "(username varchar(32),"+"password varchar(32))";
        db.execSQL(CreateTable);
        Cursor cursor=db.rawQuery("select * from "+user_table,null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
         final    String pass=cursor.getString(cursor.getColumnIndex("password"));
          final   String user=cursor.getString(cursor.getColumnIndex("username"));
            passWord.setText(pass);
            userName.setText(user);
            Cursor cursor1=db.rawQuery("select * from "+user_table,null);
            if(cursor1.getCount()!=0){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment mainFragment =new MainFragment();
                //替换掉FrameLayout中现有的Fragment
                fragmentTransaction.replace(R.id.fragment_container, mainFragment,"mainfragement");
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                fragmentTransaction.commit();
            }
            return true;
        }
        return false;
    }
    private void addData(String user,String pass){
        ContentValues contentValues=new ContentValues(2);
        contentValues.put("username",user);
        contentValues.put("password",pass);
        db.insert(user_table,null,contentValues);
    }
    private  void delte(){
        db.execSQL("delete from "+user_table);
    }
    private boolean loginNet(String pass, final String user){
        try {

            Connection connection;
            Connection.Response response;
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/login-salt")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Content-Type", "application/json")
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Referer", "http://jxglstu."+addString+"hfut.edu.cn/eams5-student/login")
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
                    connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/login")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Content-Type", "application/json")
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Referer", "http://jxglstu."+addString+"hfut.edu.cn/eams5-student/home")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .requestBody(payload.toJSONString())
                    .maxBodySize(100)
                    .timeout(1000 * 10)
                    .method(Connection.Method.POST).ignoreContentType(true);
            MainActivity.tesCoo=coo;
            LogLog.loge(coo);
            response = connection.execute();
            int flag = response.statusCode();
            String urlString = response.body();
            Log.i("CSS","http://jxglstu."+addString+"hfut.edu.cn/eams5-student/login-salt");
            if(urlString.equals("{\"result\":true,\"needCaptcha\":false}")){
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                        //转入下一个页面
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MainFragment mainFragment =new MainFragment();
                        //替换掉FrameLayout中现有的Fragment
                        fragmentTransaction.replace(R.id.fragment_container, mainFragment,"mainfragement");
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                        fragmentTransaction.commit();
                    }
                });
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                MainActivity.tesCoo=null;
                Toast toast1= Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
                try {
                    db.execSQL("delete from " + user_table);
                }catch (Exception e){
                    e.printStackTrace();
                }
                toast1.setText("账号或者密码错误");
                toast1.show();
                if(MainActivity.loadingDialog!=null)
                {
                    MainActivity.loadingDialog.dismiss();
                }
                userName.setText("");
                passWord.setText("");
            }
        });
            return false;
    }
}
