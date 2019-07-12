package client;

import models.departmentInfo;
import interfaces.clientInterface;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class advisorClient extends client implements clientInterface, Runnable {

    public advisorClient(departmentInfo department, String fullID) {
        super(department, fullID);
    }

    public String addCourse(String courseID, String semester) {
        String response = new String("");
        try {

            response = (connect().addCourse(courseID, semester));

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }

        return response;
    }

    public String removeCourse(String courseID, String semester) {
        String response = new String("");
        try {

            response = (connect().removeCourse(courseID, semester));

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            // log.severe(builder.append(" FAILED - CONNECTION ERROR").toString());
            response = "error";
        }
        return response;
    }

    public String listCourseAvailability(String semester) {
        String response = new String("");
        try {

            response = (connect().listCourseAvailability(semester));

        } catch (RemoteException | NotBoundException e) {
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