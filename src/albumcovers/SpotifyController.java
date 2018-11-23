package albumcovers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import javafx.scene.control.Label;

/**
 *
 * @author Yasaman Sabbagh Ziarani
 */
public class SpotifyController {

    final static private String SPOTIFY_CLIENT_ID = "64aceafccb884c4cb5307c6315fa8a9b";
    final static private String SPOTIFY_CLIENT_SECRET = "3483b2516f0548a4a35664679a3fd569";
    private static String artistName = FXMLDocumentController.artistName;

    private static FXMLDocumentController guiController;

    /**
     * gets an instance of FXMLDocumentController and assigns it to
     * guiController
     *
     * @param ctrl
     */
    public static void registerGUIController(FXMLDocumentController ctrl) {
        guiController = ctrl;
    }

    /**
     * takes the artist name and retrieves the artist id from the JsonObject
     * Parse the JSON output to retrieve the ID
     *
     * @param artistName takes a String
     * @return the artist id
     */
    public static String getArtistId(String artistName) {
        //  From an artist string, get the spotify ID
        // Recommended Endpoint: https://api.spotify.com/v1/search

        try {
            String endpoint = "https://api.spotify.com/v1/search";
            String params = "type=artist&q=" + artistName;
            String jsonOutput = spotifyEndpointToJson(endpoint, params);

            JsonElement jelement = new JsonParser().parse(jsonOutput);
            JsonObject rootObject = jelement.getAsJsonObject();
            JsonObject artists = rootObject.get("artists").getAsJsonObject();
            JsonArray items = artists.get("items").getAsJsonArray();
            JsonObject item0 = items.get(0).getAsJsonObject();
            String id = item0.get("id").getAsString();

            return id;
        } catch (Exception e) {

            guiController.setMessage("problem...");
        }

        return "";
    }

    /**
     * takes the artist name and gets the name of the artist's album
     *
     * @param artistName takes a String of artist's name
     * @return an ArrayList of Strings of the artist's albums names
     */
    public static ArrayList<String> getAlbumName(String artistName) {
        ArrayList<String> albumsNames = new ArrayList<String>();
        String endpoint = "https://api.spotify.com/v1/artists/"
                + getArtistId(artistName) + "/albums";
        String params = "market=CA&limit=50";
        String jsonOutput = spotifyEndpointToJson(endpoint, params);

        JsonElement jelement = new JsonParser().parse(jsonOutput);
        try {
            JsonObject rootObject = jelement.getAsJsonObject();
            JsonArray items = rootObject.get("items").getAsJsonArray();

            for (int j = 0; j < items.size(); j++) {
                JsonObject itemI = items.get(j).getAsJsonObject();
                String name = itemI.get("name").getAsString();
                albumsNames.add(name);
            }

        } catch (Exception e) {

            guiController.setMessage("Invalid artist");

        }
        return albumsNames;

    }

    /**
     * gets the image URLs for the specified artist id Retrieve album cover urls
     * (the first image is the highest resolution)
     *
     * @param spotifyArtistId gets the id of the artist
     * @return an ArrayList of String for the albumCover URLs
     */
    public static ArrayList<String> getAlbumCoversFromArtist(String spotifyArtistId) {

        // Recommended endpoint (id is the id of the artist): 
        //             https://api.spotify.com/v1/artists/{id}/albums
        // Arguments - You can filter for the CA market, and limit search to 50 albums
        ArrayList<String> albumsURL = new ArrayList<String>();
        String endpoint = "https://api.spotify.com/v1/artists/" + spotifyArtistId
                + "/albums";
        String params = "market=CA&limit=50";
        String jsonOutput = spotifyEndpointToJson(endpoint, params);

        JsonElement jelement = new JsonParser().parse(jsonOutput);
        try {
            JsonObject rootObject = jelement.getAsJsonObject();
            JsonArray items = rootObject.get("items").getAsJsonArray();

            for (int i = 0; i < items.size(); i++) {
                JsonObject itemI = items.get(i).getAsJsonObject();
                JsonArray images = itemI.get("images").getAsJsonArray();
                JsonObject images0 = images.get(0).getAsJsonObject();
                albumsURL.add(images0.get("url").getAsString());
            }
            guiController.setMessage("Artist found successfully");
        } catch (Exception e) {
            guiController.setMessage("Invalid artist");

        }

        return albumsURL;
    }

    private static String spotifyEndpointToJson(String endpoint, String params) {
        params = params.replace(' ', '+');

        try {
            String fullURL = endpoint;
            if (params.isEmpty() == false) {
                fullURL += "?" + params;
            }

            URL requestURL = new URL(fullURL);

            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            String bearerAuth = "Bearer " + getSpotifyAccessToken();
            connection.setRequestProperty("Authorization", bearerAuth);
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            String jsonOutput = "";
            while ((inputLine = in.readLine()) != null) {
                jsonOutput += inputLine;
            }
            in.close();

            return jsonOutput;
        } catch (Exception e) {
            guiController.setMessage("problem...");
        }

        return "";
    }

    // This implements the Client Credentials Authorization Flows
    // Based on the Spotify API documentation
    // 
    // It retrieves the Access Token based on the client ID and client Secret  
    //
    // You shouldn't have to modify any of this code...          
    private static String getSpotifyAccessToken() {
        try {
            URL requestURL = new URL("https://accounts.spotify.com/api/token");

            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            String keys = SPOTIFY_CLIENT_ID + ":" + SPOTIFY_CLIENT_SECRET;
            String postData = "grant_type=client_credentials";

            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(keys.getBytes()));

            // Send header parameter
            connection.setRequestProperty("Authorization", basicAuth);

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Send body parameters
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes());
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            String jsonOutput = "";
            while ((inputLine = in.readLine()) != null) {
                jsonOutput += inputLine;
            }
            in.close();

            JsonElement jelement = new JsonParser().parse(jsonOutput);
            JsonObject rootObject = jelement.getAsJsonObject();
            String token = rootObject.get("access_token").getAsString();

            return token;
        } catch (Exception e) {
            guiController.setMessage("problem...");

        }

        return "";
    }
}
