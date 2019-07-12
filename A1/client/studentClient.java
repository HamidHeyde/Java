package client;

import models.departmentInfo;
import interfaces.clientInterface;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class studentClient extends client implements clientInterface, Runnable {

    public studentClient(departmentInfo department, String fullID) {
        super(department, fullID);
    }

    public String enrolCourse(String studentID, String courseID, String semester) {
        String response = new String("");
        try {

            response =  (connect().enrolCourse(studentID, courseID,semester));

        } catch (RemoteException | NotBoundException e) {
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

        } catch (RemoteException | NotBoundException e) {
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