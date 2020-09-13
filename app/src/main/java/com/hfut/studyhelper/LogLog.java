package com.hfut.studyhelper;

import android.util.Log;

public class LogLog {
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        public static String TAG="-----LongLog-----";
        public  static void  loge(String str){
            int max_str_length=2001-TAG.length();
            //大于4000时
            while (str.length()>max_str_length){
                Log.e(TAG, str.substring(0,max_str_length) );
                str=str.substring(max_str_length);
            }
            //剩余部分
            Log.i(TAG, str );
        }
}
