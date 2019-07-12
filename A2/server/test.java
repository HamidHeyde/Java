package server;

import java.io.File;
import java.util.ArrayList;

import models.semester;
import models.student;

public class test {
    public static void main(String[] args) {

        ArrayList<String> results = new ArrayList<String>();

        String path = System.getProperty("user.dir");
        path = path + "/server/users/";
        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isFile()) { 
                String newFileName = file.getName();
                if(newFileName.substring(newFileName.length()-3).equals("log"))
                
                System.out.println(newFileName);
                String newPath = path+newFileName;
                student st = new student(newFileName.substring(0,9), newPath);
                
                String semm = st.removeCourse("comp1234");
                while (!semm.equals("error")){
                    semm = st.removeCourse("comp1234");
                }
            }
        }

    }
}