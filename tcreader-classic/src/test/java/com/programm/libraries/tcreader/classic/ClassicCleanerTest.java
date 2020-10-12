package com.programm.libraries.tcreader.classic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

public class ClassicCleanerTest {

	@Test
	public void testFileInput() throws IOException {
		InputStream in = getClass().getResourceAsStream("/TestFile1.txt");
		ClassicCodeCleaner cleaner = new ClassicCodeCleaner();
		List<String> lines = cleaner.getLines(in);

		assert lines.size() == 3;

		assert lines.get(0).equals("MyRoot[id=\"foo\"]{");
		assert lines.get(1).equals("MyChild[name=\"bar\"]");
		assert lines.get(2).equals("}");
	}

	@Test
	public void testStringInput() throws IOException {
		String code = "   //Test\n   Foo[]\n   Bar[]   ";
		InputStream in = new ByteArrayInputStream(code.getBytes());
		ClassicCodeCleaner cleaner = new ClassicCodeCleaner();

		String cleanedCode = cleaner.clean(in);

		assert cleanedCode.equals("Foo[]Bar[]");
	}

}
