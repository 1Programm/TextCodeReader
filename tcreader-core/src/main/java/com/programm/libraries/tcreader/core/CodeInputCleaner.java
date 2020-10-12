package com.programm.libraries.tcreader.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CodeInputCleaner {

	List<String> getLines(InputStream is) throws IOException;

	default String clean(InputStream is) throws IOException {
		return clean(is, "");
	}

	default String clean(InputStream is, String lineSeparator) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> lines = getLines(is);

		for (String line : lines) {
			if (sb.length() > 0) {
				sb.append(lineSeparator);
			}

			sb.append(line);
		}

		return sb.toString();
	}

}
