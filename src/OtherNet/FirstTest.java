package OtherNet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class FirstTest {
    public  String test()  {
        System.out.println("1");
        try {
            Request request = new Request.Builder()
                    .url("https://www.baidu.com/")
                    .get()
                    .build();
            System.out.println("11");
            //发起请求
            OkHttpClient client =new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间;
                    .build();
            Response response= client.newCall(request).execute();
            System.out.println("111");
            System.out.println(response.body().string());
        }catch (Exception e){
            System.out.println("SDADA");
        }
        return "";
    }
}
