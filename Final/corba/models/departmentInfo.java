package models;

public enum departmentInfo{
 
    //COMP("Computer Science Department","COMP","COMP",7000,1231),
    COMP1("Computer Science Department1","COMP1","COMP1",1251,1231),
    COMP2("Computer Science Department2","COMP2","COMP2",1252,1232),
    COMP3("Computer Science Department3","COMP3","COMP3",1253,1233),
    
    //SOEN("Software Engineering Department","SOEN","SOEN",7000,1232),
    SOEN1("Software Engineering Department1","SOEN1","SOEN1",1254,1234),
    SOEN2("Software Engineering Department2","SOEN2","SOEN2",1255,1235),
    SOEN3("Software Engineering Department3","SOEN3","SOEN3",1256,1236),

    
    //INSE("Information Security Department","INSE","INSE",7000,1233),
    INSE1("Information Security Department1","INSE1","INSE1",1257,1237),
    INSE2("Information Security Department2","INSE2","INSE2",1258,1238),
    INSE3("Information Security Department3","INSE3","INSE3",1259,1239),
    
    FE("Front End","FE","FE",7000,1248),
    SEQUENCER("Sequencer","SEQUENCER","SEQUENCER",7000,1249),
    
    RM1("Replica Manger1","RM1","RM1",7000,1241),
    RM2("Replica Manger2","RM2","RM2",7000,1242),
    RM3("Replica Manger3","RM3","RM3",7000,1243);

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