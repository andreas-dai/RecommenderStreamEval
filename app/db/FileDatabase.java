package db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.FileModel;

public class FileDatabase {
	/**
	 * 
	 * @return:  all existing data files. 
	 */
	public List<FileModel> queryFileDocuments() {
		List<FileModel> result = new ArrayList<FileModel>();
//		File folder = new File("data");
//		File[] listOfFiles = folder.listFiles();
//		for (int i = 0; i < listOfFiles.length; i++) {
//			result.add(listOfFiles[i]);
//		}
		return result;
	}

}
