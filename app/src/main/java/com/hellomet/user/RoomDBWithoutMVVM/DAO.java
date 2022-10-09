package com.hellomet.user.RoomDBWithoutMVVM;

import java.util.List;

public interface DAO {
    void deleteStu(int i);

    Student getStudent(int i);

    List<Student> getStudent();

    void studentInsertion(Student student);

    void updateStu(String str, int i);
}
