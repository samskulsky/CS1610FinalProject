import org.json.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import com.sksamuel.scrimage.*;
import com.sksamuel.scrimage.color.*;
import com.sksamuel.scrimage.pixels.*;
import com.sksamuel.scrimage.nio.*;
import com.sksamuel.scrimage.filter.*;
import org.apache.commons.io.*;

public class Pexels {

	public static HashMap<String, PexelsImage> imagesByColor = new HashMap<>();
	public static ArrayList<PexelsImage> images = new ArrayList<>();

	// for some reason, at this point, a query is required
	public static void retrieveImage(String color, String query) {
		try {
			query = query.replaceAll(" ", "%s");
			String formatted = "https://api.pexels.com/v1/search?per_page=80&orientation=square&color=" + color;
			if (!query.equals(""))
				formatted += "&query=" + query;
	
			URL url = new URL(formatted);
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
			
			JSONObject jsonObject = new JSONObject(jsonString.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject currentImage = jsonArray.getJSONObject(i);
				JSONObject links = jsonArray.getJSONObject(i).getJSONObject("src");
				int id = currentImage.getInt("id");
				int width = currentImage.getInt("width");
				int height = currentImage.getInt("height");
				String avgColor = currentImage.getString("avg_color");
				String tinyURL = links.getString("tiny");

				if (!imagesByColor.containsKey(avgColor)) {
					PexelsImage img = new PexelsImage(id, width, height, avgColor, tinyURL);
					imagesByColor.put(avgColor, img);
					images.add(img);
				}
			}
			System.out.println(((PexelsImage)(imagesByColor.values().toArray()[0])).getID());
		} catch (Exception e) {
			System.out.println("Error retrieving images: " + e.getMessage());
		}
	}

	public static void retrieveImage(String query) {
		try {
			query = query.replaceAll(" ", "%s");
			String formatted = "https://api.pexels.com/v1/search?per_page=80&orientation=square";
			if (!query.equals(""))
				formatted += "&query=" + query;
	
			URL url = new URL(formatted);
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
			
			JSONObject jsonObject = new JSONObject(jsonString.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject currentImage = jsonArray.getJSONObject(i);
				JSONObject links = jsonArray.getJSONObject(i).getJSONObject("src");
				int id = currentImage.getInt("id");
				int width = currentImage.getInt("width");
				int height = currentImage.getInt("height");
				String avgColor = currentImage.getString("avg_color");
				String tinyURL = links.getString("tiny");

				if (!imagesByColor.containsKey(avgColor)) {
					PexelsImage img = new PexelsImage(id, width, height, avgColor, tinyURL);
					images.add(img);
				}
			}
		} catch (Exception e) {
			System.out.println("Error retrieving images: " + e.getMessage());
		}
	}

	public static ImmutableImage getImageFromListByColor(int r, int g, int b) throws MalformedURLException, IOException {
		PexelsImage currentImg = null;
		double distance = Double.MAX_VALUE;

		for (PexelsImage img : images) {
			String imgHexCode = img.getAvgColor();
			java.awt.Color avgColor = hexToRgb(imgHexCode);
			int iR = avgColor.getRed();
			int iG = avgColor.getGreen();
			int iB = avgColor.getBlue();

			double dis = Math.sqrt(Math.pow(r - iR, 2) + Math.pow(g - iG, 2) + Math.pow(b - iB, 2));
			if (dis < distance) {
				distance = dis;
				currentImg = img;
			}
		}

		URL url = new URL(currentImg.getTiny());

    byte[] fileContent = IOUtils.toByteArray(url);

		ImmutableImage image = ImmutableImage.loader().fromBytes(fileContent);
		
		return image;
	}

	public static java.awt.Color hexToRgb(String colorStr) {
    return new java.awt.Color(
            Integer.valueOf(colorStr.substring(1, 3), 16),
            Integer.valueOf(colorStr.substring(3, 5), 16),
            Integer.valueOf(colorStr.substring(5, 7), 16)
		);
	}
	
}