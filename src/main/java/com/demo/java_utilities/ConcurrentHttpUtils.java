package com.demo.java_utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentHttpUtils {

    private static final int TIMEOUT_MS = 10000; // Timeout for each HTTP request in milliseconds

    /**
     * Performs concurrent HTTP GET requests to multiple URLs.
     *
     * @param urls the list of URLs to send GET requests to
     * @return a list of response bodies as strings corresponding to each URL
     * @throws InterruptedException if interrupted while waiting for tasks to complete
     */
    public static List<String> doGetConcurrently(List<String> urls)
        throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(urls.size());
        List<Future<String>> futures = new ArrayList<>();

        try {
            for (String url : urls) {
                Callable<String> callableTask = createGetCallable(url);
                Future<String> future = executor.submit(callableTask);
                futures.add(future);
            }

            List<String> responses = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    String response = future.get(
                        TIMEOUT_MS,
                        TimeUnit.MILLISECONDS
                    );
                    responses.add(response);
                } catch (ExecutionException | TimeoutException e) {
                    responses.add("Error: " + e.getMessage());
                }
            }
            return responses;
        } finally {
            executor.shutdown();
        }
    }

    /**
     * Creates a Callable task for performing a HTTP GET request.
     *
     * @param url the URL to send the GET request to
     * @return a Callable task that performs the GET request and returns the response body
     */
    private static Callable<String> createGetCallable(String url) {
        return () -> {
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
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        };
    }

    /**
     * Example usage of the ConcurrentHttpUtils class.
     */
    public static void main(String[] args) {
        List<String> urls = List.of(
            "https://jsonplaceholder.typicode.com/posts/1",
            "https://jsonplaceholder.typicode.com/posts/2",
            "https://jsonplaceholder.typicode.com/posts/3"
        );

        try {
            List<String> responses = ConcurrentHttpUtils.doGetConcurrently(
                urls
            );
            for (int i = 0; i < responses.size(); i++) {
                System.out.println("Response from URL " + urls.get(i) + ":");
                System.out.println(responses.get(i));
                System.out.println();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
