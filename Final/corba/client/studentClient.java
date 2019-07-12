package client;

import models.departmentInfo;
import interfaces.clientInterface;

public class studentClient extends client implements clientInterface, Runnable {

    public studentClient(departmentInfo department, String fullID) {
        super(department, fullID);
    }

    public String enrolCourse(String studentID, String courseID, String semester) {
        String response = new String("");
        try {

            response =  (connect().enrolCourse(studentID, courseID,semester));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }

    public String dropCourse(String studentID, String courseID) {
        String response = new String("");
        try {

            response =  (connect().dropCourse(studentID,courseID));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }

    public String getClassSchedule(String studentID) {
        String response = new String("");
        try {

            response =  (connect().getClassSchedule(studentID));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }
    // ===========SWAP=================
    public String swapCourse(String studentID, String newCourseID, String oldCourseID, String semester){
        String response = new String("");
        try {

            response =  (connect().swapCourse(studentID,newCourseID,oldCourseID,semester));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }   

    @Override
    public void run() {
    }

}