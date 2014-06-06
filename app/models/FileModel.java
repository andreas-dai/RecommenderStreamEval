package models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileModel {
	public String filename;
	public boolean hideRating;
	

	/**
	 * Constructor
	 */
	public FileModel(){
		super();
	}
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param startdate
	 * @param enddate
	 */
	public FileModel(String filename,boolean hideRating){
		super();
		this.filename = filename;
		this.hideRating = hideRating;
	}
	/**
	 * @return: filename
	 */
	public String getFilename(){
		return "data/" + filename;
	}
	public boolean isHideRating(){
		return hideRating;
	}
	
	/**
	 * return all available files.
	 * 
	 * @return
	 */
	public static List<FileModel> all(){
		List<FileModel> result = new ArrayList<FileModel>();
		File folder = new File("public/data");
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (!filename.contains("svn")) {
				FileModel file = new FileModel(filename,false);
				result.add(file);
			}	
		}
//		folder = new File("public/data/Twitter");
//		files = folder.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			String filename = files[i].getName();
//			if (filename.contains("csv")) {
//				FileModel file = new FileModel(filename,false);
//				result.add(file);
//			}	
//		}
		return result;
	}
}
