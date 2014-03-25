package com.niubi;

import java.io.File;
import java.io.FilenameFilter;


public class GIFfileFilter implements FilenameFilter{

	@Override
	public boolean accept(File dir, String name) {

		if (name.endsWith("gif")) {
			return true;
		}
		return false;
	}

}
