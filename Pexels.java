import java.net.*;
import com.google.gson.*;

public class Pexels {

	HashMap<String,PexelsImage> imagesByColor = new HashMap<>();

	// if no search query, enter ""
	public static void retrieveImage(String color, String query) throws Exception {
		query = String.format("s=%s",
       URLEncoder.encode(query, charset));
		String URL = "https://api.pexels.com/v1/search?per_page=80&orientation=square&color=" + color;
		if (!query.equals(""))
			URL += "&query=" + query;

		url = new URL(url);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestProperty("Authorization", "563492ad6f91700001000001564c1412dc2e4785a82ac196fba3d3b3");

    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new RuntimeException("Request Failed. HTTP Error Code: " + conn.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuffer jsonString = new StringBuffer();
    String line;
    while ((line = br.readLine()) != null) {
        jsonString.append(line);
    }
    br.close();
    conn.disconnect();

		JsonReader reader = new JsonReader(
new InputStreamReader(jsonString));
		JsonParser parser = new JsonParser();
		JsonElement rootElement = parser.parse(reader);
		JsonArray imagesJson = rootElement.getJsonObject("images").getAsJsonArray();

		// Will handle actually parsing tomorrow
		List<PexelImage> images = new ArrayList<PexelImage>();
		Gson myGson = new Gson();
		for (JsonElement image : imagesJson){
			PexelsImage img = myGson.fromJson(image, PexelsImage.class);
			images.add(img);
		}
	}
	
}