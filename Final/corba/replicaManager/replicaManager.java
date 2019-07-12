package replicaManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import models.departmentInfo;
import models.logger;

public class replicaManager implements Runnable{

    private final departmentInfo department;
    private logger log;
    // private String Scenario;

    public replicaManager(departmentInfo dep){
        this.department = dep;
        // this.Scenario = Scenario;
    }

    @Override
    public void run() {
        
        // ===========UDP Server=================
        udpServer listener = new udpServer(department);
        new Thread(listener).start();

    }

// ===========UDP CLASSES=================
    //UDP Server Class
    public class udpServer implements Runnable {

        private final departmentInfo department;
        //private logger log;
        private int id=0;

        public udpServer(departmentInfo dep){

            this.department = dep;
            
        }

        // ===========UDP Request=================
        private String udpRequest(byte[] messageInByte, int length, int serverPort) {
            
            DatagramSocket socket;
            try {
                socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("localhost");
                DatagramPacket request = new DatagramPacket(messageInByte, length, address, serverPort);
                
                socket.send(request);

                byte[] buffer = new byte[50000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Error: I/O Exception";
        }

        @Override
        public void run() {

            System.out.println(department.serverName + " Running UDP Server on PORT => "+department.udpPort );

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
                        System.out.println("(5).In ["+department.serverName+"]"+
                        "..from [FE] on "+request.getPort()+
                        "\n"+new String(request.getData())+"*"+request.getAddress()+"*"+request.getPort());
                        
                        if (!new String(request.getData()).contains("Error"))
                        {
                            String[] tokens = new String(request.getData()).trim().split("\\*");
                            String serverToForwardTo = new String(request.getData()).substring(2,6);
                            //String serverToForwardTo = tokens[1].trim();
                            
                            if(department.serverName.equals("RM1")){
                                serverToForwardTo+= "1";
                            } else if(department.serverName.equals("RM2")){
                                serverToForwardTo+= "2";
                            }else if(department.serverName.equals("RM3")){
                                serverToForwardTo+= "3";
                            }

                            //================================
                            String outMesage = new String(request.getData(), request.getOffset(), request.getLength())+
                            "*"+request.getAddress().toString()+"*"+Integer.toString(request.getPort());

                            departmentInfo dep = departmentInfo.getDepartment(serverToForwardTo);
                        
                            System.out.println("(5.1) Forwarding to =>"+serverToForwardTo+
                            "Sending Message From ["+department.serverName+"] \n Message=:"+outMesage+
                            "\nTO PORT=>"+dep.rmiPort);
                            

                            byte[] messageBytes = outMesage.getBytes();

                            udpRequest(messageBytes, messageBytes.length, dep.rmiPort);
                            
                            
                            //this.id++;
                        }
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