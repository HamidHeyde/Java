package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class student {

    private String id;
    private String path;
    private ArrayList<ArrayList<String>> data;
    private ArrayList<String> semesters;

    public student(String fullId, String filePath) {
        this.id = fullId;
        this.path = filePath;

        semesters = new ArrayList<String>();
        semesters.addAll(Arrays.asList("fall", "winter", "summer"));

        loadData();
    }

    public int coursesFromOtherdeps(){

        int counter=0;
        for (ArrayList<String> sem : this.data){
            for (String c : sem){
                
                if (!c.equals("-")){
                    if (!c.substring(0, 4).equals(this.id.substring(0, 4).toLowerCase())){
                        counter++;
                    }   
                }
            }
        }
        return counter;
    }

    public boolean canTakeCourse(String sem, String course) {
        course = course.toLowerCase();

        if (data.get(semesters.indexOf(sem)).size() < 4) {
            if (course.substring(0, 4).equals(this.id.substring(0, 4).toLowerCase()))
            {
                return true;
            }else{
                if (coursesFromOtherdeps()<2)
                {
                    return true;
                } 
            }
            
        }
        return false;
    }


    public String addCourse(String course, String semester) {
        course = course.toLowerCase();
        semester = semester.toLowerCase();

        try {
            if (canTakeCourse(semester,course)) {
                if (data.get(semesters.indexOf(semester)).indexOf(course) == -1) {

                    data.get(semesters.indexOf(semester)).add(course);

                    saveData();
                    loadData();
                    return "success";
                }
            }
        } catch (Exception e) {

        }
        return "error";
    }

    public String removeCourse(String course, String semester) {

        course = course.toLowerCase();
        semester = semester.toLowerCase();

        try {
            // for (int i = 0; i < data.size(); i++) {
                int i=0;
                if (semester.equals("fall")) {
                    i = 0;
                } else if (semester.equals("winter")) {
                    i = 1;
                } else if (semester.equals("summer")) {
                    i = 2;
                }

                for (int j = 0; j < data.get(i).size(); j++) {
                    if (data.get(i).get(j).equals(course)) {
                        data.get(i).remove(j);

                        saveData();
                        loadData();

                        return semester;
                    }
                }
            // }
        } catch (Exception e) {
        }
        
        return "error";
    }
    /// EDIT nNO SEMEESTER
    public String removeCourse(String course) {
        course = course.toLowerCase();
        String semester = "";
        try {
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).size(); j++) {
                    if (data.get(i).get(j).equals(course)) {
                        data.get(i).remove(j);
                        if (i == 0) {
                            semester = "fall";
                        } else if (i == 1) {
                            semester = "winter";
                        } else if (i == 2) {
                            semester = "summer";
                        }

                        saveData();
                        loadData();

                        return semester;
                    }
                }
            }
        } catch (Exception e) {
        }

        return "error";
    }

    public String getSchedule() {
        String result = "";

        for (int i = 0; i < data.size(); i++) {
            
            if (i == 0) {
                result += "fall ";
            } else if (i == 1) {
                result += "winter ";
            } else if (i == 2) {
                result += "summer ";
            }

            for (int j = 0; j < data.get(i).size(); j++) {
                result += data.get(i).get(j)+" ";
            }

        }

        return result;
    }

    private void loadData() {
        data = new ArrayList<ArrayList<String>>();

        BufferedReader reader;
        synchronized (this) {
            try {

                reader = new BufferedReader(new FileReader(path));

                String line = reader.readLine();

                while (line != null) {

                    String[] tokens = line.split("\\s");

                    ArrayList<String> temp = new ArrayList<String>();

                    for (String token : tokens) {
                        temp.add(token.toLowerCase());
                    }

                    data.add(temp);

                    line = reader.readLine();

                }

                reader.close();

            } catch (IOException e) {
                saveData();
            }
        }
    }

    private void saveData() {

        if (data.size() == 0) {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add("-");
            for (int i = 0; i < 3; i++) {
                data.add(temp);
            }
        }

        File file = new File(path);
        FileWriter fr = null;

        synchronized (this) {
            try {
                // FOR APPENDING
                // fr = new FileWriter(file,true);
                fr = new FileWriter(file);
                int i = 0;
                for (ArrayList<String> ars : data) {
                    for (String s : ars) {
                        fr.write(s.toLowerCase() + " ");
                    }
                    if (i != (this.data.size() - 1)) {
                        fr.write('\n');
                    }
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printData() {

        System.out.println(data.size());

        if (data.size() > 0) {
            System.out.println(data.get(0).size());
            System.out.println(data.get(0));
            System.out.println(data.get(1).size());
            System.out.println(data.get(1));
            System.out.println(data.get(2).size());
            System.out.println(data.get(2));
        }
    }

}