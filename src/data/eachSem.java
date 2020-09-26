package data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import static data.Insertinfo.InsertSemAvgAll;
import static data.Insertinfo.InsertSemAvgB;
import static data.SelectSemRank.SemRank;

//计算每个学期的均分和返回所需信息
public class eachSem {
    public static float english=1.0f;
    //判断英语分级
    public static void English(Elements row){
        english=1.0f;
        for(Element ele:row) {
            Element courseBody = ele.select(".col-sm-12 tbody").first();
            Elements courses = courseBody.select("tr");
            for (Element el : courses) {
                //取出每一门课成绩内容str
                String str = el.text();
                //分割得到课程-0，成绩-5，学分-3
                String[] split = str.split(" ");
//                System.out.println(split[0]+","+split[5]);

                if (split[0].indexOf("英语（一）") != -1 && split[5].indexOf("免修") != -1) {
                    english = 1.1f;
//                    System.out.println("1\n");
                }

            }
        }
        System.out.println(english);
    }
    //完整的每个学期的成绩清单
    public static Avecourse[] eachwholeSem(Element row, Stuinfo stu) throws SQLException, ClassNotFoundException {
        Avecourse[] avgsem=new Avecourse[2];//1不加2加
        Jsonsem jsonsem=new Jsonsem();
        String whole="";
//        JSONObject wholesem=new JSONObject(true);
        String avgcore="";//平均成绩（不加-加公选）
        String rank="";//排名
        JSONArray contents=new JSONArray();//成绩单

        //学期
        String rowfun=row.select(".col-sm-12").first().text();//获取学期

//        System.out.println(sem);

        //获取成绩单主体
        Element courseBody=row.select(".col-sm-12 tbody").first();
        //获取每一门课成绩
        Elements courses=courseBody.select("tr");
        //返回数据：成绩单
        contents=eachsemCourse(courses);

        //返回数据：均分
        avgsem[1]=allsemAve(courses);//加公选2
        avgsem[0]=BsemAve(courses);//不加1
        DecimalFormat df =new DecimalFormat("#0.0000");
        avgcore=df.format(avgsem[0].getAvec())+"-"+df.format(avgsem[1].getAvec());
        //入库、更新操作(补)
        //加一个学期
        avgsem[1].Stuid(stu.getId());
        avgsem[0].Stuid(stu.getId());
        avgsem[1].Sem(rowfun);
        avgsem[0].Sem(rowfun);
        avgsem[1].setAvec();
        avgsem[0].setAvec();
//        System.out.println(avgsem[0].getSem());
        //入库函数

        InsertSemAvgB(avgsem[0].getStuid(),avgsem[0].getSem(),avgsem[0].getSumcore(),avgsem[0].getSumcourse(),avgsem[0].getAvec());
        InsertSemAvgAll(avgsem[1].getStuid(),avgsem[1].getSem(),avgsem[1].getSumcore(),avgsem[1].getSumcourse(),avgsem[1].getAvec());


        //返回数据：排名
        //查库
        String SemRankB=SemRank(0,avgsem[0],stu);
        String SemRankAll=SemRank(1,avgsem[1],stu);
        rank=SemRankB+"-"+SemRankAll;

        jsonsem.setTerm_id(rowfun);
        jsonsem.setMean_score(avgcore);
        jsonsem.setRank(rank);
        jsonsem.setContents(contents);
        whole=JSONObject.toJSONString(jsonsem);
//        System.out.println(whole);

        //最终
        avgsem[0].Jsonsem(whole);
        avgsem[1].Jsonsem(whole);
        return avgsem;
    }


    //每个学期成绩contents
    public static JSONArray eachsemCourse(Elements elements){

        JSONArray courseall=new JSONArray();
        courseall.add("课程名称-成绩-学分");
        String course="";
        //遍历每一门课的成绩
        for(Element ele:elements){
            //取出每一门课成绩内容str
            String str=ele.text();
            //分割得到课程-0，成绩-5，学分-3
            String[] split = str.split(" ");
            if(split[5].equals("--")){
                course=split[0]+"-待评教-0";
            }
            else{
                course=split[0]+"-"+split[5]+"-"+split[3];
            }
            courseall.add(course);

            //System.out.println(split[0]+","+split[1]+","+split[3]+","+split[5]);
        }
        //输出contents
//        System.out.println(courseall);
        return courseall;
    }

    //每个学期均分（加公选）
    public static Avecourse allsemAve(Elements elements){
        Pattern pattern = Pattern.compile("[+-]*\\d+\\.?\\d*[Ee]*[+-]*\\d+");
        Avecourse allave=new Avecourse();
        float core=0;
        float course=0;

        //遍历每一门课的成绩
        for(Element ele:elements){
            //取出每一门课成绩内容str
            String str=ele.text();
            //分割得到课程-0，成绩-5，学分-3
            String[] split = str.split(" ");
//            System.out.println(split[5]);
            switch (split[5]){
                case "优" :course+=90.0*Float.parseFloat(split[3]);
                    core+=Float.parseFloat(split[3]);
                    break;
                case "良" :course+=80.0*Float.parseFloat(split[3]);
                    core+=Float.parseFloat(split[3]);
                    break;
                case "中" :course+=70.0*Float.parseFloat(split[3]);
                    core+=Float.parseFloat(split[3]);
                    break;
                case "合格" :course+=60.0*Float.parseFloat(split[3]);
                    core+=Float.parseFloat(split[3]);
                    break;
            }
            if(pattern.matcher(split[5]).matches()){
                //英语分级，（一）免修则*1.1
                if(split[0].indexOf("英语（")!=-1){
                    if(Float.parseFloat(split[5])* english>100){
                        course+=100.0*Float.parseFloat(split[3]);
                    }
                    else{
                        course+=Float.parseFloat(split[5])*Float.parseFloat(split[3])* english;
                    }

                }
                else {
                    course+=Float.parseFloat(split[5])*Float.parseFloat(split[3]);
                }
                core+=Float.parseFloat(split[3]);

//                System.out.println(split[5]);
//                System.out.println(split[0]);
            }

            //System.out.println(split[0]+","+split[1]+","+split[3]+","+split[5]);
        }

//        System.out.println(core);

        allave.sumCore(core);
        allave.sumCourse(course);
//        allave.setAvec();
//        System.out.println(allave.getAvec());

//        System.out.println(allave.getAvec()+"\t"+allave.getSumcore()+"\t"+allave.getSumcourse());
        //输出contents
//        System.out.println(courseall);
        return allave;
    }

    //学期均分（不加公选）
    public static Avecourse BsemAve(Elements elements){
        Pattern pattern = Pattern.compile("[+-]*\\d+\\.?\\d*[Ee]*[+-]*\\d+");
        Pattern pattern2 = Pattern.compile("\\d*[1-9][X]$");
        Avecourse allave=new Avecourse();
        float core=0;
        float course=0;
        String gongshi="";
        //遍历每一门课的成绩
        for(Element ele:elements){
            //取出每一门课成绩内容str
            String str=ele.text();
            //分割得到课程-0，成绩-5，学分-3
            String[] split = str.split(" ");
//            System.out.println(split[5]);
            if(!pattern2.matcher(split[1]).matches()){
                switch (split[5]){
                    case "优" :course+=90.0*Float.parseFloat(split[3]);
                        core+=Float.parseFloat(split[3]);
                        gongshi+="90*"+split[3]+"+";
                        break;
                    case "良" :course+=80.0*Float.parseFloat(split[3]);
                        core+=Float.parseFloat(split[3]);
                        gongshi+="80*"+split[3]+"+";
                        break;
                    case "中" :course+=70.0*Float.parseFloat(split[3]);
                        core+=Float.parseFloat(split[3]);
                        gongshi+="70*"+split[3]+"+";
                        break;
                    case "合格" :course+=60.0*Float.parseFloat(split[3]);
                        core+=Float.parseFloat(split[3]);
                        break;
                }
            }

            if(pattern.matcher(split[5]).matches()&&!pattern2.matcher(split[1]).matches()&&split[0].indexOf("大学生艺术团实践课程")==-1){
                //英语分级，（一）免修则*1.1
                if(split[0].indexOf("英语（")!=-1){
                    if(Float.parseFloat(split[5])* english>100){
                        course+=100.0*Float.parseFloat(split[3]);
                    }
                    else{
                        course+=Float.parseFloat(split[5])*Float.parseFloat(split[3])* english;
                    }
                    gongshi+=split[5]+"*"+split[3]+"*"+english+"+";
                }
                else {
                    course+=Float.parseFloat(split[5])*Float.parseFloat(split[3]);
//                    gongshi+=split[5]+"*"+split[3]+"+";
                }
                core+=Float.parseFloat(split[3]);
//                course+=Float.parseFloat(split[5])*Float.parseFloat(split[3]);
//                System.out.println(split[5]);
//                System.out.println(split[0]);
            }

            //System.out.println(split[0]+","+split[1]+","+split[3]+","+split[5]);
        }
        System.out.println(gongshi+"0\n");
//        core=(float)(Math.round(core*1000))/1000;
//        course=(float)(Math.round(course*1000))/1000;
        allave.sumCore(core);
        allave.sumCourse(course);
//        allave.setAvec();
//        System.out.println(allave.getAvec());


//        System.out.println(allave.getAvec()+"\t"+allave.getSumcore()+"\t"+allave.getSumcourse());
        //输出contents
//        System.out.println(courseall);
        return allave;
    }
}
