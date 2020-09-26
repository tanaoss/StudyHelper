package data;


import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

//爬虫教务

public class Login {

    public  static  String addString="";//封网的时候用这个，平时可以置空
    //模拟登陆
    public static  String loginNet(String pass, final String user){
        try {

            Connection connection;
            Connection.Response response;
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/login-salt")//安全登录
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
            JSONObject payload = new JSONObject(true);//?可以按顺序么这样
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
//                    .request(payload.toJSONString())
                    .requestBody(payload.toJSONString())
                    .maxBodySize(100)
                    .timeout(1000 * 10)
                    .method(Connection.Method.POST).ignoreContentType(true);

            response = connection.execute();
            String urlString = response.body();

            if(urlString.equals("{\"result\":true,\"needCaptcha\":false}")){
                return coo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "false";
    }

    //获得信息
    public static Document getStuinfo(String coo)  {
        try {
            Connection connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/student-info")
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
//            System.out.println(meesage);
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/student-info/info/" + meesage)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Referer", "http://jxglstu."+addString+"hfut.edu.cn//eams5-student/for-std/grade/sheet/semester-index" + meesage)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .method(Connection.Method.GET).ignoreContentType(true);
//            body1 = connection.execute().body();
            Document doc=connection.get();
//            System.out.println(body1);
            return doc;
        }catch (IOException e){
            e.printStackTrace();
        }
        Document doc=new Document("null");
        return doc;
    }
    //获得成绩
    public static Document getGrade(String coo)  {
        try {
            Connection connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/grade/sheet")
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
//            System.out.println(meesage);
            connection = Jsoup.connect("http://jxglstu."+addString+"hfut.edu.cn/eams5-student/for-std/grade/sheet/info/" + meesage+"?semester=")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept-Language", "en-US, en; q=0.8, zh-Hans-CN; q=0.5, zh-Hans; q=0.3")
                    .header("Connection", "Keep-Alive")
                    .header("Cookie", coo)
                    .header("Host", "jxglstu."+addString+"hfut.edu.cn")
                    .header("Referer", "http://jxglstu."+addString+"hfut.edu.cn//eams5-student/for-std/grade/sheet/semester-index" + meesage)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .method(Connection.Method.GET).ignoreContentType(true);
//            body1 = connection.execute().body();
            Document doc=connection.get();
//            System.out.println(body1);
            return doc;
        }catch (IOException e){
            e.printStackTrace();
        }
        Document doc=new Document("null");
        return doc;
    }
}
