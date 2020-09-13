package com.hfut.studyhelper.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hfut.studyhelper.LoadingDialog;
import com.hfut.studyhelper.LogLog;
import com.hfut.studyhelper.R;
import com.hfut.studyhelper.TimeActivity;
import com.hfut.studyhelper.configTime;
import com.hfut.studyhelper.dialogActivities;
import com.hfut.studyhelper.editActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class schedule  extends RecyclerView.Adapter<schedule.MyViewHolder>{
    SQLiteDatabase sqLiteDatabase;
   static final String date_table_1="date_1";
    final String my_course_table="my_course";
    private Activity activity;
    private Fragment fragment;
    private List<String> data=new ArrayList<>();
    public void setData(List<String> data1) {
        for(String i:data1){
            data.add(i);
        }
    }
    public void add(String s){
        data.add(s);
        itemMove(data.size()-1,1);
    }
    public void remove(String obj){
        int res=0;
        for(int i =1;i<data.size();i++){
            if(data.get(i).equals(obj)){
                res=i;
                break;
            }
        }
        if(res>0){
            data.remove(res);
            notifyItemRemoved(res);
            notifyItemRangeChanged(Math.min(res, data.size()), Math.abs((res-data.size()) +1));//受影响的itemd都刷新下
        }
    }
    public schedule(Activity activity, Fragment context, SQLiteDatabase MysqLiteDatabase) {
        this.activity = activity;
        this.fragment=context;
        //日程表
        sqLiteDatabase=MysqLiteDatabase;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=activity.getLayoutInflater();
        View view = null;
        if(viewType == R.layout.schedule_list_item){
            view=inflater.inflate(R.layout.schedule_list_item,parent,false);
        }else if(viewType ==R.layout.shecule_list_item0){
            view=inflater.inflate(R.layout.shecule_list_item0,parent,false);
        }else if(viewType ==R.layout.schedule_list_item1){
            view=inflater.inflate(R.layout.schedule_list_item1,parent,false);
        }
        else {
            view =inflater.inflate(R.layout.shedule_list_item3,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, dialogActivities.class);
                    activity.startActivity(intent);
                }
            });
        }
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return R.layout.shecule_list_item0;
        }else {
            String temp=data.get(position).split("-")[2];
            switch (temp){
                case "1":return R.layout.schedule_list_item1;
                case "2":return R.layout.schedule_list_item;
                default:return R.layout.shedule_list_item3;
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if(position==0)
            return;
        View v=holder.itemView;
        TextView textView1=v.findViewById(R.id.titleItem);
        ImageView imageView =v.findViewById(R.id.imageView);
         TextView textView2=v.findViewById(R.id.timeItem);
        final String[] temp=data.get(position).split("-");
        LinearLayout linearLayout=v.findViewById(R.id.widePart);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, editActivity.class);
                intent.putExtra("flag",temp[2]);
                intent.putExtra("id",temp[5].split("#")[2]);
                if(fragment!=null)
                    fragment.startActivityForResult(intent,3);
            }
        });
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent= new Intent(activity, configTime.class);
                intent.putExtra("id",temp[5].split("#")[2]);
                intent.putExtra("name",temp[0]);
                if(fragment!=null)
                    fragment.startActivityForResult(intent,8);
                return false;
            }
        });
        textView1.setText(temp[0]);
        textView2.setText(temp[1]);
        if(temp[4].equals("finish")){
            textView1.setTextColor(Color.parseColor("#848484"));
            imageView.setImageResource(R.drawable.finish);
        }else if(temp[4].equals("unfinish")){
            textView1.setTextColor(Color.parseColor("#636363"));
            if(temp[2].equals("1")){
                imageView.setImageResource(R.drawable.bianqian);
            }else if(temp[2].equals("2")){
                imageView.setImageResource(R.drawable.item_book);
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp[2].equals("1")){
                    if(temp[4].equals("unfinish")){
                        data.set(position,temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-finish-"+temp[5]);
                        String condition=" WHERE id==\""+temp[5]+ "\"";
                        String update="UPDATE "+date_table_1 +" SET statute = \"finish\" "+condition;
                        sqLiteDatabase.execSQL(update);
                        itemMove(position,data.size()-1);
                    }else {
                        data.set(position,temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-unfinish-"+temp[5]);
                        String condition=" WHERE id==\""+temp[5]+ "\"";
                        String update="UPDATE "+date_table_1 +" SET statute = \"unfinish\" "+condition;
                        sqLiteDatabase.execSQL(update);
                        itemMove(position,1);
                    }
                }else if(temp[2].equals("2")){
                    if(temp[4].equals("unfinish")){
                        data.set(position,temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-finish-"+temp[5]);
                        String condition=" WHERE name==\""+temp[0]+
                                "\" and place==\""+temp[3]+ "\" and flag= \""+temp[5]+"\"";
                        String update="UPDATE "+my_course_table +" SET statute = \"finish\" "+condition;
                        sqLiteDatabase.execSQL(update);
//                        Log.i("CC",condition);
//                       Cursor cursor=sqLiteDatabase.rawQuery("select * from "+my_course_table,null);
//                        if(cursor.moveToFirst()){
//                            do{
//                                int statuteCode=1;
//                                String time=cursor.getString(cursor.getColumnIndex("weeks"));
//                                String thing=cursor.getString(cursor.getColumnIndex("days"));
//                                String place1=cursor.getString(cursor.getColumnIndex("be"));
//                                String statute=cursor.getString(cursor.getColumnIndex("statute"));
//                                if(time.equals("学期计划")){
//                                    statuteCode=1;
//                                }
//                                String insertData=thing+"-"+time+"-"+statute+"-"+place1;
//                                Log.i("CC",insertData);
//                            }while (cursor.moveToNext());
//                        }
                        itemMove(position,data.size()-1);
                    }else {
                        data.set(position,temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-unfinish-"+temp[5]);
                        String condition=" WHERE name==\""+temp[0]+
                                "\" and place==\""+temp[3]+ "\" and flag= \""+temp[5]+"\"";
                        String update="UPDATE "+my_course_table +" SET statute = \"unfinish\" "+condition;
                        sqLiteDatabase.execSQL(update);

                        itemMove(position,1);
                    }
                }
            }
        });
    }
    public void itemMove(int fromPosition, int toPosition){

        data.add(toPosition,data.remove(fromPosition));//数据更换
         notifyItemMoved(fromPosition,toPosition);//执行动画
        notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition -toPosition) +1);//受影响的itemd都刷新下

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
