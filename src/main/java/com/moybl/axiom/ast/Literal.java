package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class Literal extends Expression {

	public enum Kind {
		INTEGER,
		FLOAT,
		STRING,
		NIL,
		BOOLEAN
	}

	private Kind kind;
	private Object value;

	public Literal(Position position, Kind kind, Object value) {
		super(position);
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
