package data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

import static data.Info.analyseInfo;
import static data.Info.selectInfo;
import static data.Insertinfo.*;
import static data.Login.*;
import static data.SelectRank.Rank;
import static data.UpdateDelete.deletesem;
import static data.UpdateDelete.updateall;
import static data.eachSem.English;
import static data.eachSem.eachwholeSem;
import static data.judge.*;

public class identify {

    //获得json字符串
    public static String dataAndmysql(String stuid,String pass) throws IOException, SQLException, ClassNotFoundException {
        //静态解析html
//        File file = new File("D:\\html\\130255");
//        System.out.println(doc.getElementsByTag("title").first().text());

        Stuinfo stu=new Stuinfo();
        stu.setId(stuid);

        //数据库中没有此学生信息
        if(judgeStu(stu)==0){
            //爬虫（基本信息，学科和入学年份）
            Document doc = getStuinfo(loginNet(pass,stu.getId()));
            //获得doc，解析html,Stuinfo赋值
            stu=analyseInfo(doc,stu.getId());
            //入库
            InsertStuInfo(stu.getId(),stu.getMajor(),stu.getTime());
        }
        else {
            stu=selectInfo(stu.getId());
        }

        //爬虫（成绩）
        Document doc = getGrade(loginNet(pass,stu.getId()));
        //获得doc，解析html，返回json字符串
        String res=Respond(doc,stu);
//        System.out.println(res);
        return res;
    }



    public static String Respond(Document doc,Stuinfo stu) throws SQLException, ClassNotFoundException {

        //获得总均分，加公选-1 不加公选-0
        float sumcore0 = 0;
        float sumcourse0 = 0;
        float sumcore1 = 0;
        float sumcourse1 = 0;
        String sems="";
        String allsem="";
        //获取row，每学期成绩单
        Avecourse[] eachsem;//每个学期的成绩单（输出）
        JSONArray allsems=new JSONArray();//总排名的学期
        Elements row = doc.getElementsByClass("row");
        int semB=judgeSem(0,stu);
        int semall=judgeSem(1,stu);
        if(semB!=0||semall!=0){
            deletesem(0,stu);
            deletesem(1,stu);
        }
        English(row);
        for (Element rowItem:row){
            eachsem = eachwholeSem(rowItem,stu);
            allsems.add(eachsem[0].getSem());
            sems+=eachsem[0].getJsonsem()+",";
//            res.add(eachsem[0].getJsonsem());
//            System.out.println(eachsem[0].getAvec());
            //总成绩的计算
            sumcore0 += eachsem[0].getSumcore();
            sumcourse0 += eachsem[0].getSumcourse();

            sumcore1 += eachsem[1].getSumcore();
            sumcourse1 += eachsem[1].getSumcourse();

        }
//        sumcore0=(float)(Math.round(sumcore0*10000))/10000;
//        sumcourse0=(float)(Math.round(sumcourse0*10000))/10000;
//        sumcore1=(float)(Math.round(sumcore1*10000))/10000;
//        sumcourse1=(float)(Math.round(sumcourse1*10000))/10000;
//        System.out.println(sumcore0+"\t"+sumcourse0);
        sems=sems.substring(0,sems.length()-1);

//        System.out.println(allsems);
//        System.out.println(sumcore0);

        //总成绩单
        Avecourse allsemB = new Avecourse();//不加公选0
        Avecourse allsemall = new Avecourse();//加公选1
        JSONObject all=new JSONObject();
        String avg="";
        String rank="";
        String res="";

        allsemB.Stuid(stu.getId());
        allsemB.Sem("all");
        allsemB.sumCore(sumcore0);
        allsemB.sumCourse(sumcourse0);
        allsemB.getAvec();
        allsemall.Stuid(stu.getId());
        allsemall.Sem("all");
        allsemall.sumCore(sumcore1);
        allsemall.sumCourse(sumcourse1);
        allsemall.getAvec();
        avg += allsemB.getAvec()+"-"+allsemall.getAvec();
        //入库
//        System.out.println("在这里入库总成绩");
        int bavg=judgeAll(0,stu);
        if(bavg==0) {
            InsertAvgB(allsemB.getStuid(), allsemB.getSumcore(), allsemB.getSumcourse(), allsemB.getAvec());
        }
        else {
            updateall(0,stu,allsemB);
        }

        int allavg=judgeAll(1,stu);
        if(allavg==0) {
            InsertAvgAll(allsemall.getStuid(), allsemall.getSumcore(), allsemall.getSumcourse(), allsemall.getAvec());
        }
        else {
            updateall(1,stu,allsemall);
        }

        //查排名
        String SemRankB=Rank(0,allsemB,stu);
        String SemRankAll=Rank(1,allsemall,stu);
        rank=SemRankB+"-"+SemRankAll;
//        System.out.println(rank);

        all.put("avg",avg);
        all.put("rank",rank);
        all.put("学期",allsems);
        allsem=all.toString();
        res="["+allsem+","+sems+"]";

//        System.out.println(res);
        return res;
    }

}
