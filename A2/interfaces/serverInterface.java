package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface serverInterface extends Remote {

    // ===========Advisor=================
    default String addCourse(String courseID, String semester, int capacity) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    default String removeCourse(String courseID, String semester) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    default String listCourseAvailability(String semester) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    // ===========Student=================
    default String enrolCourse(String studentID, String courseID, String semester) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    default String dropCourse(String studentID, String courseID) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    default String getClassSchedule(String studentID) throws RemoteException {
        throw new UnsupportedOperationException();
    }

}