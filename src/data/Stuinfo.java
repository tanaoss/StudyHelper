package data;

public class Stuinfo {
    private String id="";
    private String major="";
    private String time="";

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getId() {
        return id;
    }

    public String getMajor() {
        return major;
    }

    public String getTime() {
        return time;
    }
}
