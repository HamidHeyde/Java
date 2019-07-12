package client;

import models.departmentInfo;
import interfaces.clientInterface;


public class advisorClient extends client implements clientInterface, Runnable {

    public advisorClient(departmentInfo department, String fullID) {
        super(department, fullID);
    }

    public String addCourse(String advisorID, String courseID, String semester, String capacity) {
        String response = new String("");
        try {

            response = (connect().addCourse(advisorID, courseID, semester, capacity));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }

        return response;
    }

    public String removeCourse(String advisorID, String courseID, String semester) {
        String response = new String("");
        try {

            response = (connect().removeCourse(advisorID, courseID, semester));

        } catch (Exception e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }

    public String listCourseAvailability(String advisorID, String semester) {
        String response = new String("");
        try {

            response = (connect().listCourseAvailability(advisorID, semester));

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