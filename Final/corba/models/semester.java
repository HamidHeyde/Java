package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import models.course;

public class semester {

    public String server;
    public String name;
    public ArrayList<course> courses;
    public String courseFile;

    public semester(String semesterName, String serverName, String path) {
        this.server = serverName;
        this.name = semesterName;
        this.courseFile = path;

        loadCourses();
    }

    public void loadCourses() {
        this.courses = new ArrayList<course>();
        BufferedReader reader;
        synchronized (this) {
            try {
                reader = new BufferedReader(new FileReader(courseFile));

                String line = reader.readLine();

                // System.out.println(line);

                while (line != null) {

                    String[] tokens = line.split("\\s");

                    courses.add(new course(tokens[0].toLowerCase(), Integer.parseInt(tokens[1])));

                    line = reader.readLine();

                }
                reader.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    public void saveCourses() {

        File file = new File(courseFile);
        FileWriter fr = null;

        synchronized (this) {
            try {
                // FOR APPENDING
                // fr = new FileWriter(file,true);
                fr = new FileWriter(file);
                int i = 0;
                for (course c : courses) {
                    fr.write(c.name.toLowerCase() + " " + c.capacity);
                    if (i != (this.courses.size() - 1)) {
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

    public String enrolCourse(String courseName) {
        courseName = courseName.toLowerCase();

        for (course c : courses) {
            if (c.name.equals(courseName)) {
                if (c.capacity > 0) {
                    c.capacity -= 1;
                    saveCourses();
                    loadCourses();
                    return "success";
                }
            }
        }
        return "error";
    }

    public String dropCourse(String courseName) {
        courseName = courseName.toLowerCase();

        for (course c : courses) {
            if (c.name.equals(courseName)) {
                c.capacity += 1;
                saveCourses();
                loadCourses();
                return "success";
            }
        }
        return "error";
    }

    public String addCourse(String courseName, int capacity) {
        courseName = courseName.toLowerCase();

        course cc = getCourseByName(courseName);

        if (cc == null) {

            course c = new course(courseName, capacity);
            courses.add(c);
            saveCourses();
            loadCourses();
            return "success";
        }
        return "error";

    }

    public String removeCourse(String courseName) {
        courseName = courseName.toLowerCase();

        course c = getCourseByName(courseName);
        if (c != null) {
            courses.remove(c);
            saveCourses();
            loadCourses();
            return "success";
        }
        return "error";
    }

    public course getCourseByName(String courseName) {
        courseName = courseName.toLowerCase();

        for (course c : courses) {
            if (c.name.equals(courseName)) {
                return c;
            }
        }
        return null;
    }

    public String encodeString() {
        String result = new String("");
        if (courses.size() > 0) {
            result = "";
            int i = 0;
            for (course c : courses) {
                if (i > 0) {
                    result += "*";
                }
                result += c.name + "_" + c.capacity;
                i++;
            }
            return result;
        }
        return "error";
    }

    public ArrayList<course> decodeString(String str) {

        ArrayList<course> temp = new ArrayList<course>();

        if (!str.equals("error")) {
            String[] allCourses = str.split("\\*");
            for (String eachCourse : allCourses) {
                String[] courseDetails = eachCourse.split("\\_");
                course c = new course(courseDetails[0], Integer.parseInt(courseDetails[1]));
                temp.add(c);
            }
            return temp;
        }
        return null;
    }
}