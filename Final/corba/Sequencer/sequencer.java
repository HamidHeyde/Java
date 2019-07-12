package sequencer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import models.departmentInfo;
import models.logger;

public class sequencer implements Runnable{

    private final departmentInfo department;
    private logger log;

    public sequencer(departmentInfo dep){
        this.department = dep;
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
        private int id=0;

        public udpServer(departmentInfo dep){
            this.department = dep;
        }

        @Override
        public void run() {

            System.out.println(department.serverName + " Running UDP Server on => "+department.udpPort);

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
                        
                        System.out.println("(2) => in [Squencer] \n"+ "Receiving Request from [FE]"+
                        "\n Request => "+new String(request.getData(),request.getOffset(), request.getLength()) );
                        
                        this.id++;

                        System.out.println("(2.1) Assigning id = ["+this.id+"] to the request \n "+
                        "Sending back ID to FE");

                        new Thread(new Responder(socket, request,id)).start();
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

// ===========RESPONDER CLASSES=================
public class Responder implements Runnable {
    private DatagramSocket socket = null;
    private DatagramPacket request = null;
    private int id = 0;

    Responder(DatagramSocket socket, DatagramPacket packet, int id) {
        this.socket = socket;
        this.request = packet;
        this.id = id;
    }

    @Override
    public void run() {
        try {

            String outMesage = Integer.toString(id);
            byte[] data = outMesage.getBytes();

            if (data != null) {
                DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(),request.getPort());
                socket.send(response);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
}