package models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import recommender.RecommenderTest;

@Entity
public class Team {
	/* team id */
	@Id
	public String _id;
	/* team's email */
	@Email
	public String email;
	/* team name */
	public String teamName;
	/* team password */
	public String password;

	static Properties propertiesTeam = new Properties();
	static FileOutputStream output = null;
	static FileInputStream input = null;
	private static String server_url = "http://localhost:9000/evaluate/start/";

	/**
	 * Default constructor.
	 */
	public Team() {
		super();
	}

	/**
	 * 
	 * @param teamId
	 */
	public Team(String teamId) {
		super();
		this._id = teamId;
	}

	/**
	 * Constructor.
	 * 
	 * @param teamId
	 * @param email
	 * @param password
	 */
	public Team(String teamName, String email, String password, String _id) {
		super();
		this.teamName = teamName;
		this.email = email;
		this.password = password;
		this._id = _id;
	}

	/**
	 * create new team and save it to database or properties, new id would be
	 * generated.
	 * 
	 * @param teamName
	 * @param email
	 * @param password
	 * @return
	 */
	public static String create(String teamName, String email, String password) {
		Integer id = (int) (Math.random() * 1000);
		String key = email;
		String value = id + ";" + teamName + ";" + password;
		updateProperties(key, value);
		return id.toString();
	}

	/**
	 * delete team from database.
	 * 
	 * @param id
	 */
	public static void delete(String id) {
		// TODO
	}

	/**
	 * check team existing, then return team id, when not return null.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static String signinTeam(String email, String password) {
		loadProperties();
		if (propertiesTeam.containsKey(email)) {
			String prop = propertiesTeam.getProperty(email);
			if (prop.contains(password)) {
				return prop.split(";")[0];
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * check email existing.
	 * 
	 * @param value
	 * @return
	 */
	public boolean isEmailExists(String email) {
		if (propertiesTeam.containsKey(email)) {
			return true;
		}
		return false;
	}

	/**
	 * check team name existing.
	 * 
	 * @param teamName
	 * @return
	 */
	public Boolean isTeamNameExists(String teamName) {
		return false;
	}

	/**
	 * send the recommendations back to server.
	 * 
	 * @param response
	 */
	public void sendBackRecommendations(String response) {

		ArrayList<String> recommendations = RecommenderTest
				.predictRecommendations(response);
		try {
			for (String rec : recommendations) {
				System.out.println(rec);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	};

	/**
	 * insert recommendations to team.
	 * 
	 * @param team
	 * @param recommendations
	 */
	public static void insertRecommendations(String teamid,
			ArrayList<String> recommendations) {

	}

	/**
	 * 
	 * @param _id
	 * @param impression
	 * @param lstPredictions
	 * @param startTimeStamp
	 * @param endTimeStamp
	 */
	public static void insertRecommendations(String _id, Object impression,
			List<Long> lstPredictions, Long startTimeStamp, Long endTimeStamp) {

	}

	/**
	 * 
	 * @return
	 */
	public static List<Team> all() {
		List<Team> teams = new ArrayList<Team>();
		loadProperties();
		for (Object key : propertiesTeam.keySet()) {
			String email = (String) key;
			String[] info = ((String) propertiesTeam.get(key)).split(";");
			String _id = info[0];
			String name = info[1];
			String password = info[2];

			Team team = new Team(name, email, password, _id);
			teams.add(team);
		}
		return teams;
	}

	/**
	 * 
	 */
	private static void loadProperties() {
		FileInputStream input = null;
		try {
			input = new FileInputStream("team.properties");
			propertiesTeam.load(input);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void updateProperties(String key, String value) {
		loadProperties();
		FileOutputStream output = null;
		try {
			output = new FileOutputStream("team.properties", false);
			if (propertiesTeam.get(key) == null) {
				propertiesTeam.setProperty(key, value);
			} else {
				String data = (String) propertiesTeam.get(key);
				String dataWithoutId = data.substring(data.indexOf(";"),
						data.length());
				String valuaWithoutId = value.substring(value.indexOf(";"),
						value.length());
				if (!dataWithoutId.equals(valuaWithoutId)) {
					propertiesTeam.setProperty(key, value);

				}
			}
			propertiesTeam.store(output, null);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {

				}
			}
		}

	}

	/**
	 * 
	 * @param teamId
	 * @return complete team's information
	 */
	public static String getTeam(String teamId) {
		String result = null;
		if (propertiesTeam.isEmpty()) {
			loadProperties();
		}
		for (Object key : propertiesTeam.keySet()) {
			String[] obj = ((String) propertiesTeam.get(key)).split(";");
			if (obj[0].equals(teamId)) {
				result = key + "=" + propertiesTeam.get(key);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		create("khactucao", "khactucao@gmail.com", "123456");
		create("pia", "pia@gmail.com", "123456");
		all();
		System.out.println(signinTeam("khactucao@gmail.com", "123456"));
		System.out.println("csacjsac");
	}

	/**
	 * 
	 * 
	 * @param teamId
	 * @return
	 */
	public String getResponseFromUrl(String database) {
		String result = "";
		BufferedReader br = null;
		URL url = null;
		try {
			url = new URL(server_url + database + "/" + _id);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
