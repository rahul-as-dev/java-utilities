package com.demo.java_utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Utility class for standard cli applications
public class CLIUtils {

    public static String readLineFromStdIn() throws IOException {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in)
        );
        return br.readLine();
    }

    public static String readLineFromStdInWithMessage(String message)
        throws IOException {
        System.out.println(message);
        return readLineFromStdIn();
    }

    public static String readLineFromStdInWithMessage(
        String message,
        String defaultResponse
    ) throws IOException {
        System.out.println(message + " (" + defaultResponse + ")");
        String input = readLineFromStdIn();
        if (input.trim().length() == 0) return defaultResponse;
        return input;
    }

    public static boolean readYesNoFromStdInWithMessage(String message)
        throws IOException {
        String response = "";
        while (
            !"y".equals(response) &&
            !"yes".equals(response) &&
            !"n".equals(response) &&
            !"no".equals(response)
        ) {
            response = readLineFromStdInWithMessage(message + " (y/n)");
            response = response.toLowerCase();
        }
        return "y".equals(response) || "yes".equals(response);
    }

    public static boolean readYesNoFromStdInWithMessage(
        String message,
        boolean defaultResponse
    ) throws IOException {
        String response;
        String defaultMarker = defaultResponse ? "y" : "n";
        do {
            response = readLineFromStdInWithMessage(
                message + " (y/n)[" + defaultMarker + "]"
            );
            response = response.toLowerCase();
        } while (
            !"y".equals(response) &&
            !"yes".equals(response) &&
            !"n".equals(response) &&
            !"no".equals(response) &&
            response.trim().length() > 0
        );

        return (
            "y".equals(response) ||
            "yes".equals(response) ||
            (response.trim().isEmpty() && defaultResponse)
        );
    }
}
