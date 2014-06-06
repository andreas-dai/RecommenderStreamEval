package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.FileModel;
import play.mvc.Controller;
import play.mvc.Result;

public class Download extends Controller {
	static final SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy");
	static final SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	/**
	 * it shows files that are able to download.
	 * 
	 * @return
	 */
	public static Result index() {
		List<FileModel> fileMap = FileModel.all();
		return ok(views.html.download.render(fileMap));
	}

	/**
	 * create file in the given time interval.
	 * 
	 * @param date
	 * @return
	 * @throws IOException
	 */

	public static Result createFilesByDate(String date) throws IOException {

		String[] dates = date.split("&");

		if (createfiles(dates[0], dates[1])) {
			return redirect("/download");
		} else {
			return ok("Failed to generate the files.");
		}
	}

	/**
	 * create the new file that contains appropriate timestamp.
	 * 
	 * @param _startDate
	 * @param _endDate
	 * @return
	 */
	private static Boolean createfiles(String _startDate, String _endDate) {
		String filePath = "public/data/Movielens" + "_" + _startDate + "_" + _endDate
				+ ".dat";
		String filePath1="public/data/Twitter" + "_" + _startDate + "_" + _endDate
				+ ".csv";

		Date startDate = null;
		try {
			startDate = simpleFormat.parse(_startDate);
		} catch (ParseException e) {
			System.out.println(e.toString());
			return false;
		}
		Long start = startDate.getTime();
		
		Date endDate = null;
		try {
			endDate = simpleFormat.parse(_endDate);
		} catch (ParseException e) {
			System.out.println(e.toString());
			return false;
		}
		Long end = endDate.getTime() + 86400000L;
		System.out.println(startDate + " : " + start);
		BufferedReader br = null;
		BufferedWriter out = null;
		
		
		try {
			out = new BufferedWriter(new FileWriter(filePath));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"public/data/ratings.dat"), "UTF-8"));
			String line;
			for (line = br.readLine(); line != null; line = br.readLine()) {
				Long timestamp = Long.parseLong(line.split("::")[3]);
				if (timestamp < end && timestamp > start) {
					out.append(line);
					out.newLine();
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}

			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		/*##################################################*/
		BufferedReader br1 = null;
		BufferedWriter out1 = null;
		
		
		try {
			out1 = new BufferedWriter(new FileWriter(filePath1));
			br1 = new BufferedReader(new InputStreamReader(new FileInputStream(
					"public/data/Twitter.csv"), "UTF-8"));
			String line;
			for (line = br1.readLine(); line != null; line = br1.readLine()) {
				if (line.contains("Date;Hour")) {
					continue;
				}
				Date currentDate = null;
				try {
					currentDate = sdf.parse(line.split("\t")[0]);
				} catch (ParseException e) {
					System.out.println(e.toString());
				}
				Long timestamp = 0L;
				if (currentDate != null) {
					timestamp = currentDate.getTime();
					
				}
				
				if (timestamp < end && timestamp > start) {
					out1.append(line);
					out1.newLine();
					System.out.println("year");
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			return false;
		} finally {
			if (br1 != null) {
				try {
					br1.close();
				} catch (IOException e) {

				}

			}
			if (out1 != null) {
				try {
					out1.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}
}
