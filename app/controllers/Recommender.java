package controllers;

import java.io.IOException;
import java.util.List;

import models.Request;
import models.Team;

import org.apache.commons.math.stat.regression.SimpleRegression;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import recommender.RecommenderTest;

public class Recommender extends Controller {
	static SimpleRegression regression = new SimpleRegression();

	/**
	 * 
	 * @return
	 */
	public static Result index() {
		List<Team> teams = Team.all();
		return ok(views.html.recommender.render(teams));
	}

	public static Result evaluate(String teamId) {
		double[] matches = new double[9];
		matches[0] = regression.getIntercept();
		matches[1] = regression.getInterceptStdErr();
		matches[2] = regression.getMeanSquareError();
		matches[4] = regression.getR();
		matches[3] = regression.getRSquare();
		matches[5] = regression.getSlope();
		matches[6] = regression.getSlopeStdErr();
		matches[7] = regression.getRegressionSumSquares();
		matches[8] = regression.getTotalSumSquares();
		return ok(Json.toJson(matches));

	}

	/**
	 * 
	 * @param teamId
	 * @return
	 * @throws IOException
	 */
	public static Result run(String teamId) throws IOException {
		String[] datas = teamId.split(":");
		String id = "0";
		String database = "MovieLens";
		for (int i = 0; i < datas.length; i++) {
			if (i==0) {
				id = datas[0];
			}
			if (i==1) {
				database= datas[1];
			}
		}
		Team team = new Team(id);
		String response = team.getResponseFromUrl(database);
		Request request;
		Request trueData ;
		int count = 0;
		do {
			request = Evaluate.rq;
			trueData = Evaluate.trueData;
			double predictRating = RecommenderTest.predictRating(request);
			request.rating = predictRating + "";
			count++;
			if (trueData.rating.equals(request.rating) && count % 5 == 0) {
				//System.out.println(request);
				//System.out.println(count);
			}
			double trueRating = Double.parseDouble(trueData.rating);
			regression.addData(predictRating, trueRating);
			response = team.getResponseFromUrl(database);
			
			
		} while (request != null && !response.equals("done")
				&& !response.equals("error") && count < 1000);
		return ok("success");

	}
}
