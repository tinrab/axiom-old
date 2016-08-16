package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class ReferenceExpression extends Expression {

	public enum Kind {
		THIS, BASE
	}

	private Kind kind;

	public ReferenceExpression(Position position, Kind kind) {
		super(position);
		this.kind = kind;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Kind getKind() {
		return kind;
	}

}
