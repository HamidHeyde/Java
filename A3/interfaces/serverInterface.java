package interfaces;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface serverInterface{

    // ===========Advisor=================
    default String addCourse(String courseID, String semester, String capacity){
        throw new UnsupportedOperationException();
    }

    default String removeCourse(String courseID, String semester){
        throw new UnsupportedOperationException();
    }

    default String listCourseAvailability(String semester){
        throw new UnsupportedOperationException();
    }

    // ===========Student=================
    default String enrolCourse(String studentID, String courseID, String semester){
        throw new UnsupportedOperationException();
    }

    default String dropCourse(String studentID, String courseID){
        throw new UnsupportedOperationException();
    }

    default String getClassSchedule(String studentID){
        throw new UnsupportedOperationException();
    }

    // ===========SWAP=================
    default String swapCourse(String studentID, String newCourseID, String oldCourseID, String semester){
        throw new UnsupportedOperationException();
    }

}