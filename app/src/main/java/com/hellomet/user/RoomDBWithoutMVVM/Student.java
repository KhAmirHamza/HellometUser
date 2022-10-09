package com.hellomet.user.RoomDBWithoutMVVM;

public class Student {
    String stuClass;
    String stuFirstName;
    int stuId;
    String stuLastName;

    public Student(String stuFirstName2, String stuLastName2, String stuClass2) {
        this.stuFirstName = stuFirstName2;
        this.stuLastName = stuLastName2;
        this.stuClass = stuClass2;
    }

    public int getStuId() {
        return this.stuId;
    }

    public void setStuId(int stuId2) {
        this.stuId = stuId2;
    }

    public String getStuFirstName() {
        return this.stuFirstName;
    }

    public void setStuFirstName(String stuFirstName2) {
        this.stuFirstName = stuFirstName2;
    }

    public String getStuLastName() {
        return this.stuLastName;
    }

    public void setStuLastName(String stuLastName2) {
        this.stuLastName = stuLastName2;
    }

    public String getStuClass() {
        return this.stuClass;
    }

    public void setStuClass(String stuClass2) {
        this.stuClass = stuClass2;
    }
}
