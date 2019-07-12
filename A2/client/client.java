package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import models.departmentInfo;
import interfaces.serverInterface;

public abstract class client implements Runnable{
 
    protected String fullID;
    protected departmentInfo department;
    protected String usernameDep;
    protected String usernameType;
    protected int usernameId;


    client(departmentInfo department, String fullID){
        this.department = department;
        this.fullID = fullID;
        this.usernameDep = fullID.substring(0, 4).toUpperCase();
        this.usernameType = fullID.substring(4, 5).toUpperCase();
        this.usernameId = Integer.parseInt(fullID.substring(5, 9));
    }

    protected serverInterface connect() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry(this.department.rmiPort);
        return (serverInterface) registry.lookup(this.department.serverName);
    }

}