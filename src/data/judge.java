package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static data.Insertinfo.connect;

public class judge {
    public static int judgeStu(Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int have = 0;
        String sql = "select count(*) from stuinfo where id=?"; // 生成一条sql语句
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);

        //开始赋值
        ps.setString(1,stu.getId());

        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            have=rs.getInt("count(*)");
        }
//        System.out.println(sql);
        conn.close();
        return have;
    }


    public static int judgeAll(int flag,Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int have = 0;
        String sql; // 生成一条sql语句
        if(flag==0){
            sql="select count(*) from avgB where id=?";
        }
        else {
            sql="select count(*) from avgall where id=?";
        }
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);

        //开始赋值
        ps.setString(1,stu.getId());

        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            have=rs.getInt("count(*)");
        }
//        System.out.println(sql);
        conn.close();
        return have;
    }

    public static int judgeSem(int flag,Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int have = 0;
        String sql; // 生成一条sql语句
        if(flag==0){
            sql="select count(*) from semavgB where id=?";
        }
        else {
            sql="select count(*) from semavgall where id=?";
        }
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);

        //开始赋值
        ps.setString(1,stu.getId());

        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            have=rs.getInt("count(*)");
        }
//        System.out.println(sql);
        conn.close();
        return have;
    }
}
