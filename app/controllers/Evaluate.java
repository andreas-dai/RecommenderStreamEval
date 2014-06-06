package controllers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import models.Request;
import models.Team;
import play.mvc.Controller;
import play.mvc.Result;

public class Evaluate extends Controller {
	static Team team;
	static Request rq;
	static Request trueData;
	static BufferedReader br1;
	static BufferedReader br2;
	static int count = 0;

	/**
	 * list all team
	 * 
	 * @return
	 */
	public static Result index() {
		List<Team> teamList = Team.all();
		return ok(views.html.evaluate.render(teamList));
	}

	/**
	 * send request to client.
	 * 
	 * @param teamId
	 * @return
	 */
	public static Result getRequestByTeamId(String database, String teamId) {
		if (team == null) {
			team = new Team(teamId);
		}

		if (database.contains("MovieLens") && team._id != null && br1 == null) {
			try {

				br1 = new BufferedReader(
						new InputStreamReader(new FileInputStream(
								"public/data/ratings.dat"), "UTF-8"));

				trueData = new Request(database, br1.readLine());
				rq = new Request(trueData.userid, trueData.itemid,
						trueData.rating, trueData.tweetId, trueData.timestamp);

			} catch (IOException e) {
				System.out.println(e.toString());
			}
			// team.sendBackRecommendations(rq.toString());
			count++;
			return ok(rq.toString());

		} else if (database.contains("MovieLens") && team._id != null
				&& br1 != null) {
			try {
				while (true) {
					trueData = new Request(database, br1.readLine());
					if (trueData == null) {
						break;
					}
					rq = new Request(trueData.userid, trueData.itemid,
							trueData.rating, trueData.tweetId,
							trueData.timestamp);

					// team.sendBackRecommendations(rq.toString());
					count++;
					if (count % 5 == 0) {
						rq.rating = "?";
					}
					return ok(rq.toString());

				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			return ok("done");

		}
		if (database.contains("Twitter") && team._id != null && br2 == null) {
			try {

				br2 = new BufferedReader(
						new InputStreamReader(new FileInputStream(
								"public/data/Twitter.csv"), "UTF-8"));
				String line = br2.readLine();
				if (line.contains("Date;Hour")) {
					line = br2.readLine();
				}
				trueData = new Request(database, line);
				rq = new Request(trueData.userid, trueData.itemid,
						trueData.rating, trueData.tweetId, trueData.timestamp);

			} catch (IOException e) {
				System.out.println(e.toString());
			}
			// team.sendBackRecommendations(rq.toString());
			count++;
			return ok(rq.toString());

		} else if (database.contains("Twitter") && team._id != null
				&& br2 != null) {
			try {
				while (true) {
					String line = br2.readLine();
					if (line.contains("Date;Hour")) {
						line = br2.readLine();
					}
					trueData = new Request(database, line);
					if (trueData == null) {
						break;
					}
					rq = new Request(trueData.userid, trueData.itemid,
							trueData.rating, trueData.tweetId,
							trueData.timestamp);

					// team.sendBackRecommendations(rq.toString());
					count++;
					if (count % 5 == 0) {
						rq.rating = "?";
					}
					return ok(rq.toString());

				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			return ok("done");

		} else {
			System.out.println(team._id);
			return ok("error");
		}

	}

	/**
	 * 
	 * @param teamId
	 * @return
	 */
	public static Result getRecommendedItemByTeamId(String teamId) {
		if (team == null || !team._id.equals(teamId)) {
			team = new Team(teamId);
		}

		return TODO;
	}

	/**
	 * send a recommendations back to server.
	 * 
	 * @param teamId
	 * @param recommendations
	 * @return
	 */
	public static Result sendRecommendations2Server(String teamId,
			String recommendations) {
		if (team == null) {
			team = new Team(teamId);
		}
		String teamdata = Team.getTeam(team._id);
		if (teamdata != null) {
			String key = teamdata.split("=")[0];
			String value = teamdata.split("=")[1];
			if (value.contains("]")) {
				value = value.replace("]", "") + recommendations + "]";
			} else {
				value = value + ";[" + recommendations + "]";
			}
			Team.updateProperties(key, value);
			return ok("succes");
		} else {
			return ok("error");
		}
	}

	public static Result evaluate(String recommendedItem) {
		return TODO;
	}

	public static Result createData(String teamEmail) throws IOException,
			InterruptedException {
		return TODO;

	}
}
