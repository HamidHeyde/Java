package server;

import models.departmentInfo;
import server.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class serverRunner {

    public static void main(String[] args)  throws RemoteException{

        LocateRegistry.createRegistry(7000);
        
        server compServer = new server(departmentInfo.COMP);
        server soenServer = new server(departmentInfo.SOEN);
        server inseServer = new server(departmentInfo.INSE);

        // compServer.initServer();
        // soenServer.initServer();
        // inseServer.initServer();

        Thread thread1 = new Thread(compServer);
        Thread thread2 = new Thread(soenServer);
        Thread thread3 = new Thread(inseServer);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}