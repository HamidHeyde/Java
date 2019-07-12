package server;

import models.semester;

import java.io.File;
import java.util.ArrayList;

import models.course;
import models.student;;

public class courseTest {
    public static void main(String[] args) {
        // String path = System.getProperty("user.dir");
        // path = path + "/server/courses/COMPFall.log";
        // semester sem = new semester("fall", "COMP", path);
        // System.out.println(sem.encodeString());
        // sem.removeCourse("NOTAREALONE");
        // sem.addCourse("comp1237",40);
        // sem.addCourse("COMP1237",40);
        // // sem.enrolCourse("COMP1236");
        // // sem.dropCourse("COMP1235");
        // // ArrayList<course> cs = sem.decodeString(sem.encodeString());

        // // if (cs != null) {
        // // for (course c : cs) {
        // // System.out.println(c.name);
        // // }
        // // }
        // // else{
        // // System.out.println("EMPTY");
        // // }
        String studentID = "COMPs2234";
        String path = System.getProperty("user.dir");
        String studentPath =  path + "/server/users/"+studentID.toLowerCase()+".log";
        student st = new student(studentID, studentPath);
        st.printData();
    }
}