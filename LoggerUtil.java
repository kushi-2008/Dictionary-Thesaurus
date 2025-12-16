package com.lexicon.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoggerUtil {

    private static final String LOG_FILE = "activity.log";

    public static void log(String message) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(System.currentTimeMillis() + " :: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}