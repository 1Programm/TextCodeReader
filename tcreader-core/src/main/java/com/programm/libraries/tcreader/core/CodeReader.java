package com.programm.libraries.tcreader.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class CodeReader {

	public final Node readCode(InputStream is) throws CodeReaderException {
		CodeInputCleaner cleaner = getInputCleaner();
		String code = null;

		try {
			code = cleaner.clean(is);
		} catch (IOException e) {
			throw new CodeReaderException(e);
		}

		return doReadCode(code);
	}

	public final Node readCodeLines(InputStream is) throws CodeReaderException {
		CodeInputCleaner cleaner = getInputCleaner();
		List<String> codeLines = null;

		try {
			codeLines = cleaner.getLines(is);
		} catch (IOException e) {
			throw new CodeReaderException(e);
		}

		return doReadCodeLines(codeLines);
	}

	protected abstract Node doReadCode(String code) throws CodeReaderException;

	protected abstract Node doReadCodeLines(List<String> codeLines) throws CodeReaderException;

	protected abstract CodeInputCleaner getInputCleaner();

}
