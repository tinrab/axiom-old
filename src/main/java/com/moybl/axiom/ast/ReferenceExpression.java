package com.moybl.axiom.ast;

public class ReferenceExpression implements Expression {

	public enum Kind {
		THIS, BASE
	}

	private Kind kind;

	public ReferenceExpression(Kind kind) {
		this.kind = kind;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Kind getKind() {
		return kind;
	}

}
