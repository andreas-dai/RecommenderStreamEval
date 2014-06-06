package recommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Request;

public class RecommenderTest {

	static Map<String, ArrayList<Double>> rating2item = new HashMap<String, ArrayList<Double>>();
	static String server_url = "http://localhost:9000/evaluate/start/";

	/**
	 * predict recommendations
	 * 
	 * @param reponse
	 * @return
	 */
	public static ArrayList<String> predictRecommendations(String reponse) {
		ArrayList<String> result = new ArrayList<String>();
		result.add(reponse);
		return result;
	}

	/**
	 * predict the rating
	 * 
	 * @param response
	 * @return
	 */
	public static double predictRating(Request request) {
		double result = 0;
		System.out.println(request.toString());
		String rating = request.rating;
		String itemid = request.itemid;
		if (itemid == null) {
			itemid = request.tweetId;
		}
		if (rating.contains("?")) {
			result = calculateRating(itemid);
		} else {
			try {
				result = Double.parseDouble(rating);
			} catch (NumberFormatException e) {
				System.out.println("NumberFormatException: "+ rating);
			}
			
		}
		if (result != 0) {
			ArrayList<Double> array = new ArrayList<Double>();
			if (rating2item.containsKey(itemid)) {
				array = rating2item.get(itemid);
				array.add(result);

			} else {
				array.add(result);
			}
			rating2item.put(itemid, array);

		}
		return result;
	}

	/**
	 * 
	 * @param itemid
	 * @return
	 */
	private static int calculateRating(String itemid) {
		if (rating2item.containsKey(itemid)) {
			ArrayList<Double> ratingsArr = rating2item.get(itemid);
			int sum = 0;
			for (Double rating : ratingsArr) {
				sum += rating;
			}
			return sum / ratingsArr.size();
		} else {
			return 5;
		}
	}

	/**
	 * get response from url
	 * 
	 * @param teamId
	 * @return
	 */
	public static String getResponseFromUrl(String teamId) {
		String result = "";
		BufferedReader br = null;
		URL url = null;
		try {
			url = new URL(server_url + teamId);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
