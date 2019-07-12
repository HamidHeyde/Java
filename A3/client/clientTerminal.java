package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import client.advisorClient;
import client.studentClient;
import models.departmentInfo;
import client.client;
import interfaces.clientInterface;
import models.logger;

public class clientTerminal implements Runnable {

    private boolean exitApp;
    private boolean isAdvisor;

    private String FullID;
    private String usernameDep;
    private String usernameType;
    private int usernameId;

    private departmentInfo dep;
    clientInterface user;
    logger log;

    private ArrayList<String> studentOperations;
    private ArrayList<String> advisorOperations;

    public static void main(String[] args) {
        clientTerminal terminal = new clientTerminal();
        Thread thread = new Thread(terminal);
        thread.start();
    }

    private void init() {
        displayMessage("Please Enter your username:");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // Reading and Processing UserName
        try {
            String username = null;
            do {
                if (username != null) {
                    displayMessage("Invalid username, Try Again...");
                }
                username = bufferedReader.readLine();

            } while (!evaluateAndConnect(username));

            FullID = username.toUpperCase();
            // Logger initiation
            log = new logger(System.getProperty("user.dir") + "/client/logs/" + FullID.toUpperCase() + ".log");

            String loginMessage = new String("Hello, " + (isAdvisor ? "Advisor" : "Student") + " " + usernameId + "\n"
                    + "Connecting to => " + dep.name);
            displayMessage(loginMessage);
            // Logging the message
            log.add(loginMessage);

            studentOperations = new ArrayList<String>();
            studentOperations.addAll(Arrays.asList("enrolCourse", "dropCourse","swapCourse", "getClassSchedule", "exit"));

            advisorOperations = new ArrayList<String>();
            advisorOperations.addAll(Arrays.asList("addCourse", "removeCourse", "listCourseAvailability", "exit"));

            showMenuFor(usernameType);

            exitApp = false;
            do {
                displayMessage("Please Choose an operation:");
                String input = bufferedReader.readLine();
                log.add("Operation Requested:" + input);
                parseInput(input.toLowerCase(), bufferedReader);
            } while (!exitApp);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean evaluateAndConnect(String username) {
        // Length of the Full username
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

        // extracting Department Info
        // Check if the Department exist
        dep = departmentInfo.getDepartment(usernameDep);
        if (dep == null)
            return false;

        // Classifying based on Student - Advisor Role
        if (usernameType.equals("A")) {
            user = new advisorClient(dep, username);
            isAdvisor = true;
            return true;
        } else if (usernameType.equals("S")) {
            user = new studentClient(dep, username);
            isAdvisor = false;
            return true;
        }

        // If non of above, then false
        return false;
    }

    private boolean evaluate(String itemToEvaluate, String userInput) {

        ArrayList<String> availableSemesters = new ArrayList<String>();
        availableSemesters.addAll(Arrays.asList("fall", "winter", "summer"));

        ArrayList<String> availableDeps = new ArrayList<String>();
        availableDeps.addAll(Arrays.asList("COMP", "SOEN", "INSE"));

        switch (itemToEvaluate) {
        case "semester": {
            if (availableSemesters.indexOf(userInput.toLowerCase()) > -1) {
                return true;
            } else {
                log.add("Wrong Semester Name! Try again..");
                messageClient("Wrong Semester Name! Try again..");
            }
            break;
        }
        case "capacity": {
            try{

                int cp = Integer.parseInt(userInput);
                if (cp>0){
                    return true;
                }else{
                    log.add("Not the right Capacity! Try again.. ");
                    messageClient("Not the right Capacity! Try again.. ");
                }

            }catch(Exception e){
                log.add("Not the right Capacity! Try again.. ");
                messageClient("Not the right Capacity! Try again.. ");
            }
            break;
        }
        case "courseID": {
            if (userInput.length() == 8) {
                if (availableDeps.indexOf(userInput.substring(0, 4).toUpperCase()) > -1) {
                    if (userInput.substring(0, 4).toUpperCase().equals(usernameDep)) {
                        try {
                            int a = Integer.parseInt(userInput.substring(4, 8));
                            return true;
                        } catch (Exception e) {
                        }

                    } else {
                        log.add("You cannot add " + userInput.substring(0, 4).toUpperCase() + " courses");
                        messageClient("You cannot add " + userInput.substring(0, 4).toUpperCase() + " courses");
                    }
                }
            }
            log.add("Wrong Course ID Format! Try again..");
            messageClient("Wrong Course ID Format! Try again..");
            break;
        }
        case "stcourseID": {
            if (userInput.length() == 8) {
                if (availableDeps.indexOf(userInput.substring(0, 4).toUpperCase()) > -1) {

                    try {
                        int a = Integer.parseInt(userInput.substring(4, 8));
                        return true;
                    } catch (Exception e) {
                    }

                }
            }
            log.add("Wrong Course ID Format! Try again..");
            messageClient("Wrong Course ID Format! Try again..");
            break;
        }
        }

        return false;
    }

    private void displayMessage(String Message) {
        System.out.println("------------------------------");
        System.out.println(Message);
        System.out.println("------------------------------");
    }

    private void messageClient(String Message) {
        System.out.println(Message);
    }

    private void showMenuFor(String userType) {
        System.out.println("********************************");
        if (userType.toLowerCase().equals("s")) {
            for (String ops : studentOperations) {
                System.out.println(ops);
            }
        } else if (userType.toLowerCase().equals("a")) {
            for (String ops : advisorOperations) {
                System.out.println(ops);
            }
        }
        System.out.println("********************************");
    }

    private void parseInput(String input, BufferedReader reader) throws IOException {
        if (isAdvisor) {
            switch (input) {
            case "addcourse":
                addCourse(reader);
                break;
            case "removecourse":
                removeCourse(reader);
                break;
            case "listcourseavailability":
                listCourseAvailability(reader);
                break;
            case "exit":
                exitApp = true;
                break;
            default:
                displayMessage("Invalid Operation, Try Again...");
                log.add("Invalid Operation, Try Again...");
                showMenuFor(usernameType);
                break;
            }
        } else {
            switch (input) {
            case "enrolcourse":
                enrolCourse(reader);
                break;
            case "dropcourse":
                dropCourse(reader);
                break;
            case "getclassschedule":
                getClassSchedule(reader);
                break;
            case "swapcourse":
                swapCourse(reader);
                break;
            case "exit":
                exitApp = true;
                break;
            default:
                displayMessage("Invalid Operation, Try Again...");
                log.add("Invalid Operation, Try Again...");
                showMenuFor(usernameType);
                break;
            }
        }
    }

    // ===========Advisor=================
    private void addCourse(BufferedReader reader) throws IOException {

        log.add("Advisor addCourse Function");

        String courseID;
        String semester;
        int capacity;

        messageClient("Please type in the Course ID");
        log.add("Please type in the Course ID");

        courseID = reader.readLine().toLowerCase();
        if (evaluate("courseID", courseID)) {

            messageClient("Please Enter the Semester");
            log.add("Please Enter the Semester");

            semester = reader.readLine().toLowerCase();
            if (evaluate("semester", semester)) {

                messageClient("Please Enter Capacity for the Course");
                log.add("Please Enter Capacity for the Course");
                String temp = reader.readLine().toLowerCase();
                if (evaluate("capacity", temp)){
                    capacity = Integer.parseInt(temp);

                    // Sending Request
                    String response = user.addCourse(courseID, semester, temp);
                    // Handling the Response
                    if (response.equals("error")) {
                        messageClient("The Course Already Exist! Try Again..");
                        log.add("The Course Already Exist! Try Again..");
                    } else {
                        messageClient("The Course Added Successfully!");
                        log.add("The Course Added Successfully!");
                    }
                }
            }
        }
    }

    private void removeCourse(BufferedReader reader) throws IOException {

        log.add("Advisor Remove Course Function");

        String courseID;
        String semester;

        messageClient("Please type in the Course ID");
        log.add("Please type in the Course ID");

        courseID = reader.readLine().toLowerCase();
        if (evaluate("courseID", courseID)) {
            messageClient("Please Enter the Semester");
            log.add("Please Enter the Semester");

            semester = reader.readLine().toLowerCase();
            if (evaluate("semester", semester)) {

                // Sending Request
                String response = user.removeCourse(courseID, semester);
                // Handling the Response
                if (response.equals("error")) {
                    messageClient("The Course Does not Exist! Try Again..");
                    log.add("The Course Does not! Try Again..");
                } else {
                    messageClient("The Course Reemoved Successfully!");
                    log.add("The Course Removed Successfully!");
                }

            }
        }
    }

    private void listCourseAvailability(BufferedReader reader) throws IOException {

        log.add("Advisor listCourseAvailability Function");

        String semester = "";

        messageClient("Please Enter the Semester");
        log.add("Please Enter the Semester");

        semester = reader.readLine().toLowerCase();
        if (evaluate("semester", semester)) {
            // Sending Request
            String response = user.listCourseAvailability(semester);
            // Handling the Response
            if (response.equals("error")) {
                messageClient("No Course to Display!");
                log.add("No Course to Display!");
            } else {
                response = response.replace("error", "No Course to Display!");
                response = response.replace('_', '>');
                response = response.replace('=', '\n');
                messageClient(response);
                log.add(response);
            }
        }
    }

    // ===========Student=================
    private void enrolCourse(BufferedReader reader) throws IOException {

        log.add("Student enrolCourse Function");

        String studentID = this.FullID;
        String courseID;
        String semester;

        messageClient("Please type in the Course ID");
        log.add("Please type in the Course ID");

        courseID = reader.readLine().toLowerCase();
        if (evaluate("stcourseID", courseID)) {
            messageClient("Please Enter the Semester");
            log.add("Please Enter the Semester");

            semester = reader.readLine().toLowerCase();
            if (evaluate("semester", semester)) {

                String response = user.enrolCourse(studentID, courseID, semester);
                messageClient(response);
                log.add(response);
            }
        }
    }

    private void dropCourse(BufferedReader reader) throws IOException {
        log.add("Student dropCourse Function");

        String studentID = this.FullID;
        String courseID;

        messageClient("Please type in the Course ID");
        log.add("Please type in the Course ID");

        courseID = reader.readLine().toLowerCase();
        if (evaluate("stcourseID", courseID)) {

            String response = user.dropCourse(studentID, courseID);
            messageClient(response);
            log.add(response);

        }
    }

    private void getClassSchedule(BufferedReader reader) throws IOException {

        String studentID = this.FullID;
        String response = user.getClassSchedule(studentID);
        messageClient(response);
        log.add(response);

    }

    // ===========SWAP=================
    private void swapCourse(BufferedReader reader) throws IOException {
        
        log.add("Student swapCourse Function");

        String studentID = this.FullID;
        String newCourseID;
        String oldCourseID;
        String semester;

        messageClient("Please type in the NEW Course ID");
        log.add("Please type in the NEW Course ID");

        newCourseID = reader.readLine().toLowerCase();
        if (evaluate("stcourseID", newCourseID)) {
            
            messageClient("Please type in the OLD Course ID");
            log.add("Please type in the OLD Course ID");

            oldCourseID = reader.readLine().toLowerCase();
            if (evaluate("stcourseID", oldCourseID)) {

                messageClient("Please Enter the Semester");
                log.add("Please Enter the Semester");

                semester = reader.readLine().toLowerCase();
                if (evaluate("semester", semester)) {

                    String response = user.swapCourse(studentID, newCourseID,oldCourseID,semester);
                    messageClient(response);
                    log.add(response);
                }
            }
        }
    }

    @Override
    public void run() {
        init();
        System.out.println("... BYE ...");
        log.add("... BYE ...");
    }

}