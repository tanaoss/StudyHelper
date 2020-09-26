package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static data.Insertinfo.connect;

public class SelectSemRank {
    public static String SemRank(int flag,Avecourse avg,Stuinfo stu) throws SQLException, ClassNotFoundException {
        int all=0;
        int rank=0;
        String semrank;
        rank=selectSemRank(flag, avg, stu)+1;
        all=selectSemall(flag, avg, stu);
        semrank=rank+"/"+all;
        return semrank;
    }


    //    排名
//    select count(*) from chengji join stuinfo on stuinfo.id=chengji.id
// where nianji=”teding” and zhuanye=”teding” and xueqi=”teding” and junfen>teding;
    public static int selectSemRank(int flag,Avecourse avg,Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int semrank=0;

        String sql="select count(*) from ";
        if(flag==0){
            sql+="semavgB join stuinfo on stuinfo.id=semavgB.id where major=? and time=? and sem=? and avgB>?";
        }
        else {
            sql+="semavgall join stuinfo on stuinfo.id=semavgall.id where major=? and time=? and sem=? and avgall>?";
        }
        PreparedStatement ps=conn.prepareStatement(sql);

        ps.setString(1,stu.getMajor());
        ps.setString(2,stu.getTime());
        ps.setString(3,avg.getSem());
        ps.setFloat(4,avg.getAvec());
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            semrank=rs.getInt("count(*)");
//            System.out.println(semrank+" please");
        }
//        System.out.println(sql);
        conn.close();
        return semrank;
    }

    //    select count(*) from chengji join stuinfo on stuinfo.id=chengji.id
// where nianji=”teding” and zhuanye=”teding” and xueqi=”teding”;
    public static int selectSemall(int flag,Avecourse avg,Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        int semall=0;

        String sql="select count(*) from ";
        if(flag==0){
            sql+="semavgB join stuinfo on stuinfo.id=semavgB.id where major=? and time=? and sem=?";
        }
        else{
            sql+="semavgall join stuinfo on stuinfo.id=semavgall.id where major=? and time=?and sem=?";
        }
        PreparedStatement ps=conn.prepareStatement(sql);

            ps.setString(1,stu.getMajor());
            ps.setString(2,stu.getTime());
            ps.setString(3,avg.getSem());

        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            semall=rs.getInt("count(*)");
//            System.out.println(semall);
        }
//        System.out.println(sql);
        conn.close();
        return semall;
    }
}
