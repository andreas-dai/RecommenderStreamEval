package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Request {
	@Id
	public String userid;
	public String itemid;
	public String rating;
	public Long timestamp;
	public String tweetId;
	static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

	/**
	 * Constructor.
	 */
	public Request() {
		super();
	}

	public Request(String userid, String itemId, String rating, String tweetId,
			Long timestamp) {
		super();
		this.userid = userid;
		this.itemid = itemId;
		this.rating = rating;
		this.tweetId = tweetId;
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @param line
	 */
	public Request(String database, String line) {
		if (database.equals("MovieLens")) {
			String[] lineToken = line.split("::");
			for (int i = 0; i < lineToken.length; i++) {
				if (i == 0) {
					this.userid = lineToken[i];
				}
				if (i == 1) {
					this.itemid = lineToken[i];
				}
				if (i == 2) {
					this.rating = lineToken[i];
				}
				if (i == 3) {
					this.timestamp = Long.parseLong(lineToken[i]);
				}
			}
		} else {
			String[] lineToken = line.split(";");

			for (int i = 0; i < lineToken.length; i++) {
				if (i == 0) {
					System.out.println("date:" + lineToken[i]);
					Date date = null;
					try {
						date = sdf.parse(lineToken[i]);
					} catch (ParseException e) {
						System.out.println(e.toString());
						System.out.println(line);
					}
					if (date != null) {
						this.timestamp = date.getTime();
					}
				}
				if (i == 2) {
					this.tweetId = lineToken[i];
				}
				if (i == 4) {
					this.itemid = lineToken[i];
				}
				if (i == 6) {
					this.rating = lineToken[6];
				}
			}
		}

	}

	/**
	 * list of all requests
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<Request> all(BufferedReader br, String database)
			throws IOException {
		List<Request> result = new ArrayList<Request>();
		String line;
		for (line = br.readLine(); line != null; line = br.readLine()) {
			Request rq = new Request(database, line);
			result.add(rq);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public String toString() {
		return "{\"userid\" : \"" + userid + "\",\"itemid\" : \"" + itemid
				+ "\",\"rating\" : \"" + rating + "\",\"timestamp\" : "
				+ timestamp + ",\"TweetId\" : " + tweetId + "}";
	}

}
