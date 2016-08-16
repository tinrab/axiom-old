package com.moybl.axiom.im;

public class ImException extends RuntimeException {

	public ImException(String message) {
		super(message);
	}

	public static ImException internal() {
		return new ImException("Internal error");
	}

}
