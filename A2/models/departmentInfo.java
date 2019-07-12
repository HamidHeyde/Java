package models;

public enum departmentInfo{
 
    COMP("Computer Science Department","COMP","COMP",7000,1231),
    SOEN("Software Engineering Department","SOEN","SOEN",7000,1232),
    INSE("Information Security Department","INSE","INSE",7000,1233);

    public String name;
    public String abrev;
    public String serverName;
    public int rmiPort;
    public int udpPort;


    departmentInfo(
        String departmentName,String abreviation,
        String serverName,int rmiServerPort,int udpServerPort){

        this.name = departmentName;
        this.abrev = abreviation;
        this.serverName = serverName;
        this.rmiPort = rmiServerPort;
        this.udpPort = udpServerPort;
    }

    public static departmentInfo getDepartment(String abreviation){
        for (departmentInfo dep : departmentInfo.values()){
            if (dep.abrev.equals(abreviation)){
                return dep;
            }
        }

        return null;
    }
}