package client;

import models.departmentInfo;
import interfaces.serverInterface;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;


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

    protected serverInterface connect() throws MalformedURLException{
        
        URL COMPurl = new URL("http://localhost:8001/comp?wsdl");
        URL SOENurl = new URL("http://localhost:8002/soen?wsdl");
        URL INSEurl = new URL("http://localhost:8003/inse?wsdl");

        URL url = new URL("http://localhost:8001/comp?wsdl");

        switch (department.serverName) {
            case "COMP":
                {
                    url = COMPurl;
                    break;
                }
            case "SOEN":
                {
                    url = SOENurl;
                    break;
                }
                
            case "INSE":
                {
                    url = INSEurl;
                    break;
                }
        }

        // System.out.println("connecting to .. "+url+" ..");
        QName qname = new QName("http://server/","serverService");
        Service service = Service.create(url,qname);

        serverInterface server = service.getPort(serverInterface.class);
        return server;
    }
}