package server;

import javax.xml.ws.Endpoint;
import models.departmentInfo;
import server.server;

public class serverRunner {

    public static void main(String[] args){

        
        server COMP = new server();
        server SOEN = new server();
        server INSE = new server();
        
        String COMPurl = "http://localhost:8001/comp";
        String SOENurl = "http://localhost:8002/soen";
        String INSEurl = "http://localhost:8003/inse";

        Endpoint.publish(COMPurl, COMP);
        Endpoint.publish(SOENurl, SOEN);
        Endpoint.publish(INSEurl, INSE);

        COMP.initServer(departmentInfo.COMP);
        SOEN.initServer(departmentInfo.SOEN);
        INSE.initServer(departmentInfo.INSE);
    }
}