package frontEnd;

import server.idl.departmentServerCorba.departmentServerInterface;
import server.idl.departmentServerCorba.departmentServerInterfacePOA;
import server.idl.departmentServerCorba.departmentServerInterfaceHelper;


import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.util.ArrayList;
import java.util.Arrays;

import javax.lang.model.util.ElementScanner6;

import interfaces.serverInterface;
import models.departmentInfo;
import models.logger;
import models.semester;
import models.student;

public class frontEnd extends departmentServerInterfacePOA implements Runnable {

    private final departmentInfo department;
    private logger log;
    private final String[] orbParams;
    private static int idd = 0;
    private String Scenario;

    // Constructor
    public frontEnd(departmentInfo dep, String[] orbParams, String Scenario){
        
        this.department = dep;

        log = new logger(System.getProperty("user.dir") + "/server/logs/" + department.serverName + "server.log");
        log.add(department.serverName + " Server Initiated");

        this.orbParams = orbParams;

        this.Scenario = Scenario;
    }

    private void initializeORB() {
        try {
            // create and initialize the ORB
            //get reference to rootpoa &amp; activate the POAManager

            ORB orb = ORB.init(orbParams, null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            //get object reference from the servant
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(this);
            departmentServerInterface href = departmentServerInterfaceHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name(department.serverName);
            ncRef.rebind(path, href);

            System.out.println(department.name + " ready \n => ORB Initialized");
            while (true) {
                orb.run();
            }
        } catch (InvalidName | AdapterInactive | org.omg.CosNaming.NamingContextPackage.InvalidName
                | ServantNotActive | WrongPolicy | CannotProceed | NotFound invalidName) {
            invalidName.printStackTrace();
        }
    }

    @Override
    public void run() {
        
        // ===========UDP Server=================
        udpServer listener = new udpServer(department);
        new Thread(listener).start();

        initializeORB();
        
    }

    // ===========sendWarning Request=================
    private void sendWarning(byte[] messageInByte, int length, int serverPort) {
        log.add(department.serverName + " Sending UDP request ...");
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(messageInByte, length, address, serverPort);
            socket.send(request);
            

            synchronized(this){
                System.out.println("Sending Request from FE [udpRequest] to RM ..");
            }

            log.add(department.serverName + " Waiting for UDP Reply ...");
            socket.close();
    }catch (Exception ste){
                
    }
}

    // ===========UDP Request=================
    private String udpRequest(byte[] messageInByte, int length, int serverPort) {
        
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(messageInByte, length, address, serverPort);
            socket.send(request);
            socket.setSoTimeout(3000);

            
            byte[] buffer = new byte[50000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            
            try{
                socket.receive(reply);
            }catch (SocketTimeoutException ste){
                
                socket.close();  
                String returnMessage = "Error=>TimeOUT*";

                return returnMessage;
            }
        
            socket.close();
            return new String(reply.getData()).trim();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: I/O Exception";
    }

    // ===========Response Trimmer=================
    private String responseTrimmer(String messageToTrim){
        
        String[] tokens = messageToTrim.split("\\*");
        String finalResponse = new String("");
        if (!messageToTrim.contains("Error")){
           
            for (int i=0;i<tokens.length-2;i++){
                finalResponse+=tokens[i];
                if (i!= tokens.length-3){
                    finalResponse+="*";
                }
            }

        }else{
            finalResponse = messageToTrim;
        }
        
        return finalResponse;
    }
    // ===========Request Handler=================
    private String requestHandler(String messageToSend, String fullID, String operation){
        
        System.out.println("(1) => in [FE] \n Sending Request to [Sequencer] for [id] for ["+operation+"]");
        
        String idMessage = fullID.substring(0,4)+"*"+operation;
        departmentInfo sequencer = departmentInfo.getDepartment("SEQUENCER");
        String idResponse = udpRequest(idMessage.getBytes(), idMessage.length(), sequencer.udpPort);

        System.out.println("(3) => in [FE] \n Receiving Id => ["+idResponse+"] from Sequencer for ["+operation+"]");
        
        departmentInfo depRM1 = departmentInfo.getDepartment("RM1");
        departmentInfo depRM2 = departmentInfo.getDepartment("RM2");
        departmentInfo depRM3 = departmentInfo.getDepartment("RM3");
        

        String messageToRM = idResponse+"*"+messageToSend;
        System.out.println("(4) => in [FE] \n Sending Message => ["+messageToRM+"] To RMs for ["+operation+"]");

        ArrayList<String> responses = new ArrayList<String>();
        String response1 = udpRequest(messageToRM.getBytes(), messageToRM.length(), depRM1.udpPort);
        //response1 = response1.substring(0,response1.indexOf("*"));
        response1 = responseTrimmer(response1);
        String response2 = udpRequest(messageToRM.getBytes(), messageToRM.length(), depRM2.udpPort);
        //response2 = response2.substring(0,response2.indexOf("*"));
        response2 = responseTrimmer(response2);
        
        String response3 = udpRequest(messageToRM.getBytes(), messageToRM.length(), depRM3.udpPort);
        //response3 = response3.substring(0,response3.indexOf("*"));
        response3 = responseTrimmer(response3);

        responses.add(response1);
        responses.add(response2);
        responses.add(response3);

        System.out.println("(8) => in [FE] \n Receiving Response from [Server] for ["+operation+"]");
        synchronized(this){
            System.out.println("==>>Response1 .."+ responses.get(0));
            System.out.println("==>>Response2 .."+ responses.get(1));
            System.out.println("==>>Response3 .."+ responses.get(2));
        }

        String finalResponse = responses.get(0);
        for (int i=0;i<3;i++){
            for (int j=i+1;j<3;j++){
                if (responses.get(i).equals(responses.get(j))){
                    finalResponse = responses.get(i);
                    break;
                }
            }
        }

        
        if(Scenario.equals("faultTolerance"))
        {
            for (int i=0;i<3;i++){
                if(!responses.get(i).equals(finalResponse)){
                    
                    String warningMessage = "Error"+"*"+fullID.substring(0,4)+"*"+operation+"*"+Scenario;
                    String departmentName = "RM"+Integer.toString(i+1);
                    System.out.println("<=> "+departmentName+" <==>");
                    departmentInfo warningDepartment = departmentInfo.getDepartment(departmentName);
                    sendWarning(warningMessage.getBytes(), warningMessage.length(), warningDepartment.udpPort);

                    break;
                }
            }
        }else if(Scenario.equals("highAvailability"))
        {
            for (int i=0;i<3;i++){
                if (responses.get(i).contains("Error")){
                    
                    String warningMessage = "Error"+"*"+fullID.substring(0,4)+"*"+operation+"*"+Scenario;
                    String departmentName = "RM"+Integer.toString(i+1);
                    System.out.println("<=> "+departmentName+" <==>");
                    departmentInfo warningDepartment = departmentInfo.getDepartment(departmentName);
                    sendWarning(warningMessage.getBytes(), warningMessage.length(), warningDepartment.udpPort);

                    break;
                }
            }
        }

        
        return finalResponse;
    }

    // ===========Advisor=================
    @Override
    public String addCourse(String advisorID, String courseID, String semester, String capacity) {
        
        String message = advisorID.substring(0,4)+"*addcourse*"+courseID+"*"+semester+"*"+capacity;
        return requestHandler(message,advisorID,"addCourse");
    }

    @Override
    public String removeCourse(String advisorID, String courseID, String semester) {

        String message = advisorID.substring(0,4)+"*removeCourse*"+courseID+"*"+semester;
        return requestHandler(message,advisorID,"removeCourse");
    }

    @Override
    public String listCourseAvailability(String advisorID, String semester) {

        String message = advisorID.substring(0,4)+"*listCourseAvailability*"+semester;
        return requestHandler(message,advisorID,"listCourseAvailability");
    }

    // ===========Student=================
    @Override
    public String enrolCourse(String studentID, String courseID, String semester) {
        
        String message = studentID.substring(0,4)+"*enrolCourse*"+studentID+"*"+courseID+"*"+semester;
        return requestHandler(message,studentID,"enrolCourse");
    }

    @Override
    public String dropCourse(String studentID, String courseID) {
        String message = studentID.substring(0,4)+"*dropCourse*"+studentID+"*"+courseID;
        return requestHandler(message,studentID,"dropCourse");
    }

    @Override
    public String getClassSchedule(String studentID) {

        String message = studentID.substring(0,4)+"*getClassSchedule*"+studentID;
        return requestHandler(message,studentID,"getClassSchedule");
    }
    // ===========SWAP=================
    @Override
    public String swapCourse(String studentID, String newCourseID, String oldCourseID, String semester){
        String message = studentID.substring(0,4)+"*swapCourse*"+studentID+"*"+newCourseID+"*"+oldCourseID+"*"+semester;
        return requestHandler(message,studentID,"swapCourse");
    }

    // ===========UDP CLASSES=================
    //UDP Server Class
    public class udpServer implements Runnable {

        private final departmentInfo department;
        private logger log;

        public udpServer(departmentInfo dep){

            this.department = dep;
            log = new logger(System.getProperty("user.dir") + "/server/logs/" + department.serverName + "server.log");
        }

        @Override
        public void run() {

            log.add(department.serverName + " Running UDP Server\n"+
                department.serverName + " Running UDP Server => PORT : "+ department.udpPort);

            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(department.udpPort);
                byte[] buffer;
                DatagramPacket request;
                while (true) {
                    
                    buffer = new byte[50000];
                    request = new DatagramPacket(buffer, buffer.length);
                    socket.receive(request);

                    synchronized(this){
                        System.out.println("(7)in [FE] -> [UDP Server] \n Receiving Message from ["+new String(request.getData()).substring(2,6)+"] server\n"+
                        "MEssage=> "+new String(request.getData())+"\nSending Reply back to FE Function..");

                        String[] tokens = new String(request.getData()).trim().split("\\*");
                        DatagramPacket response = new DatagramPacket(request.getData(), request.getData().length, 
                        InetAddress.getByName(tokens[(tokens.length-2)].substring(1)),Integer.parseInt(tokens[(tokens.length-1)]));
                        socket.send(response);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null)
                    socket.close();
            }
        }
    }

}