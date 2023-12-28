package com.demo.java_utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    /**
     * Performs a HTTP GET request to the specified URL.
     *
     * @param url the URL to send the GET request to
     * @return the response body as a string
     * @throws IOException if an I/O exception occurs
     */
    public static String doGet(String url) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL getUrl = new URL(url);
            conn = (HttpURLConnection) getUrl.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            closeResources(conn, reader);
        }
    }

    /**
     * Performs a HTTP POST request to the specified URL with the given payload.
     *
     * @param url     the URL to send the POST request to
     * @param payload the payload to include in the POST request body
     * @return the response body as a string
     * @throws IOException if an I/O exception occurs
     */
    public static String doPost(String url, String payload) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        OutputStream outputStream = null;
        try {
            URL postUrl = new URL(url);
            conn = (HttpURLConnection) postUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Write the payload to the request body
            outputStream = conn.getOutputStream();
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            // Read the response
            reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            closeResources(conn, reader, outputStream);
        }
    }

    /**
     * Sets a header in the HTTP request.
     *
     * @param conn   the HttpURLConnection object
     * @param name   the name of the header
     * @param value  the value of the header
     */
    public static void setHeader(
        HttpURLConnection conn,
        String name,
        String value
    ) {
        conn.setRequestProperty(name, value);
    }

    /**
     * Closes all resources associated with the HTTP connection.
     *
     * @param conn    the HttpURLConnection object
     * @param reader  the BufferedReader for reading response
     * @param output  the OutputStream for writing request body (optional)
     */
    private static void closeResources(
        HttpURLConnection conn,
        BufferedReader reader,
        OutputStream output
    ) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (output != null) {
                output.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        } catch (IOException e) {
            // Handle or log exception
            e.printStackTrace();
        }
    }

    /**
     * Performs a HTTP POST request with multipart form data to the specified URL.
     *
     * @param url        the URL to send the POST request to
     * @param multipartData the multipart form data as a byte array
     * @param contentType the content type of the multipart data (e.g., "multipart/form-data; boundary=...")
     * @return the response body as a string
     * @throws IOException if an I/O exception occurs
     */
    public static String doPostMultipart(
        String url,
        byte[] multipartData,
        String contentType
    ) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        OutputStream outputStream = null;
        try {
            URL postUrl = new URL(url);
            conn = (HttpURLConnection) postUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);

            // Write the multipart data to the request body
            outputStream = conn.getOutputStream();
            outputStream.write(multipartData);
            outputStream.flush();

            // Read the response
            reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            closeResources(conn, reader, outputStream);
        }
    }

    /**
     * Closes all resources associated with the HTTP connection.
     *
     * @param conn      the HttpURLConnection object
     * @param reader    the BufferedReader for reading response
     * @param output    the OutputStream for writing request body (optional)
     */
    private static void closeResources(
        HttpURLConnection conn,
        BufferedReader reader,
        OutputStream output
    ) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (output != null) {
                output.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        } catch (IOException e) {
            // Handle or log exception
            e.printStackTrace();
        }
    }

    /**
     * Example usage of the HttpUtils class.
     */
    public static void main(String[] args) {
        String apiUrl = "https://jsonplaceholder.typicode.com/posts/1";

        try {
            // Perform a GET request
            String getResponse = HttpUtils.doGet(apiUrl);
            System.out.println("GET Response:");
            System.out.println(getResponse);

            // Perform a POST request with payload
            String postPayload =
                "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";
            String postResponse = HttpUtils.doPost(apiUrl, postPayload);
            System.out.println("\nPOST Response:");
            System.out.println(postResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
