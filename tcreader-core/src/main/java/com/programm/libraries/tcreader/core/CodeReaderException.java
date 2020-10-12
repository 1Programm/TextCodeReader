package com.programm.libraries.tcreader.core;

import java.io.IOException;

public class CodeReaderException extends IOException {

	private static final long serialVersionUID = 1L;

	public CodeReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodeReaderException(String message) {
		super(message);
	}

	public CodeReaderException(Throwable cause) {
		super(cause);
	}

}
