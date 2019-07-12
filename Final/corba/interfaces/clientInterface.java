package interfaces;

public interface clientInterface {

    // ===========Advisor=================
    default String addCourse(String advisorID, String courseID, String semester, String capacity) {
        throw new UnsupportedOperationException();
    }

    default String removeCourse(String advisorID, String courseID, String semester) {
        throw new UnsupportedOperationException();
    }

    default String listCourseAvailability(String advisorID, String semester) {
        throw new UnsupportedOperationException();
    }

    // ===========Student=================
    default String enrolCourse(String studentID, String courseID, String semester) {
        throw new UnsupportedOperationException();
    }

    default String dropCourse(String studentID, String courseID) {
        throw new UnsupportedOperationException();
    }

    default String getClassSchedule(String studentID) {
        throw new UnsupportedOperationException();
    }
    // ===========SWAP=================
    default String swapCourse(String studentID, String newCourseID, String oldCourseID, String semester){
        throw new UnsupportedOperationException();
    }

}