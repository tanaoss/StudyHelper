package data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static data.Insertinfo.connect;

public class Info {
    public static Stuinfo analyseInfo(Document doc, String stuid){
        String stuinfo="";
        Stuinfo stuin=new Stuinfo();
        Element stu=doc.getElementsByClass("col-xs-12 col-sm-8").first();
        Elements info=stu.select("dl > dd");
        for(Element ele:info){
            stuinfo+=ele.text()+" ";
        }
        String[] split=stuinfo.split(" ");
        stuin.setId(stuid);
        stuin.setMajor(split[6]);
        stuin.setTime(split[2]);
//        stuinfo=split[2]+" "+split[6]+" ";
//        System.out.println(stuinfo);
        return stuin;
    }
    public static Stuinfo selectInfo(String stuid) throws SQLException, ClassNotFoundException {
        String stuinfo="";
        Stuinfo stuin=new Stuinfo();
        Connection conn;
        conn=connect();
        String sql = "select * from stuinfo where id=?"; // 生成一条sql语句
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);
        //开始赋值
        ps.setString(1,stuid);

        ResultSet rs=ps.executeQuery();

        while (rs.next()){
            stuin.setId(rs.getString("id"));
            stuin.setMajor(rs.getString("major"));
            stuin.setTime(rs.getString("time"));
        }

        conn.close();

        return stuin;
    }
}
