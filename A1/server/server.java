package server;

import java.rmi.AlreadyBoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import interfaces.serverInterface;
import models.departmentInfo;
import models.logger;
import models.semester;
import models.student;

public class server extends UnicastRemoteObject implements serverInterface, Runnable {

    private final departmentInfo department;
    private logger log;

    // Constructor
    public server(departmentInfo dep) throws RemoteException {
        super(dep.rmiPort);
        this.department = dep;

        log = new logger(System.getProperty("user.dir") + "/server/logs/" + department.serverName + "server.log");
        log.add(department.serverName + " Server Initiated");
    }

    @Override
    public void run() {
        registerWithRegistry();
        runUDPServer();
    }

    private void registerWithRegistry() {
        try {
            Registry registry = LocateRegistry.getRegistry(7000);
            registry.bind(department.serverName, this);
            displayMessage(department.serverName + " server has been started");
            log.add(department.serverName + " server has been started");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
            log.add(department.serverName + " Cannot bind to registry");
        }
    }

    // ===========UDP Server=================
    private void runUDPServer() {
        
        log.add(department.serverName + " Running UDP Server");
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(department.udpPort);
            byte[] buffer;
            DatagramPacket request;
            while (true) {
                log.add(department.serverName + " Waiting for UDP request ...");
                buffer = new byte[10000];
                request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                log.add(department.serverName + " Waiting for UDP request Received...");
                new Thread(new Responder(socket, request)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
    }

    // ===========UDP Request=================
    private String udpRequest(byte[] messageInByte, int length, int serverPort) {
        log.add(department.serverName + " Sending UDP request ...");
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(messageInByte, length, address, serverPort);
            socket.send(request);

            log.add(department.serverName + " Waiting for UDP Reply ...");
            byte[] buffer = new byte[10000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            return new String(reply.getData()).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: I/O Exception";
    }

    // ===========Advisor=================
    @Override
    public String addCourse(String courseID, String semester) {

        String path = System.getProperty("user.dir");
        path = path + "/server/courses/" + department.serverName + semester.toLowerCase() + ".log";
        semester sem = new semester(semester, department.serverName, path);

        log.add(department.serverName + " Adding => " + courseID + " For => " + semester);
        String response = sem.addCourse(courseID, 40);
        log.add(response);

        return response;
    }

    @Override
    public String removeCourse(String courseID, String semester) {

        String path = System.getProperty("user.dir");
        path = path + "/server/courses/" + department.serverName + semester.toLowerCase() + ".log";
        semester sem = new semester(semester, department.serverName, path);

        log.add(department.serverName + " Removing => " + courseID + " For => " + semester);
        String response = sem.removeCourse(courseID);
        log.add(response);

        return response;
    }

    @Override
    public String listCourseAvailability(String semester) {

        log.add("Course List for => " + semester);

        String path = System.getProperty("user.dir");
        path = path + "/server/courses/" + department.serverName + semester.toLowerCase() + ".log";
        semester sem = new semester(semester, department.serverName, path);

        log.add("Course List from => " + department.serverName);
        String response = sem.encodeString();

        ArrayList<String> otherServerCourses = new ArrayList<String>();
        if (department.serverName == "SOEN") {
            log.add("Course List from => COMP");
            response += "=" + getOtherServerCourses("COMP", semester);
            log.add("Course List from => INSE");
            response += "=" + getOtherServerCourses("INSE", semester);

        } else if (department.serverName == "COMP") {
            log.add("Course List from => SOEN");
            response += "=" + getOtherServerCourses("SOEN", semester);
            log.add("Course List from => INSE");
            response += "=" + getOtherServerCourses("INSE", semester);

        } else if (department.serverName == "INSE") {
            log.add("Course List from => COMP");
            response += "=" + getOtherServerCourses("COMP", semester);
            log.add("Course List from => SOEN");
            response += "=" + getOtherServerCourses("SOEN", semester);
        }

        log.add(response);
        return response;
    }

    private String getOtherServerCourses(String sName, String semester) {
        departmentInfo dep = departmentInfo.getDepartment(sName);
        String message = semester;
        String Response = udpRequest(message.getBytes(), message.length(), dep.udpPort);
        return Response;
    }

    // ===========Student=================
    @Override
    public String enrolCourse(String studentID, String courseID, String semester) {
        courseID = courseID.toLowerCase();

        log.add("Enrolling => " + studentID + " in => " + courseID + " for => " + semester);        

        String response = "You have been enrolled Successfully !!!";

        String path = System.getProperty("user.dir");
        String coursePath = path + "/server/courses/" + department.serverName + semester.toLowerCase() + ".log";
        semester sem = new semester(semester, department.serverName, coursePath);
        String studentPath = path + "/server/users/" + studentID.toLowerCase() + ".log";
        student st = new student(studentID, studentPath);

        if (st.canTakeCourse(semester)) {

            String semEnrol = "";
            if (courseID.substring(0, 4).equals(department.serverName.toLowerCase())) {
                semEnrol = sem.enrolCourse(courseID);
            } else {
                semEnrol = otherServerOp("add", courseID, semester);
            }
            log.add("Checking to see if " + studentID + " can take any course ...");
            if (!semEnrol.equals("error")) {

                log.add("Trying to enrol the " + studentID + " in the " + courseID);
                if (st.addCourse(courseID, semester).equals("error")) {
                    response = "You cannot take more than 3 courses OR you have already Registered for this course !!";
                    // Adding the deducted number
                    sem.dropCourse(courseID);
                }
            } else {
                response = "No space available OR the course doesnt exist in that semester !!";
            }

        }else {
            response = "You cannot take more than 3 courses !!";
        }

        log.add(response);
        return response;
    }

    private String otherServerOp(String op, String course, String semm) {
        departmentInfo dep = departmentInfo.getDepartment(course.substring(0, 4).toUpperCase());
        String message = op.toLowerCase() + " " + course.toLowerCase() + " " + semm.toLowerCase();
        String Response = udpRequest(message.getBytes(), message.length(), dep.udpPort);
        return Response;
    }

    @Override
    public String dropCourse(String studentID, String courseID) {
        log.add("Dropping => " + courseID + " for  => " + studentID);

        String response = "Dropped Successfully !!!";

        String path = System.getProperty("user.dir");
        String studentPath = path + "/server/users/" + studentID.toLowerCase() + ".log";
        student st = new student(studentID, studentPath);

        // Find the Semseter for Course and then remove it

        String semm = st.removeCourse(courseID);
        // System.out.println(semm);
        if (!semm.equals("error")) {

            if (courseID.substring(0, 4).equals(department.serverName.toLowerCase())) {
                String coursePath = path + "/server/courses/" + department.serverName + semm.toLowerCase() + ".log";
                semester sem = new semester(semm, department.serverName, coursePath);
                sem.dropCourse(courseID);
            } else {

                otherServerOp("drop", courseID, semm);
            }

        } else {
            response = "The course doesn't Exist !!";
        }

        log.add(response);
        return response;
    }

    @Override
    public String getClassSchedule(String studentID) {

        log.add("Getting schedule for => " + studentID);

        String path = System.getProperty("user.dir");
        String studentPath = path + "/server/users/" + studentID.toLowerCase() + ".log";
        student st = new student(studentID, studentPath);

        String response = st.getSchedule();
        log.add(response);
        return response;
    }

    private void displayMessage(String Message) {
        System.out.println("------------------------------");
        System.out.println(Message);
        System.out.println("------------------------------");
    }

    // Responder Class
    public class Responder implements Runnable {
        private DatagramSocket socket = null;
        private DatagramPacket request = null;

        Responder(DatagramSocket socket, DatagramPacket packet) {
            this.socket = socket;
            this.request = packet;
        }

        @Override
        public void run() {
            try {
                byte[] data = makeResponse(request.getData());

                if (data != null) {
                    DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(),
                            request.getPort());
                    socket.send(response);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        private byte[] makeResponse(byte[] inMessage) {

            String response = "";
            String message = new String(inMessage).trim();

            ArrayList<String> semesters = new ArrayList<String>();
            semesters.addAll(Arrays.asList("fall", "winter", "summer"));

            if (semesters.indexOf(message) > -1) {

                String path = System.getProperty("user.dir");
                path = path + "/server/courses/" + department.serverName + message.toLowerCase() + ".log";

                semester sem = new semester(message, department.serverName, path);
                response = sem.encodeString();

            } else if (message.contains("add")) {
                String[] tokens = message.split("\\s");
                String path = System.getProperty("user.dir");
                String coursePath = path + "/server/courses/" + department.serverName + tokens[2].toLowerCase()
                        + ".log";
                semester sem = new semester(tokens[2].toLowerCase(), department.serverName, coursePath);

                response = sem.enrolCourse(tokens[1]);

            } else if (message.contains("drop")) {
                String[] tokens = message.split("\\s");
                String path = System.getProperty("user.dir");
                String coursePath = path + "/server/courses/" + department.serverName + tokens[2].toLowerCase()
                        + ".log";
                semester sem = new semester(tokens[2].toLowerCase(), department.serverName, coursePath);

                response = sem.dropCourse(tokens[1]);
            }

            return response.getBytes();
        }
    }

}