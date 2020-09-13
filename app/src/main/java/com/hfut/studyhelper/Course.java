package com.hfut.studyhelper;


import java.io.Serializable;

public class Course implements Serializable {

    public String getCode() {
        return code;
    }

    public String getCredits() {
        return credits;
    }

    public int getClassStart() {
        return classStart;
    }

    public int getClassEnd() {
        return classEnd;
    }

    private String courseName;
    private String teacher;
    private String classRoom;
    private String code;
    private String credits;
    private String id;
    private int day;
    private int classStart;
    private int classEnd;

    public String getId() {
        return id;
    }

    public Course(String courseName, String id, String code, String credits, String classRoom, int day, int classStart, int classEnd) {
        this.courseName = courseName;
        this.classRoom = classRoom;
        this.code=code;
        this.day = day;
        this.id=id;
        this.classStart = classStart;
        this.classEnd = classEnd;
        this.credits=credits;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return classStart;
    }

    public void setStart(int classStart) {
        this.classEnd = classStart;
    }

    public int getEnd() {
        return classEnd;
    }

    public void setEnd(int classEnd) {
        this.classEnd = classEnd;
    }
}
