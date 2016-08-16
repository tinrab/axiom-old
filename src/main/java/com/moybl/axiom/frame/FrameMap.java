package com.moybl.axiom.frame;

import com.moybl.axiom.ast.Node;

import java.util.HashMap;
import java.util.Map;

public class FrameMap {

	private Map<Node, Frame> frames;
	private Map<Node, Access> accesses;

	public FrameMap() {
		frames = new HashMap<>();
		accesses = new HashMap<>();
	}

	public Frame getFrame(Node node) {
		return frames.get(node);
	}

	public void putFrame(Node node, Frame frame) {
		frames.put(node, frame);
	}

	public Access getAccess(Node node) {
		return accesses.get(node);
	}

	public void putAccess(Node node, Access access) {
		accesses.put(node, access);
	}

}
