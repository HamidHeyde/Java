package client;

import client.client;

import client.advisorClient;
import client.studentClient;
import models.departmentInfo;
import interfaces.clientInterface;

public class clientTest {

    public boolean isAdvisor;
    private String FullID;
    private String usernameDep;
    private String usernameType;
    private int usernameId;
    private departmentInfo dep;

    public clientTest(String userName) {
        this.FullID = userName;
    }

    public boolean evaluateAndConnect() {
        // Length of the Full username
        String username = this.FullID;
        if (username.length() != 9)
            return false;

        // Department, ID, Type
        usernameDep = username.substring(0, 4).toUpperCase();
        usernameType = username.substring(4, 5).toUpperCase();
        try {
            usernameId = Integer.parseInt(username.substring(5, 9));
        } catch (NumberFormatException e) {
            return false;
        }

        dep = departmentInfo.getDepartment(usernameDep);
        if (dep == null)
            return false;

        // Classifying based on Student - Advisor Role
        if (usernameType.equals("A")) {
            isAdvisor = true;
            return true;
        } else if (usernameType.equals("S")) {
            isAdvisor = false;
            return true;
        }

        // If non of above, then false
        return false;
    }
    // ===========Advisor=================

    public void addCourse(String courseID, String semester) {

        evaluateAndConnect();
        clientInterface user = new advisorClient(dep, FullID);
        // Sending Request
        String response = user.addCourse(courseID, semester);
        // Handling the Response
        if (response.equals("error")) {
            System.out.println("The Course Already Exist! Try Again..");
        } else {
            System.out.println("The Course Added Successfully!");
        }
    }

    public void removeCourse(String courseID, String semester) {

        evaluateAndConnect();
        clientInterface user = new advisorClient(dep, FullID);
        // Sending Request
        String response = user.removeCourse(courseID, semester);
        // Handling the Response
        if (response.equals("error")) {
            System.out.println("The Course Does not Exist! Try Again..");
        } else {
            System.out.println("The Course Reemoved Successfully!");
        }
    }

    public void listCourseAvailability(String semester) {

        evaluateAndConnect();
        clientInterface user = new advisorClient(dep, FullID);
        String response = user.listCourseAvailability(semester);

        // Handling the Response
        if (response.equals("error")) {
            System.out.println("No Course to Display!");
        } else {
            response = response.replace("error", "No Course to Display!");
            response = response.replace('_', '>');
            response = response.replace('=', '\n');
            System.out.println(response);
        }

    }

    // ===========Student=================
    public void enrolCourse(String courseID, String semester) {

        evaluateAndConnect();
        clientInterface user = new studentClient(dep, FullID);

        String studentID = this.FullID;
        String response = user.enrolCourse(studentID, courseID, semester);
        System.out.println(response);
    }

    public void dropCourse(String courseID) {

        evaluateAndConnect();
        clientInterface user = new studentClient(dep, FullID);

        String studentID = this.FullID;
        String response = user.dropCourse(studentID, courseID);
        System.out.println(response);
    }

    public void getClassSchedule() {

        evaluateAndConnect();
        clientInterface user = new studentClient(dep, FullID);

        String studentID = this.FullID;
        String response = user.getClassSchedule(studentID);
        System.out.println(response);

    }
}