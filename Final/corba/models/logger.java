package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class logger {

    private String logFileAddress;

    public logger(String path) {
        this.logFileAddress = path;
    }

    public void add(String Message) {

        synchronized (this) {
            File file = new File(this.logFileAddress);
            FileWriter fr = null;
            try {
                // Below constructor argument decides whether to append or override
                fr = new FileWriter(file, true);
                fr.write('\n');

                // DATE TIME NOW
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                // System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43

                fr.write(Message + " @[ " + dateFormat.format(date) + " ]");

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
}