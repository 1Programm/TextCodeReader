package com.programm.libraries.tcreader.classic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.programm.libraries.tcreader.core.CodeInputCleaner;

public class ClassicCodeCleaner implements CodeInputCleaner {

	@Override
	public List<String> getLines(InputStream is) throws IOException {
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			boolean inComment = false;
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (line.isEmpty() || line.startsWith("//"))
					continue;

				int nextComment = line.indexOf("//");
				if (nextComment != -1) {
					line = line.substring(0, nextComment);
				}

				String actualLine = "";
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					boolean hasNext = i + 1 < line.length();

					if (inComment) {
						if (hasNext && c == '*' && line.charAt(i + 1) == '/') {
							inComment = false;
							i++;
						}
					} else if (hasNext && c == '/' && line.charAt(i + 1) == '*') {
						inComment = true;
						i++;
					} else {
						actualLine += c;
					}
				}

				actualLine = actualLine.trim();

				if (!actualLine.isEmpty()) {
					lines.add(actualLine);
				}
			}
		}

		return lines;
	}
}
