import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;

public class PostRequestScriptMissions {
    public static final String TOKEN = "";
    public static final String CID = "";
    public static final String PRODUCT_ID = "";

    //Mission data automatically gathered
    public static int missionId;
    public static long missionTime;

    // Counter to stop after reaching a specified amount of points
    public static final int POINTS_THRESHOLD = 100;
    public static int totalPoints = 0;

    // Optional random delays
    public static boolean useRandomDelays = true;
    public static final int MIN_DELAY = 500; // Minimum delay in milliseconds
    public static final int MAX_DELAY = 1500; // Maximum delay in milliseconds

    public static void sendFirstPost() {
        try {
            // Set the URL for the API endpoint
            URL url = new URL("https://app.slooh.com/api/reservation/reserveCommunityMission");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Create the JSON object for the first post request
            JSONObject postData = new JSONObject();
            postData.put("at", "32");
            postData.put("token", TOKEN);
            postData.put("cid", CID);
            postData.put("callSource", "featuredObjectsDashboardV4New");
            postData.put("scheduledMissionId", PostRequestScriptMissions.missionId);
            postData.put("missionStart", PostRequestScriptMissions.missionTime);
            postData.put("clientDeviceDetails", new JSONObject());
            postData.put("locale", "en");
            postData.put("productId", PRODUCT_ID);
            postData.put("amplitudeDeviceID", JSONObject.NULL);
            postData.put("sourcePageViewedURL", "https://app.slooh.com/NewDashboard");

            // Send the first POST request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            Scanner scanner;
            if (conn.getResponseCode() == 200) {
                scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                scanner = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8);
            }
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Print the response
            System.out.println("First POST Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendSecondPost() {
        try {
            // Set the URL for the API endpoint
            URL url = new URL("https://app.slooh.com/api/reservation/cancelMissionReservation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject postData = new JSONObject();
            postData.put("at", "32");
            postData.put("token", TOKEN);
            postData.put("cid", CID);
            postData.put("scheduledMissionId", PostRequestScriptMissions.missionId);
            postData.put("clientDeviceDetails", new JSONObject());
            postData.put("locale", "en");
            postData.put("productId", PRODUCT_ID);
            postData.put("amplitudeDeviceID", JSONObject.NULL);
            postData.put("sourcePageViewedURL", "https://app.slooh.com/NewDashboard");

            // Send the second POST request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            Scanner scanner;
            if (conn.getResponseCode() == 200) {
                scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                scanner = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8);
            }
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Print the response
            System.out.println("Second POST Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFeaturedMissions() {
        try {
            // Set the URL for the API endpoint
            URL url = new URL("https://app.slooh.com/api/reservation/getDashboardFeaturedObjects");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject postData = new JSONObject();
            postData.put("at", "32");
            postData.put("token", TOKEN);
            postData.put("cid", CID);
            postData.put("clientDeviceDetails", new JSONObject());
            postData.put("locale", "en");
            postData.put("amplitudeDeviceID", JSONObject.NULL);
            postData.put("sourcePageViewedURL", "https://app.slooh.com/NewDashboard");

            // Send the POST request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            Scanner scanner;
            if (conn.getResponseCode() == 200) {
                scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                scanner = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8);
            }
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject responseObject = new JSONObject(response);
            JSONArray missionList = responseObject.getJSONArray("missionList");
            JSONObject randomMission = missionList.getJSONObject(0); // Get the first mission

            int scheduledMissionId = randomMission.getInt("scheduledMissionId");
            long missionStart = randomMission.getLong("missionStart");

            PostRequestScriptMissions.missionId = scheduledMissionId;
            PostRequestScriptMissions.missionTime = missionStart;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startPosting() {
        while (totalPoints < POINTS_THRESHOLD) { // Stop once reaching the points threshold
            try {
                sendFirstPost();  // Send first post
                totalPoints += 5; // Gain 5 points per loop
                System.out.println("Total Points: " + totalPoints);

                if (useRandomDelays) {
                    int randomDelay = ThreadLocalRandom.current().nextInt(MIN_DELAY, MAX_DELAY);
                    System.out.println("Random delay: " + randomDelay + " ms");
                    Thread.sleep(randomDelay);
                } else {
                    Thread.sleep(500);  // Fixed 500 ms delay
                }

                sendSecondPost();

                // Add delay after the second post
                if (useRandomDelays) {
                    int randomDelay = ThreadLocalRandom.current().nextInt(MIN_DELAY, MAX_DELAY);
                    System.out.println("Random delay: " + randomDelay + " ms");
                    Thread.sleep(randomDelay);
                } else {
                    Thread.sleep(500);  // Fixed 500 ms delay
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Reached the points threshold: " + POINTS_THRESHOLD + ". Stopping...");
    }

    public static void main(String[] args) {
        getFeaturedMissions();
        startPosting();
    }
}
