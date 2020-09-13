package com.hfut.studyhelper;

public class MyCourse {
    private String courseName;
    private String classRoom;
    private String code;
    private int day;
    private int Start;
    private int End;
    private String id;
    private String statute;

    public MyCourse(String courseName, String classRoom, String code, int day, int start, int end, String id, String statute) {
        this.courseName = courseName;
        this.classRoom = classRoom;
        this.code = code;
        this.day = day;
        Start = start;
        End = end;
        this.id = id;
        this.statute = statute;
    }


    public String getId() {
        return id;
    }
    public String getCourseName() {
        return courseName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getCode() {
        return code;
    }

    public int getDay() {
        return day;
    }

    public int getStart() {
        return Start;
    }

    public int getEnd() {
        return End;
    }

    public String getStatute() {
        return statute;
    }
}
