package Servlet;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import static data.identify.dataAndmysql;

public class LoginServlet extends HttpServlet { //需要继承HttpServlet  并重写doGet  doPost方法
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        InputStreamReader insr = new InputStreamReader(request.getInputStream(),"utf-8");
        String result = "";
        int respInt = insr.read();
        while(respInt!=-1) {
        result +=(char)respInt;
        respInt = insr.read();
        }
        JSONObject jsonRet = JSONObject.parseObject(result);
        System.out.println(jsonRet.get("name"));
        System.out.println(jsonRet.toJSONString());
        // 返回值sring就是这个用户的数据,你们自己写Net类
        try (PrintWriter out = response.getWriter()) {
            JSONObject jsonObject=new JSONObject();
            String data=dataAndmysql(jsonRet.get("name").toString(),jsonRet.get("paw").toString());
            //最后把这里改成jsonObject.put("响应"，data)
            System.out.println(data);
            jsonObject.put("响应",data);
            out.write(jsonObject.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}