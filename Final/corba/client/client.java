package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import models.departmentInfo;
import interfaces.serverInterface;

//NEW
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import server.idl.departmentServerCorba.*;


public abstract class client implements Runnable{
 
    protected String fullID;
    protected departmentInfo department;
    protected String usernameDep;
    protected String usernameType;
    protected int usernameId;
    //NEW
    private final String ORB_PORT = "6666";
    protected departmentServerInterface departmentServer;


    client(departmentInfo department, String fullID){
        this.department = department;
        this.fullID = fullID;
        this.usernameDep = fullID.substring(0, 4).toUpperCase();
        this.usernameType = fullID.substring(4, 5).toUpperCase();
        this.usernameId = Integer.parseInt(fullID.substring(5, 9));
        //NEW
        setConnection();
    }

    private void setConnection() {
        try {
            String[] params = {"-ORBInitialPort", ORB_PORT, "-ORBInitialHost", "localhost"};
            ORB orb = ORB.init(params, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            departmentServer = departmentServerInterfaceHelper.narrow(ncRef.resolve_str(department.serverName));
        } catch (Exception e) {
            System.out.println("Hello Client exception: " + e);
            e.printStackTrace();
        }
    }

    protected departmentServerInterface connect(){
        return this.departmentServer;
    }
}