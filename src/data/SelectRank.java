package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static data.Insertinfo.connect;

public class SelectRank {
    public static String Rank(int flag, Avecourse avg, Stuinfo stu) throws SQLException, ClassNotFoundException {
        int all=0;
        int rank=5;
        String allrank;
        rank=selectRank(flag, avg, stu)+1;
        all=selectall(flag, avg, stu);
        allrank=rank+"/"+all;
        return allrank;
    }


//    排名
//    select count(*) from chengji join stuinfo on stuinfo.id=chengji.id
// where nianji=”teding” and zhuanye=”teding” and xueqi=”teding” and junfen>teding;
    public static int selectRank(int flag, Avecourse avg, Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int rank=0;

        String sql="select count(*) from ";
        if(flag==0){
            sql+="avgB join stuinfo on stuinfo.id=avgB.id where major=? and time=? and avgB>?";
        }
        else{
            sql+="avgall join stuinfo on stuinfo.id=avgall.id where major=? and time=? and avgall>?";
        }
        PreparedStatement ps=conn.prepareStatement(sql);


        ps.setString(1,stu.getMajor());
        ps.setString(2,stu.getTime());
        ps.setFloat(3,avg.getAvec());


        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            rank=rs.getInt("count(*)");
        }
//        System.out.println(sql);
        conn.close();
        return rank;
    }

    //    select count(*) from chengji join stuinfo on stuinfo.id=chengji.id
// where nianji=”teding” and zhuanye=”teding” and xueqi=”teding”;
    public static int selectall(int flag, Avecourse avg, Stuinfo stu) throws SQLException, ClassNotFoundException {
//        System.out.println("selec");
        Connection conn;
        conn=connect();
        int all=0;

//        String sql="select count(*) from avgB";
//        String sql="select count(*) from stuinfo where major=? and time=?";
        String sql="select count(*) from ";
        if(flag==0){
            sql+="avgB join stuinfo on stuinfo.id=avgB.id where major=? and time=?";
        }
        else{
            sql+="avgall join stuinfo on stuinfo.id=avgall.id where major=? and time=?";
        }
        PreparedStatement ps=conn.prepareStatement(sql);
//        System.out.println(stu.getMajor()+","+stu.getTime());
        ps.setString(1,stu.getMajor());
        ps.setString(2,stu.getTime());

//        System.out.println(sql);

        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            all=rs.getInt("count(*)");
//            System.out.println(all);
        }
        conn.close();
        return all;
    }
}
