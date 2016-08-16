package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public abstract class Node {

	private Position position;

	public Node(Position position) {
		this.position = position;
	}

	public abstract void accept(Visitor visitor);

	public Position getPosition() {
		return position;
	}

}
