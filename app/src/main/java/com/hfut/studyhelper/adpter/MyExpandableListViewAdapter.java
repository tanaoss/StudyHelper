package com.hfut.studyhelper.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.hfut.studyhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    Activity activity;
    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList = new String[]{"first", "second", "third"};
    private List<String> childrenList1 = new ArrayList<>();
    private List<String> childrenList2 = new ArrayList<>();
    private List<String> childrenList3 = new ArrayList<>();

    public MyExpandableListViewAdapter(Map<String, List<String>> dataset, String[] parentList,Activity activity) {
        this.dataset = dataset;
        this.parentList = parentList;
        this.activity=activity;
    }
    public MyExpandableListViewAdapter(Activity activity) {//测试模拟用
        this.activity=activity;
        childrenList1.add("88.365-22/120");
        childrenList1.add("课程名称-成绩-学分");
        childrenList1.add("编译原理"+ "-" + "69-2.5");
        childrenList1.add("编译原理"+ "-" + "69-2");
        childrenList1.add("编译原理"+ "-" + "69-2");
        dataset.put(parentList[0], childrenList1);
        childrenList2.add("88.365-22/120");
        childrenList2.add("课程名称-成绩-学分");
        childrenList2.add("编译原理"+ "-" + "69-2");
        childrenList2.add("编译原理"+ "-" + "69-2");
        childrenList2.add("编译原理"+ "-" + "69-2");
        dataset.put(parentList[1], childrenList2);
        childrenList3.add("88.365-22/120");
        childrenList3.add("课程名称-成绩-学分");
        childrenList3.add("编译原理"+ "-" + "69-2");
        childrenList3.add("编译原理"+ "-" + "69-2");
        childrenList3.add("编译原理"+ "-" + "69-2");
        childrenList3.add("编译原理"+ "-" + "69-2");
        childrenList3.add("编译原理"+ "-" + "69-2");
        dataset.put(parentList[2], childrenList3);
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentList[parentPos]).get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(parentList[parentPos]).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentList[parentPos]);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parent_item, null);
        }
        view.setTag(R.layout.parent_item, parentPos);
        TextView text = (TextView) view.findViewById(R.id.textTerms);
        text.setText(parentList[parentPos]);
        ImageView imageView =view.findViewById(R.id.imageUp);
        if(b){
            imageView.setImageResource(R.drawable.down);
        }else {
            imageView.setImageResource(R.drawable.back_1);
        }
        return view;
    }
    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(childPos!=0)
                 view = inflater.inflate(R.layout.child_item, null);
            else if(childPos==0)
                view = inflater.inflate(R.layout.child_item1, null);
        view.setTag(R.layout.parent_item, parentPos);
        TextView text = (TextView) view.findViewById(R.id.textCourse);
        text.setText(dataset.get(parentList[parentPos]).get(childPos).split("-")[0]);
        TextView text1 = (TextView) view.findViewById(R.id.textscore);
        text1.setText(dataset.get(parentList[parentPos]).get(childPos).split("-")[1]);
        if(childPos>0) {
            if(childPos==1){
                TextView text2 = (TextView) view.findViewById(R.id.textViewCredit);
                text2.setText(dataset.get(parentList[parentPos]).get(childPos).split("-")[2]);
            }else if(childPos>1){
                TextView text2 = (TextView) view.findViewById(R.id.textViewS);
                text2.setText(dataset.get(parentList[parentPos]).get(childPos).split("-")[2]);
            }
        }
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setData(Map<String, List<String>> dataset,String[] parentList) {
        this.dataset = dataset;
        this.parentList = parentList;
        notifyDataSetChanged();
    }

}
