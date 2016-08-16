package com.moybl.axiom.ast;

public class Literal implements Expression {

	public enum Kind {
		INTEGER,
		FLOAT,
		STRING,
		NIL,
		BOOLEAN
	}

	private Kind kind;
	private Object value;

	public Literal(Kind kind, Object value) {
		this.kind = kind;
		this.value = value;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Kind getKind() {
		return kind;
	}

	public Object getValue() {
		return value;
	}

}
