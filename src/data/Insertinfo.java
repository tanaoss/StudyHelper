package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//插入信息
public class Insertinfo {

    public static Connection connect() throws ClassNotFoundException, SQLException {

            // 加载数据库驱动
        Class.forName("com.hive.jdbc.HiveDriver");
            // 声明数据库testscore的URL
        String url = "jdbc:hive2://localhost:10000/testscore?useUnicode=true&characterEncoding=utf-8";
            // 数据库用户名
        String user = "root";
            // 数据库密码
        String password = "123";
            // 建立数据库连接，获得连接对象conn
        Connection conn = DriverManager.getConnection(url, user, password);
        if(conn==null){
            System.out.println("连接失败");
        }
//        System.out.println("连接成功");
        flush(conn);
        return conn;
    }
    public static void flush(Connection conn) throws SQLException {
        String[] flush;
        String sql = "set character_set_results=\"utf8\""; // 生成一条sql语句
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        sql = "set character_set_database=\"utf8\"";
        ps = conn.prepareStatement(sql);
        ps.execute();
        sql = "set character_set_connection=\"utf8\"";
        ps = conn.prepareStatement(sql);
        ps.execute();
        sql = "set character_set_client=\"utf8\"";
        ps = conn.prepareStatement(sql);
        ps.execute();
        sql = "set character_set_server=\"utf8\"";
        ps = conn.prepareStatement(sql);
        ps.execute();
    }

    //学生信息表
    public static void InsertStuInfo(String stuid, String major, String sem) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql = "insert into stuinfo (id,major,time) values(?,?,?)"; // 生成一条sql语句
        //声明一个statement对象
        PreparedStatement ps = conn.prepareStatement(sql);

        //开始赋值
        ps.setString(1,stuid);
        ps.setString(2,major);
        ps.setString(3,sem);
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }

    //总成绩表不加公选
    public static void InsertAvgB(String stuid,float sumcore,float sumcourse,float avec) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql="insert into avgB values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1,stuid);
        ps.setDouble(2, sumcore);
        ps.setDouble(3, sumcourse);
        ps.setDouble(4, avec);
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }

    //总成绩表加公选
    public static void InsertAvgAll(String stuid,double sumcore,double sumcourse,double avec) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql="insert into avgall values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1,stuid);
        ps.setDouble(2, sumcore);
        ps.setDouble(3, sumcourse);
        ps.setDouble(4, avec);
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }


    //学期成绩不加公选
    public static void InsertSemAvgB(String avgsum,String sem,float sumcore,float sumcourse,float avec) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql="insert into semavgB values(?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1,avgsum);
        ps.setString(2,sem);
        ps.setDouble(3, sumcore);
        ps.setDouble(4, sumcourse);
        ps.setDouble(5, avec);
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }


    //学期成绩加公选
    public static void InsertSemAvgAll(String avgsum,String sem,float sumcore,float sumcourse,float avec) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql="insert into semavgall values(?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1,avgsum);
        ps.setString(2,sem);
        ps.setDouble(3, sumcore);
        ps.setDouble(4, sumcourse);
        ps.setDouble(5, avec);
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }

}
