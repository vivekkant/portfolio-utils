package org.weekendsoft.portfolioutil.util;

import java.io.File;
import java.io.FileFilter;

public class CSVFileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		
		if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
			return true;
		}
		
		return false;
	}

}
