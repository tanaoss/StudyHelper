package com.hfut.studyhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.hfut.studyhelper.adpter.MyExpandableListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScoreActivity extends AppCompatActivity {
    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList;
    OkHttpClient client = new OkHttpClient();
    MyExpandableListViewAdapter adapter;
    private List<String> childrenList1 = new ArrayList<>();
    String temp="";
    int state=0;//状态为0位不计算公选,1位计算
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        View statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        ExpandableListView expandableListView = findViewById(R.id.expandablelistview);
        RoundImageView roundImageView=findViewById(R.id.headImageMain);
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final  LoadingDialog loadingDialog1=LoadingDialog.getInstance(ScoreActivity.this);
        loadingDialog1.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                        //TS[0]账号，TS[1]密码
                        String[] TS=Utils.getUserInformation(ScoreActivity.this);
                try {
                    postRequest(TS[0],TS[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                        Log.i("WEB","DASDa"+temp);
                Handler handler = new Handler(getMainLooper());
                handler.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                            adapter = new MyExpandableListViewAdapter(dataset,parentList,ScoreActivity.this);
                        update(temp);
                        expandableListView.setAdapter(adapter);

                        expandableListView.expandGroup(0);//默认展开第一学期的成绩

                        loadingDialog1.dismiss();
                    }
                });

            }
        }).start();
        final TextView textViewPopMenu=findViewById(R.id.textViewPopMenu);
        textViewPopMenu.setOnClickListener(new View.OnClickListener() {
            PopupWindow pop=null;
            @Override
            public void onClick(View v) {
                pop=null;
                //创建
                pop = new PopupWindow(ScoreActivity.this);
                LinearLayout menu =null;
                menu =(LinearLayout) LayoutInflater.from(ScoreActivity.this).
                        inflate(R.layout.pop_menu_score, null);
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
                        update(temp);
                    }
                });
                //设置窗口出现时的焦点，这样按下返回键，窗口才会消失
                pop.setFocusable(true);
                LinearLayout pop_line1 = menu.findViewById(R.id.pop_line1);
                LinearLayout pop_line2 = menu.findViewById(R.id.pop_line2);
                pop_line1.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        final  LoadingDialog loadingDialo=LoadingDialog.getInstance(ScoreActivity.this);
                        loadingDialo.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //TS[0]账号，TS[1]密码
                                String[] TS=Utils.getUserInformation(ScoreActivity.this);
                                try {
                                    postRequest(TS[0],TS[1]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i("WEB","DASDa"+temp);
                                Handler handler = new Handler(getMainLooper());
                                handler.postAtFrontOfQueue(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new MyExpandableListViewAdapter(dataset,parentList,ScoreActivity.this);
                                        update(temp);
                                        expandableListView.setAdapter(adapter);

                                        expandableListView.expandGroup(0);//默认展开第一学期的成绩

                                        loadingDialo.dismiss();
                                    }
                                });

                            }
                        }).start();
                    }
                });
                Switch ms=pop_line2.findViewById(R.id.switch1);
                if(state==1)
                    ms.setChecked(true);
                else if(state==0)
                    ms.setChecked(false);
                ms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            state=1;
                        } else {
                           state=0;
                        }

                    }
                });
                pop.showAsDropDown(v,-pop.getWidth()+40,-10);
            }
        });
    }
    public void update(String emp){
        getData(emp);
        adapter.setData(dataset,parentList);
    }
    public  void  getData(String temp1){
        TextView textView1=findViewById(R.id.totalScore);
        TextView textView2=findViewById(R.id.textView32);
        if(temp1.length()==0){
            dataset.clear();
            parentList=new String[]{};
            textView1.setText("您当前还没有成绩");
            textView2.setText("");
            return;
        }
        dataset.clear();
        JSONArray json = JSON.parseArray(temp1);//将json字符串转换为json对象
        JSONObject jsonObj1 = json.getJSONObject(0);
        JSONArray sObj=jsonObj1.getJSONArray("学期");
        parentList=new String[sObj.size()];
        for(int i=0;i<sObj.size();i++){
            parentList[i]=(String) sObj.get(i);
        }
        textView1.setText("总均分:"+(String)jsonObj1.get("avg").toString().split("-")[state]);
        textView2.setText("总排名:"+(String)jsonObj1.get("rank").toString().split("-")[state]);
        for(int i=1;i<json.size();i++){
            JSONObject jsonObj = json.getJSONObject(i);
            String id=jsonObj.getString("term_id");
            String temp=jsonObj.getString("mean_score").split("-")[state]+"-"+jsonObj.getString("rank").split("-")[state];
            childrenList1.add(temp);
            JSONArray sObjArr=jsonObj.getJSONArray("contents");
//            Log.i("CSSD",sObjArr.toJSONString());
            for(int j=0;j<sObjArr.size();j++){
                childrenList1.add((String) sObjArr.get(j));
            }
            ArrayList<String> TA= new ArrayList<String>();
            TA.addAll(childrenList1);
            dataset.put(id,TA);
            childrenList1.clear();
        }
//        for(String i : dataset.get("大三上")){
//            Log.i("CSSD",i);
//        }
    }

    //向服务器提交post请求
    private void postRequest(String username,String password) throws IOException {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //建立请求表单，添加上传服务器的参数
        JSONObject json=new JSONObject();
        json.put("name",username);
        json.put("paw",password);
        RequestBody body = RequestBody.create(JSON,json.toJSONString());
        Log.i("WEB",json.toJSONString());

        //发起请求
        final Request request = new Request.Builder()
                .url("http://175.24.45.114:80/second_war/Login")
                .post(body)
                .build();
        Log.i("WEB","1");
        Response response=client.newCall(request).execute();
        String result = response.body().string();
        JSONObject jsonObject=JSONObject.parseObject(result);
        jsonObject.get("响应");
        temp= jsonObject.get("响应").toString();
        response.close();
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
