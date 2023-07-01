package com.zxy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
public class CallShell {


    public static void runShell() {
        ProcessBuilder pb =
                new ProcessBuilder("./" + "open.sh");
        pb.directory(new File("/"));
        int runningStatus = 0;
        String s = null;
        try {
            Process p = pb.start();
            System.out.println("shell is running");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            try {
                runningStatus = p.waitFor();
            } catch (InterruptedException e) {
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}

