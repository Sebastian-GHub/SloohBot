import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONObject;


/** Farms points for slooh online telescope
 * @deprecated use {@link PostRequestScriptMissions} instead for more, faster points without rate limits.
 */
@Deprecated
public class PostRequestScriptLikes {
    // Function to generate a random replyId
    public static int getRandomReplyId() {
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);  // Random number between 10000000 and 99999999
    }

    // Function to send POST request
    public static void sendPost() {
        try {
            // Set the URL for the API endpoint
            URL url = new URL("https://app.slooh.com/api/forum/likeReply");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Create JSON object with post data
            JSONObject postData = new JSONObject();
            postData.put("at", "32");
            postData.put("token", PostRequestScriptMissions.TOKEN);
            postData.put("cid", "653313");
            postData.put("replyId", getRandomReplyId());
            postData.put("authorId", 650881);
            postData.put("forumId", 24844);
            postData.put("topicId", 24908);
            postData.put("callSource", "photoview");
            postData.put("clientDeviceDetails", new JSONObject());
            postData.put("locale", "en");
            postData.put("productId", PostRequestScriptMissions.PRODUCT_ID);
            postData.put("amplitudeDeviceID", JSONObject.NULL);
            postData.put("sourcePageViewedURL", "https://app.slooh.com/NewDashboard");

            // Send POST request
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
            System.out.println("Response: " + response);

            // Check if the response contains the likePrompt message
            if (response.contains("You have reached your like limit. Try again later.")) {
                System.out.println("Like limit reached. Exiting program...");
                System.exit(0);  // Terminate the program
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to send post regularly
    public static void startPosting() {
        sendPost();  // Send first post immediately

        while (true) {
            try {
                Thread.sleep(10000);  // Wait 10 seconds
                sendPost();  // Send subsequent posts
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        startPosting();
    }
}
