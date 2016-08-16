package com.moybl.axiom.im;

import com.moybl.axiom.frame.Frame;

public class Chunk {

	private Frame frame;
	private ImStatement code;

	public Chunk(Frame frame, ImStatement code) {
		this.frame = frame;
		this.code = code;
	}

	public Frame getFrame() {
		return frame;
	}

	public ImStatement getCode() {
		return code;
	}

}
