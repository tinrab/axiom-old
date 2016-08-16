package com.moybl.axiom.frame;

public class Access {

	private String id;
	private Frame frame;

	public Access(String id, Frame frame) {
		this.id = id;
		this.frame = frame;
	}

	public String getId() {
		return id;
	}

	public Frame getFrame() {
		return frame;
	}

}
