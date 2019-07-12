package server;

import models.departmentInfo;
import server.server;
import sequencer.sequencer;
import frontEnd.frontEnd;
import replicaManager.replicaManager;

public class serverRunner {

    public static void main(String[] args){

        //String scenario = new String("normal");
        //String scenario = new String("faultTolerance");
        //String scenario = new String("highAvailability");

        //CORBA
        String[] params = {"-ORBInitialPort", "6666", "-ORBInitialHost", "localhost"};

        // Server + CORBA params
        //COMP servers
        //server compServer = new server(departmentInfo.COMP);
        server compServer1 = new server(departmentInfo.COMP1);
        // server compServer2 = new server(departmentInfo.COMP2);
        // server compServer3 = new server(departmentInfo.COMP3);

        //SOEN servers
        //server soenServer = new server(departmentInfo.SOEN);
        server soenServer1 = new server(departmentInfo.SOEN1);
        // server soenServer2 = new server(departmentInfo.SOEN2);
        // server soenServer3 = new server(departmentInfo.SOEN3);


        //INSE servers
        //server inseServer = new server(departmentInfo.INSE);
        server inseServer1 = new server(departmentInfo.INSE1);
        // server inseServer2 = new server(departmentInfo.INSE2);
        // server inseServer3 = new server(departmentInfo.INSE3);

        //FE
        // frontEnd frontEndServer = new frontEnd(departmentInfo.FE, params,"normal");
        // frontEnd frontEndServer = new frontEnd(departmentInfo.FE, params,"faultTolerance");
        frontEnd frontEndServer = new frontEnd(departmentInfo.FE, params,"highAvailability");
        
        //Sequencer
        sequencer seq = new sequencer(departmentInfo.SEQUENCER);

        //Replica Managers
        replicaManager rm1 = new replicaManager(departmentInfo.RM1);
        // replicaManager rm2 = new replicaManager(departmentInfo.RM2);
        // replicaManager rm3 = new replicaManager(departmentInfo.RM3);

        

        //Thread thread1 = new Thread(compServer);
        Thread thread11 = new Thread(compServer1);
        //Thread thread12 = new Thread(compServer2);
        //Thread thread13 = new Thread(compServer3);
        //Thread thread2 = new Thread(soenServer);
        Thread thread21 = new Thread(soenServer1);
        //Thread thread22 = new Thread(soenServer2);
        //Thread thread23 = new Thread(soenServer3);
        //Thread thread3 = new Thread(inseServer);
        Thread thread31 = new Thread(inseServer1);
        //Thread thread32 = new Thread(inseServer2);
        //Thread thread33 = new Thread(inseServer3);

        Thread thread4 = new Thread(frontEndServer);

        Thread thread5 = new Thread(seq);
        
        Thread thread6 = new Thread(rm1);
        // Thread thread7 = new Thread(rm2);
        // Thread thread8 = new Thread(rm3);

        //COMP
        //thread1.start();
        thread11.start();
        //thread12.start();
        //thread13.start();
        //SOEN
        //thread2.start();
        thread21.start();
        //thread22.start();
        //thread23.start();
        //INSE
        //thread3.start();
        thread31.start();
        //thread32.start();
        //thread33.start();

        //FE
        thread4.start();
        //Sequencer
        thread5.start();

        //Replica Manager
        thread6.start();
        // thread7.start();
        // thread8.start();


    }
}