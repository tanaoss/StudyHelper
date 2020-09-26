package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static data.Insertinfo.connect;
//对于表中已存在的信息进行更新和删除
public class UpdateDelete {
    public static void updateall(int flag, Stuinfo stu, Avecourse ave) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql;
        if(flag==0){
            sql="update avgB set sumscore=?,sumcredit=?,avgB=? where id=?";
        }
        else{
            sql="update avgall set sumscore=?,sumcredit=?,avgall=? where id=?";
        }
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, ave.getSumcore());
        ps.setDouble(2, ave.getSumcourse());
        ps.setDouble(3, ave.getAvec());
        ps.setString(4, stu.getId());
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }

    public static void deletesem(int flag, Stuinfo stu) throws SQLException, ClassNotFoundException {
        Connection conn;
        conn=connect();
        String sql;
        if(flag==0){
            sql="delete from semavgB where id=?";
        }
        else{
            sql="delete from semavgall where id=?";
        }
        PreparedStatement ps = conn.prepareStatement(sql);


        ps.setString(1, stu.getId());
        ps.executeUpdate();
//        System.out.println(sql);
        conn.close();
    }
}
