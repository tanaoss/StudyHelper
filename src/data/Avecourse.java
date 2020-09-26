package data;

//平均成绩类
public class Avecourse {
    private String stuid="";//id
    private String sem="";//学期
    private String jsonsem="";//contents内容
    private float avec=0;//平均成绩
    private float sumcourse=0;//加权和
    private float sumcore=0;//总学分

    public void Jsonsem(String jsonsem) {
        this.jsonsem = jsonsem;
    }

    public void Stuid(String stuid){
        this.stuid=stuid;
    }

    public void Sem(String sem){
        this.sem=sem;
    }

    public void sumCourse(float course) {
        sumcourse+=course;
    }

    public void sumCore(float core) {
        sumcore+=core;
    }

    public void setAvec() {
        if(sumcore==0){
            avec=0;
        }
        else {
            avec = sumcourse/sumcore;
        }
        avec=(float)(Math.round(avec*10000))/10000;
    }

    public String getStuid() {
        return stuid;
    }

    public float getSumcore() {
        return sumcore;
    }

    public float getSumcourse() {
        return sumcourse;
    }

    public float getAvec() {
        setAvec();
        return avec;
    }

    public String  getSem(){
        return sem;
    }

    public String getJsonsem() {
        return jsonsem;
    }
}
